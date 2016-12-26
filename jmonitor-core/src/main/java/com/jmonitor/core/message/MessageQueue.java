package com.jmonitor.core.message;

import com.jmonitor.core.message.type.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MessageQueue {
	private BlockingQueue<Message> queue;

	public MessageQueue(int size) {
		queue = new LinkedBlockingQueue<>(size);
	}

	public boolean offer(Message message) {
		return queue.offer(message);
	}

	public Message poll() {
		try {
			return queue.poll(5, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			return null;
		}
	}

	public int size() {
		return queue.size();
	}
}
