package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Category;
import com.yuralil.domain.entities.Exhibit;
import com.yuralil.domain.entities.Multimedia;
import com.yuralil.infrastructure.util.ConnectionHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExhibitDao {

    private static final ExhibitDao INSTANCE = new ExhibitDao();

    private static final String INSERT_SQL = """
        INSERT INTO exhibit (name, category_id, description, acquisition_date, multimedia_id)
        VALUES (?, ?, ?, ?, ?)
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT id, name, category_id, description, acquisition_date, multimedia_id
        FROM exhibit
        WHERE id = ?
        """;

    private static final String SELECT_ALL_SQL = """
        SELECT id, name, category_id, description, acquisition_date, multimedia_id
        FROM exhibit
        """;

    private static final String DELETE_SQL = """
        DELETE FROM exhibit
        WHERE id = ?
        """;

    private static final String UPDATE_SQL = """
        UPDATE exhibit
        SET name = ?, category_id = ?, description = ?, acquisition_date = ?, multimedia_id = ?
        WHERE id = ?
        """;

    private ExhibitDao() {}

    public static ExhibitDao getInstance() {
        return INSTANCE;
    }

    public Exhibit insert(Exhibit exhibit) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, exhibit.getName());
            ps.setInt(2, exhibit.getCategory().getId());
            ps.setString(3, exhibit.getDescription());
            ps.setDate(4, Date.valueOf(exhibit.getAcquisitionDate()));
            ps.setInt(5, exhibit.getMultimedia().getId());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                exhibit.setId(keys.getInt(1));
            }

            return exhibit;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert exhibit", e);
        }
    }

    public Optional<Exhibit> findById(int id) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Exhibit exhibit = buildExhibit(rs);
                return Optional.of(exhibit);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find exhibit", e);
        }
    }

    public List<Exhibit> findAll() {
        List<Exhibit> exhibits = new ArrayList<>();
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                exhibits.add(buildExhibit(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load all exhibits", e);
        }
        return exhibits;
    }

    public boolean update(Exhibit exhibit) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, exhibit.getName());
            ps.setInt(2, exhibit.getCategory().getId());
            ps.setString(3, exhibit.getDescription());
            ps.setDate(4, Date.valueOf(exhibit.getAcquisitionDate()));
            ps.setInt(5, exhibit.getMultimedia().getId());
            ps.setInt(6, exhibit.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update exhibit", e);
        }
    }

    public boolean delete(int id) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete exhibit", e);
        }
    }

    private Exhibit buildExhibit(ResultSet rs) throws SQLException {
        return new Exhibit(
                rs.getInt("id"),
                rs.getString("name"),
                new Category(rs.getInt("category_id"), null, null),
                rs.getString("description"),
                rs.getDate("acquisition_date").toLocalDate(),
                new Multimedia(rs.getInt("multimedia_id"), null, null, null)
        );
    }
}
