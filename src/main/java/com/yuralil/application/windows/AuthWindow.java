package com.yuralil.application.windows;

import com.yuralil.application.form.LoginForm;
import com.yuralil.application.form.RegisterForm;
import javafx.geometry.Insets;
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

    public void show(Stage stage) {
        Label title = new Label("Exhibitly");
        title.setStyle("""
            -fx-font-size: 36px;
            -fx-font-weight: bold;
            -fx-text-fill: #1a3e2b;
        """);

        LoginForm loginForm = new LoginForm();
        RegisterForm registerForm = new RegisterForm();

        StackPane formPane = new StackPane(loginForm);
        formPane.setMinHeight(260);
        formPane.setMaxHeight(260);

        Button loginTab = new Button("Login");
        Button registerTab = new Button("Register");
        loginTab.setPrefWidth(120);
        registerTab.setPrefWidth(120);

        styleTabButton(loginTab, true);
        styleTabButton(registerTab, false);

        loginTab.setOnAction(e -> {
            formPane.getChildren().setAll(loginForm);
            styleTabButton(loginTab, true);
            styleTabButton(registerTab, false);
        });

        registerTab.setOnAction(e -> {
            formPane.getChildren().setAll(registerForm);
            styleTabButton(loginTab, false);
            styleTabButton(registerTab, true);
        });

        HBox tabButtons = new HBox(loginTab, registerTab);
        tabButtons.setSpacing(2);
        tabButtons.setAlignment(Pos.CENTER);

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 16;
            -fx-padding: 25;
        """);
        container.setMaxWidth(360);
        container.setPrefWidth(340);
        container.setMinHeight(400);
        container.setMaxHeight(460);

        VBox.setMargin(title, new Insets(10, 0, 10, 0));
        VBox.setMargin(tabButtons, new Insets(0, 0, 4, 0));
        VBox.setMargin(formPane, new Insets(0));

        container.getChildren().addAll(title, tabButtons, formPane);

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #fdfdfd, #f4f2ee);");
        root.setPrefSize(720, 540);

        Pane bg = createBackgroundCircles();
        root.getChildren().addAll(bg, container);

        Scene scene = new Scene(root);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            bg.setPrefWidth(newVal.doubleValue());
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            bg.setPrefHeight(newVal.doubleValue());
        });

        stage.setScene(scene);
    }

    private Pane createBackgroundCircles() {
        Pane pane = new Pane();

        List<Circle> circles = new ArrayList<>();

        // 💥 ЯСКРАВІ КОЛЬОРИ
        circles.add(createBlurredCircle(240, Color.web("#ec4899"), -500, -300, 0.4)); // рожевий
        circles.add(createBlurredCircle(220, Color.web("#3b82f6"), 300, -350, 0.4));  // синій
        circles.add(createBlurredCircle(200, Color.web("#10b981"), 500, -200, 0.4));  // м'ятний
        circles.add(createBlurredCircle(240, Color.web("#facc15"), -350, 500, 0.4));  // жовтий
        circles.add(createBlurredCircle(220, Color.web("#8b5cf6"), 0, 600, 0.4));     // фіолетовий
        circles.add(createBlurredCircle(200, Color.web("#0ea5e9"), 800, 200, 0.4));   // бірюзовий
        circles.add(createBlurredCircle(280, Color.web("#f43f5e"), -450, 200, 0.4));  // червоно-рожевий
        circles.add(createBlurredCircle(220, Color.web("#6366f1"), 900, 500, 0.4));   // індиго

        // ☀️ БІЛІ ТА СВІТЛІ для балансу
        circles.add(createBlurredCircle(200, Color.web("red"), 200, 150, 0.18));
        circles.add(createBlurredCircle(240, Color.web("#ffffff"), 600, 0, 0.2));
        circles.add(createBlurredCircle(220, Color.web("red"), -300, 400, 0.15));
        circles.add(createBlurredCircle(250, Color.web("#f9fafb"), 700, -100, 0.2));
        circles.add(createBlurredCircle(250, Color.web("#f3f4f6"), -600, 200, 0.18));

        // 🖼 Додаткові по краях, щоб заповнити екран
        circles.add(createBlurredCircle(220, Color.web("#22d3ee"), 1100, -150, 0.4));
        circles.add(createBlurredCircle(230, Color.web("#fde047"), 1200, 500, 0.4));
        circles.add(createBlurredCircle(210, Color.web("#fb7185"), -300, 850, 0.4));
        circles.add(createBlurredCircle(240, Color.web("#e879f9"), -800, 100, 0.4));

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
