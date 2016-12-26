package com.jmonitor.web.controller;

import com.jmonitor.core.report.content.transaction.TransactionInfo;
import com.jmonitor.core.report.content.transaction.TransactionReport;
import com.jmonitor.web.service.TransactionReportService;

import java.util.Date;
import java.util.List;

/**
 * EventController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class TransactionController  extends AbstractController {
	{
		reportService = new TransactionReportService();
	}

	public void index(){
		processInternal(true);
		render("/transactions.jsp");
	}

	public void now(){
		setSessionAttr("startTime", null);
		setSessionAttr("step", null);
		processInternal(true);
		render("/transactions.jsp");
	}

	public void decrease() {
		processInternal(false);
		render("/transactions.jsp");
	}

	public void increase() {
		processInternal(true);
		render("/transactions.jsp");
	}

	private void processInternal(boolean isIncrease){

		Date time = getTime(isIncrease);
		String ip = getIp();
		System.out.println("请求是：" + "time:" + time +", ip: " + ip);
		List<TransactionInfo> transactions = queryTransactions(time, ip);
		setAttr("transactions", transactions);
	}

	private List<TransactionInfo> queryTransactions(Date time, String ip){
		TransactionReport report = null;
		String step = getStep();
		switch (step){
			case "month":
				report = (TransactionReport)reportService.getMonthly(time);
				break;
			case "week":
				report = (TransactionReport)reportService.getWeekly(time);
				break;
			case "day":
				report = (TransactionReport)reportService.getDaily(time);
				break;
			case "hour":
				report = (TransactionReport)reportService.getHourly(time);
				break;
			default:
				System.out.println("error step");
		}
		List<TransactionInfo> transactions = null;
		if (report != null) {
			if(report.getMachines() != null && report.getMachines().containsKey(ip)) {
				transactions = report.getMachines().get(ip).getTransactionInfoList();
			}
		}
		return transactions;
	}
}


