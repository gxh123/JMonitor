package com.jmonitor.core.status;

import com.jmonitor.core.status.model.*;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

public class StatusInfoCollector {

	public void collect(StatusInfo status) {
		visitOs(status.getOs());
		visitDisk(status.getDisk());
		visitMemory(status.getMemory());
		visitThread(status.getThread());
	}

	public void visitOs(OsInfo os) {
		OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();

		os.setArch(bean.getArch());
		os.setName(bean.getName());
		os.setAvailableProcessors(bean.getAvailableProcessors());

		// for Sun JDK
		if (isInstanceOfInterface(bean.getClass(), "com.sun.management.OperatingSystemMXBean")) {
			com.sun.management.OperatingSystemMXBean b = (com.sun.management.OperatingSystemMXBean) bean;

			os.setTotalPhysicalMemory(b.getTotalPhysicalMemorySize());
//			long start = System.currentTimeMillis();
//			long startP = b.getProcessCpuTime();
//			try{
//				TimeUnit.SECONDS.sleep(1);
//			}catch (InterruptedException e){
//				e.printStackTrace();
//			}
//			long end = System.currentTimeMillis();
//			long endP = b.getProcessCpuTime();
//			os.setProcessorUseRatio((endP-startP)/1000000.0/(end-start)/b.getAvailableProcessors());

			//使用sigar测量CPU使用率
			try {
				Sigar sigar = new Sigar();
				CpuInfo infos[] = sigar.getCpuInfoList();
				CpuPerc cpuList[] = null;
				cpuList = sigar.getCpuPercList();
				double cpuRatio = 0;
				for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
					cpuRatio += cpuList[i].getCombined();
				}
				os.setProcessorUseRatio(cpuRatio / infos.length);
//				System.out.println("CPU总的使用率:    " + CpuPerc.format(cpuRatio / infos.length));// 总的使用率
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	boolean isInstanceOfInterface(Class<?> clazz, String interfaceName) {
		if (clazz == Object.class) {
			return false;
		} else if (clazz.getName().equals(interfaceName)) {
			return true;
		}

		Class<?>[] interfaceclasses = clazz.getInterfaces();

		for (Class<?> interfaceClass : interfaceclasses) {
			if (isInstanceOfInterface(interfaceClass, interfaceName)) {
				return true;
			}
		}
		return isInstanceOfInterface(clazz.getSuperclass(), interfaceName);
	}

	public void visitDisk(DiskInfo disk) {
		File[] roots = File.listRoots();

		if (roots != null) {
			for (File root : roots) {
				disk.addDiskVolume(new DiskVolumeInfo(root.getAbsolutePath()));
			}
		}

		for (DiskVolumeInfo diskVolume : disk.getDiskVolumes()) {
			visitDiskVolume(diskVolume);
		}
	}

	public void visitDiskVolume(DiskVolumeInfo diskVolume) {
		File volume = new File(diskVolume.getId());

		diskVolume.setTotal(volume.getTotalSpace());
		diskVolume.setUsable(volume.getUsableSpace());
	}

	public void visitMemory(MemoryInfo memory) {
		MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
		Runtime runtime = Runtime.getRuntime();

		memory.setMax(runtime.maxMemory());
		memory.setTotal(runtime.totalMemory());
		memory.setHeapUsage(bean.getHeapMemoryUsage().getUsed());
	}

	public void visitThread(ThreadsInfo thread) {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();

		bean.setThreadContentionMonitoringEnabled(true);
		thread.setCount(bean.getThreadCount());
		thread.setPeekCount(bean.getPeakThreadCount());
	}

}