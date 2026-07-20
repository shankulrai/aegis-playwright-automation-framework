package com.enterprise.framework.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date helper utilities.
 */
public final class DateUtils {
    private DateUtils() {
    }

    public static String now(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }
}
