package com.enterprise.framework.utilities;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * CSV helper utilities.
 */
public final class CsvUtils {
    private static final CsvMapper MAPPER = new CsvMapper();

    private CsvUtils() {
    }

    public static List<Map<String, String>> read(Path file) throws IOException {
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        return MAPPER.readerFor(Map.class).with(schema).<Map<String, String>>readValues(file.toFile()).readAll();
    }
}
