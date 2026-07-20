package com.enterprise.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Page object for https://testautomationpractice.blogspot.com/.
 */
public class TestAutomationPracticePage extends BasePage {
    private static final String PAGE_URL = "https://testautomationpractice.blogspot.com/";

    public TestAutomationPracticePage(Page page) {
        super(page);
    }

    public void open() {
        navigate(PAGE_URL);
    }

    public String title() {
        return page.title();
    }

    public void fillName(String value) {
        firstExisting("#name", "input[name='name']", "input[placeholder*='Name']").fill(value);
    }

    public void fillEmail(String value) {
        firstExisting("#email", "input[name='email']", "input[type='email']").fill(value);
    }

    public void fillPhone(String value) {
        firstExisting("#phone", "input[name='phone']", "input[type='tel']").fill(value);
    }

    public void fillAddress(String value) {
        firstExisting("#textarea", "textarea#textarea", "textarea[name='address']", "textarea").fill(value);
    }

    public void selectGender(String gender) {
        if ("male".equalsIgnoreCase(gender)) {
            firstExisting("#male", "input[value='male']").check();
        } else {
            firstExisting("#female", "input[value='female']").check();
        }
    }

    public void selectDays(List<String> days) {
        for (String day : days) {
            firstExisting("#" + day.toLowerCase(), "input[value='" + capitalize(day) + "']", "input[value='" + day.toUpperCase() + "']").check();
        }
    }

    public void selectCountry(String value) {
        firstExisting("#country", "select[name='country']").selectOption(value);
    }

    public void selectColors(String... values) {
        List<String> normalizedTargets = Arrays.stream(values).map(value -> value.toLowerCase().trim()).toList();
        firstExisting("#colors", "select[name='colors']").evaluate(
            "(node, targets) => {" +
                "const targetSet = new Set(targets);" +
                "Array.from(node.options).forEach(opt => {" +
                    "const byValue = String(opt.value).toLowerCase();" +
                    "const byText = String(opt.text).toLowerCase();" +
                    "opt.selected = targetSet.has(byValue) || targetSet.has(byText);" +
                "});" +
                "node.dispatchEvent(new Event('change', { bubbles: true }));" +
            "}",
            normalizedTargets
        );
    }

    public List<String> selectedColors() {
        @SuppressWarnings("unchecked")
        List<Object> selectedValues = (List<Object>) firstExisting("#colors", "select[name='colors']")
            .evaluate("node => Array.from(node.selectedOptions).map(o => o.value)");
        return selectedValues.stream().map(String::valueOf).collect(Collectors.toList());
    }

    public void setDate(String value) {
        firstExisting("#datepicker", "input[id*='date']").fill(value);
    }

    public String dateValue() {
        return firstExisting("#datepicker", "input[id*='date']").inputValue();
    }

    public String triggerAlert() {
        return triggerDialogAndAccept("button:has-text('Alert')");
    }

    public String triggerConfirm() {
        return triggerDialogAndAccept("button:has-text('Confirm')");
    }

    public String triggerPrompt(String input) {
        AtomicReference<String> message = new AtomicReference<>();
        page.onceDialog(dialog -> {
            message.set(dialog.message());
            dialog.accept(input);
        });
        firstExisting("button:has-text('Prompt')", "button[onclick*='prompt']").click();
        return message.get();
    }

    public long webTableRowCount() {
        Locator rows = firstExisting("table tbody tr", "table tr");
        return rows.count();
    }

    public void uploadFile(Path filePath) {
        Locator fileInput = firstExisting("input[type='file']");
        fileInput.setInputFiles(filePath);
    }

    public boolean hasFileInput() {
        return page.locator("input[type='file']").count() > 0;
    }

    private String triggerDialogAndAccept(String selector) {
        AtomicReference<String> message = new AtomicReference<>();
        page.onceDialog(dialog -> {
            message.set(dialog.message());
            dialog.accept();
        });
        firstExisting(selector).click();
        return message.get();
    }

    private Locator firstExisting(String... selectors) {
        for (String selector : selectors) {
            Locator locator = page.locator(selector);
            if (locator.count() > 0) {
                return locator.first();
            }
        }
        throw new IllegalStateException("None of the selectors were found: " + String.join(", ", selectors));
    }

    private static String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        String lower = value.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
