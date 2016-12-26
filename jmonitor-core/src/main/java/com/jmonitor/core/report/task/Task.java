package com.jmonitor.core.report.task;

import java.util.Date;

public class Task {
    private static final int STATUS_TODO = 1;
    private static final int STATUS_DOING = 2;
    private static final int STATUS_DONE = 3;
    private static final int STATUS_FAILED = 4;

    private int id;
    private String producer;
    private String consumer;
    private int failureCount;
    private String reportName;
    private Date reportPeriod;
    private int status;
    private int taskType;
    private Date creationDate;
    private Date startDate;
    private Date endDate;
    private int count;

    public String getConsumer() {
        return consumer;
    }

    public int getCount() {
        return count;
    }

    public int getId() {
        return id;
    }

    public String getReportName() {
        return reportName;
    }

    public Date getReportPeriod() {
        return reportPeriod;
    }

    public int getStatus() {
        return status;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setReportPeriod(Date reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Object[] getFieldsValue(){
        Object[] objects = new Object[10];
        objects[0] = this.producer;
        objects[1] = this.consumer;
        objects[2] = this.failureCount;
        objects[3] = this.reportName;
        objects[4] = this.reportPeriod;
        objects[5] = this.status;
        objects[6] = this.taskType;
        objects[7] = this.creationDate;
        objects[8] = this.startDate;
        objects[9] = this.endDate;
        return objects;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder(1024);

        sb.append("Task[");
        sb.append("consumer: ").append(consumer);
        sb.append(", count: ").append(count);
        sb.append(", creation-date: ").append(creationDate);
        sb.append(", end-date: ").append(endDate);
        sb.append(", failure-count: ").append(failureCount);
        sb.append(", id: ").append(id);
        sb.append(", producer: ").append(producer);
        sb.append(", report-name: ").append(reportName);
        sb.append(", report-period: ").append(reportPeriod);
        sb.append(", start-date: ").append(startDate);
        sb.append(", status: ").append(status);
        sb.append(", task-type: ").append(taskType);
        sb.append("]");
        return sb.toString();
    }

}




