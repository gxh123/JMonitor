package com.jmonitor.web.service;

import java.util.Date;
import java.util.List;

/**
 * Created by jmonitor on 2016/12/16.
 */
public interface ReportService<T> {
    public static final String HOURLY_REPORT_TABLE = "hourly_report";
    public static final String DAILY_REPORT_TABLE = "daily_report";
    public static final String WEEKLY_REPORT_TABLE = "weekly_report";
    public static final String MONTHLY_REPORT_TABLE = "monthly_report";

    public T getHourly(Date startTime);

    public T getDaily(Date startTime);

    public T getWeekly(Date startTime);

    public T getMonthly(Date startTime);

    public List<String> allMachines();

}
