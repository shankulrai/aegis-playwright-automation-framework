package com.enterprise.framework.utilities;

import com.microsoft.playwright.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Screenshot capture utilities.
 */
public final class ScreenshotUtils {
    private ScreenshotUtils() {
    }

    public static byte[] capture(Page page) {
        return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }

    public static Path capture(Page page, Path target) throws IOException {
        Files.createDirectories(target.toAbsolutePath().getParent());
        page.screenshot(new Page.ScreenshotOptions().setPath(target).setFullPage(true));
        return target;
    }
}
