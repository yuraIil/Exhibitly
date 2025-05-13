package com.yuralil;

import com.yuralil.application.windows.IntroWindow;
import com.yuralil.domain.dao.CategoryDao;
import com.yuralil.infrastructure.util.ConnectionHolder;
import com.yuralil.infrastructure.util.ConnectionPool;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;

/**
 * Головний клас запуску JavaFX-застосунку Exhibitly.
 * Відповідає за ініціалізацію підключення до бази даних, дефолтних категорій та запуск стартового екрану.
 */
public class ExhibitlyMain extends Application {

    /**
     * Метод, який викликається при запуску JavaFX-застосунку.
     * Ініціалізує підключення до бази даних, дефолтні категорії та відкриває стартове вікно {@link IntroWindow}.
     *
     * @param primaryStage головне вікно JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Connection conn = new ConnectionPool().getConnection();
            ConnectionHolder.set(conn);

            // Ініціалізуємо 10 категорій (якщо їх ще нема)
            CategoryDao.getInstance().initDefaults();
        } finally {
            ConnectionHolder.clear();
        }

        IntroWindow introWindow = new IntroWindow();
        introWindow.showIntro(primaryStage);
    }

    /**
     * Точка входу в застосунок.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
