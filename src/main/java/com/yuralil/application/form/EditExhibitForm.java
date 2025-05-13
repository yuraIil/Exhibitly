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

/**
 * JavaFX форма для редагування наявного експоната.
 * Дозволяє змінити назву, категорію, опис, дату надходження та фото.
 */
public class EditExhibitForm extends VBox {

    private final TextField nameField = new TextField();
    private final ComboBox<Category> categoryComboBox = new ComboBox<>();
    private final DatePicker acquisitionDate = new DatePicker();
    private final TextArea descriptionArea = new TextArea();
    private final Label photoPathLabel = new Label("No file selected");
    private final ImageView previewImage = new ImageView();
    private File selectedPhotoFile;

    /**
     * Конструктор форми редагування експоната.
     *
     * @param ownerStage вікно-власник
     * @param dialog     діалогове вікно, в якому відкривається форма
     * @param exhibit    експонат, який редагується
     * @param listView   список експонатів, який оновлюється після редагування
     */
    public EditExhibitForm(Stage ownerStage, Stage dialog, Exhibit exhibit, ExhibitListView listView) {
        setSpacing(10);
        setPadding(new Insets(20));
        setStyle("""
            -fx-background-color: white;
            -fx-border-color: #ccc;
            -fx-border-radius: 12;
            -fx-background-radius: 12;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 6);
        """);

        Label title = new Label("Edit Exhibit");
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
        nameField.setText(exhibit.getName());

        try (Connection conn = new com.yuralil.infrastructure.util.ConnectionPool().getConnection()) {
            ConnectionHolder.set(conn);
            categoryComboBox.getItems().addAll(CategoryDao.getInstance().findAll());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load categories: " + e.getMessage()).showAndWait();
        } finally {
            ConnectionHolder.clear();
        }

        categoryComboBox.setMaxWidth(Double.MAX_VALUE);
        categoryComboBox.setValue(exhibit.getCategory());

        acquisitionDate.setPromptText("Acquisition Date");
        acquisitionDate.setValue(exhibit.getAcquisitionDate());

        descriptionArea.setPromptText("Description");
        descriptionArea.setText(exhibit.getDescription());
        descriptionArea.setPrefRowCount(4);

        previewImage.setFitWidth(160);
        previewImage.setFitHeight(120);
        previewImage.setPreserveRatio(true);
        previewImage.setSmooth(true);

        if (exhibit.getMultimedia() != null && exhibit.getMultimedia().getFilePath() != null) {
            File imageFile = Path.of("storage/images", exhibit.getMultimedia().getFilePath()).toFile();
            if (imageFile.exists()) {
                previewImage.setImage(new Image(imageFile.toURI().toString()));
                photoPathLabel.setText(imageFile.getName());
            }
        }

        Button selectPhotoButton = new Button("\uD83D\uDCF7 Choose New Photo");
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
                    descriptionArea.getText().isBlank()) {

                com.yuralil.components.ConfirmDialog.showWarning("Please fill in all required fields.", getScene().getWindow());
                return;
            }

            try {
                Connection conn = new com.yuralil.infrastructure.util.ConnectionPool().getConnection();
                ConnectionHolder.set(conn);

                if (selectedPhotoFile != null) {
                    Path imagesDir = Path.of("storage/images");
                    if (!Files.exists(imagesDir)) Files.createDirectories(imagesDir);
                    Path destPath = imagesDir.resolve(selectedPhotoFile.getName());
                    Files.copy(selectedPhotoFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

                    Multimedia newMedia = new Multimedia();
                    newMedia.setType("image");
                    newMedia.setFilePath(selectedPhotoFile.getName());
                    MultimediaDao.getInstance().update(newMedia);
                    exhibit.setMultimedia(newMedia);
                }

                exhibit.setName(nameField.getText());
                exhibit.setCategory(categoryComboBox.getValue());
                exhibit.setDescription(descriptionArea.getText());
                exhibit.setAcquisitionDate(acquisitionDate.getValue());

                ExhibitDao.getInstance().update(exhibit);
                listView.loadExhibitsFromDb();

                dialog.close();
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "Failed to save image: " + ex.getMessage()).showAndWait();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Failed to update exhibit: " + ex.getMessage()).showAndWait();
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
     * Показує модальне вікно редагування експоната.
     *
     * @param parentStage головне вікно
     * @param exhibit     експонат для редагування
     * @param listView    список експонатів для оновлення після збереження
     */
    public static void showForm(Stage parentStage, Exhibit exhibit, ExhibitListView listView) {
        Stage dialog = new Stage();
        dialog.initOwner(parentStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);

        EditExhibitForm form = new EditExhibitForm(parentStage, dialog, exhibit, listView);
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
