package com.enterprise.framework.core.retry;

import com.enterprise.framework.core.logging.LoggerManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Simple retry executor for flaky interactions.
 */
public final class RetryMechanism {
    private static final Logger LOGGER = LoggerManager.getLogger(RetryMechanism.class);

    private RetryMechanism() {
    }

    public static <T> T execute(Callable<T> action, int maxAttempts, Duration backoff) {
        Objects.requireNonNull(action, "action");
        Objects.requireNonNull(backoff, "backoff");
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be greater than zero");
        }
        RuntimeException lastFailure = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return action.call();
            } catch (RuntimeException ex) {
                lastFailure = ex;
                LOGGER.warn("Retry attempt {} failed: {}", attempt, ex.getMessage());
            } catch (Exception ex) {
                lastFailure = new IllegalStateException("Retryable action failed", ex);
                LOGGER.warn("Retry attempt {} failed: {}", attempt, ex.getMessage());
            }
            if (attempt < maxAttempts) {
                sleep(backoff);
            }
        }
        throw lastFailure;
    }

    public static void execute(Runnable action, int maxAttempts, Duration backoff) {
        execute(() -> {
            action.run();
            return null;
        }, maxAttempts, backoff);
    }

    private static void sleep(Duration backoff) {
        try {
            Thread.sleep(Math.max(0L, backoff.toMillis()));
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Retry wait interrupted", interruptedException);
        }
    }
}
