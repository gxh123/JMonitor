package com.jmonitor.core.report.task.process;

import com.jmonitor.core.report.content.status.Machine;
import com.jmonitor.core.report.content.status.StatusReport;
import com.jmonitor.core.status.StatusInfo;

import java.util.List;

public class StatusMerger {

    public StatusMerger() {

    }

    public StatusReport merge(List<StatusReport> reports) {
        StatusReport target = new StatusReport();
        for (StatusReport source : reports) {
            visitStatusReport(source, target);
        }
        return target;
    }

    public void visitStatusReport(StatusReport from, StatusReport to) {
        to.setStartTime(from.getStartTime());
        to.setEndTime(from.getEndTime());
        for (Machine source : from.getMachines().values()) {
            Machine target = to.findMachine(source.getIp());
            if (target == null) {
                target = new Machine(source.getIp());
                to.putMachine(source.getIp(), target);
            }
            visitMachine(source, target);
        }
    }

    public void visitMachine(Machine from, Machine to) {
        for (StatusInfo source : from.getStatusInfoMap().values()) {
            to.putStatusInfo(source.getTimestamp().getTime(), source);
        }
    }
}
