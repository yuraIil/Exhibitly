package com.yuralil.application.form;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RegisterForm extends VBox {

    public RegisterForm() {
        setSpacing(8);
        setPadding(new Insets(0, 10, 10, 10));
        setAlignment(Pos.CENTER);

        // Email
        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-font-size: 13px;");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(250);
        emailField.setStyle("""
            -fx-background-radius: 8;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
            -fx-padding: 6 10;
        """);

        // Password
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 13px;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);
        passwordField.setStyle("""
            -fx-background-radius: 8;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
            -fx-padding: 6 10;
        """);

        // Role
        Label roleLabel = new Label("Role");
        roleLabel.setStyle("-fx-font-size: 13px;");
        ChoiceBox<String> roleChoice = new ChoiceBox<>();
        roleChoice.getItems().addAll("visitor", "scientific_staff", "administrator");
        roleChoice.setValue("visitor");
        roleChoice.setMaxWidth(250);
        roleChoice.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-padding: 6 10;
            -fx-font-size: 13px;
            -fx-text-fill: #333;
        """);

        // Button
        Button registerButton = new Button("Register");
        registerButton.setPrefWidth(250);
        registerButton.setStyle("""
            -fx-background-color: #2a5e3f;
            -fx-text-fill: white;
            -fx-background-radius: 8;
            -fx-font-weight: bold;
        """);

        // Контейнер полів
        VBox fields = new VBox(5,
                emailLabel, emailField,
                passwordLabel, passwordField,
                roleLabel, roleChoice,
                registerButton
        );
        fields.setAlignment(Pos.CENTER_LEFT);
        fields.setMaxWidth(250);

        getChildren().add(fields);
    }
}
