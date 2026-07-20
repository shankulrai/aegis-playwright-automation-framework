package com.enterprise.framework.ui.tests;

import com.enterprise.framework.core.driver.DriverManager;
import com.enterprise.framework.core.test.BaseTest;
import com.enterprise.framework.pages.TestAutomationPracticePage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic end-to-end automation suite for testautomationpractice.blogspot.com.
 */
public class TestAutomationPracticeScenariosTest extends BaseTest {
    private TestAutomationPracticePage practicePage;

    @BeforeEach
    void initialize() {
        setUp();
        practicePage = new TestAutomationPracticePage(DriverManager.getPage());
        practicePage.open();
    }

    @AfterEach
    void cleanup() {
        tearDown();
    }

    @Test
    void shouldOpenPracticePage() {
        assertThat(practicePage.title()).containsIgnoringCase("automation");
    }

    @Test
    void shouldFillMainFormFields() {
        practicePage.fillName("Shankul");
        practicePage.fillEmail("shankul@example.com");
        practicePage.fillPhone("9999999999");
        practicePage.fillAddress("New York");
        practicePage.selectGender("male");
        practicePage.selectDays(List.of("monday", "wednesday", "friday"));
    }

    @Test
    void shouldSelectDropdownAndMultiselectValues() {
        practicePage.selectCountry("India");
        practicePage.selectColors("Red", "Blue");
        List<String> normalized = practicePage.selectedColors().stream()
            .map(value -> value.toLowerCase(Locale.ROOT))
            .toList();
        assertThat(normalized).contains("red", "blue");
    }

    @Test
    void shouldSetDatePickerValue() {
        practicePage.setDate("08/15/2026");
        assertThat(practicePage.dateValue()).isEqualTo("08/15/2026");
    }

    @Test
    void shouldHandleBrowserDialogs() {
        assertThat(practicePage.triggerAlert()).isNotBlank();
        assertThat(practicePage.triggerConfirm()).isNotBlank();
        assertThat(practicePage.triggerPrompt("Playwright")).isNotBlank();
    }

    @Test
    void shouldValidateWebTableHasRows() {
        assertThat(practicePage.webTableRowCount()).isGreaterThan(0);
    }

    @Test
    void shouldUploadFileWhenAvailable() throws Exception {
        Assumptions.assumeTrue(practicePage.hasFileInput(), "File input is not present on page");
        Path uploadFile = Files.createTempFile("practice-upload-", ".txt");
        try {
            Files.writeString(uploadFile, "playwright upload sample");
            practicePage.uploadFile(uploadFile);
            assertThat(Files.exists(uploadFile)).isTrue();
        } finally {
            Files.deleteIfExists(uploadFile);
        }
    }
}
