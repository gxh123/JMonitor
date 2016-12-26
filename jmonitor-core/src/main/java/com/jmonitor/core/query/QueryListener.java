package com.jmonitor.core.query;

import com.jmonitor.core.message.io.MessageReceiver;
import com.jmonitor.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * Created by jmonitor on 2016/12/21.
 */
public class QueryListener implements Runnable{
    private MessageReceiver receiver;
    private int port;
    Logger logger = LoggerFactory.getLogger("com.jmonitor.core.query.QueryListener");

    public QueryListener(MessageReceiver receiver, Properties p){
        this.receiver = receiver;
        this.port = Integer.parseInt(p.getProperty("queryPort"));
    }

    @Override
    public void run() {
        try {
//            int port = 5011;
            ServerSocket server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                new Thread(new Task(socket)).start();
            }
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    class Task implements Runnable {

        private Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                handleSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void handleSocket() throws Exception {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String temp;
            int index;
            while ((temp = br.readLine()) != null) {
                if ((index = temp.indexOf("EOF")) != -1) {  //遇到eof时就结束接收
                    sb.append(temp.substring(0, index));
                    break;
                }
                sb.append(temp);
            }
            System.out.println("get command: " + sb.toString());
            String command = sb.toString();
            String start = command.substring(0, command.indexOf("?"));
            Object result = null;
            if(start.equals("QUERY")){
                result = handleQuery(command);
            }
            Writer writer = new OutputStreamWriter(socket.getOutputStream());
            System.out.println(JsonUtil.toJson(result));
            writer.write(JsonUtil.toJson(result));
            writer.write("EOF\n");
            writer.flush();
            writer.close();
            br.close();
            socket.close();
        }

        private Object handleQuery(String command) throws Exception {
            QueryProcessor processor = new QueryProcessor(receiver);
            String content = command.substring(command.indexOf("?")+1, command.length());
            for(String s: content.split("&")){
                String[] value = s.split("=");
                if(value[0].equals("time")){
                    processor.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value[1]));
                }else if(value[0].equals("step")){
                    processor.setStep(value[1]);
                }else if(value[0].equals("type")){
                    processor.setType(value[1]);
                }
            }
            return processor.doProcess();
        }
    }


}
