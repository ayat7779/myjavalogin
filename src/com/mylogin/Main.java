package com.mylogin;

import com.mylogin.config.DatabaseConnection;
import com.mylogin.ui.LoginFrame;
import javax.swing.*;

/**
 * Entry point for the MyLogin Swing Application powered by FlatLaf UI Framework and MySQL.
 */
public class Main {
    public static void main(String[] args) {
        // Initialize FlatLaf Modern Look and Feel UI Framework
        try {
            com.formdev.flatlaf.FlatIntelliJLaf.setup();
            // Global UI rounded corner customization
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ProgressBar.arc", 10);
        } catch (Exception e) {
            // Fallback to Nimbus or System LookAndFeel if FlatLaf is missing
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) {
            }
        }

        // Check MySQL Database Connection asynchronously on startup
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);

            // Check connection in background
            new Thread(() -> {
                boolean connected = DatabaseConnection.testConnection();
                if (!connected) {
                    String errorMsg = DatabaseConnection.getLastErrorMessage();
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(loginFrame,
                                "Gagal terhubung ke database MySQL!\n\n" +
                                "Detail Error:\n" + errorMsg + "\n\n" +
                                "Langkah Penyelesaian:\n" +
                                "1. Pastikan Service MySQL (misal: XAMPP / Laragon) sudah dijalankan.\n" +
                                "2. Buat database 'mylogin_db' via phpMyAdmin / SQL tool.",
                                "Koneksi Database Gagal",
                                JOptionPane.WARNING_MESSAGE);
                    });
                }
            }).start();
        });
    }
}
