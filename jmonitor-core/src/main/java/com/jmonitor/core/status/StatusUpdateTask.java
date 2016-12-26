package com.jmonitor.core.status;

import com.jmonitor.core.JMonitor;
import com.jmonitor.core.message.type.Message;
import com.jmonitor.core.message.type.Status;
import com.jmonitor.core.util.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusUpdateTask implements Runnable{
	private String ip;
	private static final long INTERVAL = 60 * 1000; // 60 seconds
    private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.status.StatusUpdateTask");

    public void run() {
        this.ip = NetworkUtil.getLocalIp();
		JMonitor.logEvent("Event", "reboot", Message.SUCCESS, null);

        StatusInfoCollector statusInfoCollector = new StatusInfoCollector();
		while (true) {
			long start = System.currentTimeMillis();

            System.out.println("generateStatus,time:"+ new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(start)));
            Status h = JMonitor.newStatus("Status", this.ip);
            StatusInfo status = new StatusInfo();
            try {
                statusInfoCollector.collect(status);
                h.addData(status.toString());
                h.setStatus(Message.SUCCESS);
            } catch (Throwable e) {
                h.setStatus(e);
                logger.error(e.getMessage());
            } finally {
                h.complete();
            }

            long elapsed = System.currentTimeMillis() - start;
            if (elapsed < this.INTERVAL) {
                try {
                    Thread.sleep(this.INTERVAL - elapsed);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                    break;
                }
            }
	    }
    }
}
