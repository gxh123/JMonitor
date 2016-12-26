package com.jmonitor.core.message.type;

import com.jmonitor.core.message.produce.MessageProducer;

public class Message {
	public static final String SUCCESS = "0";

	private String hostName;    //产生message的主机名
	private String ip;          //产生message的主机ip
	private String messageId;   //消息的唯一id
	private String threadId;    //产生消息的线程的id
	private String threadName;  //产生消息的线程的名字
	private String type;        //消息的基本类型(event, error,status,transaction之一)
	private String name;        //消息的名字，即基本类型下的具体类型（）
	private String status = "unset";  //用于表示event或者transaction的状态（success，failure，unset）
	private long timestamp;     //消息产生的时间戳
    private String data;  //消息携带的数据
	protected long duration = 0;
	protected transient MessageProducer producer;

	public Message(String type, String name) {
		this.type = String.valueOf(type);
		this.name = String.valueOf(name);
		this.timestamp = System.currentTimeMillis();
	}

	public Message(String type, String name, MessageProducer producer) {
		this.type = String.valueOf(type);
		this.name = String.valueOf(name);
		this.producer = producer;
		this.timestamp = System.currentTimeMillis();
	}


	public void addData(String data) {
		if (this.data == null) {
			this.data = data;
		} else {
			StringBuilder sb = new StringBuilder(this.data);
			sb.append('&').append(data);
			this.data = sb.toString();
		}
	}

	public void addData(String key, Object value) {
		StringBuilder sb = new StringBuilder(data);
		sb.append('&').append(key).append('=').append(value);
		data = sb.toString();
	}

	public CharSequence getData() {
		if (data == null) {
			return "";
		} else {
			return data;
		}
	}

	public void complete() {
		producer.send(this);
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getType() {
		return type;
	}

	public boolean isSuccess() {
		return SUCCESS.equals(status);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatus(Throwable e) {
		this.status = e.getClass().getName();
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

}
