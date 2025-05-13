package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Category;
import com.yuralil.infrastructure.util.ConnectionHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO (Data Access Object) для роботи з таблицею {@code category}.
 * Реалізує CRUD-операції для категорій.
 */
public class CategoryDao {

    private static final CategoryDao INSTANCE = new CategoryDao();

    private static final String INSERT_SQL = """
        INSERT INTO category (name, description)
        VALUES (?, ?)
        """;

    private static final String SELECT_ALL_SQL = """
        SELECT id, name, description FROM category
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT id, name, description FROM category WHERE id = ?
        """;

    /**
     * Приватний конструктор для синглтона.
     */
    private CategoryDao() {
    }

    /**
     * Повертає єдиний екземпляр DAO.
     *
     * @return екземпляр {@code CategoryDao}
     */
    public static CategoryDao getInstance() {
        return INSTANCE;
    }

    /**
     * Ініціалізує стандартні категорії, якщо вони ще не існують у базі.
     */
    public void initDefaults() {
        List<String> defaultNames = List.of(
                "Painting", "Sculpture", "Photograph", "Document", "Artifact",
                "Jewelry", "Textile", "Tool", "Weapon", "Other"
        );

        for (String name : defaultNames) {
            if (!existsByName(name)) {
                insert(new Category(0, name, name + " category"));
            }
        }
    }

    /**
     * Перевіряє, чи існує категорія з вказаною назвою.
     *
     * @param name назва категорії
     * @return {@code true} якщо існує, {@code false} інакше
     */
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

    /**
     * Додає нову категорію до бази даних.
     *
     * @param category об'єкт {@link Category}, який потрібно зберегти
     * @return збережений об'єкт з оновленим ID
     */
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

    /**
     * Повертає список усіх категорій з бази даних.
     *
     * @return список об'єктів {@link Category}
     */
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

    /**
     * Повертає категорію за її ID.
     *
     * @param id ідентифікатор категорії
     * @return {@link Optional} з об'єктом {@link Category}, якщо знайдено
     */
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
            throw new RuntimeException("Failed to find category by ID", e);
        }
    }
}
