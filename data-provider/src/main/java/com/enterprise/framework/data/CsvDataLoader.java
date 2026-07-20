package com.enterprise.framework.data;

import com.enterprise.framework.utilities.CsvUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * CSV data loader.
 */
public class CsvDataLoader implements DataLoader<List<Map<String, String>>> {
    @Override
    public List<Map<String, String>> load(Path path) throws IOException {
        return CsvUtils.read(path);
    }
}
