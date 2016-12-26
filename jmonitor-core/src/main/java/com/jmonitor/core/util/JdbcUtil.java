package com.jmonitor.core.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jmonitor on 2016/10/6.
 */
public class JdbcUtil {
    private static Properties p;

    static{
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("c3p0.properties");
        if (is == null) {
            is = JdbcUtil.class.getResourceAsStream("c3p0.properties");
        }
        p = new Properties();
        try {
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ComboPooledDataSource getDataSource()
    {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(p.getProperty("c3p0.driverClass"));
        }catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        dataSource.setJdbcUrl(p.getProperty("c3p0.jdbcUrl"));
        dataSource.setUser(p.getProperty("c3p0.user"));
        dataSource.setPassword(p.getProperty("c3p0.password"));
        return dataSource;
    }

}
