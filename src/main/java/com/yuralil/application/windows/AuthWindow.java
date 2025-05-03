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
        root.setPrefSize(720, 540);

        Pane bg = createBackgroundCircles();
        root.getChildren().addAll(bg, container);

        root.widthProperty().addListener((obs, oldVal, newVal) -> bg.setPrefWidth(newVal.doubleValue()));
        root.heightProperty().addListener((obs, oldVal, newVal) -> bg.setPrefHeight(newVal.doubleValue()));

        return root;
    }

    public void show(Stage stage) {
        Scene scene = new Scene(getRoot(), stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
    }

    private Pane createBackgroundCircles() {
        Pane pane = new Pane();
        List<Circle> circles = new ArrayList<>();
        circles.add(createBlurredCircle(240, Color.web("#ec4899"), -500, -300, 0.4));
        circles.add(createBlurredCircle(220, Color.web("#3b82f6"), 300, -350, 0.4));
        circles.add(createBlurredCircle(150, Color.web("red"), 200, 200, 0.4));
        circles.add(createBlurredCircle(200, Color.web("#10b981"), 500, -200, 0.4));
        circles.add(createBlurredCircle(240, Color.web("#facc15"), -350, 500, 0.4));
        circles.add(createBlurredCircle(220, Color.web("#8b5cf6"), 0, 600, 0.4));
        circles.add(createBlurredCircle(200, Color.web("#0ea5e9"), 800, 200, 0.4));
        circles.add(createBlurredCircle(280, Color.web("#f43f5e"), -450, 200, 0.4));
        circles.add(createBlurredCircle(220, Color.web("#6366f1"), 900, 500, 0.4));
        pane.getChildren().addAll(circles);
        return pane;
    }

    private Circle createBlurredCircle(double radius, Color color, double x, double y, double opacity) {
        Circle circle = new Circle(radius, color);
        circle.setOpacity(opacity);
        circle.setEffect(new BoxBlur(40, 40, 2));
        circle.setTranslateX(x);
        circle.setTranslateY(y);
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
