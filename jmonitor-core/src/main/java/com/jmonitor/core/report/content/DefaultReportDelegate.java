package com.jmonitor.core.report.content;

import com.jmonitor.core.report.store.DatabaseManager;
import com.jmonitor.core.report.store.StoredReport;
import com.jmonitor.core.report.task.TaskCreator;
import com.jmonitor.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;

/**
 * Created by jmonitor on 2016/12/11.
 */
public class DefaultReportDelegate<T> {
    private TaskCreator taskCreator = new TaskCreator();
    private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.report.content.DefaultReportDelegate");

    public byte[] buildBinary(T report) {
        return JsonUtil.toJson(report).getBytes();
    }

    //只是将report序列化后存到数据库中，不是持久化。因为只需要将整个内容作为content存进去，不需要对
    //report里面的内容进行增删改查，所以不用持久化
    public void storeHourlyReports(long startTime, T report, String type) {
        Date period = new Date(startTime);
        try {
            StoredReport r = DatabaseManager.createStoredReport();
            r.setContentFormat(1);
            byte[] binaryContent = buildBinary(report);
            r.setContent(binaryContent);
            r.setReportType(type);
            r.setStartTime(period);
            DatabaseManager.insertHourlyReport(r);
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
    }

    public void storeIp(Set<String> ips){
        DatabaseManager.insertIp(ips);
    }

    public void createTask(Date startTime, String type) {
        taskCreator.createTask(startTime, type);
    }
}
