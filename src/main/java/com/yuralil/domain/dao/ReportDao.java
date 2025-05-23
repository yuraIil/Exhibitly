package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Report;
import com.yuralil.infrastructure.util.ConnectionHolder;

import java.sql.*;
import java.util.Optional;

/**
 * DAO (Data Access Object) для роботи з таблицею {@code report}.
 * Дозволяє виконувати CRUD-операції для об'єктів звітів.
 */
public class ReportDao {

    private static final ReportDao INSTANCE = new ReportDao();

    private static final String INSERT_SQL = """
        INSERT INTO report (type, generated_at, content)
        VALUES (?, ?, ?)
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT id, type, generated_at, content
        FROM report
        WHERE id = ?
        """;

    private static final String UPDATE_SQL = """
        UPDATE report
        SET type = ?, generated_at = ?, content = ?
        WHERE id = ?
        """;

    private static final String DELETE_SQL = """
        DELETE FROM report
        WHERE id = ?
        """;

    /**
     * Приватний конструктор для реалізації патерну Singleton.
     */
    private ReportDao() {}

    /**
     * Повертає єдиний екземпляр {@code ReportDao}.
     *
     * @return екземпляр ReportDao
     */
    public static ReportDao getInstance() {
        return INSTANCE;
    }

    /**
     * Додає новий звіт до бази даних.
     *
     * @param report обʼєкт {@link Report}, який потрібно зберегти
     * @return збережений звіт з оновленим ID
     */
    public Report insert(Report report) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, report.getType());
            ps.setTimestamp(2, Timestamp.valueOf(report.getGeneratedAt()));
            ps.setString(3, report.getContent());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                report.setId(keys.getInt(1));
            }

            return report;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert report", e);
        }
    }

    /**
     * Знаходить звіт за його ID.
     *
     * @param id ідентифікатор звіту
     * @return {@link Optional} з об'єктом {@link Report}, якщо знайдено
     */
    public Optional<Report> findById(int id) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Report report = new Report(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getTimestamp("generated_at").toLocalDateTime(),
                        rs.getString("content")
                );
                return Optional.of(report);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find report", e);
        }
    }

    /**
     * Оновлює звіт у базі даних.
     *
     * @param report обʼєкт {@link Report} з оновленими даними
     * @return {@code true}, якщо оновлення пройшло успішно
     */
    public boolean update(Report report) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, report.getType());
            ps.setTimestamp(2, Timestamp.valueOf(report.getGeneratedAt()));
            ps.setString(3, report.getContent());
            ps.setInt(4, report.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update report", e);
        }
    }

    /**
     * Видаляє звіт за його ID.
     *
     * @param id ідентифікатор звіту
     * @return {@code true}, якщо видалення пройшло успішно
     */
    public boolean delete(int id) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete report", e);
        }
    }
}
