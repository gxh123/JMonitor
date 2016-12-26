package com.jmonitor.core.message.type;

import com.jmonitor.core.message.produce.MessageProducer;

/**
 * Created by jmonitor on 2016/12/2.
 */
public class Transaction extends Message {
    private long durationStart;

    public Transaction(String type, String name) {
        super(type, name);
    }

    public Transaction(String type, String name, MessageProducer producer) {
        super(type, name, producer);
        durationStart = System.nanoTime();
    }

    @Override
    public void complete() {
        try {
            duration = (System.nanoTime() - durationStart) / 1000L;
            super.complete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
