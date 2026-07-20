package com.enterprise.framework.core.playwright;

import com.enterprise.framework.core.browser.BrowserSettings;
import com.enterprise.framework.core.browser.BrowserType;
import com.enterprise.framework.core.browser.ExecutionMode;
import com.enterprise.framework.core.config.ConfigManager;
import com.enterprise.framework.core.logging.LoggerManager;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType.ConnectOverCDPOptions;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Thread-safe Playwright factory supporting local and remote execution.
 */
public final class PlaywrightFactory {
    private static final Logger LOGGER = LoggerManager.getLogger(PlaywrightFactory.class);
    private static final ThreadLocal<PlaywrightSession> SESSION = new ThreadLocal<>();

    private PlaywrightFactory() {
    }

    public static PlaywrightSession createSession() {
        PlaywrightSession current = SESSION.get();
        if (current != null) {
            return current;
        }

        ConfigManager configManager = ConfigManager.getInstance();
        BrowserSettings settings = BrowserSettings.builder()
            .browserType(resolveBrowserType(configManager.browser()))
            .executionMode(configManager.remote() ? ExecutionMode.REMOTE : (configManager.headless() ? ExecutionMode.HEADLESS : ExecutionMode.HEADED))
            .remoteEndpoint(configManager.remoteEndpoint())
            .headless(configManager.headless())
            .slowMoMs(configManager.slowMoMs())
            .timeoutMs(configManager.timeoutMs())
            .build();

        Playwright playwright = Playwright.create();
        Browser browser = createBrowser(playwright, settings);
        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1440, 1080));
        context.setDefaultTimeout(settings.timeoutMs());
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
        Page page = context.newPage();
        PlaywrightSession session = new PlaywrightSession(playwright, browser, context, page);
        SESSION.set(session);
        LOGGER.info(
            "Created Playwright session: browser={}, mode={}, headless={}, slowMoMs={}",
            settings.browserType(),
            settings.executionMode(),
            settings.headless(),
            settings.slowMoMs()
        );
        return session;
    }

    public static PlaywrightSession getSession() {
        PlaywrightSession session = SESSION.get();
        if (session == null) {
            throw new IllegalStateException("Playwright session has not been created for this thread");
        }
        return session;
    }

    public static Page getPage() {
        return getSession().page();
    }

    public static void closeSession() {
        PlaywrightSession session = SESSION.get();
        if (session == null) {
            return;
        }
        try {
            session.context().close();
        } finally {
            try {
                session.browser().close();
            } finally {
                session.playwright().close();
                SESSION.remove();
                LOGGER.info("Closed Playwright session");
            }
        }
    }

    private static Browser createBrowser(Playwright playwright, BrowserSettings settings) {
        if (settings.executionMode() == ExecutionMode.REMOTE) {
            if (settings.browserType() != BrowserType.CHROMIUM) {
                throw new IllegalArgumentException("Remote mode currently supports Chromium CDP connections only");
            }
            if (settings.remoteEndpoint() == null || settings.remoteEndpoint().isBlank()) {
                throw new IllegalStateException("remoteEndpoint must be configured when remote mode is enabled");
            }
            return playwright.chromium().connectOverCDP(settings.remoteEndpoint(), new ConnectOverCDPOptions());
        }

        LaunchOptions launchOptions = new LaunchOptions()
            .setHeadless(settings.headless())
            .setSlowMo(settings.slowMoMs());

        return switch (settings.browserType()) {
            case CHROMIUM -> playwright.chromium().launch(launchOptions);
            case FIREFOX -> playwright.firefox().launch(launchOptions);
            case WEBKIT -> playwright.webkit().launch(launchOptions);
        };
    }

    private static BrowserType resolveBrowserType(String browser) {
        Objects.requireNonNull(browser, "browser");
        return BrowserType.valueOf(browser.trim().toUpperCase());
    }
}
