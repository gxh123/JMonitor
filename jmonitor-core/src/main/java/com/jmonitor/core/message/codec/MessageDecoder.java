package com.jmonitor.core.message.codec;

import com.jmonitor.core.message.type.Message;
import com.jmonitor.core.util.JsonUtil;
import io.netty.buffer.ByteBuf;

/**
 * Created by jmonitor on 2016/12/1.
 */
public class MessageDecoder {

    public Message decode(ByteBuf buf, int length) {
        byte[] body = new byte[length];
        buf.readBytes(body);
        Message message = JsonUtil.fromJson(new String(body), Message.class);
        return message;
    }

}
