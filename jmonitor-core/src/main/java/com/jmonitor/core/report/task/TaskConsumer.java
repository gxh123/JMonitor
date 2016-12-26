package com.jmonitor.core.report.task;

import com.jmonitor.core.report.store.DatabaseManager;
import com.jmonitor.core.report.task.process.*;
import com.jmonitor.core.util.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaskConsumer implements Runnable {
	public static final int STATUS_TODO = 1;
	public static final int STATUS_DONE = 2;
	public static final int STATUS_FAIL = 3;
	private volatile boolean running = true;
    private Map<String, TaskProcessor> reportBuilders = new HashMap<String, TaskProcessor>();  //TaskProcessor process task to build report
	private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.report.task.TaskConsumer");


    public TaskConsumer(){
        reportBuilders.put("Event",new EventTaskProcessor());
        reportBuilders.put("Problem",new ProblemTaskProcessor());
        reportBuilders.put("Status",new StatusTaskProcessor());
		reportBuilders.put("Transaction",new TransactionTaskProcessor());
    }

    public boolean processTask(Task task) {
		boolean result = false;
		int type = task.getTaskType();
		String reportName = task.getReportName();
		Date reportPeriod = task.getReportPeriod();
		TaskProcessor reportBuilder = reportBuilders.get(reportName);

		if (type == TaskCreator.REPORT_DAILY) {
			result = reportBuilder.processDailyTask(reportName, reportPeriod);
		} else if (type == TaskCreator.REPORT_WEEK) {
			result = reportBuilder.processWeeklyTask(reportName, reportPeriod);
		} else if (type == TaskCreator.REPORT_MONTH) {
			result = reportBuilder.processMonthlyTask(reportName, reportPeriod);
		}
		return result;
	}

	public void run() {
		String ip = NetworkUtil.getLocalIp();
		while (running) {
			try {
                Task task = findTodoTask();
				if (task != null) {
					task.setConsumer(ip);
                    if(!processTask(task)) {
                        updateToFailure(task);
                    }
                    else{
                        updateToDone(task);
                    }
				}
			} catch (Throwable e) {
				logger.error(e.getMessage());
			}
		}
	}

	public void stop() {
		this.running = false;
	}

	protected Task findTodoTask() {
        return DatabaseManager.findTaskByStatusConsumer(STATUS_TODO, null);
	}

	protected void updateToDone(Task doing) {
		doing.setStatus(STATUS_DONE);
		doing.setEndDate(new Date());
		DatabaseManager.updateTaskToDone(doing);
	}

	protected void updateToFailure(Task doing) {
		doing.setStatus(STATUS_FAIL);
		doing.setEndDate(new Date());
		DatabaseManager.updateTaskToFail(doing);
	}
}
