package com.yuralil.application.form;

import com.yuralil.components.ConfirmDialog;
import com.yuralil.domain.dao.ExhibitDao;
import com.yuralil.infrastructure.util.ConnectionHolder;
import com.yuralil.infrastructure.util.ConnectionPool;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;

public class ExhibitManagerForm extends VBox {

    private final ExhibitListView exhibitListView;
    private final TextField searchField;

    public ExhibitManagerForm() {
        setSpacing(20);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 24;");

        Label title = new Label("Manage Exhibits");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a3e2b;");

        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(300);
        searchField.setStyle("""
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-border-color: #ccc;
            -fx-padding: 6 10;
        """);

        Button searchBtn = new Button("Search");
        searchBtn.setStyle("""
            -fx-background-color: #e6efe9;
            -fx-text-fill: #1a3e2b;
            -fx-background-radius: 8;
            -fx-font-weight: bold;
            -fx-padding: 6 14;
        """);

        HBox searchBox = new HBox(10, searchField, searchBtn);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 0, 10, 0));

        exhibitListView = new ExhibitListView();

        ScrollPane scrollPane = new ScrollPane(exhibitListView);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMaxHeight(500);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox contentBox = new VBox(10, title, searchBox, scrollPane);
        VBox.setVgrow(contentBox, Priority.ALWAYS);

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> AddExhibitForm.showForm((Stage) getScene().getWindow(), exhibitListView));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> {
            var selected = exhibitListView.getSelectedExhibits();
            if (selected.isEmpty()) {
                ConfirmDialog.showWarning("Please select exhibits to delete.", getScene().getWindow());

                return;
            }

            ConfirmDialog.show(
                    "Are you sure you want to delete selected exhibits?",
                    () -> {
                        try {
                            Connection conn = new ConnectionPool().getConnection();
                            ConnectionHolder.set(conn);
                            for (var exhibit : selected) {
                                ExhibitDao.getInstance().delete(exhibit.getId());
                            }
                            exhibitListView.loadExhibitsFromDb();
                        } catch (Exception ex) {
                            new Alert(Alert.AlertType.ERROR, "Failed to delete: " + ex.getMessage()).showAndWait();
                        } finally {
                            ConnectionHolder.clear();
                        }
                    },
                    getScene().getWindow() // ✅ передаємо головне вікно
            );

        });

        for (Button btn : new Button[]{addBtn, deleteBtn}) {
            btn.setStyle("""
                -fx-background-color: #e6efe9;
                -fx-text-fill: #1a3e2b;
                -fx-background-radius: 8;
                -fx-font-weight: bold;
                -fx-padding: 6 14;
            """);
        }

        HBox buttonBox = new HBox(10, addBtn, deleteBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        getChildren().addAll(contentBox, buttonBox);
    }
}
