// ConnectionPool.java
package com.yuralil.infrastructure.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionPool {
    private final BlockingQueue<Connection> pool;
    private final String url;
    private final String user;
    private final String password;
    private final int maxConnections;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public ConnectionPool() {
        this.url = PropertiesUtil.get("db.url");
        this.user = PropertiesUtil.get("db.username");
        this.password = PropertiesUtil.get("db.password");
        this.maxConnections = Integer.parseInt(PropertiesUtil.get("db.pool.size", "5"));
        this.pool = new ArrayBlockingQueue<>(maxConnections);
        init();
    }

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
