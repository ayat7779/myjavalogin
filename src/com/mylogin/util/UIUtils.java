package com.mylogin.util;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Utility class for UI colors, fonts, and custom FlatLaf components styling.
 */
public class UIUtils {
    // Theme Colors (Modern Indigo & Neutral Palette)
    public static final Color COLOR_PRIMARY = new Color(79, 70, 229);      // #4F46E5 Indigo
    public static final Color COLOR_PRIMARY_HOVER = new Color(67, 56, 202); // #4338CA Darker Indigo
    public static final Color COLOR_BACKGROUND = new Color(243, 244, 246);  // #F3F4F6 Light Gray
    public static final Color COLOR_CARD_BG = new Color(255, 255, 255);     // #FFFFFF White
    public static final Color COLOR_TEXT_PRIMARY = new Color(17, 24, 39);   // #111827 Dark Gray
    public static final Color COLOR_TEXT_MUTED = new Color(107, 114, 128);  // #6B7280 Muted Gray
    public static final Color COLOR_BORDER = new Color(209, 213, 219);      // #D1D5DB Light Border
    public static final Color COLOR_ERROR = new Color(220, 38, 38);         // #DC2626 Red
    public static final Color COLOR_SUCCESS = new Color(16, 185, 129);      // #10B981 Green

    // Standard Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    /**
     * Creates a styled text field with placeholder text and FlatLaf properties.
     */
    public static JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField();
        styleInputField(textField);
        if (placeholder != null) {
            textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        }
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        return textField;
    }

    public static JTextField createStyledTextField() {
        return createStyledTextField(null);
    }

    /**
     * Creates a styled password field with placeholder text and FlatLaf properties.
     */
    public static JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField();
        styleInputField(passwordField);
        if (placeholder != null) {
            passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        }
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        return passwordField;
    }

    public static JPasswordField createStyledPasswordField() {
        return createStyledPasswordField(null);
    }

    private static void styleInputField(JComponent field) {
        field.setFont(FONT_INPUT);
        field.setForeground(COLOR_TEXT_PRIMARY);
        field.setBackground(Color.WHITE);
        field.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 6,10,6,10");
    }

    /**
     * Creates a primary styled button.
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setForeground(Color.WHITE);
        button.setBackground(COLOR_PRIMARY);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PRIMARY_HOVER);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PRIMARY);
            }
        });

        return button;
    }

    /**
     * Creates a link-styled button.
     */
    public static JButton createLinkButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_SMALL);
        button.setForeground(COLOR_PRIMARY);
        button.setBackground(COLOR_CARD_BG);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
