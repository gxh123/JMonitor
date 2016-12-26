package com.jmonitor.core.report.task.process;

import com.jmonitor.core.report.content.problem.Machine;
import com.jmonitor.core.report.content.problem.ProblemInfo;
import com.jmonitor.core.report.content.problem.ProblemReport;

import java.util.List;

public class ProblemMerger {

    public ProblemMerger() {

    }

    public ProblemReport merge(List<ProblemReport> reports) {
        ProblemReport target = new ProblemReport();
        for (ProblemReport source : reports) {
            visitProblemReport(source, target);
        }
        return target;
    }

    public void visitProblemReport(ProblemReport from, ProblemReport to) {
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
        for (ProblemInfo source : from.getProblemInfoMap().values()) {
            ProblemInfo target = to.findProblemInfo(source.getDetail());

            if (target == null) {
                target = new ProblemInfo(source.getDetail());
                to.putProblemInfo(source.getDetail(), target);
            }
            visitProblemInfo(source, target);
        }
    }

    public void visitProblemInfo(ProblemInfo from, ProblemInfo to) {
        to.setCount(to.getCount() + from.getCount());
        to.setProblemType(from.getProblemType());
    }
}
