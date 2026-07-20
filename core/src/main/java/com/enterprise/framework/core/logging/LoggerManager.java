package com.enterprise.framework.core.logging;

import com.enterprise.framework.core.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Central logger access and run-scoped initialization.
 */
public final class LoggerManager {
    private static volatile boolean initialized;

    private LoggerManager() {
    }

    public static Logger getLogger(Class<?> type) {
        initialize();
        return LogManager.getLogger(type);
    }

    public static synchronized void initialize() {
        if (!initialized) {
            String runId = System.getProperty(FrameworkConstants.SYSTEM_RUN_ID);
            if (runId == null || runId.isBlank()) {
                runId = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(OffsetDateTime.now()) + "-" + UUID.randomUUID();
                System.setProperty(FrameworkConstants.SYSTEM_RUN_ID, runId);
            }
            try {
                Files.createDirectories(Path.of(FrameworkConstants.DEFAULT_LOG_DIR));
            } catch (Exception exception) {
                throw new IllegalStateException("Unable to create log directory", exception);
            }
            initialized = true;
        }
    }
}
