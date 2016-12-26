package com.jmonitor.core.message.consume;

import com.jmonitor.core.message.analysis.MessageAnalyzerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.jmonitor.core.util.TimeUtil.ONE_MINUTE;

/**
 * description: manage the period
 * author jmonitor
 * date 2016/10/9
 */
public class PeriodManager implements Runnable {
	public static final long EXTRA_TIME = 3 * ONE_MINUTE;

	private PeriodStrategy strategy;   //PeriodStrategy determines if the next period can be started and the current period can be ended
	private List<Period> periods = new ArrayList<Period>();  //the list of the period
	private boolean active = true;
	private MessageAnalyzerManager analyzerManager = new MessageAnalyzerManager();
	private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.message.consume.PeriodManager");

	public PeriodManager(long duration) {
		this.strategy = new PeriodStrategy(duration, EXTRA_TIME, EXTRA_TIME);
        long startTime = strategy.next(System.currentTimeMillis());
        startPeriod(startTime);
	}

    /**
     * description：start a period
     * param：period's startTime
     * return: void
     */
    private void startPeriod(long startTime) {
        long endTime = startTime + strategy.getDuration();
        Period period = new Period(startTime, endTime, analyzerManager);

        periods.add(period);
        period.start();
    }

    /**
     * description：find the certain period
     * param：period's startTime
     * return: the period
     */
    public Period findPeriod(long timestamp) {
        for (Period period : periods) {
            if (period.isIn(timestamp)) {
                return period;
            }
        }
        return null;
    }

    /**
     * description：end the certain period
     * param：period's startTime
     * return: void
     */
	private void endPeriod(long startTime) {
		int len = periods.size();

		for (int i = 0; i < len; i++) {
			Period period = periods.get(i);
			if (period.isIn(startTime)) {
				period.finish();
				periods.remove(i);
				break;
			}
		}
	}

    /**
     * description：every second, determines if the next period can be started and the current period can be ended
     *              note that start next period and end current period is not the same same time, details in PeriodStrategy
     * param：no param
     * return: void
     */
	public void run() {
		while (active) {
			try {
				long now = System.currentTimeMillis();
				long value = strategy.next(now);
				if (value > 0) {
					startPeriod(value);
				} else if (value < 0) {
                    new Thread(new EndPeriodThread(-value)).start();
				}
			} catch (Throwable e) {
				logger.error(e.getMessage());
			}

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public void shutdown() {
		active = false;
	}


    /**
     * description: invoke the end period method in backend
     * author jmonitor
     * date 2016/10/9
     */
	private class EndPeriodThread implements Runnable {

		private long startTime;

		public EndPeriodThread(long startTime) {
			this.startTime = startTime;
		}

		@Override
		public void run() {
			endPeriod(this.startTime);
		}
	}
}