package com.jmonitor.core.server;

import com.jmonitor.core.message.io.MessageReceiver;
import com.jmonitor.core.query.QueryListener;
import com.jmonitor.core.report.task.TaskConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jmonitor on 2016/10/2.
 */
public class JMonitorServer {
    private static JMonitorServer instance = new JMonitorServer();

    public void start(){
        Logger logger = LoggerFactory.getLogger("com.jmonitor.core.server.JMonitorServer");
        logger.info("JMonitorServer starting...");

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties");
        if (is == null) {
            is = MessageReceiver.class.getResourceAsStream("server.properties");
        }
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        MessageReceiver receiver;
        new Thread(receiver = new MessageReceiver(p)).start();
        new Thread(new TaskConsumer()).start();
        new Thread(new QueryListener(receiver,p)).start();
    }

    public static void main(String[] args){
        instance.start();
    }
}
