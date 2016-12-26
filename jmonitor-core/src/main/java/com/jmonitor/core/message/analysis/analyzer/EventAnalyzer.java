package com.jmonitor.core.message.analysis.analyzer;

import com.jmonitor.core.message.analysis.AbstractMessageAnalyzer;
import com.jmonitor.core.message.analysis.Analyzer;
import com.jmonitor.core.message.type.Message;
import com.jmonitor.core.report.content.DefaultReportDelegate;
import com.jmonitor.core.report.content.event.EventInfo;
import com.jmonitor.core.report.content.event.EventReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;

//import com.jmonitor.core.report.delegate.EventReportDelegate;

/**
 * Created by jmonitor on 2016/10/2.
 */
@Analyzer
public class EventAnalyzer extends AbstractMessageAnalyzer<EventReport> {
    private EventReport report = new EventReport();
    private DefaultReportDelegate<EventReport> reportDelegate = new DefaultReportDelegate();
    private HashSet<String> ips = new HashSet<>();
    private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.message.analysis.analyzer.EventAnalyzer");

    @Override
    public void initialize(long startTime, long duration, long extraTime) {
        super.initialize(startTime, duration, extraTime);
        this.report.setStartTime(new Date(startTime));
        this.report.setEndTime(new Date(startTime + duration - 1));
    }

    @Override
    public EventReport getCurrentReport() {
        return report;
    }

    @Override
    public void doCheckpoint() {
        logger.info("EventAnalyzer doCheckpoint");
        reportDelegate.storeHourlyReports(getStartTime(), report, "Event");
        reportDelegate.storeIp(ips);
        reportDelegate.createTask(report.getStartTime(),"Event");
    }

    @Override
    public synchronized void process(Message message) {
        String ip = message.getIp();
        ips.add(ip);            //记录ip

        EventInfo eventInfo = this.report.findOrCreateMachine(ip).findOrCreateEventInfo(message.getName());
        eventInfo.setTotalCount(eventInfo.getTotalCount() + 1);
        if (!message.isSuccess()) {
            eventInfo.setFailCount(eventInfo.getFailCount());
        }
        eventInfo.setFailPercent(eventInfo.getFailCount() * 100.0 / eventInfo.getTotalCount());
    }

}
