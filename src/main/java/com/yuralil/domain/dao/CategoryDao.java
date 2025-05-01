package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Category;
import com.yuralil.infrastructure.until.ConnectionManager;

import java.sql.*;
import java.util.Optional;

public class CategoryDao {

    private static final CategoryDao INSTANCE = new CategoryDao();

    private static final String INSERT_SQL = """
        INSERT INTO category (name, description)
        VALUES (?, ?)
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT id, name, description
        FROM category
        WHERE id = ?
        """;

    private static final String UPDATE_SQL = """
        UPDATE category
        SET name = ?, description = ?
        WHERE id = ?
        """;

    private static final String DELETE_SQL = """
        DELETE FROM category
        WHERE id = ?
        """;

    private CategoryDao() {}

    public static CategoryDao getInstance() {
        return INSTANCE;
    }

    public Category insert(Category category) {
        try (Connection conn = ConnectionManager.open();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                category.setId(keys.getInt(1));
            }

            return category;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert category", e);
        }
    }

    public Optional<Category> findById(int id) {
        try (Connection conn = ConnectionManager.open();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Category category = new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                return Optional.of(category);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find category", e);
        }
    }

    public boolean update(Category category) {
        try (Connection conn = ConnectionManager.open();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update category", e);
        }
    }

    public boolean delete(int id) {
        try (Connection conn = ConnectionManager.open();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete category", e);
        }
    }
}
