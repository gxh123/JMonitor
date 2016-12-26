package com.jmonitor.core.report.store;

import com.jmonitor.core.report.task.Task;
import com.jmonitor.core.util.JdbcUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.jmonitor.core.report.task.TaskConsumer.STATUS_DONE;
import static com.jmonitor.core.report.task.TaskConsumer.STATUS_FAIL;
import static com.jmonitor.core.util.TimeUtil.*;

/**
 * Created by jmonitor on 2016/10/6.
 */
public class DatabaseManager {
    public final static String TASK_TABLE_NAME = "TASK";
    public final static String HOURLY_REPORT_TABLE_NAME = "HOURLY_REPORT";
    public final static String DAILY_REPORT_TABLE_NAME = "daily_report";
    public final static String WEEKLY_REPORT_TABLE_NAME = "WEEKLY_REPORT";
    public final static String MONTHLY_REPORT_TABLE_NAME = "MONTHLY_REPORT";
    private static Logger logger = LoggerFactory.getLogger("com.jmonitor.core.report.store.DatabaseManager");

    private static ComboPooledDataSource dataSource = JdbcUtil.getDataSource();

    public static StoredReport createStoredReport(){
        return new StoredReport();
    }

    public static Task createStoredTask(){
        return new Task();
    }

    public static void insertTask(Task task){
        QueryRunner qr = new QueryRunner(dataSource);
        String sql = "insert into task(producer,consumer,failureCount,reportName,reportPeriod, " +
                "status, taskType, creationDate, startDate, endDate) values(?, ?, ?, ?, ?, ?, ?, ? ,?, ?)";
        Object[] params = task.getFieldsValue();
        try {
            qr.update(sql, params);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static Task findTaskByStatusConsumer(int status, String ip){
        ResultSetHandler<Task> rsh = new BeanHandler<Task>(Task.class);
        QueryRunner qr = new QueryRunner(dataSource);
        String sql;
        if(ip != null) {
            ip = "\'" + ip + "\'";
            sql = "select * from task where task.consumer = " + ip + " and task.status = " + status;
        }else {
            sql = "select * from task where task.status = " + status;
        }
        Task task = null;
        try {
            task = qr.query(sql, rsh);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return task;
    }

    private static boolean updateTaskToStatus(Task task, int status){
        String reportName = "\'" + task.getReportName() + "\'";
        int taskType = task.getTaskType();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        String reportPeriod = "\'" + format.format(task.getReportPeriod()) + "\'";

        QueryRunner qr = new QueryRunner(dataSource);
        String sql = "update task set task.status = ? where task.reportName = " + reportName
                + " and task.taskType = " + taskType + " and task.reportPeriod = " + reportPeriod;
        Object[] params = new Object[] { status };
        try {
            qr.update(sql, params);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return true;
    }

    public static boolean updateTaskToDone(Task task){
        return updateTaskToStatus(task, STATUS_DONE);
    }

    public static boolean updateTaskToFail(Task task){
        return updateTaskToStatus(task, STATUS_FAIL);
    }

    public static void insertHourlyReport(StoredReport report){
        insertReport(report, HOURLY_REPORT_TABLE_NAME);
    }

    public static void insertDailyReport(StoredReport report){
        insertReport(report, DAILY_REPORT_TABLE_NAME);
    }

    public static void insertWeeklyReport(StoredReport report){
        insertReport(report, WEEKLY_REPORT_TABLE_NAME);
    }

    public static void insertMonthlyReport(StoredReport report){
        insertReport(report, MONTHLY_REPORT_TABLE_NAME);
    }

    private static void insertReport(StoredReport report, String tableName){
        QueryRunner qr = new QueryRunner(dataSource);
        String sql = "insert into " + tableName + "(reportType,contentFormat,content,startTime,createTime) values(?, ?, ?, ?, ?)";
        Object[] params = report.getFieldsValue();
        try {
            qr.update(sql, params);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void insertIp(Set<String> ips){
        QueryRunner qr = new QueryRunner(dataSource);
        for(String ip: ips) {
            String sql = "select count(*) from machines where ip=?";
            try {
                int count = ((Long)qr.query(sql, new ScalarHandler(1), ip)).intValue();
                if(count == 0) {
                    sql = "insert into machines (ip) values(?)";
                    qr.update(sql, ip);
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static List<StoredReport> queryHourlyReport(String reportType, Date start, Date end) {
        return queryReport(reportType, HOURLY_REPORT_TABLE_NAME, start, end);
    }

    public static List<StoredReport> queryDailyReport(String reportType, Date start, Date end) {
        return queryReport(reportType, DAILY_REPORT_TABLE_NAME, start, end);
    }

    public static List<StoredReport> queryWeeklyReport(String reportType, Date start, Date end) {
        return queryReport(reportType, WEEKLY_REPORT_TABLE_NAME, start, end);
    }

    private static List<StoredReport> queryReport(String reportType, String tableName, Date start, Date end) {
        long startTime = start.getTime();
        long endTime = end.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        QueryRunner qr = new QueryRunner(dataSource);
        List<StoredReport> reports = new ArrayList<>();
        long timeInterval = 0;
        if( tableName == HOURLY_REPORT_TABLE_NAME) {
            timeInterval = ONE_HOUR;
        }else if(tableName == DAILY_REPORT_TABLE_NAME){
            timeInterval = ONE_DAY;
        }else if(tableName == WEEKLY_REPORT_TABLE_NAME){
            timeInterval = ONE_WEEK;
        }
        for (; startTime < endTime; startTime = startTime + timeInterval) {
            try {
                Object[] params = new Object[]{reportType, format.format(startTime)};

                //没有用bean的写法，会有错
                ResultSetHandler<Object[]> rsh2 = new ArrayHandler();
                String sql2 = "select * from " + tableName +" where "+ tableName +".reportType = ? and " + tableName + ".startTime = ?";
                Object[] arr = qr.query(sql2, rsh2, params);
                if(arr.length > 0 ){
                    StoredReport report = new StoredReport();
                    report.setId((int)arr[0]);
                    report.setReportType((String)arr[1]);
                    report.setContentFormat((int)arr[2]);
                    report.setContent((byte[])arr[3]);
                    report.setStartTime((Date) arr[4]);
                    report.setCreateTime((Date) arr[5]);
                    reports.add(report);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return reports;
    }



}
