package com.jmonitor.core.report.task.process;

import com.jmonitor.core.report.store.DatabaseManager;
import com.jmonitor.core.report.store.StoredReport;
import com.jmonitor.core.util.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by jmonitor on 2016/12/12.
 */
public abstract class AbstractTaskProcessor implements TaskProcessor {
    protected static final int DAY = 0x01;
    protected static final int WEEK = 0x02;
    protected static final int MONTH = 0x04;

    public boolean processDailyTask(String type, Date start) {
        Date end = TimeUtil.tomorrowZero(start);
        List<StoredReport> storedReports = DatabaseManager.queryHourlyReport(type, start, end);
        return mergeAndStore(storedReports, type, start, DAY);
    }

    public boolean processWeeklyTask(String type, Date start) {
        Date end = null;
        if (start.equals(TimeUtil.getCurrentWeek())) {
            end = TimeUtil.getCurrentDay();
        } else {
            end = new Date(start.getTime() + TimeUtil.ONE_WEEK);
        }
        List<StoredReport> storedReports = DatabaseManager.queryDailyReport(type, start, end);
        return mergeAndStore(storedReports, type, start, WEEK);
    }

    public boolean processMonthlyTask(String type, Date start) {
        Date end = null;
        if (start.equals(TimeUtil.getCurrentMonth())) {          //先这么写，感觉有问题
            end = TimeUtil.getCurrentDay();
        } else {
            end = TimeUtil.nextMonthStart(start);
        }
        List<StoredReport> storedReports = DatabaseManager.queryDailyReport(type, start, end);
        return mergeAndStore(storedReports, type, start, MONTH);
    }

    protected abstract boolean mergeAndStore(List<StoredReport> storedReports,
                                             String type, Date start, int chooseTable);


}
