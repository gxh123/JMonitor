package com.jmonitor.web.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jmonitor.core.report.content.transaction.TransactionReport;
import com.jmonitor.core.util.JsonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jmonitor on 2016/12/16.
 */
public class TransactionReportService extends AbstractReportService<TransactionReport>  {

    @Override
    protected TransactionReport query(Date startTime, String tableName) {
        String sql = "SELECT * FROM " + tableName +" WHERE startTime = ? and reportType = ?";
        List<Record> records = Db.find(sql, startTime, "Transaction");
        if(records.size() != 0){
            String content = new String((byte[])records.get(0).get("content"));
            TransactionReport report = JsonUtil.fromJson(content, TransactionReport.class);
            return report;
        }else{
            return null;
        }
    }

    @Override
    protected TransactionReport sendQueryAndWait(Date startTime, String step) {
        String prefix = "QUERY";
        String command = prefix + "?time=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
                + "&step=" + step + "&type=" + "Transaction" + "EOF\n";
        return sendQueryAndWaitInternal(command, new TransactionReport());
    }
}
