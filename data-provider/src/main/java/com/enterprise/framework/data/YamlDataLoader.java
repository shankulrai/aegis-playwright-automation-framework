package com.enterprise.framework.data;

import com.enterprise.framework.utilities.YamlUtils;

import java.io.IOException;
import java.nio.file.Path;

/**
 * YAML data loader.
 */
public class YamlDataLoader<T> implements DataLoader<T> {
    private final Class<T> type;

    public YamlDataLoader(Class<T> type) {
        this.type = type;
    }

    @Override
    public T load(Path path) throws IOException {
        return YamlUtils.read(path, type);
    }
}
