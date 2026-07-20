package com.enterprise.framework.teamalpha.tests;

import com.enterprise.framework.core.driver.DriverManager;
import com.enterprise.framework.core.test.BaseTest;
import com.enterprise.framework.pages.LoginPage;
import com.enterprise.framework.utilities.ApiUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example of a team-owned module that consumes the framework without changing core.
 */
class TeamAlphaHybridSmokeTest extends BaseTest {
    private LoginPage loginPage;
    private HttpServer localApiServer;
    private String localApiBaseUrl;

    @BeforeEach
    void init() throws IOException {
        setUp();
        loginPage = new LoginPage(DriverManager.getPage());
        loginPage.loadDemoPage();
        localApiServer = HttpServer.create(new InetSocketAddress(0), 0);
        localApiServer.createContext("/health", new JsonHandler());
        localApiServer.start();
        localApiBaseUrl = "http://localhost:" + localApiServer.getAddress().getPort();
    }

    @AfterEach
    void cleanup() {
        if (localApiServer != null) {
            localApiServer.stop(0);
        }
        tearDown();
    }

    @Test
    void shouldExecuteHybridUiAndApiFlowInTeamModule() {
        loginPage.login("demo", "demo123");
        assertThat(loginPage.status()).isEqualTo("SUCCESS");

        Response response = given()
            .spec(ApiUtils.requestSpec(localApiBaseUrl))
            .when()
            .get("/health")
            .then()
            .extract()
            .response();

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.getBody().asString()).contains("\"status\":\"UP\"");
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
