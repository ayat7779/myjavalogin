package com.mylogin.ui;

import com.mylogin.service.AuthService;
import com.mylogin.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dialog window for User Registration.
 */
public class RegisterDialog extends JDialog {
    private JTextField txtFullName;
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JLabel lblError;

    public RegisterDialog(Frame owner) {
        super(owner, "Register New Account", true);
        initUI();
    }

    private void initUI() {
        setSize(420, 560);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIUtils.COLOR_CARD_BG);
        contentPanel.setBorder(new EmptyBorder(24, 28, 24, 28));

        // Header
        JLabel lblTitle = new JLabel("Daftar Akun Baru");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Lengkapi data di bawah untuk membuat akun baru.");
        lblSub.setFont(UIUtils.FONT_SUBTITLE);
        lblSub.setForeground(UIUtils.COLOR_TEXT_MUTED);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Form Fields
        txtFullName = UIUtils.createStyledTextField("Contoh: John Doe");
        txtUsername = UIUtils.createStyledTextField("Contoh: johndoe");
        txtEmail = UIUtils.createStyledTextField("Contoh: john@example.com");
        txtPassword = UIUtils.createStyledPasswordField("Minimal 6 karakter");
        txtConfirmPassword = UIUtils.createStyledPasswordField("Ulangi password di atas");

        lblError = new JLabel(" ");
        lblError.setFont(UIUtils.FONT_SMALL);
        lblError.setForeground(UIUtils.COLOR_ERROR);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnRegister = UIUtils.createPrimaryButton("Daftar Sekarang");
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnRegister.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegister.addActionListener(e -> handleRegister());

        // Assembly
        contentPanel.add(lblTitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        contentPanel.add(lblSub);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        addFormField(contentPanel, "Nama Lengkap", txtFullName);
        addFormField(contentPanel, "Username", txtUsername);
        addFormField(contentPanel, "Email", txtEmail);
        addFormField(contentPanel, "Password", txtPassword);
        addFormField(contentPanel, "Konfirmasi Password", txtConfirmPassword);

        contentPanel.add(lblError);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        contentPanel.add(btnRegister);

        add(contentPanel);
    }

    private void addFormField(JPanel parent, String labelText, JComponent inputField) {
        JLabel label = new JLabel(labelText);
        label.setFont(UIUtils.FONT_LABEL);
        label.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (inputField instanceof JTextField) {
            inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        }

        parent.add(label);
        parent.add(Box.createRigidArea(new Dimension(0, 4)));
        parent.add(inputField);
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void handleRegister() {
        String fullName = txtFullName.getText().trim();
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            lblError.setText("Semua field harus diisi!");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            lblError.setText("Format email tidak valid!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            lblError.setText("Konfirmasi password tidak cocok!");
            return;
        }

        if (password.length() < 6) {
            lblError.setText("Password minimal 6 karakter!");
            return;
        }

        AuthService authService = AuthService.getInstance();
        if (authService.isUsernameTaken(username)) {
            lblError.setText("Username sudah digunakan. Pilih username lain.");
            return;
        }

        boolean success = authService.register(fullName, username, email, password);
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Registrasi berhasil!\nAkun Anda sedang menunggu persetujuan (approval) dari Admin sebelum dapat digunakan.",
                    "Registrasi Sukses (Menunggu Approval)",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            lblError.setText("Gagal mendaftarkan akun.");
        }
    }
}
