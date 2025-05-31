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

        VBox container = new VBox(10);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.CENTER);
        container.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 16;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 4);
        """);

        Button closeButton = new Button("\u2716");
        closeButton.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 14px;
            -fx-text-fill: #666;
        """);
        closeButton.setOnAction(e -> dialog.close());

        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        if (exhibit.getMultimedia() != null && exhibit.getMultimedia().getFilePath() != null) {
            File file = Path.of("storage/images", exhibit.getMultimedia().getFilePath()).toFile();
            if (file.exists()) {
                imageView.setImage(new Image(file.toURI().toString()));
            }
        }

        Label name = new Label(exhibit.getName());
        name.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label category = new Label("Category: " + exhibit.getCategory().getName());
        Label date = new Label("Date: " + exhibit.getAcquisitionDate());
        Label description = new Label(exhibit.getDescription());
        description.setWrapText(true);

        VBox details = new VBox(6, name, category, date, description);
        details.setAlignment(Pos.CENTER_LEFT);

        HBox topBar = new HBox(new Region(), closeButton);
        HBox.setHgrow(topBar.getChildren().get(0), Priority.ALWAYS);

        container.getChildren().addAll(topBar, imageView, details);

        StackPane root = new StackPane(container);
        root.setStyle("-fx-background-color: rgba(0,0,0,0.25);");

        Scene scene = new Scene(root, 450, 450);
        scene.setFill(Color.TRANSPARENT);

        dialog.setScene(scene);

        ScaleTransition st = new ScaleTransition(Duration.millis(200), container);
        container.setScaleX(0.8);
        container.setScaleY(0.8);
        st.setToX(1);
        st.setToY(1);
        st.play();

        dialog.showAndWait();
    }
}
