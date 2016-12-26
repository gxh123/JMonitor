package com.jmonitor.core.report.task.process;

import com.jmonitor.core.report.content.DefaultReportDelegate;
import com.jmonitor.core.report.content.problem.ProblemReport;
import com.jmonitor.core.report.store.DatabaseManager;
import com.jmonitor.core.report.store.StoredReport;
import com.jmonitor.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProblemTaskProcessor extends AbstractTaskProcessor {

	private ProblemMerger problemMerger = new ProblemMerger();
    private DefaultReportDelegate<ProblemReport> reportDelegate = new DefaultReportDelegate();
    private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.report.task.process.ProblemTaskProcessor");

    protected boolean mergeAndStore(List<StoredReport> storedReports, String type, Date start, int chooseTable){
        try {
            List<ProblemReport> reports = new ArrayList<>();
            for (StoredReport r : storedReports) {
                int length = (int) r.getContent().length();
                String s = new String(r.getContent().getBytes(1, length));
                reports.add(JsonUtil.fromJson(s, ProblemReport.class));
            }
            ProblemReport problemReport = problemMerger.merge(reports);
            byte[] binaryContent = reportDelegate.buildBinary(problemReport);
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
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
