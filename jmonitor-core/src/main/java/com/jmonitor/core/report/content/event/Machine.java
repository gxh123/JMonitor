package com.jmonitor.core.report.content.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmonitor on 2016/10/5.
 */
public class Machine{
    private String ip;
    private Map<String, EventInfo> eventInfoMap = new HashMap<>();

    public Machine(){

    }

    public Machine(String ip){
        this.ip = ip;
    }

    public EventInfo findOrCreateEventInfo(String eventType){
        EventInfo eventInfo = eventInfoMap.get(eventType);
        if(eventInfo == null){
            eventInfo = new EventInfo(eventType);
            eventInfoMap.put(eventType, eventInfo);
        }
        return eventInfo;
    }

    public List<EventInfo> getEventInfoList() {
        List<EventInfo> list = new ArrayList<>();
        for(String key: eventInfoMap.keySet()) {
            list.add(eventInfoMap.get(key));
        }
        return list;
    }

    public EventInfo findEventInfo(String eventType){
        return eventInfoMap.get(eventType);
    }

    public void putEventInfo(String eventType, EventInfo eventInfo){
        eventInfoMap.put(eventType, eventInfo);
    }

    public Map<String, EventInfo> getEventInfoMap() {
        return eventInfoMap;
    }

    public void setEventInfoMap(Map<String, EventInfo> eventInfoMap) {
        this.eventInfoMap = eventInfoMap;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
