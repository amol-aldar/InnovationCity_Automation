package org.rakdao.utils;

import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Logger log = LoggerUtil.getLogger(ConfigReader.class); // ✅ Logger instance
    private static final Properties prop = new Properties();
    private static final String CONFIG_PATH = System.getProperty("user.dir")+"/src/main/resources/config.properties";

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            prop.load(fis);
            log.info("Successfully loaded config properties from '{}'", CONFIG_PATH);
        } catch (IOException e) {
            log.error("Error loading config file from '{}'", CONFIG_PATH, e);
            throw new RuntimeException("Failed to load config.properties file", e);
        }
    }


     // Returns the string value for a given key from config.properties.

    public static String get(String key) {
        String value = prop.getProperty(key);
        if (value == null) {
            log.warn("Property '{}' not found in config file.", key);
        } else {
            log.debug("Property '{}' resolved to '{}'", key, value);
        }
        return value;
    }


     //Returns an integer value for a given key.

    public static int getInt(String key) {
        String value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error("Property '{}' is not a valid integer. Value: '{}'", key, value);
            throw e;
        }
    }


     // Returns a boolean value for a given key.

    public static boolean getBoolean(String key) {
        String value = get(key);
        return Boolean.parseBoolean(value);
    }
}

