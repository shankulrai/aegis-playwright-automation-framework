package com.enterprise.framework.utilities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Rest Assured helper utilities.
 */
public final class ApiUtils {
    private ApiUtils() {
    }

    public static RequestSpecification requestSpec(String baseUri) {
        return new RequestSpecBuilder()
            .setBaseUri(baseUri)
            .setContentType(ContentType.JSON)
            .build();
    }
}
