package com.yuralil.application.form;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Заглушка для форми налаштувань.
 */
public class SettingsForm extends VBox {

    public SettingsForm() {
        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.CENTER);

        Label label = new Label("⚙ Налаштування будуть доступні у наступній версії.");
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: #555;");
        getChildren().add(label);
    }
}
