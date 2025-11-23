package com.booking.tests.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties");
        }
    }

    public static String getUsername() {
        return System.getProperty("username", properties.getProperty("default.username"));
    }

    public static String getPassword() {
        return System.getProperty("password", properties.getProperty("default.password"));
    }

    public static String getBaseUrl() {
        return System.getProperty("base.url", properties.getProperty("base.url"));
    }
}
