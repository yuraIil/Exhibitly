package com.yuralil.infrastructure.util;

import com.yuralil.domain.dao.CategoryDao;
import com.yuralil.domain.dao.UsersDao;

import java.sql.Connection;

public class AppInitializer {

    public static void initAll() {
        ConnectionPool pool = new ConnectionPool();
        Connection connection = pool.getConnection();
        ConnectionHolder.set(connection); // зберігаємо глобальне з'єднання

        CategoryDao.getInstance().initDefaults();     // дефолтні категорії
        UsersDao.getInstance().initAdmin();           // ⬅️ тепер створює адміністратора
    }
}
