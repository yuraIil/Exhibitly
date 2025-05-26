package com.yuralil;

import com.yuralil.application.windows.IntroWindow;
import com.yuralil.infrastructure.util.AppInitializer;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Головний клас запуску JavaFX-застосунку Exhibitly.
 */
public class ExhibitlyMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        AppInitializer.initAll(); // 🔥 новий централізований виклик

        IntroWindow introWindow = new IntroWindow();
        introWindow.showIntro(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
