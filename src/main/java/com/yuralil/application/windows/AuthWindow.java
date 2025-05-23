package com.yuralil.application.windows;

import com.yuralil.application.form.AuthForm;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Вікно авторизації, що містить вкладки "Login", "Register" та кнопку для гостя.
 * Має анімований фон з кольоровими колами.
 */
public class AuthWindow {

    /**
     * Створює та повертає кореневий елемент інтерфейсу авторизаційного вікна.
     *
     * @return StackPane з усіма візуальними компонентами
     */
    public StackPane getRoot() {
        Label title = new Label("Exhibitly");
        title.setStyle("""
            -fx-font-size: 36px;
            -fx-font-weight: bold;
            -fx-text-fill: #1a3e2b;
        """);

        AuthForm authForm = new AuthForm();

        authForm.getActionButton().setOnAction(e -> {
            if (authForm.getCurrentMode() == AuthForm.Mode.LOGIN) {
                Stage stage = (Stage) authForm.getScene().getWindow();
                boolean wasFullScreen = stage.isFullScreen();
                MainMenuWindow mainMenuWindow = new MainMenuWindow();
                mainMenuWindow.setUserRole("user");
                mainMenuWindow.show(stage, wasFullScreen);
            }
        });

        Button loginTab = new Button("Login");
        Button registerTab = new Button("Register");
        loginTab.setPrefWidth(120);
        registerTab.setPrefWidth(120);
        styleTabButton(loginTab, true);
        styleTabButton(registerTab, false);

        loginTab.setOnAction(e -> {
            authForm.switchToLogin();
            styleTabButton(loginTab, true);
            styleTabButton(registerTab, false);
        });

        registerTab.setOnAction(e -> {
            authForm.switchToRegister();
            styleTabButton(loginTab, false);
            styleTabButton(registerTab, true);
        });

        HBox tabButtons = new HBox(loginTab, registerTab);
        tabButtons.setSpacing(2);
        tabButtons.setAlignment(Pos.CENTER);

        Button guestButton = new Button("Continue as Guest");
        guestButton.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: #1a3e2b;
            -fx-underline: true;
            -fx-font-size: 12px;
            -fx-font-weight: bold;
        """);
        guestButton.setOnAction(e -> {
            Stage stage = (Stage) guestButton.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();
            MainMenuWindow mainMenuWindow = new MainMenuWindow();
            mainMenuWindow.setUserRole("visitor");
            mainMenuWindow.show(stage, wasFullScreen);
        });

        VBox container = new VBox(22, title, tabButtons, authForm, guestButton);
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 16;
            -fx-padding: 20 5 40 5;
        """);
        container.setMaxWidth(360);
        container.setPrefWidth(340);
        container.setMaxHeight(460);
        container.setPrefHeight(460);

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #fdfdfd, #f4f2ee);");
        root.setPrefSize(1920, 1080);

        Pane bg = createBackgroundCircles(1920, 1080);
        root.getChildren().addAll(bg, container);

        return root;
    }

    /**
     * Встановлює сцену цього вікна на переданий Stage.
     *
     * @param stage основне вікно, в якому буде відображено інтерфейс
     */
    public void show(Stage stage) {
        Scene scene = new Scene(getRoot(), stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
    }

    /**
     * Створює фонову панель із розмитими кольоровими колами для декору.
     *
     * @param width  ширина сцени
     * @param height висота сцени
     * @return панель з анімованими колами
     */
    private Pane createBackgroundCircles(double width, double height) {
        Pane pane = new Pane();
        pane.setPrefSize(width, height);

        List<Circle> circles = new ArrayList<>(List.of(
                createBlurredCircle(260, Color.web("#f87171"), width * 0.15, height * 0.2, 0.3),
                createBlurredCircle(240, Color.web("#60a5fa"), width * 0.4, height * 0.15, 0.3),
                createBlurredCircle(220, Color.web("#34d399"), width * 0.2, height * 0.7, 0.3),
                createBlurredCircle(240, Color.web("#facc15"), width * 0.5, height * 0.75, 0.3),
                createBlurredCircle(220, Color.web("#a78bfa"), width * 0.65, height * 0.35, 0.3),
                createBlurredCircle(200, Color.web("#fb7185"), width * 0.75, height * 0.2, 0.3),
                createBlurredCircle(250, Color.web("#4ade80"), width * 0.8, height * 0.7, 0.3),
                createBlurredCircle(210, Color.web("#fcd34d"), width * 0.45, height * 0.85, 0.3),
                createBlurredCircle(180, Color.web("#38bdf8"), width * 0.7, height * 0.85, 0.3)
        ));

        pane.getChildren().addAll(circles);
        return pane;
    }

    /**
     * Створює окреме коло з розмиттям для фонового оформлення.
     *
     * @param radius   радіус кола
     * @param color    колір кола
     * @param layoutX  позиція по X
     * @param layoutY  позиція по Y
     * @param opacity  прозорість
     * @return обʼєкт кола з ефектом розмиття
     */
    private Circle createBlurredCircle(double radius, Color color, double layoutX, double layoutY, double opacity) {
        Circle circle = new Circle(radius, color);
        circle.setOpacity(opacity);
        circle.setLayoutX(layoutX);
        circle.setLayoutY(layoutY);
        circle.setEffect(new BoxBlur(40, 40, 2));
        return circle;
    }

    /**
     * Застосовує стиль до кнопки вкладки ("Login" або "Register") в залежності від активності.
     *
     * @param button кнопка, до якої застосовується стиль
     * @param active чи активна кнопка
     */
    private void styleTabButton(Button button, boolean active) {
        button.setStyle(
                active
                        ? """
                    -fx-background-color: #e6efe9;
                    -fx-border-color: #2a5e3f;
                    -fx-border-width: 0 0 2 0;
                    -fx-font-weight: bold;
                """
                        : """
                    -fx-background-color: transparent;
                    -fx-border-color: transparent;
                    -fx-text-fill: #999;
                """
        );
        button.setFocusTraversable(false);
    }
}
