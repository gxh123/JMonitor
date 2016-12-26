package com.jmonitor.core.report.content.problem;

import com.jmonitor.core.report.content.DefaultReport;

import java.util.HashMap;
import java.util.Map;

public class ProblemReport  extends DefaultReport {
    private Map<String, Machine> machines = new HashMap<>();

    public ProblemReport(){

    }

    public Machine findOrCreateMachine(String ip){
        Machine machine  = machines.get(ip);

        if(machine == null){
            machine = new Machine(ip);
            machines.put(ip,machine);
        }
        return machine;
    }

    public Machine findMachine(String ip){
        return machines.get(ip);
    }

    public Machine putMachine(String ip, Machine target){
        return machines.put(ip, target);
    }

    public Map<String, Machine> getMachines() {
        return machines;
    }

    public void setMachines(Map<String, Machine> machines) {
        this.machines = machines;
    }
}
