package com.yuralil.infrastructure.util;

import com.yuralil.domain.dao.CategoryDao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Відповідає за ініціалізацію структури бази даних та дефолтних даних.
 * Використовується на старті застосунку для створення таблиць і первинного заповнення.
 */
public class PersistenceInitializer {

    private static final String INIT_SCRIPT_PATH = "db/init.sql";
    private final ConnectionPool connectionPool;

    /**
     * Створює новий {@code PersistenceInitializer} з наданим пулом з'єднань.
     *
     * @param connectionPool екземпляр {@link ConnectionPool}, який буде використано
     */
    public PersistenceInitializer(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Ініціалізує базу даних: виконує SQL-скрипт і додає дефолтні категорії, якщо їх ще немає.
     */
    public void init() {
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement()) {

            // 1. Створення структури бази
            statement.execute(getSql(INIT_SCRIPT_PATH));

            // 2. Ініціалізація категорій
            ConnectionHolder.set(connection);
            CategoryDao.getInstance().initDefaults();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize the database", e);
        } finally {
            ConnectionHolder.clear();
        }
    }

    /**
     * Зчитує SQL-скрипт з ресурсів класу.
     *
     * @param resourcePath шлях до SQL-файлу у ресурсах (наприклад: {@code db/init.sql})
     * @return SQL-рядок, об'єднаний по рядках
     */
    private String getSql(String resourcePath) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(PersistenceInitializer.class.getClassLoader().getResourceAsStream(resourcePath))))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read SQL script", e);
        }
    }
}
