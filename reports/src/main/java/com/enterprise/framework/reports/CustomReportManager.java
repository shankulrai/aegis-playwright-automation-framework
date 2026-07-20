package com.enterprise.framework.reports;

import org.apache.logging.log4j.Logger;
import com.enterprise.framework.core.logging.LoggerManager;

/**
 * Common report facade for logging to Extent and Allure.
 */
public final class CustomReportManager {
    private static final Logger LOGGER = LoggerManager.getLogger(CustomReportManager.class);

    private CustomReportManager() {
    }

    public static void info(String message) {
        LOGGER.info(message);
        ExtentReportManager.currentTest().info(message);
    }

    public static void error(String message) {
        LOGGER.error(message);
        ExtentReportManager.currentTest().fail(message);
    }
}
