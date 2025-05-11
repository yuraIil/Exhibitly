package com.yuralil;

import com.yuralil.application.windows.IntroWindow;
import com.yuralil.domain.dao.CategoryDao;
import com.yuralil.infrastructure.util.ConnectionHolder;
import com.yuralil.infrastructure.util.ConnectionPool;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;

public class ExhibitlyMain extends Application {

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

    public static void main(String[] args) {
        launch(args);
    }
}
