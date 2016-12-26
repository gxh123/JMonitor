package com.jmonitor.core.report.task.process;

import java.util.Date;

public interface TaskProcessor {

	public boolean processDailyTask(String name, Date period);

	public boolean processWeeklyTask(String name, Date period);

	public boolean processMonthlyTask(String name, Date period);

}
