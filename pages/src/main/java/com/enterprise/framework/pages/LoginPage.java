package com.enterprise.framework.pages;

import com.enterprise.framework.pages.components.HeaderComponent;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Sample login page object backed by a local HTML fixture.
 */
public class LoginPage extends BasePage {
    private final Locator username = page.locator("#username");
    private final Locator password = page.locator("#password");
    private final Locator submit = page.locator("#submit");
    private final Locator status = page.locator("#status");
    private final HeaderComponent header;

    public LoginPage(Page page) {
        super(page);
        this.header = new HeaderComponent(page);
    }

    public void loadDemoPage() {
        page.setContent("""
            <html>
              <head><title>Demo Login</title></head>
              <body>
                <h1 id="header">Demo Login</h1>
                <form>
                  <input id="username" />
                  <input id="password" type="password" />
                  <button id="submit" type="button" onclick="document.getElementById('status').innerText = 'SUCCESS'">Login</button>
                </form>
                <div id="status">READY</div>
              </body>
            </html>
            """);
    }

    public void login(String user, String pass) {
        fill(username, user);
        fill(password, pass);
        click(submit);
    }

    public String status() {
        return status.textContent();
    }

    public HeaderComponent header() {
        return header;
    }
}
