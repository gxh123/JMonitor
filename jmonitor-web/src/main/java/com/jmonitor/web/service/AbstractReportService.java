package com.jmonitor.web.service;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jmonitor.core.util.JsonUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jmonitor on 2016/12/17.
 */
public abstract class AbstractReportService<T> implements ReportService<T> {
    @Override
    public T getHourly(Date startTime) {
        if (isNowByHour(startTime)){    //当前小时的数据 数据库中没有，需要向JMonitorServer请求
            return sendQueryAndWait(startTime, "hour");
        }else {
            return query(startTime, HOURLY_REPORT_TABLE);
        }
    }

    @Override
    public T getDaily(Date startTime) {
        if (isNowByDay(startTime)){
            return null;                //还未实现
        }else {
            return query(startTime, DAILY_REPORT_TABLE);
        }
    }

    @Override
    public T getWeekly(Date startTime) {
        if (isNowByWeek(startTime)){
            return null;                //还未实现
        }else {
            return query(startTime, WEEKLY_REPORT_TABLE);
        }
    }

    @Override
    public T getMonthly(Date startTime) {
        if (isNowByMonth(startTime)){
            return null;                //还未实现
        }else {
            return query(startTime, MONTHLY_REPORT_TABLE);
        }
    }

    @Override
    public List<String> allMachines(){
        String sql = "SELECT ip FROM machines";
        List<Record> records = Db.find(sql);
        if(records.size() == 0){
            return new ArrayList<String>();
        }
        List<String> ips = new ArrayList<String>();
        for(Record r: records){
            ips.add((String)r.get("ip"));
        }
        return ips;
    }

    protected abstract T query(Date startTime, String tableName);

    private boolean isNowByHour(Date startTime){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTime().getTime()/1000 == startTime.getTime()/1000;
    }

    private boolean isNowByDay(Date startTime){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime().getTime()/1000 == startTime.getTime()/1000;
    }

    private boolean isNowByWeek(Date startTime){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        //本周第一天
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) day_of_week = 7;
        calendar.add(Calendar.DAY_OF_MONTH, -day_of_week + 1);

        return calendar.getTime().getTime()/1000 == startTime.getTime()/1000;
    }

    private boolean isNowByMonth(Date startTime){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime().getTime()/1000 == startTime.getTime()/1000;
    }

    protected abstract T sendQueryAndWait(Date startTime, String step);

    protected T sendQueryAndWaitInternal(String command, T t){
        T object = null;
        StringBuffer sb = new StringBuffer();
        try {
//            String host = "127.0.0.1";  //要连接的服务端IP地址
            String host = PropKit.get("serverIp");
//            int port = 5011;            //要连接的服务端对应的监听端口
            int port = PropKit.getInt("serverPort");
            System.out.println("host:"+host + ",port:" + port);
            Socket client = new Socket(host, port);
            Writer writer = new OutputStreamWriter(client.getOutputStream());

            System.out.println("command:"+command);
            writer.write(command);
            writer.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //设置超时间为5秒
            client.setSoTimeout(5000);
            int index;
            String temp = null;
            try {
                while ((temp = br.readLine()) != null) {
                    if ((index = temp.indexOf("EOF")) != -1) {  //遇到eof时就结束接收
                        sb.append(temp.substring(0, index));
                        break;
                    }
                    sb.append(temp);
                }
            } catch (SocketTimeoutException e) {
                System.out.println("数据读取超时");
            }
            if(sb.toString().length() > 0) {
                object = (T) JsonUtil.fromJson(sb.toString(), t.getClass());
            }
            System.out.println("from server: " + sb);
            writer.close();
            br.close();
            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }
}
