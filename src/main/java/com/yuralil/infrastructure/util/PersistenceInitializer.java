package com.yuralil.infrastructure.util;

import com.yuralil.domain.dao.CategoryDao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Collectors;

public class PersistenceInitializer {

    private static final String INIT_SCRIPT_PATH = "db/init.sql";
    private final ConnectionPool connectionPool;

    public PersistenceInitializer(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void init() {
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement()) {

            // 1. Ініціалізація бази даних
            statement.execute(getSql(INIT_SCRIPT_PATH));

            // 2. Ініціалізація категорій (тільки якщо їх немає)
            ConnectionHolder.set(connection);
            CategoryDao.getInstance().initDefaults();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize the database", e);
        } finally {
            ConnectionHolder.clear();
        }
    }

    private String getSql(String resourcePath) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(PersistenceInitializer.class.getClassLoader().getResourceAsStream(resourcePath))))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read SQL script", e);
        }
    }
}
