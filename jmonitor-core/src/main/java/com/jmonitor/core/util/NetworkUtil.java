package com.jmonitor.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jmonitor on 2016/11/29.
 */
public class NetworkUtil {

    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Get local ip error !");
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Get host name error !");
    }
}
