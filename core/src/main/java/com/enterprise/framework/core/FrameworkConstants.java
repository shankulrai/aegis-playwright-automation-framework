package com.enterprise.framework.core;

/**
 * Central framework constants.
 */
public final class FrameworkConstants {
    public static final String ENV_PROPERTY = "env";
    public static final String DEFAULT_ENV = "local";
    public static final String SYSTEM_RUN_ID = "framework.runId";
    public static final String DEFAULT_LOG_DIR = "build/logs";
    public static final long DEFAULT_TIMEOUT_MS = 30_000L;
    public static final long DEFAULT_SLOW_MO_MS = 0L;

    private FrameworkConstants() {
    }
}
