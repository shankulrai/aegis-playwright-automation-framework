package com.enterprise.framework.data;

import com.enterprise.framework.data.models.TestUser;
import com.enterprise.framework.utilities.RandomDataUtils;

/**
 * Sample builder/factory for test fixtures.
 */
public final class TestDataFactory {
    private TestDataFactory() {
    }

    public static TestUser user(String role) {
        return new TestUser("user_" + RandomDataUtils.alphaNumeric(6), "Password123!", role);
    }
}
