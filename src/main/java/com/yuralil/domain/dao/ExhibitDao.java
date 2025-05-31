package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Category;
import com.yuralil.domain.entities.Exhibit;
import com.yuralil.domain.entities.Multimedia;
import com.yuralil.infrastructure.util.ConnectionHolder;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * DAO (Data Access Object) для роботи з таблицею {@code exhibit}.
 * Реалізує основні CRUD-операції для експонатів, а також пошук.
 */
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

    /**
     * Приватний конструктор для реалізації патерну Singleton.
     */
    private ExhibitDao() {}

    /**
     * Повертає єдиний екземпляр ExhibitDao.
     *
     * @return екземпляр ExhibitDao
     */
    public static ExhibitDao getInstance() {
        return INSTANCE;
    }

    /**
     * Додає новий експонат у базу даних.
     *
     * @param exhibit експонат, який потрібно зберегти
     * @return збережений експонат з оновленим ID
     */
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

    /**
     * Шукає експонат за ID.
     *
     * @param id унікальний ідентифікатор експоната
     * @return Optional з експонатом, якщо знайдено
     */
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

    /**
     * Повертає список усіх експонатів з бази даних.
     *
     * @return список експонатів
     */
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
    public Map<String, Integer> countByCategory() {
        String sql = """
        SELECT c.name AS category_name, COUNT(e.id) AS exhibit_count
        FROM category c
        LEFT JOIN exhibit e ON c.id = e.category_id
        GROUP BY c.name
        """;

        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String category = rs.getString("category_name");
                int count = rs.getInt("exhibit_count");
                result.put(category, count);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при підрахунку експонатів по категоріях", e);
        }

        return result;
    }


    /**
     * Оновлює наявний експонат у базі.
     *
     * @param exhibit оновлений об'єкт
     * @return {@code true}, якщо оновлення відбулося успішно
     */
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

    /**
     * Видаляє експонат з бази за ID.
     *
     * @param id ідентифікатор експоната
     * @return {@code true}, якщо видалення пройшло успішно
     */
    public boolean delete(int id) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete exhibit", e);
        }
    }

    /**
     * Побудова об'єкта Exhibit з ResultSet.
     *
     * @param rs результат SQL-запиту
     * @return об'єкт Exhibit
     * @throws SQLException якщо виникає помилка при зчитуванні
     */
    private Exhibit buildExhibit(ResultSet rs) throws SQLException {
        int multimediaId = rs.getInt("multimedia_id");
        Multimedia multimedia = MultimediaDao.getInstance().findById(multimediaId)
                .orElse(new Multimedia(multimediaId, null, null, null));

        int categoryId = rs.getInt("category_id");
        Category category = CategoryDao.getInstance().findById(categoryId)
                .orElse(new Category(categoryId, null, null));

        return new Exhibit(
                rs.getInt("id"),
                rs.getString("name"),
                category,
                rs.getString("description"),
                rs.getDate("acquisition_date").toLocalDate(),
                multimedia
        );
    }

    /**
     * Пошук експонатів за назвою або описом (часткове співпадіння).
     *
     * @param query ключове слово для пошуку
     * @return список знайдених експонатів
     */
    public List<Exhibit> searchByNameOrDescription(String query) {
        List<Exhibit> results = new ArrayList<>();
        String sql = """
        SELECT id, name, category_id, description, acquisition_date, multimedia_id
        FROM exhibit
        WHERE LOWER(name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?)
        """;

        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + query + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(buildExhibit(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to search exhibits", e);
        }

        return results;
    }
}
