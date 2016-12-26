package com.jmonitor.core.message.type;

import com.jmonitor.core.message.produce.MessageProducer;

public class Status extends Message {

	public Status(String type, String name) {
		super(type, name);
	}

	public Status(String type, String name, MessageProducer producer) {
		super(type, name, producer);
   }

	public static void logStatus(MessageProducer producer, String type, String name, String status, String data) {
        Status heartbeat = new Status(type, name, producer);
        heartbeat.addData(data);
        heartbeat.setStatus(status);
        heartbeat.complete();
    }
}
