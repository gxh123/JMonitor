package com.jmonitor.core.message.analysis;

import com.jmonitor.core.message.MessageQueue;
import com.jmonitor.core.message.type.Message;

public abstract class AbstractMessageAnalyzer<T> implements MessageAnalyzer<T> {
	private long extraTime;
	protected long startTime;
	protected long duration;

	@Override
	public void analyze(MessageQueue queue) {
        while (!isTimeout()) {
			Message message = queue.poll();
			if (message != null) {
				try {
					process(message);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public abstract void doCheckpoint();

	@Override
	public long getStartTime() {
		return startTime;
	}

	@Override
	public void initialize(long startTime, long duration, long extraTime) {
		this.extraTime = extraTime;   //3 min
		this.startTime = startTime;
		this.duration = duration;
	}

	protected boolean isTimeout() {
		long currentTime = System.currentTimeMillis();
		long endTime = startTime + duration + extraTime;
		return currentTime > endTime;
	}

	protected abstract void process(Message message);
}
