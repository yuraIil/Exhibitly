package com.yuralil.application.form;

import com.yuralil.domain.dao.ExhibitDao;
import com.yuralil.domain.entities.Exhibit;
import com.yuralil.infrastructure.util.ConnectionHolder;
import com.yuralil.infrastructure.util.ConnectionPool;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.File;
import java.sql.Connection;
import java.util.*;

/**
 * Компонент для відображення списку експонатів з підтримкою вибору, пошуку та завантаження.
 */
public class ExhibitListView extends VBox {

    private final Map<Exhibit, CheckBox> exhibitCheckboxMap = new LinkedHashMap<>();

    /**
     * Конструктор створює візуальний компонент і одразу завантажує експонати з бази.
     */
    public ExhibitListView() {
        setSpacing(12);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: transparent;");
        loadExhibitsFromDb();
    }

    /**
     * Додає картку експоната у вигляді VBox з усіма його даними та чекбоксом вибору.
     *
     * @param exhibit експонат для відображення
     */
    public void addExhibitCard(Exhibit exhibit) {
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

        CheckBox selectBox = new CheckBox();
        exhibitCheckboxMap.put(exhibit, selectBox);

        Label nameLabel = new Label("Name: " + exhibit.getName());
        Label categoryLabel = new Label("Category: " + exhibit.getCategory().getName());
        Label dateLabel = new Label("Date: " + exhibit.getAcquisitionDate());
        Label descLabel = new Label("Description: " + exhibit.getDescription());

        for (Label label : new Label[]{nameLabel, categoryLabel, dateLabel, descLabel}) {
            label.setStyle("-fx-text-fill: #333; -fx-font-size: 13px;");
        }

        ImageView imageView = new ImageView();
        try {
            File file = new File("src/main/resources/images/" + exhibit.getMultimedia().getFilePath());

            if (file.exists()) {
                imageView.setImage(new Image(file.toURI().toString()));
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);
            }
        } catch (Exception e) {
            System.err.println("⚠ Failed to load image: " + exhibit.getMultimedia().getFilePath());
        }

        HBox topRow = new HBox(10, selectBox, nameLabel);
        card.getChildren().addAll(topRow, categoryLabel, dateLabel, descLabel, imageView);
        getChildren().add(card);
    }

    /**
     * Завантажує всі експонати з бази даних і додає їх у список.
     */
    public void loadExhibitsFromDb() {
        getChildren().clear();
        exhibitCheckboxMap.clear();

        Label title = new Label("Exhibits");
        title.setFont(Font.font("Arial", 20));
        title.setStyle("-fx-text-fill: #1a3e2b; -fx-font-weight: bold;");
        getChildren().add(title);

        try {
            Connection conn = new ConnectionPool().getConnection();
            ConnectionHolder.set(conn);

            for (Exhibit exhibit : ExhibitDao.getInstance().findAll()) {
                addExhibitCard(exhibit);
            }
        } catch (Exception e) {
            Label error = new Label("❌ Failed to load exhibits: " + e.getMessage());
            error.setStyle("-fx-text-fill: red;");
            getChildren().add(error);
        } finally {
            ConnectionHolder.clear();
        }
    }

    /**
     * Виконує пошук експонатів за ключовим словом та відображає результати.
     *
     * @param query текст пошуку (нечутливий до регістру)
     */
    public void searchExhibits(String query) {
        getChildren().clear();
        exhibitCheckboxMap.clear();

        Label title = new Label("Search Results");
        title.setFont(Font.font("Arial", 20));
        title.setStyle("-fx-text-fill: #1a3e2b; -fx-font-weight: bold;");
        getChildren().add(title);

        try {
            Connection conn = new ConnectionPool().getConnection();
            ConnectionHolder.set(conn);

            List<Exhibit> all = ExhibitDao.getInstance().findAll();
            for (Exhibit exhibit : all) {
                String combined = (exhibit.getName() + exhibit.getDescription() + exhibit.getCategory().getName()).toLowerCase();
                if (combined.contains(query.toLowerCase())) {
                    addExhibitCard(exhibit);
                }
            }
        } catch (Exception e) {
            Label error = new Label("❌ Failed to search exhibits: " + e.getMessage());
            error.setStyle("-fx-text-fill: red;");
            getChildren().add(error);
        } finally {
            ConnectionHolder.clear();
        }
    }

    /**
     * Повертає список усіх вибраних експонатів (із відміченими чекбоксами).
     *
     * @return список вибраних експонатів
     */
    public List<Exhibit> getSelectedExhibits() {
        List<Exhibit> selected = new ArrayList<>();
        for (Map.Entry<Exhibit, CheckBox> entry : exhibitCheckboxMap.entrySet()) {
            if (entry.getValue().isSelected()) {
                selected.add(entry.getKey());
            }
        }
        return selected;
    }

    /**
     * Завантажує та відображає список переданих експонатів.
     *
     * @param exhibits список експонатів для відображення
     */
    public void loadExhibits(List<Exhibit> exhibits) {
        getChildren().clear();
        exhibitCheckboxMap.clear();

        Label title = new Label("Exhibits");
        title.setFont(Font.font("Arial", 20));
        title.setStyle("-fx-text-fill: #1a3e2b; -fx-font-weight: bold;");
        getChildren().add(title);

        for (Exhibit exhibit : exhibits) {
            addExhibitCard(exhibit);
        }
    }
}
