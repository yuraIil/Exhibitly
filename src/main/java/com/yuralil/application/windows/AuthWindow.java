package com.yuralil.application.windows;

import com.yuralil.application.form.AuthForm;
import com.yuralil.domain.dao.UsersDao;
import com.yuralil.domain.entities.Users;
import com.yuralil.domain.security.HashUtil;
import com.yuralil.domain.service.ValidationService;
import com.yuralil.infrastructure.util.ConnectionHolder;
import com.yuralil.infrastructure.util.ConnectionPool;
import com.yuralil.infrastructure.util.Session;
import com.yuralil.infrastructure.util.SessionStorage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Вікно авторизації та реєстрації користувача.
 * <p>
 * Містить форму {@link AuthForm} з обробкою:
 * <ul>
 *     <li>входу (логін)</li>
 *     <li>реєстрації</li>
 *     <li>гостьового режиму</li>
 * </ul>
 * <p>
 * Декоративний фон побудовано з розмитих кольорових кіл.
 */
public class AuthWindow {

    /**
     * Створює кореневий елемент інтерфейсу для вікна авторизації.
     *
     * @return StackPane із вмістом форми
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
                try {
                    Connection connection = new ConnectionPool().getConnection();
                    ConnectionHolder.set(connection);

                    String login = authForm.getEmailField().getText();
                    String password = authForm.getPasswordField().getText();

                    List<String> errors = ValidationService.getInstance().validateLogin(login, password);
                    authForm.clearErrors();
                    authForm.clearSuccess();

                    if (errors.isEmpty()) {
                        Users user = UsersDao.getInstance().findByUsername(login).orElse(null);
                        if (user != null && user.getPassword().equals(HashUtil.hash(password))) {
                            Session.setCurrentUser(user);
                            SessionStorage.saveUsername(user.getUsername());
                            Stage stage = (Stage) authForm.getScene().getWindow();
                            boolean wasFullScreen = stage.isFullScreen();
                            MainMenuWindow mainMenuWindow = new MainMenuWindow();
                            mainMenuWindow.setUserRole(user.getRole().toLowerCase());
                            mainMenuWindow.show(stage, wasFullScreen);
                        } else {
                            authForm.showLoginError("Невірний логін або пароль");
                        }
                    } else {
                        for (String error : errors) {
                            if (error.toLowerCase().contains("логін")) {
                                authForm.showLoginError(error);
                            } else if (error.toLowerCase().contains("пароль")) {
                                authForm.showPasswordError(error);
                            }
                        }
                    }
                } finally {
                    ConnectionHolder.clear();
                }
            }

            if (authForm.getCurrentMode() == AuthForm.Mode.REGISTER) {
                try {
                    Connection conn = new ConnectionPool().getConnection();
                    ConnectionHolder.set(conn);

                    String login = authForm.getEmailField().getText();
                    String password = authForm.getPasswordField().getText();

                    List<String> errors = ValidationService.getInstance().validateRegistration(login, password);
                    authForm.clearErrors();
                    authForm.clearSuccess();

                    for (String error : errors) {
                        if (error.toLowerCase().contains("логін")) {
                            authForm.showLoginError(error);
                        } else if (error.toLowerCase().contains("пароль")) {
                            authForm.showPasswordError(error);
                        }
                    }

                    if (!errors.isEmpty()) return;

                    String hashedPassword = HashUtil.hash(password);
                    Users newUser = new Users(login, hashedPassword, "VISITOR");
                    UsersDao.getInstance().insert(newUser);

                    authForm.switchToLogin();
                    authForm.getEmailField().setText(login);
                    authForm.getPasswordField().setText("");
                    authForm.showSuccessMessage("Реєстрація успішна. Увійдіть.");
                } finally {
                    ConnectionHolder.clear();
                }
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

        HBox tabButtons = new HBox(loginTab, registerTab);
        tabButtons.setSpacing(2);
        tabButtons.setAlignment(Pos.CENTER);

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
     * Відображає вікно авторизації на сцені.
     *
     * @param stage цільове вікно
     */
    public void show(Stage stage) {
        Scene scene = new Scene(getRoot(), stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    /**
     * Створює задній фон з кольорових кругів.
     *
     * @param width  ширина екрану
     * @param height висота екрану
     * @return Pane з колами
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
     * Створює розмите коло для заднього фону.
     *
     * @param radius   радіус кола
     * @param color    колір
     * @param layoutX  позиція X
     * @param layoutY  позиція Y
     * @param opacity  прозорість
     * @return обʼєкт кола
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
     * Стилізує вкладку логіну/реєстрації як активну або неактивну.
     *
     * @param button кнопка вкладки
     * @param active чи активна
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
