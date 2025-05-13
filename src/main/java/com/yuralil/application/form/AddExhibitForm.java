package com.yuralil.application.form;

import com.yuralil.domain.dao.CategoryDao;
import com.yuralil.domain.dao.ExhibitDao;
import com.yuralil.domain.dao.MultimediaDao;
import com.yuralil.domain.entities.Category;
import com.yuralil.domain.entities.Exhibit;
import com.yuralil.domain.entities.Multimedia;
import com.yuralil.infrastructure.util.ConnectionHolder;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Connection;
import java.time.LocalDate;

/**
 * A form for adding a new exhibit, including fields for name, category, date,
 * description, and photo selection. Saves data to the database and displays the image.
 */
public class AddExhibitForm extends VBox {

    private final TextField nameField = new TextField();
    private final ComboBox<Category> categoryComboBox = new ComboBox<>();
    private final DatePicker acquisitionDate = new DatePicker();
    private final TextArea descriptionArea = new TextArea();
    private final Label photoPathLabel = new Label("No file selected");
    private final ImageView previewImage = new ImageView();
    private File selectedPhotoFile;

    /**
     * Constructs the AddExhibitForm UI and sets up all input fields and event handlers.
     *
     * @param ownerStage the parent window stage
     * @param dialog     the dialog stage that contains the form
     * @param listView   the ExhibitListView to refresh after adding a new exhibit
     */
    public AddExhibitForm(Stage ownerStage, Stage dialog, ExhibitListView listView) {
        setSpacing(10);
        setPadding(new Insets(20));
        setStyle("""
            -fx-background-color: white;
            -fx-border-color: #ccc;
            -fx-border-radius: 12;
            -fx-background-radius: 12;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 6);
        """);

        Label title = new Label("Add New Exhibit");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button closeBtn = new Button("✖");
        closeBtn.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: #444;
            -fx-font-size: 14px;
        """);
        closeBtn.setOnAction(e -> dialog.close());

        HBox titleBar = new HBox(title, new Region(), closeBtn);
        HBox.setHgrow(titleBar.getChildren().get(1), Priority.ALWAYS);
        titleBar.setAlignment(Pos.CENTER_LEFT);

        nameField.setPromptText("Exhibit Name");

        categoryComboBox.setPromptText("Select Category");
        try (Connection conn = new com.yuralil.infrastructure.util.ConnectionPool().getConnection()) {
            ConnectionHolder.set(conn);
            categoryComboBox.getItems().addAll(CategoryDao.getInstance().findAll());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load categories: " + e.getMessage()).showAndWait();
        } finally {
            ConnectionHolder.clear();
        }
        categoryComboBox.setMaxWidth(Double.MAX_VALUE);

        acquisitionDate.setPromptText("Acquisition Date");
        descriptionArea.setPromptText("Description");
        descriptionArea.setPrefRowCount(4);

        previewImage.setFitWidth(160);
        previewImage.setFitHeight(120);
        previewImage.setPreserveRatio(true);
        previewImage.setSmooth(true);

        Button selectPhotoButton = new Button("\uD83D\uDCF7 Choose Photo");
        selectPhotoButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Exhibit Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            selectedPhotoFile = fileChooser.showOpenDialog(dialog);

            if (selectedPhotoFile != null) {
                photoPathLabel.setText(selectedPhotoFile.getName());
                previewImage.setImage(new Image(selectedPhotoFile.toURI().toString()));
            }
        });

        Button saveButton = new Button("\uD83D\uDCBE Save");
        saveButton.setStyle("""
            -fx-background-color: #2a5e3f;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 6 16;
            -fx-background-radius: 6;
        """);

        saveButton.setOnAction(e -> {
            if (nameField.getText().isBlank() ||
                    categoryComboBox.getValue() == null ||
                    acquisitionDate.getValue() == null ||
                    descriptionArea.getText().isBlank() ||
                    selectedPhotoFile == null) {

                com.yuralil.components.ConfirmDialog.showWarning("Please fill in all fields and select a photo.", getScene().getWindow());
                return;
            }

            try {
                Connection conn = new com.yuralil.infrastructure.util.ConnectionPool().getConnection();
                ConnectionHolder.set(conn);

                Path imagesDir = Path.of("src/main/resources/images");
                if (!Files.exists(imagesDir)) Files.createDirectories(imagesDir);
                Path destPath = imagesDir.resolve(selectedPhotoFile.getName());
                Files.copy(selectedPhotoFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

                Multimedia multimedia = new Multimedia();
                multimedia.setType("image");
                multimedia.setFilePath(selectedPhotoFile.getName());
                multimedia = MultimediaDao.getInstance().insert(multimedia);

                Category category = categoryComboBox.getValue();

                Exhibit exhibit = new Exhibit();
                exhibit.setName(nameField.getText());
                exhibit.setCategory(category);
                exhibit.setDescription(descriptionArea.getText());
                exhibit.setAcquisitionDate(acquisitionDate.getValue());
                exhibit.setMultimedia(multimedia);

                ExhibitDao.getInstance().insert(exhibit);
                listView.loadExhibitsFromDb();
                dialog.close();
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Failed to save image: " + ex.getMessage()).showAndWait();
            } finally {
                ConnectionHolder.clear();
            }
        });

        VBox.setMargin(saveButton, new Insets(20, 0, 0, 0));

        getChildren().addAll(
                titleBar,
                new Label("Name"), nameField,
                new Label("Category"), categoryComboBox,
                new Label("Date"), acquisitionDate,
                new Label("Description"), descriptionArea,
                new Label("Photo"), selectPhotoButton, photoPathLabel, previewImage,
                saveButton
        );
    }

    /**
     * Displays the add exhibit form in a modal dialog with fade-in animation.
     *
     * @param parentStage the parent window stage
     * @param listView    the ExhibitListView to refresh after adding
     */
    public static void showForm(Stage parentStage, ExhibitListView listView) {
        Stage dialog = new Stage();
        dialog.initOwner(parentStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);

        AddExhibitForm form = new AddExhibitForm(parentStage, dialog, listView);
        Scene scene = new Scene(form, 450, 700);
        dialog.setScene(scene);

        dialog.setOnShown(e -> {
            dialog.setX(parentStage.getX() + (parentStage.getWidth() - dialog.getWidth()) / 2);
            dialog.setY(parentStage.getY() + (parentStage.getHeight() - dialog.getHeight()) / 2);
            FadeTransition ft = new FadeTransition(Duration.millis(250), form);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        });

        dialog.showAndWait();
    }
}
