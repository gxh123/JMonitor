package com.jmonitor.core.message.analysis.analyzer;

import com.jmonitor.core.message.analysis.AbstractMessageAnalyzer;
import com.jmonitor.core.message.analysis.Analyzer;
import com.jmonitor.core.message.type.Message;
import com.jmonitor.core.report.content.DefaultReportDelegate;
import com.jmonitor.core.report.content.problem.ProblemInfo;
import com.jmonitor.core.report.content.problem.ProblemReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;

/**
 * Created by jmonitor on 2016/10/2.
 */
@Analyzer
public class ProblemAnalyzer extends AbstractMessageAnalyzer {
    private ProblemReport report = new ProblemReport();
    private DefaultReportDelegate<ProblemReport> reportDelegate = new DefaultReportDelegate();
    private HashSet<String> ips = new HashSet<>();
    private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.message.analysis.analyzer.ProblemAnalyzer");

    @Override
    public void initialize(long startTime, long duration, long extraTime) {
        super.initialize(startTime, duration, extraTime);
        this.report.setStartTime(new Date(startTime));
        this.report.setEndTime(new Date(startTime + duration - 1));
    }

    @Override
    public Object getCurrentReport() {
        return report;
    }

    @Override
    public void doCheckpoint() {
        logger.info("ProblemAnalyzer doCheckpoint");
        reportDelegate.storeHourlyReports(getStartTime(), report, "Problem");
        reportDelegate.storeIp(ips);
        reportDelegate.createTask(report.getStartTime(),"Problem");
    }

    @Override
    public synchronized void process(Message message) {
        String ip = message.getIp();
        ips.add(ip);            //记录ip

        //detail相同认为是同一个error
        String errorDetail = message.getData().toString();
        ProblemInfo problemInfo = this.report.findOrCreateMachine(ip).findOrCreateProblemInfo(errorDetail);
        problemInfo.setProblemType(message.getName());
        problemInfo.setCount(problemInfo.getCount() + 1);
        problemInfo.setDetail(errorDetail);

//        System.out.println("process problem:"+ message.getName()+",from ip:"+ip);
    }
}
