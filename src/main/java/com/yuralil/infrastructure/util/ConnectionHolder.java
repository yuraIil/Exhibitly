package com.yuralil.infrastructure.util;

import java.sql.Connection;

/**
 * Утилітний клас для зберігання {@link Connection} у потоці за допомогою {@link ThreadLocal}.
 * Дозволяє безпечно передавати з'єднання між DAO-класами в межах одного потоку.
 */
public class ConnectionHolder {

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    /**
     * Зберігає з'єднання в поточному потоці.
     *
     * @param connection об'єкт {@link Connection}, який буде збережено
     */
    public static void set(Connection connection) {
        connectionHolder.set(connection);
    }

    /**
     * Повертає з'єднання, пов'язане з поточним потоком.
     *
     * @return об'єкт {@link Connection} або {@code null}, якщо не встановлено
     */
    public static Connection get() {
        return connectionHolder.get();
    }

    /**
     * Очищає з'єднання з поточного потоку.
     * Має викликатись у блоці {@code finally} після завершення роботи з БД.
     */
    public static void clear() {
        connectionHolder.remove();
    }
}
