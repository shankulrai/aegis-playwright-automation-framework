package com.enterprise.framework.data;

import com.enterprise.framework.utilities.JsonUtils;

import java.io.IOException;
import java.nio.file.Path;

/**
 * JSON data loader.
 */
public class JsonDataLoader<T> implements DataLoader<T> {
    private final Class<T> type;

    public JsonDataLoader(Class<T> type) {
        this.type = type;
    }

    @Override
    public T load(Path path) throws IOException {
        return JsonUtils.read(path, type);
    }
}
