package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Exhibit;
import com.yuralil.domain.entities.Multimedia;
import com.yuralil.infrastructure.util.ConnectionHolder;

import java.sql.*;
import java.util.Optional;

public class MultimediaDao {

    private static final MultimediaDao INSTANCE = new MultimediaDao();

    private static final String INSERT_SQL = """
        INSERT INTO multimedia (type, file_path, exhibit_id)
        VALUES (?, ?, ?)
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT id, exhibit_id, type, file_path
        FROM multimedia
        WHERE id = ?
        """;

    private static final String UPDATE_SQL = """
        UPDATE multimedia
        SET exhibit_id = ?, type = ?, file_path = ?
        WHERE id = ?
        """;

    private static final String DELETE_SQL = """
        DELETE FROM multimedia
        WHERE id = ?
        """;

    private MultimediaDao() {}

    public static MultimediaDao getInstance() {
        return INSTANCE;
    }

    public Multimedia insert(Multimedia multimedia) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, multimedia.getType());
            ps.setString(2, multimedia.getFilePath());

            if (multimedia.getExhibit() != null) {
                ps.setInt(3, multimedia.getExhibit().getId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                multimedia.setId(keys.getInt(1));
            }

            return multimedia;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert multimedia", e);
        }
    }

    public Optional<Multimedia> findById(int id) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Exhibit exhibit = new Exhibit();
                exhibit.setId(rs.getInt("exhibit_id"));

                Multimedia multimedia = new Multimedia(
                        rs.getInt("id"),
                        exhibit,
                        rs.getString("type"),
                        rs.getString("file_path")
                );
                return Optional.of(multimedia);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find multimedia", e);
        }
    }

    public boolean update(Multimedia multimedia) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setInt(1, multimedia.getExhibit().getId());
            ps.setString(2, multimedia.getType());
            ps.setString(3, multimedia.getFilePath());
            ps.setInt(4, multimedia.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update multimedia", e);
        }
    }

    public boolean delete(int id) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete multimedia", e);
        }
    }
}
