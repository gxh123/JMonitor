package com.jmonitor.web.controller;

import com.google.gson.Gson;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jmonitor.core.report.content.status.StatusReport;
import com.jmonitor.core.status.StatusInfo;
import com.jmonitor.core.status.model.*;
import com.jmonitor.web.controller.chartdata.Disk;
import com.jmonitor.web.controller.chartdata.EchartsTimeData;
import com.jmonitor.web.controller.chartdata.Memory;
import com.jmonitor.web.controller.chartdata.SystemInfo;
import com.jmonitor.web.service.StatusReportService;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * EventController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class StatusController  extends AbstractController  {
	{
		reportService = new StatusReportService();
	}

	public void index(){
        processInternal(true);
		render("/status.jsp");
	}

    public void now(){
        setSessionAttr("startTime", null);
        setSessionAttr("step", null);
        processInternal(true);
        render("/status.jsp");
    }

    public void decrease() {
        processInternal(false);
        render("/status.jsp");
    }

    public void increase() {
        processInternal(true);
        render("/status.jsp");
    }

    private void processInternal(boolean isIncrease){
        Date time = getTime(isIncrease);
        String ip = getIp();
        String step = getStep();
        System.out.println("请求是：" + "time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time) +", ip: " + ip + "step:" + step);
        String keyPrefix = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time) + "-" + ip + "-" + step + "-";

        SystemInfo systemInfo = CacheKit.get("jmonitorCache", keyPrefix + "systemInfo");
        if (systemInfo == null) {
            System.err.println("缓存不存在");
            List<StatusInfo> statusList = queryStatus(time, ip);    //all status info
            if(statusList == null) {
                setSessionAttr("systemInfo", null);
                return;
            }

            //system info
            StatusInfo firstStatusInfo = statusList.get(0);
            OsInfo osInfo = firstStatusInfo.getOs();
            DiskInfo diskInfo = firstStatusInfo.getDisk();
            MemoryInfo memoryInfo = firstStatusInfo.getMemory();
            ThreadsInfo threadsInfo = firstStatusInfo.getThread();

            systemInfo = new SystemInfo();
            systemInfo.setOs(osInfo.getName());
            systemInfo.setArch(osInfo.getArch());
            systemInfo.setExternalMemory(diskInfo.getTotal()/1024/1024/1024);
            systemInfo.setInternalMemory(osInfo.getTotalPhysicalMemory()/1024/1024);
            systemInfo.setMaxHeap(memoryInfo.getMax()/1024/1024);
            systemInfo.setProcessors(osInfo.getAvailableProcessors());
            CacheKit.put("jmonitorCache", keyPrefix + "systemInfo", systemInfo);
            setSessionAttr("systemInfo", systemInfo);

            //disk external data
            Disk disk = new Disk();
            for(DiskVolumeInfo diskVolumeInfo: diskInfo.getDiskVolumes()){
                disk.addName(diskVolumeInfo.getId());
                disk.addUsed((diskVolumeInfo.getTotal()-diskVolumeInfo.getUsable())/1024/1024/1024);
                disk.addUnused(diskVolumeInfo.getUsable()/1024/1024/1024);
            }
            CacheKit.put("jmonitorCache", keyPrefix + "disk", disk);
            //ram
            Memory memory = new Memory();
            memory.setUsed(memoryInfo.getHeapUsage()/1024/1024);
            memory.setUnused((memoryInfo.getMax() - memoryInfo.getHeapUsage())/1024/1024);
            CacheKit.put("jmonitorCache", keyPrefix + "memory", memory);

            List<EchartsTimeData> cpuRatioList = new ArrayList<>();
            List<EchartsTimeData> heapList = new ArrayList<>();
            List<EchartsTimeData> threadList = new ArrayList<>();

            for(StatusInfo statusInfo: statusList){
                String timeData = (new SimpleDateFormat("HH:mm")).format(statusInfo.getTimestamp());
                osInfo = statusInfo.getOs();
                diskInfo = statusInfo.getDisk();
                memoryInfo = statusInfo.getMemory();
                threadsInfo = statusInfo.getThread();

                //cpuRatio
                EchartsTimeData cpuRatio = new EchartsTimeData();
                cpuRatio.setTime(timeData);
                cpuRatio.setValue(String.format("%.0f", osInfo.getProcessorUseRatio()*100));
                cpuRatioList.add(cpuRatio);

                //heap
                EchartsTimeData heap = new EchartsTimeData();
                heap.setTime(timeData);
                heap.setValue(Long.toString(memoryInfo.getHeapUsage()/1024/1024));
                heapList.add(heap);

                //thread
                EchartsTimeData thread = new EchartsTimeData();
                thread.setTime(timeData);
                thread.setValue(Integer.toString(threadsInfo.getCount()));
                threadList.add(thread);

            }
            Collections.sort(cpuRatioList, new Comparator<EchartsTimeData>() {
                @Override
                public int compare(EchartsTimeData o1, EchartsTimeData o2) {
                    return o1.getTime().compareTo(o2.getTime());
                }
            });
            Collections.sort(heapList, new Comparator<EchartsTimeData>() {
                @Override
                public int compare(EchartsTimeData o1, EchartsTimeData o2) {
                    return o1.getTime().compareTo(o2.getTime());
                }
            });
            Collections.sort(threadList, new Comparator<EchartsTimeData>() {
                @Override
                public int compare(EchartsTimeData o1, EchartsTimeData o2) {
                    return o1.getTime().compareTo(o2.getTime());
                }
            });
            CacheKit.put("jmonitorCache", keyPrefix + "cpuRatio", cpuRatioList);
            CacheKit.put("jmonitorCache", keyPrefix + "heap", heapList);
            CacheKit.put("jmonitorCache", keyPrefix + "thread", threadList);
        }else{
            System.err.println("已经缓存");
        }
    }

    private List<StatusInfo> queryStatus(Date time, String ip){
        StatusReport report = null;
        String step = getStep();
        switch (step){
            case "month":
                report = (StatusReport)reportService.getMonthly(time);
                break;
            case "week":
                report = (StatusReport)reportService.getWeekly(time);
                break;
            case "day":
                report = (StatusReport)reportService.getDaily(time);
                break;
            case "hour":
                report = (StatusReport)reportService.getHourly(time);
                break;
            default:
                System.out.println("error step");
        }
        List<StatusInfo> statusList = null;
        if (report != null) {
            if(report.getMachines() != null && report.getMachines().containsKey(ip)) {
                statusList = report.getMachines().get(ip).getStatusInfoList();
            }
        }
        return statusList;
    }

    public void cpuRatio(){
        processInternal(true);
        String keyPrefix = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(getTime(true)) + "-" + getIp() + "-" + getStep() + "-";
        Gson gson = new Gson();
        String str = gson.toJson((List)CacheKit.get("jmonitorCache", keyPrefix + "cpuRatio"));
        System.out.println(str);
        renderJson(str);
    }

    public void heap(){
        processInternal(true);
        String keyPrefix = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(getTime(true)) + "-" + getIp() + "-" + getStep() + "-";
        Gson gson = new Gson();
        String str = gson.toJson((List)CacheKit.get("jmonitorCache", keyPrefix + "heap"));
        System.out.println(str);
        renderJson(str);
    }

    public void thread(){
        processInternal(true);
        String keyPrefix = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(getTime(true)) + "-" + getIp() + "-" + getStep() + "-";
        Gson gson = new Gson();
        String str = gson.toJson((List)CacheKit.get("jmonitorCache", keyPrefix + "thread"));
        System.out.println(str);
        renderJson(str);
    }

    public void disk(){
        processInternal(true);
        String keyPrefix = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(getTime(true)) + "-" + getIp() + "-" + getStep() + "-";
        Gson gson = new Gson();
        String str = gson.toJson((Disk)CacheKit.get("jmonitorCache", keyPrefix + "disk"));
        System.out.println(str);
        renderJson(str);
    }

    public void memory(){
        processInternal(true);
        String keyPrefix = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(getTime(true)) + "-" + getIp() + "-" + getStep() + "-";
        Gson gson = new Gson();
        String str = gson.toJson((Memory)CacheKit.get("jmonitorCache", keyPrefix + "memory"));
        System.out.println(str);
        renderJson(str);
    }
}



