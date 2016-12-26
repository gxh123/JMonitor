package com.jmonitor.web.controller;

import com.jfinal.core.Controller;
import com.jmonitor.web.service.ReportService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jmonitor on 2016/12/17.
 */
public abstract class AbstractController extends Controller {
    protected ReportService reportService;

    protected String getStep(){
        String step = getPara("step");
        if(step == null){
            step = getSessionAttr("step");
            if(step == null){
                step = "hour";
            }
        }
        setSessionAttr("step",step);
        return step;
    }

    protected String getIp(){
        List<String> ips = getSessionAttr("ips");     //放在session里，如果放application里，如果新增了机器这里还是不会变化
        if(ips == null){
            ips = reportService.allMachines();
            setSessionAttr("ips", ips);
        }

        String currentIp = getPara("ip");             //首先从请求里找ip
        if(currentIp == null) {
            currentIp = getSessionAttr("currentIp");  //请求里没有就从session里找当前ip
            if(currentIp == null){
                currentIp = ips.get(0);               //session里也没有就选数据库里的第一个
            }
        }
        setSessionAttr("currentIp", currentIp);
        return currentIp;
    }

    protected Date getTime(boolean isIncrease){
        String startContent = getSessionAttr("startTime");
        Date startTime = null;
        if(startContent != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
            try {
                startTime = df.parse(startContent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            startTime = new Date();
            startTime.setTime(startTime.getTime() - 1000*60*60);  //-1 hour
        }

        //for ip 跳转
        String newIp = getPara("ip");
        String oldIp = getSessionAttr("currentIp");
        if(newIp != null && oldIp != null && !newIp.equals(oldIp)              //ugly
                || getParaMap().isEmpty() && getSessionAttr("startTime")!=null){
            return startTime;
        }

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startTime);
        Calendar endCal = Calendar.getInstance();

        String step = getStep();
        switch (step) {
            case "month":
                startCal.add(Calendar.MONTH, isIncrease ? 1 : -1);
                startCal.set(Calendar.DAY_OF_MONTH, 1);
                startCal.set(Calendar.HOUR_OF_DAY, 0);
                startCal.set(Calendar.MINUTE, 0);
                startCal.set(Calendar.SECOND, 0);
                endCal.set(Calendar.YEAR, startCal.get(Calendar.YEAR));
                endCal.set(Calendar.MONTH, startCal.get(Calendar.MONTH));
                endCal.add(Calendar.MONTH, 1);
                endCal.set(Calendar.DAY_OF_MONTH, startCal.get(Calendar.DAY_OF_MONTH));
                endCal.add(Calendar.DAY_OF_MONTH, -1);
                endCal.set(Calendar.HOUR_OF_DAY, 23);
                endCal.set(Calendar.MINUTE, 59);
                endCal.set(Calendar.SECOND, 59);
                break;
            case "week":
                startCal.add(Calendar.WEEK_OF_YEAR, isIncrease ? 1 : -1);   //从当天开始加7天
                int day_of_week = startCal.get(Calendar.DAY_OF_WEEK) - 1;
                if (day_of_week == 0) day_of_week = 7;
                startCal.add(Calendar.DAY_OF_MONTH, -day_of_week + 1);    //上一周

                startCal.set(Calendar.HOUR_OF_DAY, 0);
                startCal.set(Calendar.MINUTE, 0);
                startCal.set(Calendar.SECOND, 0);
                endCal.set(Calendar.YEAR, startCal.get(Calendar.YEAR));
                endCal.set(Calendar.MONTH, startCal.get(Calendar.MONTH));
                endCal.set(Calendar.DAY_OF_MONTH, startCal.get(Calendar.DAY_OF_MONTH));
                endCal.add(Calendar.DAY_OF_MONTH, 6);
                endCal.set(Calendar.HOUR_OF_DAY, 23);
                endCal.set(Calendar.MINUTE, 59);
                endCal.set(Calendar.SECOND, 59);
                break;
            case "day":
                startCal.add(Calendar.DAY_OF_MONTH, isIncrease ? 1 : -1);
                startCal.set(Calendar.HOUR_OF_DAY, 0);
                startCal.set(Calendar.MINUTE, 0);
                startCal.set(Calendar.SECOND, 0);
                endCal.set(Calendar.YEAR, startCal.get(Calendar.YEAR));
                endCal.set(Calendar.MONTH, startCal.get(Calendar.MONTH));
                endCal.set(Calendar.DAY_OF_MONTH, startCal.get(Calendar.DAY_OF_MONTH));
                endCal.set(Calendar.HOUR_OF_DAY, 23);
                endCal.set(Calendar.MINUTE, 59);
                endCal.set(Calendar.SECOND, 59);
                break;
            case "hour":
                startCal.add(Calendar.HOUR_OF_DAY, isIncrease ? 1 : -1);
                startCal.set(Calendar.MINUTE, 0);
                startCal.set(Calendar.SECOND, 0);
                endCal.set(Calendar.YEAR, startCal.get(Calendar.YEAR));
                endCal.set(Calendar.MONTH, startCal.get(Calendar.MONTH));
                endCal.set(Calendar.DAY_OF_MONTH, startCal.get(Calendar.DAY_OF_MONTH));
                endCal.set(Calendar.HOUR_OF_DAY, startCal.get(Calendar.HOUR_OF_DAY));
                endCal.set(Calendar.MINUTE, 59);
                endCal.set(Calendar.SECOND, 59);
                break;
            default:
                System.out.println("error step");
        }
        setSessionAttr("startTime", new SimpleDateFormat("yyyy-MM-dd HH").format(startCal.getTime()));
        setSessionAttr("endTime", new SimpleDateFormat("yyyy-MM-dd HH").format(endCal.getTime()));
        return startCal.getTime();
    }

}
