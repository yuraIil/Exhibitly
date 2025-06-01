package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Users;
import com.yuralil.domain.security.HashUtil;
import com.yuralil.infrastructure.util.ConnectionHolder;

import java.sql.*;
import java.util.Optional;

/**
 * DAO для роботи з користувачами.
 * <p>
 * Підтримує операції додавання, оновлення, видалення, пошуку за ID або username,
 * а також ініціалізацію адміністратора та зміну пароля.
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

    private static final String SELECT_BY_USERNAME_SQL = """
        SELECT id, username, password, role
        FROM users
        WHERE username = ?
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

    private UsersDao() {}

    /**
     * Повертає єдиний інстанс {@link UsersDao}.
     *
     * @return інстанс DAO
     */
    public static UsersDao getInstance() {
        return INSTANCE;
    }

    /**
     * Додає нового користувача до бази.
     *
     * @param user користувач
     * @return користувач із згенерованим ID
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
     * Оновлює дані користувача.
     *
     * @param user об'єкт {@link Users}
     * @return {@code true}, якщо оновлення успішне
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
     * Видаляє користувача за ID.
     *
     * @param id ідентифікатор користувача
     * @return {@code true}, якщо видалення успішне
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
     * Пошук користувача за ID.
     *
     * @param id ідентифікатор
     * @return обгорнутий користувач або {@code Optional.empty()}
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
     * Пошук користувача за іменем користувача.
     *
     * @param username логін
     * @return обгорнутий користувач або {@code Optional.empty()}
     */
    public Optional<Users> findByUsername(String username) {
        try (Connection connection = ConnectionHolder.get();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_USERNAME_SQL)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(buildUser(rs));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by username", e);
        }
    }

    /**
     * Ініціалізує адміністратора, якщо він ще не існує.
     * Логін: {@code administrator}, пароль: {@code admin12345}.
     */
    public void initAdmin() {
        Optional<Users> adminOpt = findByUsername("administrator");
        if (adminOpt.isEmpty()) {
            Users admin = new Users();
            admin.setUsername("administrator");
            admin.setPassword(HashUtil.hash("admin12345"));
            admin.setRole("ADMIN");

            insert(admin);
            System.out.println("✅ Адміністратор створений: login=administrator, password=admin12345");
        }
    }

    /**
     * Оновлює лише пароль користувача.
     *
     * @param userId      ідентифікатор користувача
     * @param newPassword новий хешований пароль
     */
    public void updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при оновленні пароля", e);
        }
    }

    /**
     * Побудова об'єкта {@link Users} з {@link ResultSet}.
     *
     * @param rs результат запиту
     * @return об'єкт {@link Users}
     * @throws SQLException у випадку помилки
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
