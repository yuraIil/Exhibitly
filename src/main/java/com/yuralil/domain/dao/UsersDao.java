package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Users;
import com.yuralil.domain.security.HashUtil;
import com.yuralil.infrastructure.util.ConnectionHolder;

import java.sql.*;
import java.util.Optional;

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

    public static UsersDao getInstance() {
        return INSTANCE;
    }

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

    public void initAdmin() {
        Optional<Users> adminOpt = findByUsername("administrator");
        if (adminOpt.isEmpty()) {
            Users admin = new Users();
            admin.setUsername("administrator");
            admin.setPassword(HashUtil.hash("admin12345")); // пароль у хеші
            admin.setRole("ADMIN");

            insert(admin);
            System.out.println("✅ Адміністратор створений: login=administrator, password=admin12345");
        }
    }

    private Users buildUser(ResultSet rs) throws SQLException {
        return new Users(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role")
        );
    }
}
