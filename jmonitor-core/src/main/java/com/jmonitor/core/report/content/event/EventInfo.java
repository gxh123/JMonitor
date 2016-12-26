package com.jmonitor.core.report.content.event;

/**
 * Created by jmonitor on 2016/10/5.
 */
public class EventInfo{
    private String eventType;
    private long failCount;
    private long totalCount;
    private double failPercent;

    public EventInfo(){
    }

    public EventInfo(String eventType){
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public long getFailCount() {
        return failCount;
    }

    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public double getFailPercent() {
        return failPercent;
    }

    public void setFailPercent(double failPercent) {
        this.failPercent = failPercent;
    }
}
