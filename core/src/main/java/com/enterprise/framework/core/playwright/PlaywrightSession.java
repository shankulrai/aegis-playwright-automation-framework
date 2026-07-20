package com.enterprise.framework.core.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * Thread-scoped Playwright runtime objects.
 */
public record PlaywrightSession(Playwright playwright, Browser browser, BrowserContext context, Page page) {
}
