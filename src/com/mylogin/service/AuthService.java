package com.mylogin.service;

import com.mylogin.config.DatabaseConnection;
import com.mylogin.model.LoginResponse;
import com.mylogin.model.User;
import com.mylogin.util.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to handle authentication, registration, and admin approval using MySQL.
 */
public class AuthService {
    private static AuthService instance;

    private AuthService() {
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Authenticate user by username/email and password against MySQL database.
     */
    public LoginResponse loginResponse(String usernameOrEmail, String password) {
        if (usernameOrEmail == null || password == null || usernameOrEmail.trim().isEmpty() || password.isEmpty()) {
            return new LoginResponse(LoginResponse.ResultType.INVALID_CREDENTIALS, null, "Username/Email dan Password wajib diisi.");
        }

        String sql = "SELECT full_name, username, email, password, status FROM users WHERE LOWER(username) = LOWER(?) OR LOWER(email) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String targetKey = usernameOrEmail.trim();
            stmt.setString(1, targetKey);
            stmt.setString(2, targetKey);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String fullName = rs.getString("full_name");
                    String dbUsername = rs.getString("username");
                    String dbEmail = rs.getString("email");
                    String storedPasswordHash = rs.getString("password");
                    String status = rs.getString("status");

                    // Verify password
                    if (PasswordUtils.verifyPassword(password, storedPasswordHash) || password.equals(storedPasswordHash)) {
                        User user = new User(fullName, dbUsername, dbEmail, storedPasswordHash, status);

                        if (User.STATUS_PENDING.equalsIgnoreCase(status)) {
                            return new LoginResponse(LoginResponse.ResultType.PENDING_APPROVAL, null,
                                    "Akun Anda sedang menunggu persetujuan (approval) dari Admin.");
                        } else if (User.STATUS_REJECTED.equalsIgnoreCase(status)) {
                            return new LoginResponse(LoginResponse.ResultType.REJECTED, null,
                                    "Akun Anda ditolak oleh Admin dan tidak dapat digunakan.");
                        } else {
                            return new LoginResponse(LoginResponse.ResultType.SUCCESS, user, "Login berhasil!");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error (Login): " + e.getMessage());
            e.printStackTrace();
            return new LoginResponse(LoginResponse.ResultType.DATABASE_ERROR, null, "Gagal terhubung ke database!");
        }

        return new LoginResponse(LoginResponse.ResultType.INVALID_CREDENTIALS, null, "Username/Email atau Password salah!");
    }

    /**
     * Register a new user into MySQL database with status 'PENDING'.
     */
    public boolean register(String fullName, String username, String email, String password) {
        if (username == null || isUsernameTaken(username)) {
            return false;
        }

        String sql = "INSERT INTO users (full_name, username, email, password, status) VALUES (?, ?, ?, ?, 'PENDING')";
        String passwordHash = PasswordUtils.hashPassword(password);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName.trim());
            stmt.setString(2, username.trim());
            stmt.setString(3, email.trim());
            stmt.setString(4, passwordHash);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database Error (Register): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if username already exists in MySQL database.
     */
    public boolean isUsernameTaken(String username) {
        if (username == null || username.trim().isEmpty()) return false;

        String sql = "SELECT COUNT(*) FROM users WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error (Check Username): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves all users from the database.
     */
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT full_name, username, email, password, status FROM users ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                userList.add(new User(
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database Error (Get All Users): " + e.getMessage());
        }

        return userList;
    }

    /**
     * Updates user status ('APPROVED' or 'REJECTED').
     */
    public boolean updateUserStatus(String username, String newStatus) {
        if (username == null || newStatus == null) return false;

        String sql = "UPDATE users SET status = ? WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus.toUpperCase());
            stmt.setString(2, username.trim());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database Error (Update User Status): " + e.getMessage());
            return false;
        }
    }
}
