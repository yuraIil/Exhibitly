package com.yuralil.application.components;

import com.yuralil.domain.entities.Exhibit;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;

/**
 * Картка для відображення експоната у стилі галереї.
 */
public class ExhibitCard extends StackPane {

    public ExhibitCard(Exhibit exhibit) {
        setPrefWidth(220);
        setMaxWidth(220);

        // Фото
        ImageView imageView = new ImageView();
        String imgPath = "storage/images/" + exhibit.getMultimedia().getFilePath();
        File file = new File(imgPath);
        if (file.exists()) {
            imageView.setImage(new Image(file.toURI().toString(), 220, 130, true, true));
        }
        imageView.setFitWidth(220);
        imageView.setFitHeight(130);
        imageView.setStyle("-fx-background-radius: 12;");

        // Текст
        Label title = new Label(exhibit.getName());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        Label category = new Label(exhibit.getCategory().getName());
        category.setStyle("-fx-text-fill: #555; -fx-font-size: 11px;");

        Label date = new Label("📅 " + exhibit.getAcquisitionDate().toString());
        date.setStyle("-fx-text-fill: #777; -fx-font-size: 10px;");

        VBox info = new VBox(2, title, category, date);
        info.setPadding(new Insets(5));
        info.setAlignment(Pos.TOP_LEFT);

        VBox content = new VBox(imageView, info);
        content.setStyle("""
            -fx-background-color: #ffffff;
            -fx-background-radius: 12;
            -fx-border-color: #ddd;
            -fx-border-radius: 12;
        """);
        content.setEffect(new javafx.scene.effect.DropShadow(4, Color.rgb(0, 0, 0, 0.05)));

        // Анімація при наведенні
        ScaleTransition st = new ScaleTransition(Duration.millis(150), content);
        this.setOnMouseEntered(e -> {
            st.setToX(1.03);
            st.setToY(1.03);
            st.playFromStart();
        });
        this.setOnMouseExited(e -> {
            st.setToX(1);
            st.setToY(1);
            st.playFromStart();
        });

        // Клік — показати діалог з деталями
        this.setOnMouseClicked(e -> {
            Window window = getScene() != null ? getScene().getWindow() : null;
            if (window instanceof javafx.stage.Stage stage) {
                ExhibitDetailsDialog.show(exhibit, stage);
            }
        });

        getChildren().add(content);
        setPadding(new Insets(5));
    }
}
