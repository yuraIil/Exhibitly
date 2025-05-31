package com.yuralil.application.components;

import com.yuralil.domain.entities.Exhibit;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Path;

public class ExhibitDetailsDialog {

    public static void show(Exhibit exhibit, Stage parentStage) {
        Stage dialog = new Stage();
        dialog.initOwner(parentStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        // Close button
        Button closeButton = new Button("✖");
        closeButton.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 14px;
            -fx-text-fill: #333;
        """);
        closeButton.setOnAction(e -> dialog.close());

        HBox closeBar = new HBox(closeButton);
        closeBar.setAlignment(Pos.TOP_RIGHT);
        closeBar.setPadding(new Insets(5, 5, 0, 0));

        // Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        if (exhibit.getMultimedia() != null && exhibit.getMultimedia().getFilePath() != null) {
            File file = Path.of("storage/images", exhibit.getMultimedia().getFilePath()).toFile();
            if (file.exists()) {
                imageView.setImage(new Image(file.toURI().toString()));
            }
        }

        // Details
        Label name = new Label(exhibit.getName());
        name.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label category = new Label("Category: " + exhibit.getCategory().getName());
        Label date = new Label("Date: " + exhibit.getAcquisitionDate());
        Label description = new Label(exhibit.getDescription());
        description.setWrapText(true);

        VBox details = new VBox(6, name, category, date, description);
        details.setAlignment(Pos.TOP_LEFT);
        details.setPadding(new Insets(10));

        // Content layout
        HBox contentBox = new HBox(20, imageView, details);
        contentBox.setPadding(new Insets(10));

        // White container with border and shadow
        VBox container = new VBox(10, closeBar, contentBox);
        container.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-border-color: black;
            -fx-border-width: 2;
        """);
        container.setPadding(new Insets(10));
        container.setMaxWidth(500);

        StackPane root = new StackPane(container);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        dialog.setScene(scene);

        ScaleTransition st = new ScaleTransition(Duration.millis(250), container);
        container.setScaleX(0.85);
        container.setScaleY(0.85);
        st.setToX(1);
        st.setToY(1);
        st.play();

        dialog.showAndWait();
    }
}
