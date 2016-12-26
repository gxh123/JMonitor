package com.jmonitor.core.report.content.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jmonitor on 2016/12/22.
 */
public class Machine {
    private String ip;
    private Map<String, TransactionInfo> transactionInfoMap = new HashMap<>();

    public Machine(){

    }

    public Machine(String ip){
        this.ip = ip;
    }

    public TransactionInfo findOrCreateTransactionInfo(String transactionType){
        TransactionInfo transactionInfo = transactionInfoMap.get(transactionType);
        if(transactionInfo == null){
            transactionInfo = new TransactionInfo(transactionType);
            transactionInfoMap.put(transactionType, transactionInfo);
        }
        return transactionInfo;
    }

    public List<TransactionInfo> getTransactionInfoList() {
        List<TransactionInfo> list = new ArrayList<>();
        for(String key: transactionInfoMap.keySet()) {
            list.add(transactionInfoMap.get(key));
        }
        return list;
    }

    public TransactionInfo findTransactionInfo(String transactionType){
        return transactionInfoMap.get(transactionType);
    }

    public void putTransactionInfo(String transactionType, TransactionInfo transactionInfo){
        transactionInfoMap.put(transactionType, transactionInfo);
    }

    public Map<String, TransactionInfo> getTransactionInfoMap() {
        return transactionInfoMap;
    }

    public void setTransactionInfoMap(Map<String, TransactionInfo> transactionInfoMap) {
        this.transactionInfoMap = transactionInfoMap;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


}
