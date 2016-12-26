package com.jmonitor.core.query;

import com.jmonitor.core.message.io.MessageReceiver;

import java.util.Date;

/**
 * Created by jmonitor on 2016/12/22.
 */
public class QueryProcessor {
    private Date time;
    private String step;
    private String type;
    private MessageReceiver receiver;

    public QueryProcessor(MessageReceiver receiver){
        this.receiver = receiver;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object doProcess(){
        if(step.equals("hour")){
            return receiver.getCurrentReportByHour(type);
        }
        else    // TODO: 2016/12/22
            return null;
    }
}
