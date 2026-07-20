package com.enterprise.framework.ui.context;

import com.enterprise.framework.pages.LoginPage;
import com.sun.net.httpserver.HttpServer;
import com.microsoft.playwright.Page;
import io.restassured.response.Response;

/**
 * Per-scenario object graph for PicoContainer injection.
 */
public class ScenarioContext {
    private Page page;
    private LoginPage loginPage;
    private HttpServer localApiServer;
    private String apiBaseUrl;
    private Response apiResponse;

    public Page page() {
        return page;
    }

    public void page(Page page) {
        this.page = page;
    }

    public LoginPage loginPage() {
        return loginPage;
    }

    public void loginPage(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public HttpServer localApiServer() {
        return localApiServer;
    }

    public void localApiServer(HttpServer localApiServer) {
        this.localApiServer = localApiServer;
    }

    public String apiBaseUrl() {
        return apiBaseUrl;
    }

    public void apiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public Response apiResponse() {
        return apiResponse;
    }

    public void apiResponse(Response apiResponse) {
        this.apiResponse = apiResponse;
    }
}
