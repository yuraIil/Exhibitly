package com.yuralil.application.windows;

import com.yuralil.application.form.*;
import com.yuralil.infrastructure.util.Session;
import com.yuralil.infrastructure.util.SessionStorage;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.*;

/**
 * Головне меню застосунку Exhibitly.
 * <p>
 * Відображає доступні вкладки в залежності від ролі користувача (ADMIN, USER, VISITOR).
 * Має навігацію між формами (ExhibitManager, Catalog, Favorites, Reports, Settings),
 * кнопку виходу, адаптивний фон і підтримку повноекранного режиму.
 */
public class MainMenuWindow {

    private final String baseStyle = "-fx-font-size: 14px; -fx-text-fill: #1a3e2b;";
    private final String activeStyle = baseStyle + "-fx-background-color: #e2e8f0; -fx-background-radius: 10; -fx-padding: 6 12;";
    private Label activeItem = null;
    private VBox rightPanel;
    private String userRole = "user";

    /**
     * Встановлює роль користувача (admin/user/visitor) для визначення доступу до вкладок.
     *
     * @param role роль користувача
     */
    public void setUserRole(String role) {
        this.userRole = role;
    }

    /**
     * Відображає головне меню на переданому вікні.
     *
     * @param stage       цільове вікно
     * @param fullScreen  чи слід відразу перейти в повноекранний режим
     */
    public void show(Stage stage, boolean fullScreen) {
        stage.setFullScreen(fullScreen);
        double width = stage.getWidth() > 0 ? stage.getWidth() : 1280;
        double height = stage.getHeight() > 0 ? stage.getHeight() : 720;

        Label logo = new Label("Exhibitly");
        logo.setStyle("""
            -fx-font-size: 28px;
            -fx-font-weight: bold;
            -fx-text-fill: #1a3e2b;
        """);

        Label sessionLabel = new Label("👤 " + Optional.ofNullable(Session.getCurrentUser())
                .map(u -> u.getUsername()).orElse("Unknown"));
        sessionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        sessionLabel.setAlignment(Pos.TOP_RIGHT);

        Button backButton = new Button("↩ Log out");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #1a3e2b; -fx-underline: true; -fx-font-size: 12px;");
        backButton.setOnAction(e -> {
            SessionStorage.clear();
            Session.clear();
            AuthWindow authWindow = new AuthWindow();
            authWindow.show(stage);
        });

        HBox topBar = new HBox(10, backButton, sessionLabel);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(10, 20, 0, 0));

        Map<String, Label> menuItems = new LinkedHashMap<>();
        if (!"visitor".equalsIgnoreCase(userRole)) {
            menuItems.put("Exhibit Manager", new Label("\uD83C\uDFDB Exhibit Manager"));
        }
        menuItems.put("Collection Catalog", new Label("\uD83D\uDCDA Collection Catalog"));
        menuItems.put("Favorites", new Label("\u2764 Favorites"));

        if ("ADMIN".equalsIgnoreCase(userRole)) {
            menuItems.put("Reports", new Label("📝 Reports"));
        }

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

        BorderPane wrapper = new BorderPane(content);
        wrapper.setTop(topBar);
        wrapper.setStyle("-fx-background-color: white; -fx-background-radius: 32;");
        wrapper.setPadding(new Insets(20));
        wrapper.maxWidthProperty().bind(stage.widthProperty().subtract(60));
        wrapper.maxHeightProperty().bind(Bindings.min(stage.heightProperty().subtract(60), 700));
        wrapper.maxWidthProperty().bind(Bindings.min(stage.widthProperty().subtract(60), 1000));

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #fdfdfd, #f4f2ee);");
        root.setPrefSize(width, height);

        Pane background = createBackgroundCircles(width, height);
        root.getChildren().add(background);
        root.getChildren().add(wrapper);
        StackPane.setAlignment(background, Pos.CENTER);
        StackPane.setAlignment(wrapper, Pos.CENTER);

        Scene scene = new Scene(root, width, height);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });

        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);

        if (!"visitor".equalsIgnoreCase(userRole)) {
            setActiveItem(menuItems.get("Exhibit Manager"));
        } else {
            setActiveItem(menuItems.get("Collection Catalog"));
        }

        stage.show();
    }

    /**
     * Змінює активну вкладку та виводить відповідну форму у правій панелі.
     *
     * @param selected обрана вкладка меню
     */
    private void setActiveItem(Label selected) {
        if (activeItem != null) activeItem.setStyle(baseStyle);
        activeItem = selected;
        activeItem.setStyle(activeStyle);

        String text = selected.getText();
        VBox newContent = switch (text) {
            case "\uD83C\uDFDB Exhibit Manager" -> new ExhibitManagerForm();
            case "\uD83D\uDCDA Collection Catalog" -> new CollectionCatalogForm();
            case "\u2764 Favorites" -> new FavoriteForm();
            case "📝 Reports" -> new ReportGeneratorForm();
            case "\u2699 Settings" -> new SettingsForm();
            default -> new VBox(new Label("Coming soon..."));
        };

        styleRightPanel(newContent);

        HBox content = (HBox) rightPanel.getParent();
        content.getChildren().set(1, newContent);
        rightPanel = newContent;
    }

    /**
     * Стилізує праву панель під відображення контенту.
     *
     * @param panel VBox, в який виводиться контент
     */
    private void styleRightPanel(VBox panel) {
        panel.setStyle("-fx-background-color: transparent;");
        panel.setPadding(new Insets(0));
        VBox.setVgrow(panel, Priority.ALWAYS);
    }

    /**
     * Створює задній фон з набору кольорових кругів.
     *
     * @param width  ширина екрану
     * @param height висота екрану
     * @return панель із фоном
     */
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

    /**
     * Створює одне кольорове розмите коло.
     *
     * @param radius  радіус
     * @param color   колір
     * @param x       позиція по X
     * @param y       позиція по Y
     * @param opacity прозорість
     * @return обʼєкт кола
     */
    private Circle createBlurredCircle(double radius, Color color, double x, double y, double opacity) {
        Circle circle = new Circle(radius, color);
        circle.setOpacity(opacity);
        circle.setLayoutX(x);
        circle.setLayoutY(y);
        circle.setEffect(new BoxBlur(40, 40, 2));
        return circle;
    }
}
