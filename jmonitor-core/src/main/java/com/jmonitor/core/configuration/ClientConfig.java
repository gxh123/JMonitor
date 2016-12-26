package com.jmonitor.core.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jmonitor on 2016/10/1.
 */
public class ClientConfig {
    private static final String CLIENT_PROPERTIES = "\\client-config.properties";
    private Map<String, String> properties = new LinkedHashMap<String, String>();
    private String serverIp;
    private int serverPort;
    private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.configuration.ClientConfig");

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public ClientConfig() {
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CLIENT_PROPERTIES);    //读入资源文件
            if (in == null) {
                in = ClientConfig.class.getResourceAsStream(CLIENT_PROPERTIES);
            }
            if (in != null) {
                Properties prop = new Properties();
                prop.load(in);
                Iterator<String> it = prop.stringPropertyNames().iterator();
                while(it.hasNext()){
                    String key = it.next();
                    properties.put(key, prop.getProperty(key));
                }
                serverIp = properties.get("server.ip");
                serverPort = Integer.parseInt(properties.get("server.port"));
            } else {
                logger.error("Can't find app.properties in {}", CLIENT_PROPERTIES);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
}

