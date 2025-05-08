// ConnectionHolder.java
package com.yuralil.infrastructure.util;

import java.sql.Connection;

public class ConnectionHolder {
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public static void set(Connection connection) {
        connectionHolder.set(connection);
    }

    public static Connection get() {
        return connectionHolder.get();
    }

    public static void clear() {
        connectionHolder.remove();
    }
}