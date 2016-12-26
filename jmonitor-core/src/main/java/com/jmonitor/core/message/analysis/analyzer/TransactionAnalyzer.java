package com.jmonitor.core.message.analysis.analyzer;

import com.jmonitor.core.message.analysis.AbstractMessageAnalyzer;
import com.jmonitor.core.message.analysis.Analyzer;
import com.jmonitor.core.message.type.Message;
import com.jmonitor.core.report.content.DefaultReportDelegate;
import com.jmonitor.core.report.content.transaction.TransactionInfo;
import com.jmonitor.core.report.content.transaction.TransactionReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by jmonitor on 2016/12/22.
 */
@Analyzer
public class TransactionAnalyzer  extends AbstractMessageAnalyzer<TransactionReport> {
    private TransactionReport report = new TransactionReport();
    private DefaultReportDelegate<TransactionReport> reportDelegate = new DefaultReportDelegate();
    private HashSet<String> ips = new HashSet<>();
    private HashMap<String, Long> sumOfDurations = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.message.analysis.analyzer.TransactionAnalyzer");

    @Override
    public void initialize(long startTime, long duration, long extraTime) {
        super.initialize(startTime, duration, extraTime);
        this.report.setStartTime(new Date(startTime));
        this.report.setEndTime(new Date(startTime + duration - 1));
    }

    @Override
    public TransactionReport getCurrentReport() {
        return report;
    }

    @Override
    public void doCheckpoint() {
        logger.info("TransactionAnalyzer doCheckpoint");
        reportDelegate.storeHourlyReports(getStartTime(), report, "Transaction");
        reportDelegate.storeIp(ips);
        reportDelegate.createTask(report.getStartTime(),"Transaction");
    }

    @Override
    public synchronized void process(Message message) {
        String ip = message.getIp();
        ips.add(ip);            //记录ip

        String type = message.getName();
        TransactionInfo transactionInfo = this.report.findOrCreateMachine(ip).findOrCreateTransactionInfo(type);
        transactionInfo.setTotalCount(transactionInfo.getTotalCount() + 1);
        if (!message.isSuccess()) {
            transactionInfo.setFailCount(transactionInfo.getFailCount());
        }
        transactionInfo.setFailPercent(transactionInfo.getFailCount() * 100.0 / transactionInfo.getTotalCount());
        long duration = message.getDuration();
        transactionInfo.setDuration(duration);
        transactionInfo.setMaxDuration(Math.max(transactionInfo.getMaxDuration(), duration));
        long sum = 0;
        if(sumOfDurations.containsKey(type)){
            sum = sumOfDurations.get(type);
        }
        sum += duration;
        sumOfDurations.put(type, sum);
        transactionInfo.setAvgDuration(sum/transactionInfo.getTotalCount());

//        System.out.println("transaction: " + message.getName() + ",duration :" + duration + ",MaxDuration: "
//                + transactionInfo.getMaxDuration() + ", sumOfDuration:" + sumOfDurations.get(type) + ",count:" + transactionInfo.getTotalCount());
    }
}
