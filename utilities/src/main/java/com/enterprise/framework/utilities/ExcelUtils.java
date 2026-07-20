package com.enterprise.framework.utilities;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel helper utilities.
 */
public final class ExcelUtils {
    private ExcelUtils() {
    }

    public static List<Map<String, String>> readSheet(Path file, String sheetName) throws IOException {
        try (InputStream inputStream = Files.newInputStream(file);
             var workbook = WorkbookFactory.create(inputStream)) {
            var sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }
            Row headerRow = sheet.getRow(0);
            List<Map<String, String>> rows = new ArrayList<>();
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                Map<String, String> record = new LinkedHashMap<>();
                for (int cellIndex = 0; cellIndex < headerRow.getLastCellNum(); cellIndex++) {
                    String key = headerRow.getCell(cellIndex).getStringCellValue();
                    record.put(key, row.getCell(cellIndex) == null ? "" : row.getCell(cellIndex).toString());
                }
                rows.add(record);
            }
            return rows;
        }
    }
}
