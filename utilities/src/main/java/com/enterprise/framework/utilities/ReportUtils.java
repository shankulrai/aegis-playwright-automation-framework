package com.enterprise.framework.utilities;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Shared report artifact path helpers.
 */
public final class ReportUtils {
    private ReportUtils() {
    }

    public static Path artifactPath(String folder, String fileName) {
        return Paths.get("build", folder, fileName);
    }
}
