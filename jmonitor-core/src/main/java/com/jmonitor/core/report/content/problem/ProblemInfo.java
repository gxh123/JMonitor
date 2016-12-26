package com.jmonitor.core.report.content.problem;

/**
 * Created by jmonitor on 2016/12/11.
 */
public class ProblemInfo {
    private String problemType;
    private long count;
    private String detail;


    public ProblemInfo(){

    }

    public ProblemInfo(String problemType){
        this.problemType = problemType;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
