package com.jmonitor.web.controller.chartdata;

import java.io.Serializable;

/**
 * Created by jmonitor on 2016/12/20.
 */
public class EchartsTimeData implements Serializable {

    public String time;         //时间
    public String value;        //值

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
