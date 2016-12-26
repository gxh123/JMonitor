package com.jmonitor.core.report.task.process;

import com.jmonitor.core.report.content.DefaultReportDelegate;
import com.jmonitor.core.report.content.event.EventReport;
import com.jmonitor.core.report.store.DatabaseManager;
import com.jmonitor.core.report.store.StoredReport;
import com.jmonitor.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventTaskProcessor extends AbstractTaskProcessor {

	private EventMerger eventMerger = new EventMerger();
    private DefaultReportDelegate<EventReport> reportDelegate = new DefaultReportDelegate();
    private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.report.task.process.EventTaskProcessor");

    protected boolean mergeAndStore(List<StoredReport> storedReports, String type, Date start, int chooseTable){
        try {
            List<EventReport> reports = new ArrayList<>();
            for (StoredReport r : storedReports) {
                int length = (int) r.getContent().length();
                String s = new String(r.getContent().getBytes(1, length));
                reports.add(JsonUtil.fromJson(s, EventReport.class));
            }
            EventReport eventReport = eventMerger.merge(reports);
            byte[] binaryContent = reportDelegate.buildBinary(eventReport);
            StoredReport report = new StoredReport(type, binaryContent);
            report.setStartTime(start);
            if(chooseTable == DAY) {             //not good
                DatabaseManager.insertDailyReport(report);
            }else if(chooseTable == WEEK){
                DatabaseManager.insertWeeklyReport(report);
            }else{
                DatabaseManager.insertMonthlyReport(report);
            }
            return true;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
