package com.enterprise.framework.api;

import com.enterprise.framework.core.config.ConfigManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;

/**
 * Offline API sample using an embedded HTTP server.
 */
public class SampleApiTest extends ApiTestRunner {
    private static HttpServer server;
    private static String baseUrl;

    @BeforeAll
    static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/health", new JsonHandler());
        server.start();
        baseUrl = "http://localhost:" + server.getAddress().getPort();
    }

    @AfterAll
    static void stopServer() {
        server.stop(0);
    }

    @Test
    void shouldReadHealthEndpoint() {
        Response response = given()
            .spec(new ApiRequestBuilder().baseUri(baseUrl).build())
            .when()
            .get("/health")
            .then()
            .extract()
            .response();

        ApiResponseValidator.statusCode(response, 200);
        ApiResponseValidator.bodyContains(response, "\"status\":\"UP\"");
    }

    private static final class JsonHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            byte[] payload = "{\"status\":\"UP\"}".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, payload.length);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(payload);
            }
        }
    }
}
