package com.jmonitor.core.message.analysis;


import com.jmonitor.core.message.MessageQueue;

public interface MessageAnalyzer<T> {

	public void analyze(MessageQueue queue);

	public void doCheckpoint();

	public long getStartTime();

	public void initialize(long startTime, long duration, long extraTime);

	public T getCurrentReport();
}
