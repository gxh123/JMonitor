package com.jmonitor.core.message.consume;

import com.jmonitor.core.message.analysis.MessageAnalyzer;
import com.jmonitor.core.message.type.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.jmonitor.core.util.TimeUtil.ONE_HOUR;

/**
 * description: distribute the MessageTree which received by the netty server
 * author jmonitor
 * date 2016/10/9
 */
public class MessageConsumer{
	private PeriodManager periodManager;
	private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.message.consume.MessageConsumer");

    public MessageConsumer(){
        periodManager = new PeriodManager(ONE_HOUR);
        new Thread(periodManager).start();
    }

	/**
	 * description：find the period that the Message belongs to and distribute the tree to the tasks in that period
	 * param：tree  the tree to be distributed
	 * return: void
	 */
	public void consume(Message message) {
        long timestamp = message.getTimestamp();
        Period period = periodManager.findPeriod(timestamp);    //find the period

        if (period != null) {
            period.distribute(message);         //distribute the message
        }
	}

    /**
     * description：invoke analyzer's doCheckpoint method to store the report. the method is only invoked when the program shut down
     * param：no param
     * return: void
     */
	public void doCheckpoint() {
		try {
			long currentStartTime = getCurrentStartTime();
			Period period = periodManager.findPeriod(currentStartTime);

			for (MessageAnalyzer analyzer : period.getAnalyzers()) {
				try {
					analyzer.doCheckpoint();
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
	}

    /**
     * description：get the time in an interval of an hour, a Period lasts for an hour
     * param：no param
     * return: time
     */
	private long getCurrentStartTime() {
		long now = System.currentTimeMillis();
		long time = now - now % ONE_HOUR;
		return time;
	}

	public Object getCurrentReportByHour(String type){
        Object report = periodManager.findPeriod(getCurrentStartTime())
                .getAnalyzer(type+"Analyzer").getCurrentReport();
        return report;
    }

}