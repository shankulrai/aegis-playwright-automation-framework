package com.enterprise.framework.core.config;

import com.enterprise.framework.core.FrameworkConstants;
import org.aeonbits.owner.ConfigFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Objects;

/**
 * Singleton access point for framework configuration.
 */
public final class ConfigManager {
    private static volatile ConfigManager instance;

    private final FrameworkConfig config;

    private ConfigManager() {
        Properties properties = loadProperties();
        this.config = ConfigFactory.create(FrameworkConfig.class, properties);
    }

    public static ConfigManager getInstance() {
        ConfigManager current = instance;
        if (current == null) {
            synchronized (ConfigManager.class) {
                current = instance;
                if (current == null) {
                    instance = current = new ConfigManager();
                }
            }
        }
        return current;
    }

    public FrameworkConfig getConfig() {
        return config;
    }

    public String env() {
        return config.env();
    }

    public String browser() {
        return config.browser();
    }

    public boolean headless() {
        return config.headless();
    }

    public boolean remote() {
        return config.remote();
    }

    public String remoteEndpoint() {
        return config.remoteEndpoint();
    }

    public long timeoutMs() {
        return config.timeoutMs();
    }

    public long slowMoMs() {
        return config.slowMoMs();
    }

    public String baseUrl() {
        return config.baseUrl();
    }

    public String apiBaseUrl() {
        return config.apiBaseUrl();
    }

    public String awsProfile() {
        return config.awsProfile();
    }

    public String awsRegion() {
        return config.awsRegion();
    }

    public String awsRoleArn() {
        return config.awsRoleArn();
    }

    public String awsRoleSessionName() {
        return config.awsRoleSessionName();
    }

    public String awsExternalId() {
        return config.awsExternalId();
    }

    public String awsS3Bucket() {
        return config.awsS3Bucket();
    }

    public String awsSnsTopicArn() {
        return config.awsSnsTopicArn();
    }

    public String awsSqsQueueUrl() {
        return config.awsSqsQueueUrl();
    }

    public String runId() {
        return System.getProperty(FrameworkConstants.SYSTEM_RUN_ID);
    }

    public String require(String key, String value) {
        return Objects.requireNonNull(value, key + " must be configured");
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        loadFromClasspath(properties, "application.properties");
        String env = System.getProperty(FrameworkConstants.ENV_PROPERTY, properties.getProperty(FrameworkConstants.ENV_PROPERTY, FrameworkConstants.DEFAULT_ENV));
        loadFromClasspath(properties, "application-" + env + ".properties");
        overlaySystemProperties(properties);
        properties.putIfAbsent(FrameworkConstants.ENV_PROPERTY, env);
        return properties;
    }

    private void loadFromClasspath(Properties properties, String resourceName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load configuration resource: " + resourceName, exception);
        }
    }

    private void overlaySystemProperties(Properties properties) {
        Properties systemProperties = System.getProperties();
        Enumeration<?> names = systemProperties.propertyNames();
        while (names.hasMoreElements()) {
            Object name = names.nextElement();
            if (name instanceof String key) {
                properties.put(key, systemProperties.getProperty(key));
            }
        }
    }
}
