package com.yuralil.components;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;

public class ConfirmDialog {

    public static void show(String message, Runnable onConfirm, Window owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);
        dialog.setAlwaysOnTop(true);

        Label label = new Label(message);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        Button okButton = new Button("✔ OK");
        Button cancelButton = new Button("✖ Cancel");

        okButton.setStyle("-fx-background-color: #2a5e3f; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelButton.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #333;");

        okButton.setOnAction(e -> {
            dialog.close();
            if (onConfirm != null) onConfirm.run();
        });

        cancelButton.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(12, okButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(18, label, buttons);
        layout.setPadding(new Insets(24));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("""
            -fx-background-color: white;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 4);
        """);

        Scene scene = new Scene(layout);
        dialog.setScene(scene);

        centerOnOwner(dialog, owner);
        dialog.show();

        // Fade in animation
        FadeTransition fade = new FadeTransition(Duration.millis(200), layout);
        layout.setOpacity(0);
        fade.setToValue(1);
        fade.play();
    }

    public static void showWarning(String message, Window owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);
        dialog.setAlwaysOnTop(true);

        Label label = new Label(message);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #2a5e3f; -fx-text-fill: white;");
        okButton.setOnAction(e -> dialog.close());

        VBox layout = new VBox(18, label, okButton);
        layout.setPadding(new Insets(24));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("""
            -fx-background-color: white;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 4);
        """);

        Scene scene = new Scene(layout);
        dialog.setScene(scene);

        centerOnOwner(dialog, owner);
        dialog.show();

        FadeTransition fade = new FadeTransition(Duration.millis(200), layout);
        layout.setOpacity(0);
        fade.setToValue(1);
        fade.play();
    }

    private static void centerOnOwner(Stage dialog, Window owner) {
        dialog.setOnShown(e -> {
            dialog.setX(owner.getX() + (owner.getWidth() - dialog.getWidth()) / 2);
            dialog.setY(owner.getY() + (owner.getHeight() - dialog.getHeight()) / 2);
        });

        owner.xProperty().addListener((obs, oldX, newX) -> {
            dialog.setX(newX.doubleValue() + (owner.getWidth() - dialog.getWidth()) / 2);
        });
        owner.yProperty().addListener((obs, oldY, newY) -> {
            dialog.setY(newY.doubleValue() + (owner.getHeight() - dialog.getHeight()) / 2);
        });
        owner.widthProperty().addListener((obs, oldW, newW) -> {
            dialog.setX(owner.getX() + (newW.doubleValue() - dialog.getWidth()) / 2);
        });
        owner.heightProperty().addListener((obs, oldH, newH) -> {
            dialog.setY(owner.getY() + (newH.doubleValue() - dialog.getHeight()) / 2);
        });
    }
}
