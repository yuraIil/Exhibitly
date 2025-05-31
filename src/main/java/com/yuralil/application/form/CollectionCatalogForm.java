package com.yuralil.application.form;

import com.yuralil.application.components.ExhibitCard;
import com.yuralil.domain.dao.CategoryDao;
import com.yuralil.domain.dao.ExhibitDao;
import com.yuralil.domain.entities.Category;
import com.yuralil.domain.entities.Exhibit;
import com.yuralil.infrastructure.util.ConnectionHolder;
import com.yuralil.infrastructure.util.ConnectionPool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CollectionCatalogForm extends VBox {

    private final TilePane tilePane;
    private final ComboBox<String> categoryFilter;
    private final TextField searchField;
    private final List<Exhibit> allExhibits = new ArrayList<>();

    public CollectionCatalogForm() {
        setSpacing(16);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(this, Priority.ALWAYS);

        Text title = new Text("🖼 Колекція експонатів");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        searchField = new TextField();
        searchField.setPromptText("Пошук за назвою...");
        searchField.setPrefWidth(200);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> updateFilter());

        categoryFilter = new ComboBox<>();
        categoryFilter.setPrefWidth(160);
        categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> updateFilter());

        HBox filterBox = new HBox(10, searchField, categoryFilter);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        tilePane = new TilePane();
        tilePane.setHgap(20);
        tilePane.setVgap(20);
        tilePane.setPadding(new Insets(10));
        tilePane.setPrefTileWidth(240); // ширина картки
        tilePane.setTileAlignment(Pos.TOP_LEFT);
        tilePane.setPrefColumns(5);

        tilePane.setMaxWidth(Double.MAX_VALUE);
        tilePane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        tilePane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        tilePane.setMinHeight(Region.USE_COMPUTED_SIZE);

        ScrollPane scroll = new ScrollPane(tilePane);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setMaxHeight(Double.MAX_VALUE);
        scroll.setStyle("-fx-background-color: transparent;");
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        getChildren().addAll(title, filterBox, scroll);

        loadExhibitsFromDb();
    }

    private void loadExhibitsFromDb() {
        allExhibits.clear();
        categoryFilter.getItems().clear();
        tilePane.getChildren().clear();

        try (Connection conn = new ConnectionPool().getConnection()) {
            ConnectionHolder.set(conn);
            List<Exhibit> exhibits = ExhibitDao.getInstance().findAll();
            allExhibits.addAll(exhibits);

            List<Category> categories = CategoryDao.getInstance().findAll();
            ObservableList<String> options = FXCollections.observableArrayList("Усі категорії");
            for (Category category : categories) {
                options.add(category.getName());
            }
            categoryFilter.setItems(options);
            categoryFilter.setValue("Усі категорії");

            updateFilter();

        } catch (Exception e) {
            tilePane.getChildren().add(new Text("❌ Помилка завантаження експонатів."));
            e.printStackTrace();
        } finally {
            ConnectionHolder.clear();
        }
    }

    private void updateFilter() {
        String keyword = searchField.getText().toLowerCase();
        String selectedCategory = categoryFilter.getValue();

        tilePane.getChildren().clear();

        for (Exhibit exhibit : allExhibits) {
            boolean matchesSearch = exhibit.getName().toLowerCase().contains(keyword);
            boolean matchesCategory = selectedCategory.equals("Усі категорії") ||
                    exhibit.getCategory().getName().equals(selectedCategory);

            if (matchesSearch && matchesCategory) {
                tilePane.getChildren().add(new ExhibitCard(exhibit));
            }
        }
    }
}
