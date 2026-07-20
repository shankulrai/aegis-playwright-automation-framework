package com.enterprise.framework.integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Lightweight JDBC helper utilities.
 */
public class DatabaseUtils {
    private final Supplier<Connection> connectionSupplier;

    public DatabaseUtils(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    public <T> T query(String sql, Function<ResultSet, T> mapper) throws SQLException {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return mapper.apply(resultSet);
        }
    }

    public int execute(String sql) throws SQLException {
        try (Connection connection = connectionSupplier.get();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            return statement.executeUpdate();
        }
    }
}
