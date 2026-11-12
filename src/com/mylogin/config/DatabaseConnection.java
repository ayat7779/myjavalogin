package com.mylogin.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages JDBC Database Connection to MySQL with Auto Database & Table Initialization.
 */
public class DatabaseConnection {

    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_NAME = "mylogin_db";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "123!"; // Sesuaikan dengan password MySQL lokal Anda

    private static String lastErrorMessage = "";

    static {
        try {
            // Register MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Auto-initialize Database & Tables
            initDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver JDBC tidak ditemukan! Pastikan library mysql-connector-j ada dalam classpath.");
            e.printStackTrace();
        }
    }

    /**
     * Automatically creates database 'mylogin_db' and 'users' table if they do not exist.
     */
    public static void initDatabase() {
        // Step 1: Connect to MySQL Server (without database) and CREATE DATABASE if missing
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } catch (SQLException e) {
            lastErrorMessage = "Auto-Create Database Failed: " + e.getMessage();
            System.err.println(lastErrorMessage);
            return;
        }

        // Step 2: Connect to 'mylogin_db' and CREATE TABLE 'users' if missing
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "full_name VARCHAR(100) NOT NULL, "
                    + "username VARCHAR(50) NOT NULL UNIQUE, "
                    + "email VARCHAR(100) NOT NULL UNIQUE, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "status VARCHAR(20) NOT NULL DEFAULT 'PENDING', "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")";
            stmt.executeUpdate(createTableSQL);

            // Add 'status' column if migrating from an older table schema
            try {
                stmt.executeUpdate("ALTER TABLE users ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PENDING'");
            } catch (SQLException ignored) {
                // Column already exists
            }

            // Insert default admin user with status 'APPROVED'
            String insertAdminSQL = "INSERT IGNORE INTO users (full_name, username, email, password, status) VALUES ("
                    + "'Administrator', 'admin', 'admin@example.com', "
                    + "'240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'APPROVED')";
            stmt.executeUpdate(insertAdminSQL);

            // Ensure admin is always APPROVED
            stmt.executeUpdate("UPDATE users SET status = 'APPROVED' WHERE username = 'admin'");

        } catch (SQLException e) {
            lastErrorMessage = "Auto-Create Table Failed: " + e.getMessage();
            System.err.println(lastErrorMessage);
        }
    }

    /**
     * Gets a new connection to the MySQL database.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Tests whether the database connection is working.
     */
    public static boolean testConnection() {
        initDatabase();
        try (Connection conn = getConnection()) {
            lastErrorMessage = "";
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            lastErrorMessage = e.getClass().getSimpleName() + ": " + e.getMessage();
            System.err.println("Gagal terhubung ke MySQL Database: " + lastErrorMessage);
            return false;
        }
    }

    public static String getLastErrorMessage() {
        return lastErrorMessage;
    }
}
