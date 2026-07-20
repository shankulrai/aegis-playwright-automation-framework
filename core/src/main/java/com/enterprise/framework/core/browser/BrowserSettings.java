package com.enterprise.framework.core.browser;

/**
 * Immutable browser runtime settings.
 */
public final class BrowserSettings {
    private final BrowserType browserType;
    private final ExecutionMode executionMode;
    private final String remoteEndpoint;
    private final boolean headless;
    private final long slowMoMs;
    private final long timeoutMs;

    private BrowserSettings(Builder builder) {
        this.browserType = builder.browserType;
        this.executionMode = builder.executionMode;
        this.remoteEndpoint = builder.remoteEndpoint;
        this.headless = builder.headless;
        this.slowMoMs = builder.slowMoMs;
        this.timeoutMs = builder.timeoutMs;
    }

    public BrowserType browserType() {
        return browserType;
    }

    public ExecutionMode executionMode() {
        return executionMode;
    }

    public String remoteEndpoint() {
        return remoteEndpoint;
    }

    public boolean headless() {
        return headless;
    }

    public long slowMoMs() {
        return slowMoMs;
    }

    public long timeoutMs() {
        return timeoutMs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private BrowserType browserType = BrowserType.CHROMIUM;
        private ExecutionMode executionMode = ExecutionMode.HEADLESS;
        private String remoteEndpoint = "";
        private boolean headless = true;
        private long slowMoMs;
        private long timeoutMs = 30_000L;

        public Builder browserType(BrowserType browserType) {
            this.browserType = browserType;
            return this;
        }

        public Builder executionMode(ExecutionMode executionMode) {
            this.executionMode = executionMode;
            return this;
        }

        public Builder remoteEndpoint(String remoteEndpoint) {
            this.remoteEndpoint = remoteEndpoint;
            return this;
        }

        public Builder headless(boolean headless) {
            this.headless = headless;
            return this;
        }

        public Builder slowMoMs(long slowMoMs) {
            this.slowMoMs = slowMoMs;
            return this;
        }

        public Builder timeoutMs(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public BrowserSettings build() {
            return new BrowserSettings(this);
        }
    }
}
