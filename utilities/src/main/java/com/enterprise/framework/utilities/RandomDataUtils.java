package com.enterprise.framework.utilities;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Random test data generation utilities.
 */
public final class RandomDataUtils {
    private static final SecureRandom RANDOM = new SecureRandom();

    private RandomDataUtils() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String alphaNumeric(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder builder = new StringBuilder(length);
        for (int index = 0; index < length; index++) {
            builder.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
        }
        return builder.toString();
    }
}
