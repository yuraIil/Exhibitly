package com.yuralil.application.windows;

import com.yuralil.application.form.MainMenu;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Intro {

    public void showIntro(Stage stage) {
        Label title = new Label("Exhibitly");
        title.setStyle("-fx-font-size: 60px; -fx-font-weight: bold; -fx-text-fill: green;");

        StackPane root = new StackPane(title);
        root.setStyle("-fx-background-color: #d3ccc5;");
        Scene scene = new Scene(root, 720, 540);

        // Анімація появи (збільшення + прозорість)
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), title);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(2), title);
        scaleIn.setFromX(0.5);
        scaleIn.setFromY(0.5);
        scaleIn.setToX(1);
        scaleIn.setToY(1);

        // Поєднуємо анімації
        ParallelTransition parallelIn = new ParallelTransition(fadeIn, scaleIn);

        // Анімація зникнення
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), title);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        // Комбінуємо: з'явлення → пауза → зникнення → головне меню
        parallelIn.setOnFinished(e -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(ev -> fadeOut.play());
            pause.play();
        });

        fadeOut.setOnFinished(e -> {
            new AuthWindow().show(stage);
        });


        stage.setScene(scene);
        stage.show();
        parallelIn.play();
    }
}