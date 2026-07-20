package com.enterprise.framework.pages;

import com.microsoft.playwright.Page;

import java.lang.reflect.Constructor;

/**
 * Reflection-based page factory for constructor-injected page objects.
 */
public final class PageObjectFactory {
    private PageObjectFactory() {
    }

    public static <T> T create(Class<T> pageType, Page page) {
        try {
            Constructor<T> constructor = pageType.getDeclaredConstructor(Page.class);
            constructor.setAccessible(true);
            return constructor.newInstance(page);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to create page object: " + pageType.getName(), exception);
        }
    }
}
