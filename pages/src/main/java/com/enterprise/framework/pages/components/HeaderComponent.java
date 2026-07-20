package com.enterprise.framework.pages.components;

import com.enterprise.framework.pages.BasePage;
import com.microsoft.playwright.Page;

/**
 * Example reusable page component.
 */
public class HeaderComponent extends BasePage {
    public HeaderComponent(Page page) {
        super(page);
    }

    public String title() {
        return page.title();
    }
}
