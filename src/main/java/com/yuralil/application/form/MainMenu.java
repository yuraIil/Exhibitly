package com.yuralil.application.form;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {

    public void show(Stage stage) {
        // Заголовок
        Label title = new Label("Exhibitly");
        title.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: green;");

        // Вертикальне меню зліва
        Label exhibitManager = new Label("Exhibit Manager");
        Label collectionCatalog = new Label("Collection Catalog");
        Label favorite = new Label("Favorite");
        Label settings = new Label("Settings");

        // Стилі для пунктів меню
        String menuStyle = "-fx-font-size: 16px; -fx-text-fill: black; -fx-padding: 10;";
        exhibitManager.setStyle(menuStyle);
        collectionCatalog.setStyle(menuStyle);
        favorite.setStyle(menuStyle);
        settings.setStyle(menuStyle);

        VBox menu = new VBox(10, exhibitManager, collectionCatalog, favorite, settings);
        menu.setAlignment(Pos.TOP_LEFT);
        menu.setPadding(new Insets(20));


        Image image = new Image(getClass().getResourceAsStream("/bust.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(300);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);

        VBox imageBox = new VBox(imageView);
        imageBox.setAlignment(Pos.CENTER_RIGHT);
        imageBox.setPadding(new Insets(20));

        // Основний контейнер: меню зліва, зображення справа
        HBox mainLayout = new HBox(20, menu, imageBox);
        mainLayout.setAlignment(Pos.CENTER);

        // Головний контейнер з заголовком зверху
        VBox layout = new VBox(20, title, mainLayout);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #d3ccc5;");
        layout.setPadding(new Insets(20));
        layout.setPrefSize(720, 540);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
    }
}