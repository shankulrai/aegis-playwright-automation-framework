package com.enterprise.framework.api;

import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Response validation helpers.
 */
public final class ApiResponseValidator {
    private ApiResponseValidator() {
    }

    public static void statusCode(Response response, int expected) {
        assertThat(response.getStatusCode()).isEqualTo(expected);
    }

    public static void bodyContains(Response response, String text) {
        assertThat(response.getBody().asString()).contains(text);
    }
}
