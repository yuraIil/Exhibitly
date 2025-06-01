package com.yuralil.application.form;

import com.yuralil.application.components.ExhibitCard;
import com.yuralil.domain.dao.ExhibitDao;
import com.yuralil.domain.dao.FavoriteDao;
import com.yuralil.domain.entities.Exhibit;
import com.yuralil.infrastructure.util.ConnectionHolder;
import com.yuralil.infrastructure.util.ConnectionPool;
import com.yuralil.infrastructure.util.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.util.List;

public class FavoriteForm extends VBox {

    private final TilePane tilePane;

    public FavoriteForm() {
        setSpacing(16);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(this, Priority.ALWAYS);

        Text title = new Text("❤ Улюблені експонати");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        tilePane = new TilePane();
        tilePane.setHgap(20);
        tilePane.setVgap(20);
        tilePane.setPadding(new Insets(10));
        tilePane.setPrefTileWidth(240);
        tilePane.setTileAlignment(Pos.TOP_LEFT);
        tilePane.setPrefColumns(5);
        tilePane.setMaxWidth(Double.MAX_VALUE);

        ScrollPane scroll = new ScrollPane(tilePane);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        getChildren().addAll(title, scroll);

        loadFavorites();
    }

    private void loadFavorites() {
        tilePane.getChildren().clear();

        if (Session.getCurrentUser() == null) {
            tilePane.getChildren().add(new Text("🔒 Увійдіть, щоб переглядати улюблені експонати."));
            return;
        }

        try (Connection conn = new ConnectionPool().getConnection()) {
            ConnectionHolder.set(conn);

            int userId = Session.getCurrentUser().getId();
            FavoriteDao favoriteDao = new FavoriteDao();
            ExhibitDao exhibitDao = ExhibitDao.getInstance();

            List<Integer> favoriteIds = favoriteDao.findFavoriteExhibitIds(conn, userId);
            for (int id : favoriteIds) {
                exhibitDao.findById(id).ifPresent(exhibit -> {
                    tilePane.getChildren().add(new ExhibitCard(exhibit, conn));
                });
            }

        } catch (Exception e) {
            tilePane.getChildren().add(new Text("❌ Не вдалося завантажити улюблені експонати."));
            e.printStackTrace();
        } finally {
            ConnectionHolder.clear();
        }
    }
}
