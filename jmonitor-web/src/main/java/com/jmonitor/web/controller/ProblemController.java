package com.jmonitor.web.controller;

import com.jmonitor.core.report.content.problem.ProblemInfo;
import com.jmonitor.core.report.content.problem.ProblemReport;
import com.jmonitor.web.service.ProblemReportService;

import java.util.Date;
import java.util.List;

/**
 * ProblemController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class ProblemController extends AbstractController {
    {
        reportService = new ProblemReportService();
    }


    public void index(){
        processInternal(true);
        render("/problems.jsp");
    }

    public void now(){
        setSessionAttr("startTime", null);
        setSessionAttr("step", null);
        processInternal(true);
        render("/problems.jsp");
    }

    public void decrease() {
        processInternal(false);
        render("/problems.jsp");
    }

    public void increase() {
        processInternal(true);
        render("/problems.jsp");
    }

    private void processInternal(boolean isIncrease){
        Date time = getTime(isIncrease);
        String ip = getIp();
        System.out.println("请求是：" + "time:" + time +", ip: " + ip);
        List<ProblemInfo> problems = queryProblems(time, ip);
        if(problems != null) {
            for (ProblemInfo problem : problems) {
                System.out.println(problem.getDetail());
                System.out.println("-------------------------");
            }
        }
        setAttr("problems", problems);
    }

    private List<ProblemInfo> queryProblems(Date time, String ip){
        ProblemReport report = null;
        String step = getStep();
        switch (step){
            case "month":
                report = (ProblemReport)reportService.getMonthly(time);
                break;
            case "week":
                report = (ProblemReport)reportService.getWeekly(time);
                break;
            case "day":
                report = (ProblemReport)reportService.getDaily(time);
                break;
            case "hour":
                report = (ProblemReport)reportService.getHourly(time);
                break;
            default:
                System.out.println("error step");
        }
        List<ProblemInfo> problems = null;
        if (report != null) {
            if(report.getMachines() != null && report.getMachines().containsKey(ip)) {
                problems = report.getMachines().get(ip).getProblemInfoList();
            }
        }
        return problems;
    }

}


