package com.jmonitor.core.message.codec;

import com.jmonitor.core.message.type.Message;
import io.netty.buffer.ByteBuf;

import static com.jmonitor.core.util.JsonUtil.toJson;

public class MessageEncoder {

	public void encode(Message message, ByteBuf buf) {
		byte[] body = toJson(message).getBytes();
		buf.writeInt(body.length);
		buf.writeBytes(body);
	}

}