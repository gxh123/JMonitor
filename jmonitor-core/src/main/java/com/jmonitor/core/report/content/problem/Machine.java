package com.jmonitor.core.report.content.problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmonitor on 2016/12/11.
 */
public class Machine {

    private String ip;
    private Map<String, ProblemInfo> problemInfoMap = new HashMap<>();

    public Machine(){

    }

    public Machine(String ip){
        this.ip = ip;
    }

    public ProblemInfo findOrCreateProblemInfo(String detail){
        ProblemInfo problemInfo = problemInfoMap.get(detail);
        if(problemInfo == null){
            problemInfo = new ProblemInfo(detail);
            problemInfoMap.put(detail, problemInfo);
        }
        return problemInfo;
    }


    public List<ProblemInfo> getProblemInfoList() {
        List<ProblemInfo> list = new ArrayList<>();
        for(String key: problemInfoMap.keySet()) {
            list.add(problemInfoMap.get(key));
        }
        return list;
    }

    public ProblemInfo findProblemInfo(String detail){
        return problemInfoMap.get(detail);
    }

    public void putProblemInfo(String detail, ProblemInfo problemInfo){
        problemInfoMap.put(detail, problemInfo);
    }

    public Map<String, ProblemInfo> getProblemInfoMap() {
        return problemInfoMap;
    }

    public void setProblemInfoMap(Map<String, ProblemInfo> problemInfoMap) {
        this.problemInfoMap = problemInfoMap;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
