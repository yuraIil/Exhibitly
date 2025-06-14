package com.yuralil.application.components;

import com.yuralil.domain.dao.FavoriteDao;
import com.yuralil.domain.entities.Exhibit;
import com.yuralil.infrastructure.util.Session;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.sql.Connection;

/**
 * Компонент JavaFX, який відображає картку експоната з його назвою,
 * категорією, датою надходження та кнопкою додавання в обране.
 * <p>
 * Доступні функції:
 * <ul>
 *     <li>Відображення фото, назви, категорії та дати надходження експоната</li>
 *     <li>Анімація масштабування при наведенні</li>
 *     <li>Клік для відкриття діалогу з деталями експоната</li>
 *     <li>Кнопка "Додати в обране" (тільки для авторизованих користувачів)</li>
 * </ul>
 */
public class ExhibitCard extends StackPane {

    private final FavoriteDao favoriteDao = new FavoriteDao();

    /**
     * Конструктор, який створює картку експоната.
     *
     * @param exhibit Експонат, що буде відображено
     * @param conn    З'єднання з базою даних для перевірки обраного
     */
    public ExhibitCard(Exhibit exhibit, Connection conn) {
        setPrefWidth(220);
        setMaxWidth(220);

        // Завантаження зображення експоната
        ImageView imageView = new ImageView();
        String imgPath = "storage/images/" + exhibit.getMultimedia().getFilePath();
        File file = new File(imgPath);
        if (file.exists()) {
            imageView.setImage(new Image(file.toURI().toString(), 220, 130, true, true));
        }
        imageView.setFitWidth(220);
        imageView.setFitHeight(130);

        // Текстові поля: назва, категорія, дата
        Label title = new Label(exhibit.getName());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        Label category = new Label(exhibit.getCategory().getName());
        category.setStyle("-fx-text-fill: #555; -fx-font-size: 11px;");

        Label date = new Label("\uD83D\uDCC5 " + exhibit.getAcquisitionDate().toString());
        date.setStyle("-fx-text-fill: #777; -fx-font-size: 10px;");

        VBox info = new VBox(2, title, category, date);
        info.setPadding(new Insets(5));
        info.setAlignment(Pos.TOP_LEFT);

        // Додавання кнопки "в обране" (лише для авторизованих)
        if (Session.getCurrentUser() != null) {
            Button favButton = new Button();
            favButton.setStyle("-fx-background-color: transparent; -fx-font-size: 12px; -fx-cursor: hand;");

            int userId = Session.getCurrentUser().getId();
            boolean[] isFav = {favoriteDao.exists(conn, userId, exhibit.getId())};
            updateFavButton(favButton, isFav[0]);

            favButton.setOnAction(e -> {
                if (isFav[0]) {
                    favoriteDao.remove(conn, userId, exhibit.getId());
                } else {
                    favoriteDao.add(conn, userId, exhibit.getId());
                }
                isFav[0] = !isFav[0];
                updateFavButton(favButton, isFav[0]);
            });

            info.getChildren().add(favButton);
        }

        VBox content = new VBox(imageView, info);
        content.setStyle("""
            -fx-background-color: #ffffff;
            -fx-background-radius: 12;
            -fx-border-color: #ddd;
            -fx-border-radius: 12;
        """);
        content.setEffect(new javafx.scene.effect.DropShadow(4, Color.rgb(0, 0, 0, 0.05)));

        // Анімація при наведенні мишки
        ScaleTransition st = new ScaleTransition(Duration.millis(150), content);
        this.setOnMouseEntered(e -> {
            st.setToX(1.03);
            st.setToY(1.03);
            st.playFromStart();
        });
        this.setOnMouseExited(e -> {
            st.setToX(1);
            st.setToY(1);
            st.playFromStart();
        });

        // Відкриття діалогу з деталями при натисканні
        this.setOnMouseClicked(e -> {
            Window window = getScene() != null ? getScene().getWindow() : null;
            if (window instanceof javafx.stage.Stage stage) {
                ExhibitDetailsDialog.show(exhibit, stage);
            }
        });

        getChildren().add(content);
        setPadding(new Insets(5));
    }

    /**
     * Оновлює текст кнопки обраного відповідно до стану.
     *
     * @param button      Кнопка, яка змінює свій текст
     * @param isFavorite  Чи додано експонат до обраного
     */
    private void updateFavButton(Button button, boolean isFavorite) {
        button.setText(isFavorite ? "✅ У вибраному" : "❤ Додати в обране");
    }
}
