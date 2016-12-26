package com.jmonitor.core;

import com.jmonitor.core.message.produce.MessageProducer;
import com.jmonitor.core.message.type.Event;
import com.jmonitor.core.message.type.Status;
import com.jmonitor.core.message.type.Transaction;
import com.jmonitor.core.status.StatusUpdateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jmonitor on 2016/9/30.
 */
public class JMonitor {
    private static JMonitor instance = new JMonitor();
    private static MessageProducer producer;

    public JMonitor(){
        producer = new MessageProducer();
        Thread t = new Thread(new StatusUpdateTask());
        t.start();

        Logger logger = LoggerFactory.getLogger("com.jmonitor.core.JMonitor");
        logger.info("JMonitor start !");
    }

    public static MessageProducer getProducer() {
        return instance.producer;
    }

    public static Event newEvent(String type, String name) {
        return JMonitor.getProducer().newEvent(type, name);
    }

    public static Transaction newTransaction(String type, String name) {
        return JMonitor.getProducer().newTransaction(type, name);
    }

    public static Status newStatus(String type, String name) {
        return JMonitor.getProducer().newStatus(type, name);
    }

    public static void logError(Throwable cause) {
        JMonitor.getProducer().logError(cause);
    }

    public static void logEvent(String type, String name) {
        JMonitor.getProducer().logEvent(type, name);
    }

    public static void logEvent(String type, String name, String status, String nameValuePairs) {
        JMonitor.getProducer().logEvent(type, name, status, nameValuePairs);
    }

}
