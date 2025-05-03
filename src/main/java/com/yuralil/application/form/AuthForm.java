package com.yuralil.application.form;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AuthForm extends VBox {

    private final Label emailLabel = new Label("Email");
    private final TextField emailField = new TextField();

    private final Label passwordLabel = new Label("Password");
    private final PasswordField passwordField = new PasswordField();

    private final Label roleLabel = new Label("Role");
    private final ChoiceBox<String> roleChoice = new ChoiceBox<>();

    private final Button actionButton = new Button();
    private final Label forgotPassword = new Label("Forgot password?");
    private final Region spacer = new Region();

    private final VBox fields = new VBox(6);

    public enum Mode { LOGIN, REGISTER }
    private Mode currentMode = Mode.LOGIN;

    public AuthForm() {
        setSpacing(4);
        setPadding(new Insets(0));
        setAlignment(Pos.CENTER);

        // Email
        emailLabel.setStyle("-fx-font-size: 13px;");
        emailField.setPromptText("Email");
        emailField.setMaxWidth(250);
        emailField.setStyle(getFieldStyle());

        // Password
        passwordLabel.setStyle("-fx-font-size: 13px;");
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);
        passwordField.setStyle(getFieldStyle());

        // Role
        roleLabel.setStyle("-fx-font-size: 13px;");
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
        actionButton.setPrefWidth(250);
        actionButton.setStyle("""
            -fx-background-color: #2a5e3f;
            -fx-text-fill: white;
            -fx-background-radius: 8;
            -fx-font-weight: bold;
        """);

        forgotPassword.setStyle("-fx-text-fill: #888; -fx-font-size: 11px;");
        spacer.setMinHeight(5);

        fields.setAlignment(Pos.CENTER_LEFT);
        fields.setMaxWidth(250);

        getChildren().add(fields);
        switchToLogin();
    }

    public void switchToLogin() {
        currentMode = Mode.LOGIN;
        fields.getChildren().setAll(
                emailLabel, emailField,
                passwordLabel, passwordField,
                spacer,
                actionButton,
                forgotPassword
        );
        actionButton.setText("Log in");
    }

    public void switchToRegister() {
        currentMode = Mode.REGISTER;
        fields.getChildren().setAll(
                emailLabel, emailField,
                passwordLabel, passwordField,
                roleLabel, roleChoice,
                actionButton
        );
        actionButton.setText("Register");
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public Button getActionButton() {
        return actionButton;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public ChoiceBox<String> getRoleChoice() {
        return roleChoice;
    }

    private String getFieldStyle() {
        return """
            -fx-background-radius: 8;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
            -fx-padding: 6 10;
        """;
    }
}
