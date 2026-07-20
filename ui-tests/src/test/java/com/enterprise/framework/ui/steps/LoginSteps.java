package com.enterprise.framework.ui.steps;

import com.enterprise.framework.ui.context.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Sample UI step definitions.
 */
public class LoginSteps {
    private final ScenarioContext context;

    public LoginSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("the demo login page is opened")
    public void the_demo_login_page_is_opened() {
        context.loginPage().loadDemoPage();
    }

    @When("the user logs in with username {string} and password {string}")
    public void the_user_logs_in(String username, String password) {
        context.loginPage().login(username, password);
    }

    @Then("the status should be {string}")
    public void the_status_should_be(String expectedStatus) {
        assertThat(context.loginPage().status()).isEqualTo(expectedStatus);
    }
}
