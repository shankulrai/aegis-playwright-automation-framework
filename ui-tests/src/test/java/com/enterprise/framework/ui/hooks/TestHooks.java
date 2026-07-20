package com.enterprise.framework.ui.hooks;

import com.enterprise.framework.core.driver.DriverManager;
import com.enterprise.framework.pages.LoginPage;
import com.enterprise.framework.pages.PageObjectFactory;
import com.enterprise.framework.reports.AllureReportManager;
import com.enterprise.framework.reports.ExtentReportManager;
import com.enterprise.framework.ui.context.ScenarioContext;
import com.microsoft.playwright.Tracing;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Cucumber hooks for browser setup and failure artifacts.
 */
public class TestHooks {
    private final ScenarioContext context;

    public TestHooks(ScenarioContext context) {
        this.context = context;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        DriverManager.initialize();
        context.page(DriverManager.getPage());
        context.loginPage(PageObjectFactory.create(LoginPage.class, context.page()));
        ExtentReportManager.startTest(scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) throws Exception {
        Path artifactsDir = Path.of("build", "artifacts");
        Files.createDirectories(artifactsDir);
        Path screenshot = artifactsDir.resolve(scenario.getName().replaceAll("[^a-zA-Z0-9.-]", "_") + ".png");
        Path trace = artifactsDir.resolve(scenario.getName().replaceAll("[^a-zA-Z0-9.-]", "_") + ".zip");
        context.page().context().tracing().stop(new Tracing.StopOptions().setPath(trace));
        if (scenario.isFailed()) {
            byte[] screenshotBytes = context.page().screenshot(new com.microsoft.playwright.Page.ScreenshotOptions().setFullPage(true));
            Files.write(screenshot, screenshotBytes);
            scenario.attach(screenshotBytes, "image/png", "failure-screenshot");
            scenario.attach(context.page().content(), "text/html", "page-source");
            scenario.attach(Files.readAllBytes(trace), "application/zip", "trace");
            AllureReportManager.attachBytes("failure-screenshot", screenshotBytes);
            AllureReportManager.attachText("page-source", context.page().content());
            AllureReportManager.attachBytes("trace", Files.readAllBytes(trace));
        }
        ExtentReportManager.flush();
        if (context.localApiServer() != null) {
            context.localApiServer().stop(0);
            context.localApiServer(null);
        }
        context.apiBaseUrl(null);
        context.apiResponse(null);
        DriverManager.quit();
    }
}
