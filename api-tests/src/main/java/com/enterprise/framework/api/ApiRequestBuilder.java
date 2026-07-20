package com.enterprise.framework.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fluent request specification builder.
 */
public final class ApiRequestBuilder {
    private String baseUri;
    private final Map<String, String> headers = new LinkedHashMap<>();

    public ApiRequestBuilder baseUri(String baseUri) {
        this.baseUri = baseUri;
        return this;
    }

    public ApiRequestBuilder header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public RequestSpecification build() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
            .setBaseUri(baseUri)
            .setContentType(ContentType.JSON);
        headers.forEach(builder::addHeader);
        return builder.build();
    }
}
