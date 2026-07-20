package com.enterprise.framework.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * YAML helper utilities.
 */
public final class YamlUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory()).findAndRegisterModules();

    private YamlUtils() {
    }

    public static <T> T read(Path file, Class<T> type) throws IOException {
        return MAPPER.readValue(file.toFile(), type);
    }

    public static <T> T read(Path file, TypeReference<T> typeReference) throws IOException {
        return MAPPER.readValue(file.toFile(), typeReference);
    }
}
