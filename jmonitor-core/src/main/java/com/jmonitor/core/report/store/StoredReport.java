package com.jmonitor.core.report.store;

import javax.sql.rowset.serial.SerialBlob;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jmonitor on 2016/10/6.
 */
public class StoredReport implements Serializable{
    private int id;
    private String reportType;
    private SerialBlob content;
    private int contentFormat;
    private Date startTime;
    private Date createTime;

    public StoredReport(){

    }

    public StoredReport(String reportType, byte[] content){
        this.reportType = reportType;
        try {
            this.content = new SerialBlob(content);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.contentFormat = 1;
    }

    public SerialBlob getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        try {
            this.content = new SerialBlob(content);
        }catch (Exception e){
            //ignore
        }
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public int getContentFormat() {
        return contentFormat;
    }

    public void setContentFormat(int contentFormat) {
        this.contentFormat = contentFormat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Object[] getFieldsValue(){
        Object[] objects = new Object[5];
        objects[0] = this.reportType;
        objects[1] = this.contentFormat;
        objects[2] = this.content;
        objects[3] = this.startTime;
        objects[4] = new Date(System.currentTimeMillis());
        return objects;
    }
}
