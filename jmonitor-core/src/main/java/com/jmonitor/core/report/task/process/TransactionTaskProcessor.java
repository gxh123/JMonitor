package com.jmonitor.core.report.task.process;

import com.jmonitor.core.report.store.StoredReport;

import java.util.Date;
import java.util.List;

/**
 * Created by gxh on 2016/12/26.
 */
public class TransactionTaskProcessor extends AbstractTaskProcessor {
    @Override
    protected boolean mergeAndStore(List<StoredReport> storedReports, String type, Date start, int chooseTable) {
        //tudo
        return false;
    }
}
