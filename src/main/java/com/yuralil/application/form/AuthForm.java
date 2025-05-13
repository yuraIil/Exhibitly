package com.yuralil.application.form;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * JavaFX форма для авторизації та реєстрації користувача.
 * Містить поля для email, пароля, вибору ролі та кнопки дії.
 */
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

    /**
     * Режими форми: вхід або реєстрація.
     */
    public enum Mode { LOGIN, REGISTER }

    private Mode currentMode = Mode.LOGIN;

    /**
     * Конструктор, який ініціалізує форму авторизації/реєстрації.
     */
    public AuthForm() {
        setSpacing(4);
        setPadding(new Insets(0));
        setAlignment(Pos.CENTER);

        emailLabel.setStyle("-fx-font-size: 13px;");
        emailField.setPromptText("Email");
        emailField.setMaxWidth(250);
        emailField.setStyle(getFieldStyle());

        passwordLabel.setStyle("-fx-font-size: 13px;");
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);
        passwordField.setStyle(getFieldStyle());

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

    /**
     * Переключає форму у режим входу.
     */
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

    /**
     * Переключає форму у режим реєстрації.
     */
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

    /**
     * Повертає поточний режим форми.
     *
     * @return режим (LOGIN або REGISTER)
     */
    public Mode getCurrentMode() {
        return currentMode;
    }

    /**
     * Повертає кнопку дії (Login/Register).
     *
     * @return кнопка для виконання дії
     */
    public Button getActionButton() {
        return actionButton;
    }

    /**
     * Повертає текстове поле для email.
     *
     * @return поле для введення email
     */
    public TextField getEmailField() {
        return emailField;
    }

    /**
     * Повертає поле для введення пароля.
     *
     * @return поле пароля
     */
    public PasswordField getPasswordField() {
        return passwordField;
    }

    /**
     * Повертає вибір ролі користувача.
     *
     * @return ChoiceBox з доступними ролями
     */
    public ChoiceBox<String> getRoleChoice() {
        return roleChoice;
    }

    /**
     * Стиль, який застосовується до текстових полів.
     *
     * @return CSS-рядок стилю
     */
    private String getFieldStyle() {
        return """
            -fx-background-radius: 8;
            -fx-border-color: #ccc;
            -fx-border-radius: 8;
            -fx-padding: 6 10;
        """;
    }
}
