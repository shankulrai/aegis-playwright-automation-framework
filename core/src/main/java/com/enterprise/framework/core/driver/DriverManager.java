package com.enterprise.framework.core.driver;

import com.enterprise.framework.core.playwright.PlaywrightFactory;
import com.enterprise.framework.core.playwright.PlaywrightSession;
import com.microsoft.playwright.Page;

/**
 * Thin facade around the thread-local Playwright session.
 */
public final class DriverManager {
    private DriverManager() {
    }

    public static PlaywrightSession getSession() {
        return PlaywrightFactory.getSession();
    }

    public static Page getPage() {
        return PlaywrightFactory.getPage();
    }

    public static void initialize() {
        PlaywrightFactory.createSession();
    }

    public static void quit() {
        PlaywrightFactory.closeSession();
    }
}
