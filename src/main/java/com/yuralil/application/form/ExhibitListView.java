package com.yuralil.application.form;

import com.yuralil.domain.dao.ExhibitDao;
import com.yuralil.domain.dao.MultimediaDao;
import com.yuralil.domain.entities.Exhibit;
import com.yuralil.domain.entities.Multimedia;
import com.yuralil.infrastructure.util.ConnectionHolder;
import com.yuralil.infrastructure.util.ConnectionPool;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.List;

public class ExhibitListView extends VBox {

    public ExhibitListView() {
        setSpacing(12);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: transparent;");
        loadExhibitsFromDb();
    }

    public void addExhibit(String name, String category, String date, String description, String photoPath) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(12));
        card.setSpacing(4);
        card.setPrefWidth(600);
        card.setStyle("""
            -fx-background-color: #efefef;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-color: #ccc;
        """);

        Label nameLabel = new Label("Name: " + name);
        Label categoryLabel = new Label("Category: " + category);
        Label dateLabel = new Label("Date: " + date);
        Label descLabel = new Label("Description: " + description);

        for (Label label : new Label[]{nameLabel, categoryLabel, dateLabel, descLabel}) {
            label.setStyle("-fx-text-fill: #333; -fx-font-size: 13px;");
        }

        ImageView imageView = new ImageView();
        try {
            URL resource = getClass().getResource(photoPath);
            if (resource != null) {
                imageView.setImage(new Image(resource.toExternalForm()));
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);
            }
        } catch (Exception e) {
            System.err.println("⚠ Не вдалося завантажити фото: " + photoPath);
        }

        card.getChildren().addAll(nameLabel, categoryLabel, dateLabel, descLabel, imageView);
        getChildren().add(card);
    }

    public void loadExhibitsFromDb() {
        getChildren().clear();

        Label title = new Label("Exhibits");
        title.setFont(Font.font("Arial", 20));
        title.setStyle("-fx-text-fill: #1a3e2b; -fx-font-weight: bold;");
        getChildren().add(title);

        try {
            Connection conn = new ConnectionPool().getConnection();
            ConnectionHolder.set(conn);

            List<Exhibit> exhibits = ExhibitDao.getInstance().findAll();
            for (Exhibit exhibit : exhibits) {
                // Підтягування мультимедіа, якщо null
                if (exhibit.getMultimedia() != null && exhibit.getMultimedia().getFilePath() == null) {
                    Multimedia mm = MultimediaDao.getInstance().findById(exhibit.getMultimedia().getId()).orElse(null);
                    exhibit.setMultimedia(mm);
                }

                String path = (exhibit.getMultimedia() != null) ? exhibit.getMultimedia().getFilePath() : "";

                addExhibit(
                        exhibit.getName(),
                        exhibit.getCategory().getName(),
                        exhibit.getAcquisitionDate().toString(),
                        exhibit.getDescription(),
                        path
                );
            }
        } catch (Exception e) {
            Label error = new Label("❌ Failed to load exhibits: " + e.getMessage());
            error.setStyle("-fx-text-fill: red;");
            getChildren().add(error);
        } finally {
            ConnectionHolder.clear();
        }
    }
}
