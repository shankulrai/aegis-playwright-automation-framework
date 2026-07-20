package com.enterprise.framework.reports;

import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Allure attachment helpers.
 */
public final class AllureReportManager {
    private AllureReportManager() {
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
    }

    public static void attachBytes(String name, byte[] content) {
        Allure.addAttachment(name, new ByteArrayInputStream(content));
    }
}
