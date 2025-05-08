package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Category;
import com.yuralil.infrastructure.util.ConnectionHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    private static final String SELECT_ALL_SQL = """
        SELECT id, name, description
        FROM category
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
        try (Connection conn = ConnectionHolder.get();
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
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find category", e);
        }
    }

    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();

        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch categories", e);
        }

        return categories;
    }

    public boolean update(Category category) {
        try (Connection conn = ConnectionHolder.get();
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
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete category", e);
        }
    }
    public void initDefaults() {
        List<String> defaultNames = List.of("Painting", "Sculpture", "Photograph", "Document", "Artifact");

        for (String name : defaultNames) {
            if (!existsByName(name)) {
                insert(new Category(0, name, name + " category"));
            }
        }
    }

    private boolean existsByName(String name) {
        String sql = "SELECT 1 FROM category WHERE name = ?";
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check category existence", e);
        }
    }
    public Category findOrCreateByName(String name) {
        try (Connection conn = ConnectionHolder.get()) {
            // Шукаємо наявну категорію
            String selectSql = "SELECT id, name, description FROM category WHERE name = ?";
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new Category(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    );
                }
            }

            // Якщо не знайдено — створюємо нову
            String insertSql = "INSERT INTO category (name, description) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setString(2, name + " category");
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    return new Category(keys.getInt(1), name, name + " category");
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find or create category", e);
        }
    }


}
