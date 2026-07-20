package com.enterprise.framework.api;

import com.enterprise.framework.core.config.ConfigManager;
import io.restassured.specification.RequestSpecification;

/**
 * Base API runner support.
 */
public abstract class ApiTestRunner {
    protected RequestSpecification spec() {
        return new ApiRequestBuilder()
            .baseUri(ConfigManager.getInstance().apiBaseUrl())
            .build();
    }
}
