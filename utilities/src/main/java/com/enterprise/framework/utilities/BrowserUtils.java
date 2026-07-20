package com.enterprise.framework.utilities;

import com.microsoft.playwright.Page;

/**
 * Browser helper utilities.
 */
public final class BrowserUtils {
    private BrowserUtils() {
    }

    public static void navigate(Page page, String url) {
        page.navigate(url);
    }

    public static void refresh(Page page) {
        page.reload();
    }
}
