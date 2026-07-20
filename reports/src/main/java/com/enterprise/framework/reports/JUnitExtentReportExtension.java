package com.enterprise.framework.reports;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

/**
 * Auto-registers Extent reporting for JUnit 5 tests.
 */
public class JUnitExtentReportExtension implements BeforeEachCallback, AfterEachCallback, TestWatcher {
    @Override
    public void beforeEach(ExtensionContext context) {
        ExtentReportManager.startTest(testName(context));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        ExtentReportManager.flush();
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        ExtentReportManager.currentTest().pass("PASSED");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        ExtentReportManager.currentTest().fail(cause);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        ExtentReportManager.currentTest().skip(cause);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        ExtentReportManager.getInstance()
            .createTest(testName(context))
            .skip(reason.orElse("DISABLED"));
        ExtentReportManager.flush();
    }

    private static String testName(ExtensionContext context) {
        String className = context.getRequiredTestClass().getSimpleName();
        String methodName = context.getTestMethod()
            .map(method -> method.getName())
            .orElse(context.getDisplayName());
        return className + "." + methodName;
    }
}
