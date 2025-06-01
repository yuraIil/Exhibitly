package com.yuralil.infrastructure.util;

import com.yuralil.domain.dao.CategoryDao;
import com.yuralil.domain.dao.UsersDao;

import java.sql.Connection;

/**
 * Початкова ініціалізація даних у системі.
 * <p>
 * Після запуску:
 * <ul>
 *     <li>Встановлюється глобальне з'єднання з БД через {@link ConnectionHolder}</li>
 *     <li>Ініціалізуються стандартні категорії {@link CategoryDao#initDefaults()}</li>
 *     <li>Створюється обліковий запис адміністратора, якщо його ще не існує {@link UsersDao#initAdmin()}</li>
 * </ul>
 */
public class AppInitializer {

    /**
     * Ініціалізує початкові дані в системі:
     * дефолтні категорії та адміністратора.
     */
    public static void initAll() {
        ConnectionPool pool = new ConnectionPool();
        Connection connection = pool.getConnection();
        ConnectionHolder.set(connection);

        CategoryDao.getInstance().initDefaults();
        UsersDao.getInstance().initAdmin();
    }
}
