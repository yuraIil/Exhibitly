package com.yuralil.infrastructure.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Створює структуру бази даних при першому запуску (таблиці тощо).
 */
public class PersistenceInitializer {

    private static final String INIT_SCRIPT_PATH = "db/init.sql";
    private final ConnectionPool connectionPool;

    public PersistenceInitializer(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Ініціалізує структуру БД (створює таблиці згідно з init.sql).
     */
   /* public void initSchema() {
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(getSql(INIT_SCRIPT_PATH));

        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize DB schema", e);
        }
    }*/

    private String getSql(String resourcePath) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(PersistenceInitializer.class.getClassLoader().getResourceAsStream(resourcePath))))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read SQL script", e);
        }
    }
}
