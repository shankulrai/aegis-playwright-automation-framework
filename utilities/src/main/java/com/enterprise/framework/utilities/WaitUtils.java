package com.enterprise.framework.utilities;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.time.Duration;
import java.util.function.BooleanSupplier;

/**
 * Wait helper utilities.
 */
public final class WaitUtils {
    private WaitUtils() {
    }

    public static void waitForVisible(Locator locator, Duration timeout) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(timeout.toMillis()));
    }

    public static void waitForPageLoad(Page page) {
        page.waitForLoadState();
    }

    public static void waitUntil(BooleanSupplier condition, Duration timeout, Duration polling) {
        long deadline = System.nanoTime() + timeout.toNanos();
        while (System.nanoTime() < deadline) {
            if (condition.getAsBoolean()) {
                return;
            }
            try {
                Thread.sleep(polling.toMillis());
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Wait interrupted", interruptedException);
            }
        }
        throw new IllegalStateException("Condition was not met within " + timeout);
    }
}
