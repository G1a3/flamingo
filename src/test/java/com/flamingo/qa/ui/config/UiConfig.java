package com.flamingo.qa.ui.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class UiConfig {

    private static final Properties PROPERTIES = loadProperties();

    private UiConfig() {
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    private static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        return PROPERTIES.getProperty(key);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream stream = UiConfig.class.getClassLoader().getResourceAsStream("ui.properties")) {
            if (stream != null) {
                properties.load(stream);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load ui.properties", e);
        }
        return properties;
    }
}
