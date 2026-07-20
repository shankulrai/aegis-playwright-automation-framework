package com.enterprise.framework.data;

import com.enterprise.framework.utilities.ExcelUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Excel data loader.
 */
public class ExcelDataLoader implements DataLoader<List<Map<String, String>>> {
    private final String sheetName;

    public ExcelDataLoader(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public List<Map<String, String>> load(Path path) throws IOException {
        return ExcelUtils.readSheet(path, sheetName);
    }
}
