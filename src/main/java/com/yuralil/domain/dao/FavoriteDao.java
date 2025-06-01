package com.yuralil.domain.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для роботи з таблицею {@code favorite}.
 * <p>
 * Забезпечує додавання, видалення, перевірку існування
 * та отримання списку обраних експонатів користувача.
 */
public class FavoriteDao {

    /**
     * Додає експонат до обраного користувача.
     * Якщо запис уже існує — нічого не робиться (ON CONFLICT DO NOTHING).
     *
     * @param conn      з'єднання з БД
     * @param userId    ідентифікатор користувача
     * @param exhibitId ідентифікатор експоната
     */
    public void add(Connection conn, int userId, int exhibitId) {
        String sql = "INSERT INTO favorite (user_id, exhibit_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, exhibitId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Видаляє експонат із обраного користувача.
     *
     * @param conn      з'єднання з БД
     * @param userId    ідентифікатор користувача
     * @param exhibitId ідентифікатор експоната
     */
    public void remove(Connection conn, int userId, int exhibitId) {
        String sql = "DELETE FROM favorite WHERE user_id = ? AND exhibit_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, exhibitId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Перевіряє, чи експонат вже в обраному у користувача.
     *
     * @param conn      з'єднання з БД
     * @param userId    ідентифікатор користувача
     * @param exhibitId ідентифікатор експоната
     * @return {@code true}, якщо запис існує
     */
    public boolean exists(Connection conn, int userId, int exhibitId) {
        String sql = "SELECT 1 FROM favorite WHERE user_id = ? AND exhibit_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, exhibitId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Повертає список ID експонатів, які додані користувачем до обраного.
     *
     * @param conn   з'єднання з БД
     * @param userId ідентифікатор користувача
     * @return список ID обраних експонатів
     */
    public List<Integer> findFavoriteExhibitIds(Connection conn, int userId) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT exhibit_id FROM favorite WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("exhibit_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }
}
