package com.mylogin.ui;

import com.mylogin.model.LoginResponse;
import com.mylogin.model.User;
import com.mylogin.service.AuthService;
import com.mylogin.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Main Login Window.
 */
public class LoginFrame extends JFrame {
    private JTextField txtUsernameOrEmail;
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;
    private JLabel lblError;

    public LoginFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("MyLogin - Sign In");
        setSize(440, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background Wrapper
        JPanel bgPanel = new JPanel(new GridBagLayout());
        bgPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        // Login Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(UIUtils.COLOR_CARD_BG);
        cardPanel.setBorder(new EmptyBorder(32, 32, 32, 32));
        cardPanel.setPreferredSize(new Dimension(380, 480));

        // App Header
        JLabel lblTitle = new JLabel("Selamat Datang");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Masukkan kredensial akun Anda untuk masuk.");
        lblSubtitle.setFont(UIUtils.FONT_SUBTITLE);
        lblSubtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Inputs
        txtUsernameOrEmail = UIUtils.createStyledTextField("Masukkan username atau email");
        txtUsernameOrEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsernameOrEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        txtPassword = UIUtils.createStyledPasswordField("Masukkan password Anda");
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // KeyListener to trigger login on Enter key
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        txtUsernameOrEmail.addKeyListener(enterKeyAdapter);
        txtPassword.addKeyListener(enterKeyAdapter);

        // Show/Hide Password Checkbox
        chkShowPassword = new JCheckBox("Tampilkan Password");
        chkShowPassword.setFont(UIUtils.FONT_SMALL);
        chkShowPassword.setForeground(UIUtils.COLOR_TEXT_MUTED);
        chkShowPassword.setBackground(UIUtils.COLOR_CARD_BG);
        chkShowPassword.setFocusPainted(false);
        chkShowPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });

        // Error Message Label
        lblError = new JLabel(" ");
        lblError.setFont(UIUtils.FONT_SMALL);
        lblError.setForeground(UIUtils.COLOR_ERROR);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Login Button
        JButton btnLogin = UIUtils.createPrimaryButton("Masuk");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> handleLogin());

        // Register Action Link
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        registerPanel.setBackground(UIUtils.COLOR_CARD_BG);
        registerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNoAccount = new JLabel("Belum punya akun?");
        lblNoAccount.setFont(UIUtils.FONT_SMALL);
        lblNoAccount.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JButton btnRegister = UIUtils.createLinkButton("Daftar Akun");
        btnRegister.addActionListener(e -> {
            RegisterDialog dialog = new RegisterDialog(this);
            dialog.setVisible(true);
        });

        registerPanel.add(lblNoAccount);
        registerPanel.add(btnRegister);

        // Demo Account Box
        JPanel demoBox = new JPanel();
        demoBox.setLayout(new BoxLayout(demoBox, BoxLayout.Y_AXIS));
        demoBox.setBackground(new Color(243, 244, 246));
        demoBox.setBorder(new EmptyBorder(8, 12, 8, 12));
        demoBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDemoHeader = new JLabel("Akun Demo:");
        lblDemoHeader.setFont(UIUtils.FONT_LABEL);
        lblDemoHeader.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        JLabel lblDemoDetails = new JLabel("Username: admin | Password: admin123");
        lblDemoDetails.setFont(UIUtils.FONT_SMALL);
        lblDemoDetails.setForeground(UIUtils.COLOR_TEXT_MUTED);

        demoBox.add(lblDemoHeader);
        demoBox.add(lblDemoDetails);

        // Assembly
        cardPanel.add(lblTitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        cardPanel.add(lblSubtitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        addFormField(cardPanel, "Username / Email", txtUsernameOrEmail);
        addFormField(cardPanel, "Password", txtPassword);

        cardPanel.add(chkShowPassword);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(lblError);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        cardPanel.add(btnLogin);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        cardPanel.add(registerPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        cardPanel.add(demoBox);

        bgPanel.add(cardPanel);
        add(bgPanel);
    }

    private void addFormField(JPanel parent, String labelText, JComponent inputField) {
        JLabel label = new JLabel(labelText);
        label.setFont(UIUtils.FONT_LABEL);
        label.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        parent.add(label);
        parent.add(Box.createRigidArea(new Dimension(0, 4)));
        parent.add(inputField);
        parent.add(Box.createRigidArea(new Dimension(0, 12)));
    }

    private void handleLogin() {
        String usernameOrEmail = txtUsernameOrEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            lblError.setText("Username/Email dan Password wajib diisi.");
            return;
        }

        AuthService authService = AuthService.getInstance();
        LoginResponse response = authService.loginResponse(usernameOrEmail, password);

        if (response.getResultType() == LoginResponse.ResultType.SUCCESS) {
            lblError.setText(" ");
            dispose();
            new DashboardFrame(response.getUser()).setVisible(true);
        } else {
            lblError.setText(response.getMessage());
        }
    }
}
