package com.jmonitor.web.controller.chartdata;

import java.io.Serializable;

/**
 * Created by jmonitor on 2016/12/20.
 */
public class Memory implements Serializable{
    private long used;
    private long unused;

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getUnused() {
        return unused;
    }

    public void setUnused(long unused) {
        this.unused = unused;
    }
}
