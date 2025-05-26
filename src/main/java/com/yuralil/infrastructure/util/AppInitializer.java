package com.yuralil.infrastructure.util;

import com.yuralil.domain.dao.CategoryDao;

import java.sql.Connection;

/**
 * Ініціалізує підключення до БД, структуру та дефолтні дані на старті застосунку.
 */
public class AppInitializer {

    public static void initAll() {
        ConnectionPool pool = new ConnectionPool();
        Connection connection = pool.getConnection();
        ConnectionHolder.set(connection); // зберігаємо глобальне з'єднання

        // створення структури БД
      //  new PersistenceInitializer(pool).initSchema();

        // дефолтні категорії
        CategoryDao.getInstance().initDefaults();
    }
}
