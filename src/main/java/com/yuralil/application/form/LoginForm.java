package com.yuralil.application.form;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LoginForm extends VBox {

    public LoginForm() {
        setSpacing(8);
        setPadding(new Insets(0, 10, 10, 10)); // ⬅️ відступ зверху між табами й полями
        setAlignment(Pos.CENTER);

        // Email
        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-font-size: 13px;");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(250);
        emailField.setStyle("-fx-background-radius: 8;");

        // Password
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 13px;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);
        passwordField.setStyle("-fx-background-radius: 8;");

        // Кнопка логіну
        Button loginButton = new Button("Log in");
        loginButton.setPrefWidth(250);
        loginButton.setStyle("""
            -fx-background-color: #2a5e3f;
            -fx-text-fill: white;
            -fx-background-radius: 8;
            -fx-font-weight: bold;
        """);

        // Лінк
        Label forgotPassword = new Label("Forgot password?");
        forgotPassword.setStyle("-fx-text-fill: #888; -fx-font-size: 11px;");

        // Відступ між полем і кнопкою
        Region spacer = new Region();
        spacer.setMinHeight(5);

        // Контейнер для полів
        VBox fields = new VBox(5,
                emailLabel, emailField,
                passwordLabel, passwordField,
                spacer,
                loginButton,
                forgotPassword
        );
        fields.setAlignment(Pos.CENTER_LEFT); // 👈 вирівнюємо вліво
        fields.setMaxWidth(250);

        getChildren().add(fields);
    }
}
