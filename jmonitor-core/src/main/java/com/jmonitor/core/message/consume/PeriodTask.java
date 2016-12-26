package com.jmonitor.core.message.consume;

import com.jmonitor.core.message.MessageQueue;
import com.jmonitor.core.message.analysis.MessageAnalyzer;
import com.jmonitor.core.message.type.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * description: each PeriodTask has a unique analyzer to process specific message,  the analyzer keeps checking the queue to
 *              find if there is any message to analyze(process).
 * author jmonitor
 * date 2016/10/9
 */
public class PeriodTask implements Runnable {
	private MessageAnalyzer analyzer;
	private MessageQueue queue;
	private long startTime;
	private int queueOverflow;
	private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.message.consume.PeriodTask");

	public PeriodTask(MessageAnalyzer analyzer, MessageQueue queue, long startTime) {
		this.analyzer = analyzer;
		this.queue = queue;
		this.startTime = startTime;
	}

    /**
     * description：enqueue the message
     * param: Message
     * return: true : enqueue successfully
     */
	public boolean enqueue(Message message) {
		boolean result = this.queue.offer(message);
		if(!result) System.out.println("enqueue error");

		if (!result) {
			this.queueOverflow++;
			if (this.queueOverflow % 100 == 0) {
				System.out.println(this.analyzer.getClass().getSimpleName() + " queue overflow number " + this.queueOverflow);
			}
		}
		return result;
	}

	public void finish() {
		try {
			this.analyzer.doCheckpoint();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public MessageAnalyzer getAnalyzer() {
		return this.analyzer;
	}

	public String getName() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(this.startTime);
		return this.analyzer.getClass().getSimpleName() + "-" + cal.get(Calendar.HOUR_OF_DAY);
	}

	@Override
	public void run() {
		try {
			this.analyzer.analyze(this.queue);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}