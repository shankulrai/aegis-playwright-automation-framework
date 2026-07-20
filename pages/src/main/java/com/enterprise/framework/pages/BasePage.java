package com.enterprise.framework.pages;

import com.enterprise.framework.core.config.ConfigManager;
import com.enterprise.framework.core.logging.LoggerManager;
import com.enterprise.framework.utilities.WaitUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

/**
 * Base class for all Playwright page objects.
 */
public abstract class BasePage {
    protected final Page page;
    protected final Logger logger = LoggerManager.getLogger(getClass());
    protected final ConfigManager config = ConfigManager.getInstance();

    protected BasePage(Page page) {
        this.page = page;
    }

    protected Page page() {
        return page;
    }

    public void navigate(String pathOrUrl) {
        if (pathOrUrl.startsWith("http")) {
            page.navigate(pathOrUrl);
        } else {
            page.navigate(config.baseUrl() + pathOrUrl);
        }
    }

    public void click(Locator locator) {
        locator.click();
    }

    public void fill(Locator locator, String value) {
        locator.fill(value);
    }

    public String text(Locator locator) {
        return locator.textContent();
    }

    public void waitForVisible(Locator locator, Duration timeout) {
        WaitUtils.waitForVisible(locator, timeout);
    }

}
