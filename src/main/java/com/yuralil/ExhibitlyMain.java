package com.yuralil;

import com.yuralil.application.windows.IntroWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class ExhibitlyMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        IntroWindow introWindow = new IntroWindow();
        introWindow.showIntro(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
