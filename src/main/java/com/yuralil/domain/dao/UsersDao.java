package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Users;
import com.yuralil.infrastructure.util.ConnectionHolder;

import java.sql.*;
import java.util.Optional;

/**
 * DAO (Data Access Object) для роботи з таблицею {@code users}.
 * Дозволяє виконувати базові CRUD-операції над обліковими записами користувачів.
 */
public class UsersDao {

    private static final UsersDao INSTANCE = new UsersDao();

    private static final String INSERT_SQL = """
        INSERT INTO users (username, password, role)
        VALUES (?, ?, ?)
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT id, username, password, role
        FROM users
        WHERE id = ?
        """;

    private static final String DELETE_SQL = """
        DELETE FROM users
        WHERE id = ?
        """;

    private static final String UPDATE_SQL = """
        UPDATE users
        SET username = ?, password = ?, role = ?
        WHERE id = ?
        """;

    /**
     * Приватний конструктор для реалізації патерну Singleton.
     */
    private UsersDao() {}

    /**
     * Повертає єдиний екземпляр {@code UsersDao}.
     *
     * @return екземпляр UsersDao
     */
    public static UsersDao getInstance() {
        return INSTANCE;
    }

    /**
     * Додає нового користувача до бази даних.
     *
     * @param user обʼєкт {@link Users}, який потрібно зберегти
     * @return збережений користувач з оновленим ID
     */
    public Users insert(Users user) {
        try (Connection connection = ConnectionHolder.get();
             PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getInt(1));
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert user", e);
        }
    }

    /**
     * Оновлює існуючого користувача у базі даних.
     *
     * @param user обʼєкт {@link Users} з оновленими полями
     * @return {@code true}, якщо оновлення пройшло успішно
     */
    public boolean update(Users user) {
        try (Connection connection = ConnectionHolder.get();
             PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setInt(4, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    /**
     * Видаляє користувача з бази за ID.
     *
     * @param id унікальний ідентифікатор користувача
     * @return {@code true}, якщо видалення пройшло успішно
     */
    public boolean delete(int id) {
        try (Connection connection = ConnectionHolder.get();
             PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    /**
     * Шукає користувача за його ID.
     *
     * @param id унікальний ідентифікатор
     * @return {@link Optional} з обʼєктом {@link Users}, якщо знайдено
     */
    public Optional<Users> findById(int id) {
        try (Connection connection = ConnectionHolder.get();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(buildUser(rs));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by id", e);
        }
    }

    /**
     * Створює обʼєкт {@link Users} з даних ResultSet.
     *
     * @param rs результат виконаного SQL-запиту
     * @return обʼєкт {@link Users}
     * @throws SQLException якщо виникла помилка при читанні з ResultSet
     */
    private Users buildUser(ResultSet rs) throws SQLException {
        return new Users(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role")
        );
    }
}
