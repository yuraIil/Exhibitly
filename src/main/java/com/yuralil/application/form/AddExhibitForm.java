package com.yuralil.application.form;

import com.yuralil.domain.dao.CategoryDao;
import com.yuralil.domain.dao.ExhibitDao;
import com.yuralil.domain.dao.MultimediaDao;
import com.yuralil.domain.entities.Category;
import com.yuralil.domain.entities.Exhibit;
import com.yuralil.domain.entities.Multimedia;
import com.yuralil.infrastructure.util.ConnectionHolder;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Connection;
import java.time.LocalDate;

public class AddExhibitForm extends VBox {

    private final TextField nameField = new TextField();
    private final TextField categoryField = new TextField();
    private final DatePicker acquisitionDate = new DatePicker();
    private final TextArea descriptionArea = new TextArea();
    private final Label photoPathLabel = new Label("No file selected");
    private final ImageView previewImage = new ImageView();
    private File selectedPhotoFile;

    public AddExhibitForm(Stage ownerStage, ExhibitListView listView) {
        setSpacing(10);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8;");

        nameField.setPromptText("Exhibit Name");
        categoryField.setPromptText("Category (name)");
        acquisitionDate.setPromptText("Acquisition Date");
        descriptionArea.setPromptText("Description");
        descriptionArea.setPrefRowCount(4);

        previewImage.setFitWidth(160);
        previewImage.setFitHeight(120);
        previewImage.setPreserveRatio(true);
        previewImage.setSmooth(true);

        Button selectPhotoButton = new Button("Choose Photo");
        selectPhotoButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Exhibit Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            selectedPhotoFile = fileChooser.showOpenDialog(ownerStage);
            if (selectedPhotoFile != null) {
                photoPathLabel.setText(selectedPhotoFile.getName());
                previewImage.setImage(new Image(selectedPhotoFile.toURI().toString()));
            }
        });

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #2a5e3f; -fx-text-fill: white; -fx-padding: 6 16;");
        saveButton.setOnAction(e -> {
            try {
                Connection conn = new com.yuralil.infrastructure.util.ConnectionPool().getConnection();
                ConnectionHolder.set(conn);

                // 1. Копіюємо файл у "images/" в корені проєкту
                Path imagesDir = Path.of("images");
                if (!Files.exists(imagesDir)) {
                    Files.createDirectories(imagesDir);
                }
                Path destPath = imagesDir.resolve(selectedPhotoFile.getName());
                Files.copy(selectedPhotoFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

                // 2. Створюємо мультимедіа
                Multimedia multimedia = new Multimedia();
                multimedia.setType("image");
                multimedia.setFilePath(selectedPhotoFile.getName());
                multimedia = MultimediaDao.getInstance().insert(multimedia);

                // 3. Категорія
                Category category = CategoryDao.getInstance().findOrCreateByName(categoryField.getText());

                // 4. Експонат
                Exhibit exhibit = new Exhibit();
                exhibit.setName(nameField.getText());
                exhibit.setCategory(category);
                exhibit.setDescription(descriptionArea.getText());
                exhibit.setAcquisitionDate(acquisitionDate.getValue() != null ? acquisitionDate.getValue() : LocalDate.now());
                exhibit.setMultimedia(multimedia);
                ExhibitDao.getInstance().insert(exhibit);

                listView.loadExhibitsFromDb(); // оновлення списку
                ((Stage) getScene().getWindow()).close();
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Failed to save image: " + ex.getMessage()).showAndWait();
            } finally {
                ConnectionHolder.clear();
            }
        });

        getChildren().addAll(
                new Label("Name"), nameField,
                new Label("Category"), categoryField,
                new Label("Date"), acquisitionDate,
                new Label("Description"), descriptionArea,
                new Label("Photo"), selectPhotoButton, photoPathLabel, previewImage,
                saveButton
        );
    }

    public static void showForm(Stage parentStage, ExhibitListView listView) {
        Stage dialog = new Stage();
        dialog.initOwner(parentStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        AddExhibitForm form = new AddExhibitForm(dialog, listView);
        Scene scene = new Scene(form, 400, 600);
        dialog.setTitle("Add New Exhibit");
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
