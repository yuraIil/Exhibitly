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

public class AuthWindow {

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

        VBox container = new VBox(22, title, tabButtons, authForm);
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 16;
            -fx-padding: 20 5 40 5;
        """);
        container.setMaxWidth(360);
        container.setPrefWidth(340);
        container.setMaxHeight(420);
        container.setPrefHeight(420);

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #fdfdfd, #f4f2ee);");
        root.setPrefSize(1920, 1080);

        Pane bg = createBackgroundCircles(1920, 1080);
        root.getChildren().addAll(bg, container);

        return root;
    }

    public void show(Stage stage) {
        Scene scene = new Scene(getRoot(), stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
    }

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

    private Circle createBlurredCircle(double radius, Color color, double layoutX, double layoutY, double opacity) {
        Circle circle = new Circle(radius, color);
        circle.setOpacity(opacity);
        circle.setLayoutX(layoutX);
        circle.setLayoutY(layoutY);
        circle.setEffect(new BoxBlur(40, 40, 2));
        return circle;
    }

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