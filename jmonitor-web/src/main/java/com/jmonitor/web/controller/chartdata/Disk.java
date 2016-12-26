package com.jmonitor.web.controller.chartdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmonitor on 2016/12/20.
 */
public class Disk implements Serializable{
    private List<String> names = new ArrayList<>();
    private List<Long> used = new ArrayList<>();
    private List<Long> unused = new ArrayList<>();

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void addName(String name){
        this.names.add(name);
    }

    public List<Long> getUsed() {
        return used;
    }

    public void setUsed(List<Long> used) {
        this.used = used;
    }

    public void addUsed(Long used) {
        this.used.add(used);
    }

    public List<Long> getUnused() {
        return unused;
    }

    public void setUnused(List<Long> unused) {
        this.unused = unused;
    }

    public void addUnused(Long unused) {
        this.unused.add(unused);
    }
}
