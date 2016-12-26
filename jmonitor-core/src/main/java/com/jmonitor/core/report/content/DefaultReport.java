package com.jmonitor.core.report.content;

import java.util.Date;

/**
 * Created by jmonitor on 2016/12/11.
 */
public class DefaultReport {

    private Date startTime;
    private Date endTime;

    public DefaultReport(){

    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
