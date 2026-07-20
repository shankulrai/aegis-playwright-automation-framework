package com.enterprise.framework.reports;

import com.enterprise.framework.core.logging.LoggerManager;
import com.enterprise.framework.utilities.ReportUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Thread-safe Extent report lifecycle.
 */
public final class ExtentReportManager {
    private static final Logger LOGGER = LoggerManager.getLogger(ExtentReportManager.class);
    private static volatile ExtentReports extent;
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    public static ExtentReports getInstance() {
        ExtentReports current = extent;
        if (current == null) {
            synchronized (ExtentReportManager.class) {
                current = extent;
                if (current == null) {
                    Path reportFile = ReportUtils.artifactPath("extent", "index.html");
                    try {
                        Files.createDirectories(reportFile.getParent());
                    } catch (Exception exception) {
                        throw new IllegalStateException("Unable to create report directory", exception);
                    }
                    ExtentSparkReporter reporter = new ExtentSparkReporter(reportFile.toString());
                    extent = current = new ExtentReports();
                    current.attachReporter(reporter);
                    LOGGER.info("Extent report initialized at {}", reportFile);
                }
            }
        }
        return current;
    }

    public static void startTest(String name) {
        ExtentTest test = getInstance().createTest(name);
        CURRENT_TEST.set(test);
    }

    public static ExtentTest currentTest() {
        ExtentTest test = CURRENT_TEST.get();
        if (test == null) {
            throw new IllegalStateException("Extent test has not been started");
        }
        return test;
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
