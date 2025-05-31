package com.yuralil;

import com.yuralil.application.windows.IntroWindow;
import com.yuralil.application.windows.MainMenuWindow;
import com.yuralil.domain.dao.UsersDao;
import com.yuralil.domain.entities.Users;
import com.yuralil.infrastructure.util.AppInitializer;
import com.yuralil.infrastructure.util.Session;
import com.yuralil.infrastructure.util.SessionStorage;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Головний клас запуску JavaFX-застосунку Exhibitly.
 */
public class ExhibitlyMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        AppInitializer.initAll();

        String savedUsername = SessionStorage.loadUsername();
        System.out.println("⏳ Saved session: " + savedUsername);

        if (savedUsername != null && !savedUsername.isBlank()) {
            UsersDao.getInstance().findByUsername(savedUsername).ifPresentOrElse(user -> {
                System.out.println("✅ Found user from session: " + user.getUsername());
                Session.setCurrentUser(user);
                MainMenuWindow mainMenu = new MainMenuWindow();
                mainMenu.setUserRole(user.getRole().toLowerCase());

                // ✅ Встановлюємо fullscreen ПЕРЕД показом
                primaryStage.setFullScreen(true);
                mainMenu.show(primaryStage, true);
            }, () -> {
                System.out.println("⚠️ User from session not found in DB. Clearing session.");
                SessionStorage.clear();
                new IntroWindow().showIntro(primaryStage);
            });
        } else {
            new IntroWindow().showIntro(primaryStage);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
