package com.enterprise.framework.ui.steps;

import com.enterprise.framework.core.config.ConfigManager;
import com.enterprise.framework.ui.context.ScenarioContext;
import com.enterprise.framework.utilities.ApiUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Hybrid API step definitions executed in the same Cucumber scenario as UI steps.
 */
public class HybridApiSteps {
    private final ScenarioContext context;

    public HybridApiSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("a local health api is available")
    public void a_local_health_api_is_available() throws IOException {
        if (context.localApiServer() != null) {
            return;
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/health", new JsonHandler());
        server.start();
        context.localApiServer(server);
        context.apiBaseUrl("http://localhost:" + server.getAddress().getPort());
    }

    @When("the health api is requested")
    public void the_health_api_is_requested() {
        String baseUri = context.apiBaseUrl() != null
            ? context.apiBaseUrl()
            : ConfigManager.getInstance().apiBaseUrl();
        context.apiResponse(
            given()
                .spec(ApiUtils.requestSpec(baseUri))
                .when()
                .get("/health")
                .then()
                .extract()
                .response()
        );
    }

    @Then("the api response status should be {int}")
    public void the_api_response_status_should_be(int expectedStatusCode) {
        assertThat(context.apiResponse()).as("API response should be captured before validating status").isNotNull();
        assertThat(context.apiResponse().statusCode()).isEqualTo(expectedStatusCode);
    }

    @Then("the api response should contain {string}")
    public void the_api_response_should_contain(String expectedValue) {
        assertThat(context.apiResponse()).as("API response should be captured before validating payload").isNotNull();
        assertThat(context.apiResponse().getBody().asString()).contains(expectedValue);
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
