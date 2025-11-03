package org.billing.repo;

import org.billing.db.DatabaseConnection;

import org.billing.domain.SubscriberDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSubscriberRepository implements SubRepoBD {

    @Override
    public Optional<SubscriberDB> findByPhone(String phoneNumber) {
        return Optional.empty();
    }

    @Override
    public void save(SubscriberDB subscriber) {

    }

    @Override
    public List<SubscriberDB> findAll() {
        List<SubscriberDB> list = new ArrayList<>();

        String sql = "SELECT id, username, password_hash, email, role, blocked FROM subscribers";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SubscriberDB s = mapRowToSubscriber(rs);
                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Optional<SubscriberDB> findById(int id) {
        String sql = "SELECT id, username, password_hash, email, role, blocked FROM subscribers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToSubscriber(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<SubscriberDB> findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, email, role, blocked FROM subscribers WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToSubscriber(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void create(SubscriberDB subscriber) {
        String sql = "INSERT INTO subscribers (username, password_hash, email, role, blocked) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, subscriber.getUsername());
            stmt.setString(2, subscriber.getPasswordHash());
            stmt.setString(3, subscriber.getEmail());
            stmt.setString(4, subscriber.getRole());
            stmt.setBoolean(5, subscriber.isBlocked());

            stmt.executeUpdate();

            // Получаем сгенерированный ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    subscriber.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(SubscriberDB subscriber) {
        String sql = "UPDATE subscribers SET username=?, password_hash=?, email=?, role=?, blocked=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subscriber.getUsername());
            stmt.setString(2, subscriber.getPasswordHash());
            stmt.setString(3, subscriber.getEmail());
            stmt.setString(4, subscriber.getRole());
            stmt.setBoolean(5, subscriber.isBlocked());
            stmt.setInt(6, subscriber.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM subscribers WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SubscriberDB mapRowToSubscriber(ResultSet rs) throws SQLException {
        SubscriberDB s = new SubscriberDB();
        s.setId(rs.getInt("id"));
        s.setUsername(rs.getString("username"));
        s.setPasswordHash(rs.getString("password_hash"));
        s.setEmail(rs.getString("email"));
        s.setRole(rs.getString("role"));
        s.setBlocked(rs.getBoolean("blocked"));
        return s;
    }
}
