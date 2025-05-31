package com.yuralil.domain.dao;

import com.yuralil.domain.entities.Report;
import com.yuralil.domain.enums.ReportType;
import com.yuralil.infrastructure.util.ConnectionHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private static final String SELECT_LATEST_BY_TYPE_SQL = """
        SELECT id, type, generated_at, content
        FROM report
        WHERE type = ?
        ORDER BY generated_at DESC
        LIMIT 1
        """;

    private static final String SELECT_ALL_SQL = """
        SELECT id, type, generated_at, content
        FROM report
        ORDER BY generated_at DESC
        """;

    private static final String DELETE_SQL = """
        DELETE FROM report
        WHERE id = ?
        """;

    private ReportDao() {}

    public static ReportDao getInstance() {
        return INSTANCE;
    }

    public Report insert(Report report) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setObject(1, report.getType(), Types.OTHER);
            ps.setTimestamp(2, Timestamp.valueOf(report.getGeneratedAt()));
            ps.setBytes(3, report.getContent());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                report.setId(keys.getInt(1));
            }

            return report;
        } catch (SQLException e) {
            throw new RuntimeException("❌ Failed to insert report", e);
        }
    }

    public Optional<Report> findById(int id) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(buildReport(rs));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Failed to find report by ID", e);
        }
    }

    public Optional<Report> findLatestByType(ReportType type) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(SELECT_LATEST_BY_TYPE_SQL)) {

            ps.setObject(1, type, Types.OTHER);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(buildReport(rs));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Failed to find latest report by type", e);
        }
    }

    public List<Report> findAll() {
        List<Report> reports = new ArrayList<>();

        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reports.add(buildReport(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("❌ Failed to load all reports", e);
        }

        return reports;
    }

    public boolean delete(int id) {
        try (Connection conn = ConnectionHolder.get();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("❌ Failed to delete report", e);
        }
    }

    private Report buildReport(ResultSet rs) throws SQLException {
        return new Report(
                rs.getInt("id"),
                ReportType.valueOf(rs.getString("type")),
                rs.getTimestamp("generated_at").toLocalDateTime(),
                rs.getBytes("content")
        );
    }
}
