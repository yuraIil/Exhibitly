package com.yuralil.application.form;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AuthForm extends VBox {

    private final Label loginLabel = new Label("Login");
    private final TextField loginField = new TextField();
    private final Label loginErrorLabel = new Label();

    private final Label passwordLabel = new Label("Password");
    private final PasswordField passwordField = new PasswordField();
    private final Label passwordErrorLabel = new Label();

    private final Label successLabel = new Label(); // ✅ успішне повідомлення

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

        loginLabel.setStyle("-fx-font-size: 13px;");
        loginField.setPromptText("Login");
        loginField.setMaxWidth(250);
        loginField.setStyle(getFieldStyle());

        loginErrorLabel.setStyle("-fx-text-fill: #cc0000; -fx-font-size: 11px;");

        passwordLabel.setStyle("-fx-font-size: 13px;");
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);
        passwordField.setStyle(getFieldStyle());

        passwordErrorLabel.setStyle("-fx-text-fill: #cc0000; -fx-font-size: 11px;");

        successLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-size: 11px;");

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
        clearFields();
        fields.getChildren().setAll(
                loginLabel, successLabel, loginField, loginErrorLabel,
                passwordLabel, passwordField, passwordErrorLabel,
                spacer,
                actionButton,
                forgotPassword
        );
        actionButton.setText("Log in");
    }

    public void switchToRegister() {
        currentMode = Mode.REGISTER;
        clearFields();
        fields.getChildren().setAll(
                loginLabel, loginField, loginErrorLabel,
                passwordLabel, passwordField, passwordErrorLabel,
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
        return loginField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public void showLoginError(String message) {
        loginErrorLabel.setText(message);
    }

    public void showPasswordError(String message) {
        passwordErrorLabel.setText(message);
    }

    public void showSuccessMessage(String message) {
        successLabel.setText(message);
    }

    public void clearErrors() {
        loginErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }

    public void clearSuccess() {
        successLabel.setText("");
    }

    private void clearFields() {
        loginField.setText("");
        passwordField.setText("");
        clearErrors();
        clearSuccess();
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
