package com.jmonitor.core.message.type;

import com.jmonitor.core.message.produce.MessageProducer;

public class Event extends Message {

	public Event(String type, String name) {
		super(type, name);
	}

	public Event(String type, String name, MessageProducer producer) {
		super(type, name, producer);
	}

	public static void logEvent(MessageProducer producer, String type, String name, String status, String data) {
		Event event = new Event(type, name, producer);
		if (data != null && data.length() > 0) {
			event.addData(data);
		}
		event.setStatus(status);
		event.complete();
	}

}
