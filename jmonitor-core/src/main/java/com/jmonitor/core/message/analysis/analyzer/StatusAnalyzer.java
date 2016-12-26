package com.jmonitor.core.message.analysis.analyzer;

import com.jmonitor.core.message.analysis.AbstractMessageAnalyzer;
import com.jmonitor.core.message.analysis.Analyzer;
import com.jmonitor.core.message.type.Message;
import com.jmonitor.core.report.content.DefaultReportDelegate;
import com.jmonitor.core.report.content.status.StatusReport;
import com.jmonitor.core.status.StatusInfo;
import com.jmonitor.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;

/**
 * Created by jmonitor on 2016/10/5.
 */
@Analyzer
public class StatusAnalyzer extends AbstractMessageAnalyzer {
    private StatusReport report = new StatusReport();
    private DefaultReportDelegate<StatusReport> reportDelegate = new DefaultReportDelegate();
    private HashSet<String> ips = new HashSet<>();
    private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.message.analysis.analyzer.StatusAnalyzer");

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
        logger.info("StatusAnalyzer doCheckpoint");
        reportDelegate.storeHourlyReports(getStartTime(), report, "Status");
        reportDelegate.storeIp(ips);
        reportDelegate.createTask(report.getStartTime(),"Status");
    }

    @Override
    public synchronized void  process(Message message) {
        String ip = message.getIp();
        ips.add(ip);

        StatusInfo statusInfo = JsonUtil.fromJson(message.getData().toString(), StatusInfo.class);
        this.report.findOrCreateMachine(ip).putStatusInfo(message.getTimestamp(), statusInfo);

//        System.out.println("process status from "+ message.getName()+",from ip:"+ip);
    }
}
