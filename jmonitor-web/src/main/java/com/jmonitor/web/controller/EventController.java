package com.jmonitor.web.controller;

import com.jmonitor.core.report.content.event.EventInfo;
import com.jmonitor.core.report.content.event.EventReport;
import com.jmonitor.web.service.EventReportService;

import java.util.Date;
import java.util.List;

/**
 * EventController
 */
public class EventController extends AbstractController {
    {
        reportService = new EventReportService();
    }

	public void index(){
        processInternal(true);
		render("/events.jsp");
	}

    public void now(){
        setSessionAttr("startTime", null);
        setSessionAttr("step", null);
        processInternal(true);
        render("/events.jsp");
    }

    public void decrease() {
        processInternal(false);
        render("/events.jsp");
    }

    public void increase() {
        processInternal(true);
        render("/events.jsp");
    }

    private void processInternal(boolean isIncrease){

        Date time = getTime(isIncrease);
        String ip = getIp();
        System.out.println("请求是：" + "time:" + time +", ip: " + ip);
        List<EventInfo> events = queryEvents(time, ip);
        setAttr("events", events);
    }

    private List<EventInfo> queryEvents(Date time, String ip){
        EventReport report = null;
        String step = getStep();
        switch (step){
            case "month":
                report = (EventReport)reportService.getMonthly(time);
                break;
            case "week":
                report = (EventReport)reportService.getWeekly(time);
                break;
            case "day":
                report = (EventReport)reportService.getDaily(time);
                break;
            case "hour":
                report = (EventReport)reportService.getHourly(time);
                break;
            default:
                System.out.println("error step");
        }
        List<EventInfo> events = null;
        if (report != null) {
            if(report.getMachines() != null && report.getMachines().containsKey(ip)) {
                events = report.getMachines().get(ip).getEventInfoList();
            }
        }
        return events;
    }
}


