package com.jmonitor.core.report.task;

import com.jmonitor.core.report.store.DatabaseManager;
import com.jmonitor.core.util.NetworkUtil;
import com.jmonitor.core.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

public class TaskCreator {
	private static final int STATUS_TODO = 1;
	public static final int REPORT_HOUR = 0;
	public static final int REPORT_DAILY = 1;
	public static final int REPORT_WEEK = 2;
	public static final int REPORT_MONTH = 3;
	private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.report.task.TaskCreator");

	private void createDailyTask(Date period, String name){
		createTask(period, name, REPORT_DAILY);
	}

    private void createWeeklyTask(Date period, String name){
        createTask(period, name, REPORT_WEEK);
    }

	private void createMonthlyTask(Date period, String name){
		createTask(period, name, REPORT_MONTH);
	}

	protected void createTask(Date period, String name, int reportType){
		Task task = DatabaseManager.createStoredTask();
		task.setCreationDate(new Date());
		task.setProducer(NetworkUtil.getLocalIp());
        task.setConsumer("");
		task.setReportName(name);
		task.setReportPeriod(period);
		task.setStatus(STATUS_TODO);
		task.setTaskType(reportType);
		DatabaseManager.insertTask(task);
	}

	public void createTask(Date period, String name) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(period);

            int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
            if(hourOfDay == 0) {   //零点
                System.out.println("createDailyTask");
                createDailyTask(new Date(cal.getTime().getTime() - TimeUtil.ONE_DAY), name);
            }

            cal.add(Calendar.HOUR_OF_DAY, -hourOfDay);
            Date currentDay = cal.getTime();

            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == 1) {    //周日
                createWeeklyTask(new Date(currentDay.getTime() - 7 * TimeUtil.ONE_DAY), name);  //起始时间上周日
            }

            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            if (dayOfMonth == 1) {   //1号
                cal.add(Calendar.MONTH, -1);
                createMonthlyTask(cal.getTime(), name);            //起始时间上个月1号
            }
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
