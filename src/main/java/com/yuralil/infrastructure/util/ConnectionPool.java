package com.yuralil.infrastructure.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Пул з'єднань до бази даних.
 * Реалізує проксі для {@link Connection}, які перехоплюють метод {@code close()} та повертають з'єднання назад у пул.
 */
public class ConnectionPool {
    private final BlockingQueue<Connection> pool;
    private final String url;
    private final String user;
    private final String password;
    private final int maxConnections;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    /**
     * Створює пул з'єднань з параметрів, прочитаних із конфігурації.
     * Очікується, що у {@code application.properties} будуть присутні:
     * <ul>
     *     <li>{@code db.url}</li>
     *     <li>{@code db.username}</li>
     *     <li>{@code db.password}</li>
     *     <li>{@code db.pool.size} (необов'язково, за замовчуванням 5)</li>
     * </ul>
     */
    public ConnectionPool() {
        this.url = PropertiesUtil.get("db.url");
        this.user = PropertiesUtil.get("db.username");
        this.password = PropertiesUtil.get("db.password");
        this.maxConnections = Integer.parseInt(PropertiesUtil.get("db.pool.size", "5"));
        this.pool = new ArrayBlockingQueue<>(maxConnections);
        init();
    }

    /**
     * Ініціалізує пул з'єднань.
     * Створює проксі-з'єднання, які повертаються в пул при виклику {@code close()}.
     */
    private void init() {
        if (initialized.compareAndSet(false, true)) {
            for (int i = 0; i < maxConnections; i++) {
                try {
                    pool.put(createProxyConnection());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to initialize connection pool", e);
                }
            }
        }
    }

    /**
     * Створює проксі-обгортку над реальним {@link Connection}, яка перехоплює {@code close()} та повертає з'єднання назад у пул.
     *
     * @return проксі-з'єднання
     * @throws SQLException якщо не вдається підключитися
     */
    private Connection createProxyConnection() throws SQLException {
        Connection realConnection = DriverManager.getConnection(url, user, password);
        return (Connection) Proxy.newProxyInstance(
                ConnectionPool.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        pool.offer((Connection) proxy);
                        return null;
                    }
                    return method.invoke(realConnection, args);
                });
    }

    /**
     * Отримує з'єднання з пулу.
     * Якщо з'єднання закрите, створюється нове.
     *
     * @return активне з'єднання
     */
    public Connection getConnection() {
        try {
            Connection connection = pool.take();
            if (connection.isClosed()) {
                return createProxyConnection();
            }
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get connection from pool", e);
        }
    }

    /**
     * Закриває всі з'єднання в пулі та очищає його.
     * Повторне використання цього пулу після виклику {@code shutdown()} не рекомендовано.
     */
    public void shutdown() {
        pool.forEach(conn -> {
            try {
                conn.unwrap(Connection.class).close();
            } catch (SQLException ignored) {}
        });
        pool.clear();
        initialized.set(false);
    }
}
