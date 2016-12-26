package com.jmonitor.web.controller.chartdata;

import java.io.Serializable;

/**
 * Created by jmonitor on 2016/12/20.
 */
public class SystemInfo implements Serializable {
    public String os;
    public String arch;
    public int processors;
    public long externalMemory;
    public long internalMemory;
    public long maxHeap;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public int getProcessors() {
        return processors;
    }

    public void setProcessors(int processors) {
        this.processors = processors;
    }

    public long getExternalMemory() {
        return externalMemory;
    }

    public void setExternalMemory(long externalMemory) {
        this.externalMemory = externalMemory;
    }

    public long getInternalMemory() {
        return internalMemory;
    }

    public void setInternalMemory(long internalMemory) {
        this.internalMemory = internalMemory;
    }

    public long getMaxHeap() {
        return maxHeap;
    }

    public void setMaxHeap(long maxHeap) {
        this.maxHeap = maxHeap;
    }
}
