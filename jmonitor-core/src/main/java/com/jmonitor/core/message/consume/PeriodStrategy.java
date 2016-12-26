package com.jmonitor.core.message.consume;

/**
 * description: a period represents an hour, PeriodStrategy determines whether this period is over and should start a new period,
 *              to process the message in the beginning seconds, the next period should start a few minutes ahead,
 *              to process the message in the last seconds of period , the period should end  a few minutes later.
 *              so, a period's actual work time is : an hour + aheadTime + extraTime
 * author jmonitor
 * date 2016/10/9
 */
public class PeriodStrategy {
	private long duration;
	private long extraTime;
	private long aheadTime;
	private long lastStartTime;
	private long lastEndTime;

	public PeriodStrategy(long duration, long extraTime, long aheadTime) {
		this.duration = duration;       //a period lasts for an hour
		this.extraTime = extraTime;     //extraTime is used for the condition that in the last time ,message arrives, so should give some extra time to process
		this.aheadTime = aheadTime;     //a period start ahead for several minutes to avoid the condition: during the
                                        //period's starting procedure, the message that this period should process arrives
		this.lastStartTime = -1;
		this.lastEndTime = 0;
	}

	public long getDuration() {
		return this.duration;
	}

	public long next(long now) {
		long startTime = now - now % this.duration;

		if (startTime > this.lastStartTime) { // first time enter this period
			this.lastStartTime = startTime;
			return startTime;
		}

		if (now + this.aheadTime >= this.lastStartTime + this.duration ) {  // in the current period, prepare next period ahead
			this.lastStartTime = startTime + this.duration;
			return startTime + this.duration;
		}

		if (now - this.lastEndTime >= this.duration + this.extraTime) {  // this period is over
			long lastEndTime = this.lastEndTime;
			this.lastEndTime = startTime;
			return -lastEndTime;
		}

		return 0;
	}
}