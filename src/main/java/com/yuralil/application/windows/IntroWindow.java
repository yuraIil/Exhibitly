package com.yuralil.application.windows;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

/**
 * Початкове вікно (Splash screen) з анімацією та ефектами для логотипу Exhibitly.
 * Після анімації переходить до AuthWindow.
 */
public class IntroWindow {

    /**
     * Показує анімоване вступне вікно з розмитими колами та логотипом.
     * Після завершення анімації автоматично відкривається AuthWindow.
     *
     * @param stage головне вікно програми
     */
    public void showIntro(Stage stage) {
        Font lalezar = Font.loadFont(
                getClass().getResource("/Lalezar-Regular.ttf").toExternalForm(), 60
        );

        Label title = new Label("Exhibitly");
        title.setFont(lalezar);
        title.setTextFill(Color.web("#1a3e2b"));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth();
        double height = screenBounds.getHeight();

        Pane circles = new Pane();
        circles.setPrefSize(width, height);
        circles.setMouseTransparent(true);

        List<Circle> circleList = List.of(
                createAnimatedCircle(240, Color.web("#f87171"), width * 0.15, height * 0.2),
                createAnimatedCircle(240, Color.web("#60a5fa"), width * 0.4, height * 0.15),
                createAnimatedCircle(220, Color.web("#34d399"), width * 0.2, height * 0.7),
                createAnimatedCircle(220, Color.web("#facc15"), width * 0.5, height * 0.75),
                createAnimatedCircle(220, Color.web("#a78bfa"), width * 0.65, height * 0.35),
                createAnimatedCircle(200, Color.web("#fb7185"), width * 0.75, height * 0.2),
                createAnimatedCircle(240, Color.web("#4ade80"), width * 0.8, height * 0.7),
                createAnimatedCircle(210, Color.web("#fcd34d"), width * 0.45, height * 0.85),
                createAnimatedCircle(180, Color.web("#38bdf8"), width * 0.7, height * 0.85)
        );

        circles.getChildren().addAll(circleList);

        StackPane root = new StackPane(circles, title);
        root.setStyle("-fx-background-color: #f8f9fa;");
        Scene scene = new Scene(root, width, height);

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.F11) {
                if (stage.isFullScreen()) {
                    stage.setFullScreen(false);
                    stage.setWidth(800);
                    stage.setHeight(600);
                    stage.centerOnScreen();
                } else {
                    stage.setFullScreen(true);
                }
            }
        });

        for (int i = 0; i < circleList.size(); i++) {
            animateCircle(circleList.get(i), i * 50);
        }

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.6), title);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(1.6), title);
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1);
        scale.setToY(1);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), root);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        SequentialTransition sequence = new SequentialTransition(
                new ParallelTransition(fadeIn, scale),
                new PauseTransition(Duration.seconds(1)),
                fadeOut
        );

        fadeOut.setOnFinished(e -> {
            AuthWindow authWindow = new AuthWindow();
            StackPane authRoot = authWindow.getRoot();

            Platform.runLater(() -> {
                Scene currentScene = stage.getScene();
                currentScene.setRoot(authRoot);
                stage.setTitle("Exhibitly");
            });
        });

        stage.setTitle("Exhibitly");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);

        stage.show();

        sequence.play();
    }

    /**
     * Створює коло з ефектом розмиття та початковою прозорістю для анімації.
     *
     * @param radius   радіус кола
     * @param color    колір кола
     * @param layoutX  позиція по X
     * @param layoutY  позиція по Y
     * @return створене коло
     */
    private Circle createAnimatedCircle(double radius, Color color, double layoutX, double layoutY) {
        Circle circle = new Circle(radius, color);
        circle.setOpacity(0);
        circle.setLayoutX(layoutX);
        circle.setLayoutY(layoutY);
        circle.setEffect(new BoxBlur(40, 40, 2));
        circle.setScaleX(0.1);
        circle.setScaleY(0.1);
        return circle;
    }

    /**
     * Анімує коло (ефект появи і масштабування).
     *
     * @param circle       обʼєкт кола
     * @param delayMillis  затримка перед початком анімації
     */
    private void animateCircle(Circle circle, int delayMillis) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1.6), circle);
        fade.setFromValue(0);
        fade.setToValue(0.3);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(1.6), circle);
        scale.setFromX(0.1);
        scale.setFromY(0.1);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition pt = new ParallelTransition(fade, scale);
        pt.setDelay(Duration.millis(delayMillis));
        pt.play();
    }
}
