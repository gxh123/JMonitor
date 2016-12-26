package com.jmonitor.core.message.produce;

import com.jmonitor.core.configuration.ClientConfig;
import com.jmonitor.core.message.io.MessageSender;
import com.jmonitor.core.message.type.*;
import com.jmonitor.core.message.type.Error;
import com.jmonitor.core.util.NetworkUtil;

import static com.jmonitor.core.util.TimeUtil.ONE_HOUR;

public class MessageProducer {
    private String ip = NetworkUtil.getLocalIp();
    private String hostName = NetworkUtil.getHostName();
    private int index = 0;
    private long timestamp = System.currentTimeMillis()/ONE_HOUR;
    private ClientConfig config;
    private MessageSender sender;

    public MessageProducer(){
        config = new ClientConfig();
        sender = new MessageSender(config);
    }

    public synchronized String createMessageId() {
        long timestamp = System.currentTimeMillis() / ONE_HOUR;
        if (this.timestamp != timestamp) {  //next hour
            this.index = 0;
            this.timestamp = timestamp;
        }
        this.index++;
        StringBuilder sb = new StringBuilder(this.ip.length() + 32);
        sb.append(this.hostName);
        sb.append('-');
        sb.append(this.ip);
        sb.append('-');
        sb.append(this.timestamp);
        sb.append('-');
        sb.append(this.index);
        return sb.toString();
    }

    public void logError(Throwable cause) {
        Error.logError(this, cause);
    }

    public void logEvent(String type, String name) {
        logEvent(type, name, Message.SUCCESS, null);
    }

    public void logEvent(String type, String name, String status, String nameValuePairs) {
        Event.logEvent(this, type, name, status, nameValuePairs);
    }

    public Event newEvent(String type, String name) {
        Event event = new Event(type, name, this);
        return event;
    }

    public Transaction newTransaction(String type, String name) {
        Transaction transaction = new Transaction(type, name, this);
        return transaction;
    }

    public Status newStatus(String type, String name) {
        Status status = new Status(type, name, this);
        return status;
    }

    public void send(Message message) {
        Thread thread = Thread.currentThread();
        message.setThreadId(String.valueOf(thread.getId()));
        message.setThreadName(thread.getName());
        message.setHostName(NetworkUtil.getHostName());
        message.setIp(NetworkUtil.getLocalIp());
        message.setMessageId(createMessageId());
        sender.send(message);
    }
}

