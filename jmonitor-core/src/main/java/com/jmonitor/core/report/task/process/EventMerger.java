package com.jmonitor.core.report.task.process;

import com.jmonitor.core.report.content.event.EventInfo;
import com.jmonitor.core.report.content.event.EventReport;
import com.jmonitor.core.report.content.event.Machine;

import java.util.List;

public class EventMerger {

    public EventMerger() {

    }

    public EventReport merge(List<EventReport> reports) {
        EventReport target = new EventReport();
        for (EventReport source : reports) {
            visitEventReport(source, target);
        }
        return target;
    }

    public void visitEventReport(EventReport from, EventReport to) {
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
        for (EventInfo source : from.getEventInfoMap().values()) {
            EventInfo target = to.findEventInfo(source.getEventType());

            if (target == null) {
                target = new EventInfo(source.getEventType());
                to.putEventInfo(source.getEventType(), target);
            }
            visitEventInfo(source, target);
        }
    }

    public void visitEventInfo(EventInfo from, EventInfo to) {
        to.setTotalCount(to.getTotalCount() + from.getTotalCount());
        to.setFailCount(to.getFailCount() + from.getFailCount());

        if (to.getTotalCount() > 0) {
            to.setFailPercent(to.getFailCount() * 100.0 / to.getTotalCount());
        }
    }
}
