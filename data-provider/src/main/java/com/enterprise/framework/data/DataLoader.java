package com.enterprise.framework.data;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Generic data loader contract.
 */
public interface DataLoader<T> {
    T load(Path path) throws IOException;
}
