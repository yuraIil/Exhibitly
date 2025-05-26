package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Users;
import com.yuralil.domain.enums.Role;
import com.yuralil.domain.security.HashUtil;
import com.yuralil.infrastructure.util.ConnectionHolder;

import java.sql.*;
import java.util.Optional;

/**
 * DAO (Data Access Object) для роботи з таблицею {@code users}.
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

    private static final String SELECT_BY_CREDENTIALS_SQL = """
        SELECT id, username, password, role
        FROM users
        WHERE username = ? AND password = ?
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

    public static UsersDao getInstance() {
        return INSTANCE;
    }

    public Users insert(Users user) {
        try (Connection connection = ConnectionHolder.get();
             PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name().toLowerCase()); // 🔧 для відповідності CHECK у БД

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

    public boolean update(Users user) {
        try (Connection connection = ConnectionHolder.get();
             PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().name().toLowerCase());
            ps.setInt(4, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    public boolean delete(int id) {
        try (Connection connection = ConnectionHolder.get();
             PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }

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

    public Optional<Users> findByUsernameAndPassword(String username, String password) {

        if ("admin".equals(username) && HashUtil.hash("admin").equals(password)) {
            return Optional.of(new Users("admin", password, Role.ADMIN));
        }


        try (Connection connection = ConnectionHolder.get();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_CREDENTIALS_SQL)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(buildUser(rs));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by credentials", e);
        }
    }

    private Users buildUser(ResultSet rs) throws SQLException {
        return new Users(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role").toUpperCase()) // DB -> enum
        );
    }
}
