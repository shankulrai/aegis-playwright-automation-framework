package com.enterprise.framework.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * JSON helper utilities.
 */
public final class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    private JsonUtils() {
    }

    public static <T> T read(Path file, Class<T> type) throws IOException {
        return MAPPER.readValue(file.toFile(), type);
    }

    public static <T> T read(String json, Class<T> type) throws JsonProcessingException {
        return MAPPER.readValue(json, type);
    }

    public static <T> T read(byte[] json, Class<T> type) throws IOException {
        return MAPPER.readValue(json, type);
    }

    public static <T> T read(Path file, TypeReference<T> typeReference) throws IOException {
        return MAPPER.readValue(file.toFile(), typeReference);
    }

    public static void write(Path file, Object value) throws IOException {
        Objects.requireNonNull(file, "file");
        Files.createDirectories(file.toAbsolutePath().getParent());
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), value);
    }

    public static String stringify(Object value) throws JsonProcessingException {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
    }
}
