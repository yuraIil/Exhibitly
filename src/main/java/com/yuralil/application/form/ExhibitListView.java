package com.yuralil.application.form;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class ExhibitListView extends VBox {

    public ExhibitListView() {
        setSpacing(12);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: transparent;");

        Label title = new Label("Exhibits");
        title.setFont(Font.font("Arial", 20));
        title.setStyle("-fx-text-fill: #1a3e2b; -fx-font-weight: bold;");
        getChildren().add(title);

        // Статичний набір даних
        addExhibit("Bust of Caesar", "Sculpture", "2022-10-01", "Roman bust", "/images/caesar.png");
        addExhibit("Greek Vase", "Pottery", "2023-02-18", "Ancient ceramic vase", "/images/vase.png");
        addExhibit("Mayan Mask", "Artifact", "2021-06-12", "Gold ceremonial mask", "/images/mask.png");
    }

    public void addExhibit(String name, String category, String date, String description, String photoPath) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(12));
        card.setSpacing(4);
        card.setPrefWidth(600); // Фіксована ширина
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
        Label photoLabel = new Label("Photo: " + photoPath);

        for (Label label : new Label[]{nameLabel, categoryLabel, dateLabel, descLabel, photoLabel}) {
            label.setStyle("-fx-text-fill: #333; -fx-font-size: 13px;");
        }

        card.getChildren().addAll(nameLabel, categoryLabel, dateLabel, descLabel, photoLabel);
        getChildren().add(card);
    }
}
