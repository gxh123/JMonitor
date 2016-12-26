package com.jmonitor.core.message.type;

import com.jmonitor.core.message.produce.MessageProducer;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by jmonitor on 2016/12/1.
 */
public class Error extends Message {

    public Error(String type, String name) {
        super(type, name);
    }

    public Error(String type, String name, MessageProducer producer) {
        super(type, name, producer);
    }

    public static void logError(MessageProducer producer, Throwable cause) {
        Error error = new Error("Error", cause.getClass().getName(), producer);
        StringWriter writer = new StringWriter(2048);
        cause.printStackTrace(new PrintWriter(writer));
        String detailMessage = writer.toString();
        if (detailMessage != null && detailMessage.length() > 0) {
            error.addData(detailMessage);
        }
        error.complete();
    }
}
