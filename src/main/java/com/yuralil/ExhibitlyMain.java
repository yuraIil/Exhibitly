package com.yuralil;

import com.yuralil.application.windows.Intro;
import javafx.application.Application;
import javafx.stage.Stage;

public class ExhibitlyMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        Intro intro = new Intro();
        intro.showIntro(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
