package com.yuralil.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class ConfirmDialog {

    public static void show(String message, Runnable onConfirm, Window owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Confirm");

        Label label = new Label(message);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 14px;");

        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        okButton.setStyle("-fx-background-color: #2a5e3f; -fx-text-fill: white;");
        cancelButton.setStyle("-fx-background-color: #ddd;");

        okButton.setOnAction(e -> {
            dialog.close();
            if (onConfirm != null) {
                onConfirm.run();
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        HBox buttons = new HBox(10, okButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, label, buttons);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public static void showWarning(String message, Window owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Warning");

        Label label = new Label(message);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 14px;");

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> dialog.close());
        okButton.setStyle("-fx-background-color: #2a5e3f; -fx-text-fill: white;");

        VBox layout = new VBox(15, label, okButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
