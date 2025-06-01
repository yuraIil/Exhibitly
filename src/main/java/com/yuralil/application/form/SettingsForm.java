package com.yuralil.application.form;

import com.yuralil.domain.dao.UsersDao;
import com.yuralil.infrastructure.util.Session;
import com.yuralil.domain.security.HashUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Форма налаштувань користувача: зміна пароля, повідомлення для гостей.
 */
public class SettingsForm extends VBox {

    public SettingsForm() {
        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.CENTER);

        if (Session.getCurrentUser() == null) {
            Label guestMsg = new Label("🔒 Увійдіть, щоб змінювати налаштування.");
            guestMsg.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
            getChildren().add(guestMsg);
        } else {
            getChildren().add(createPasswordSection());
        }
    }

    private VBox createPasswordSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        Label title = new Label("🔒 Зміна пароля");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Старий пароль");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Новий пароль");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Підтвердіть новий пароль");

        Button changeBtn = new Button("Змінити пароль");
        Label status = new Label();

        changeBtn.setOnAction(e -> {
            String oldPass = currentPasswordField.getText();
            String newPass = newPasswordField.getText();
            String confirm = confirmPasswordField.getText();

            var user = Session.getCurrentUser();
            String hashedOld = HashUtil.hash(oldPass);

            if (!user.getPassword().equals(hashedOld)) {
                status.setText("❌ Неправильний старий пароль");
            } else if (!newPass.equals(confirm)) {
                status.setText("❌ Паролі не збігаються");
            } else if (newPass.length() < 6) {
                status.setText("❌ Пароль має бути мінімум 6 символів");
            } else {
                try {
                    String hashedNew = HashUtil.hash(newPass);
                    user.setPassword(hashedNew);
                    UsersDao.getInstance().updatePassword(user.getId(), hashedNew);
                    status.setText("✅ Пароль змінено");
                } catch (Exception ex) {
                    status.setText("❌ Помилка при зміні пароля");
                    ex.printStackTrace();
                }
            }
        });

        box.getChildren().addAll(
                title,
                currentPasswordField,
                newPasswordField,
                confirmPasswordField,
                changeBtn,
                status
        );
        return box;
    }
}
