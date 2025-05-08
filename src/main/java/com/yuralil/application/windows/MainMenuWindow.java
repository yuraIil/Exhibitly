package com.yuralil.application.windows;

import com.yuralil.application.form.ExhibitManagerForm;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.*;

public class MainMenuWindow {

    private final String baseStyle = "-fx-font-size: 14px; -fx-text-fill: #1a3e2b;";
    private final String activeStyle = baseStyle + "-fx-background-color: #e2e8f0; -fx-background-radius: 10; -fx-padding: 6 12;";
    private Label activeItem = null;
    private VBox rightPanel;
    private String userRole = "user";

    public void setUserRole(String role) {
        this.userRole = role;
    }

    public void show(Stage stage, boolean fullScreen) {
        double width = stage.getWidth();
        double height = stage.getHeight();

        Label logo = new Label("Exhibitly");
        logo.setStyle("""
        -fx-font-size: 28px;
        -fx-font-weight: bold;
        -fx-text-fill: #1a3e2b;
    """);

        Map<String, Label> menuItems = new LinkedHashMap<>();

        if (!"visitor".equals(userRole)) {
            menuItems.put("Exhibit Manager", new Label("\uD83C\uDFDB Exhibit Manager"));
        }
        menuItems.put("Collection Catalog", new Label("\uD83D\uDCDA Collection Catalog"));
        menuItems.put("Favorites", new Label("\u2764 Favorites"));
        menuItems.put("Settings", new Label("\u2699 Settings"));

        VBox menu = new VBox(10);
        menu.setAlignment(Pos.TOP_LEFT);
        for (Label item : menuItems.values()) {
            item.setStyle(baseStyle);
            item.setOnMouseClicked(e -> setActiveItem(item));
            menu.getChildren().add(item);
        }

        VBox leftPanel = new VBox(20, logo, menu);
        leftPanel.setAlignment(Pos.TOP_LEFT);
        leftPanel.setPadding(new Insets(30));
        leftPanel.setMinWidth(220);
        leftPanel.setMaxWidth(280);

        rightPanel = new VBox(new Label("Welcome to Exhibitly!"));
        styleRightPanel(rightPanel);

        HBox content = new HBox(leftPanel, rightPanel);
        content.setSpacing(30);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(30));
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        VBox wrapper = new VBox(content);
        wrapper.setStyle("-fx-background-color: white; -fx-background-radius: 32;");
        wrapper.setPadding(new Insets(20));
        wrapper.maxWidthProperty().bind(stage.widthProperty().subtract(60));
        wrapper.setMaxHeight(640);
        wrapper.maxHeightProperty().bind(Bindings.min(stage.heightProperty().subtract(60), 700));
        wrapper.maxWidthProperty().bind(Bindings.min(stage.widthProperty().subtract(60), 1000));

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #fdfdfd, #f4f2ee);");
        root.setPrefSize(width, height);

        Pane background = createBackgroundCircles(width, height);
        root.getChildren().addAll(background, wrapper);

        Scene scene = new Scene(root, width, height);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                if (stage.isFullScreen()) {
                    stage.setFullScreen(false);
                    stage.setWidth(800);
                    stage.setHeight(600);
                    stage.centerOnScreen();
                } else {
                    stage.setFullScreen(true);
                }
            }
        });

        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.setFullScreen(fullScreen);

        if (!"visitor".equals(userRole)) {
            setActiveItem(menuItems.get("Exhibit Manager"));
        }
    }

    private void setActiveItem(Label selected) {
        if (activeItem != null) activeItem.setStyle(baseStyle);
        activeItem = selected;
        activeItem.setStyle(activeStyle);

        String text = selected.getText();
        VBox newContent = switch (text) {
            case "\uD83C\uDFDB Exhibit Manager" -> new ExhibitManagerForm();
            default -> new VBox(new Label("Coming soon..."));
        };

        styleRightPanel(newContent);

        HBox content = (HBox) rightPanel.getParent();
        content.getChildren().set(1, newContent);
        rightPanel = newContent;
    }

    private void styleRightPanel(VBox panel) {
        panel.setStyle("-fx-background-color: transparent;");
        panel.setPadding(new Insets(0));
        VBox.setVgrow(panel, Priority.ALWAYS);
    }

    private Pane createBackgroundCircles(double width, double height) {
        Pane pane = new Pane();
        pane.setPrefSize(width, height);

        List<Circle> circles = new ArrayList<>(List.of(
                createBlurredCircle(300, Color.web("#f87171"), width * 0.2, height * 0.25, 0.25),
                createBlurredCircle(250, Color.web("#60a5fa"), width * 0.65, height * 0.2, 0.25),
                createBlurredCircle(250, Color.web("#34d399"), width * 0.7, height * 0.8, 0.25),
                createBlurredCircle(200, Color.web("#facc15"), width * 0.4, height * 0.75, 0.25),
                createBlurredCircle(220, Color.web("#a78bfa"), width * 0.15, height * 0.8, 0.25)
        ));

        pane.getChildren().addAll(circles);
        return pane;
    }

    private Circle createBlurredCircle(double radius, Color color, double x, double y, double opacity) {
        Circle circle = new Circle(radius, color);
        circle.setOpacity(opacity);
        circle.setLayoutX(x);
        circle.setLayoutY(y);
        circle.setEffect(new BoxBlur(40, 40, 2));
        return circle;
    }
}
