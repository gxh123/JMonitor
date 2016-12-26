package com.jmonitor.core.report.content.status;

import com.jmonitor.core.status.StatusInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jmonitor on 2016/12/11.
 */
public class Machine {

    private String ip;
    private Map<Long, StatusInfo> statusInfoMap = new TreeMap<>();

    public Machine(){

    }

    public Machine(String ip){
        this.ip = ip;
    }

    public StatusInfo findOrCreateStatusInfo(Long timeStamp){
        StatusInfo statusInfo = statusInfoMap.get(timeStamp);
        if(statusInfo == null){
            statusInfo = new StatusInfo();
            statusInfoMap.put(timeStamp, statusInfo);
        }
        return statusInfo;
    }

    public List<StatusInfo> getStatusInfoList() {
        List<StatusInfo> list = new ArrayList<>();
        for(long key: statusInfoMap.keySet()) {
            list.add(statusInfoMap.get(key));
        }
        return list;
    }

    public StatusInfo findStatusInfo(Long timeStamp){
        return statusInfoMap.get(timeStamp);
    }

    public void putStatusInfo(Long timeStamp, StatusInfo statusInfo){
        statusInfoMap.put(timeStamp, statusInfo);
    }

    public Map<Long, StatusInfo> getStatusInfoMap() {
        return statusInfoMap;
    }

    public void setStatusInfoMap(Map<Long, StatusInfo> statusInfoMap) {
        this.statusInfoMap = statusInfoMap;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
