package com.mylogin.ui;

import com.mylogin.model.User;
import com.mylogin.service.AuthService;
import com.mylogin.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Main Application Dashboard UI with Admin Approval Management panel.
 */
public class DashboardFrame extends JFrame {
    private final User currentUser;
    private DefaultTableModel tableModel;
    private JTable userTable;
    private List<User> userListCache;

    public DashboardFrame(User user) {
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("MyLogin - Dashboard (" + (currentUser.isAdmin() ? "ADMIN" : "USER") + ")");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header Panel (Top Bar)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIUtils.COLOR_PRIMARY);
        topBar.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel lblBrand = new JLabel("MyLogin Dashboard " + (currentUser.isAdmin() ? "[ADMIN PANEL]" : ""));
        lblBrand.setFont(UIUtils.FONT_TITLE);
        lblBrand.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(UIUtils.FONT_BUTTON);
        btnLogout.setForeground(UIUtils.COLOR_PRIMARY);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> handleLogout());

        topBar.add(lblBrand, BorderLayout.WEST);
        topBar.add(btnLogout, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        if (currentUser.isAdmin()) {
            // JTabbedPane for Admin
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(UIUtils.FONT_LABEL);
            tabbedPane.addTab("Kelola Persetujuan Akun", createAdminApprovalPanel());
            tabbedPane.addTab("Profil Saya", createProfilePanel());
            add(tabbedPane, BorderLayout.CENTER);
        } else {
            // Standard Profile view for Normal Users
            add(createProfilePanel(), BorderLayout.CENTER);
        }
    }

    private JPanel createProfilePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(32, 32, 32, 32));

        // Welcome Card
        JPanel cardWelcome = new JPanel(new BorderLayout());
        cardWelcome.setBackground(UIUtils.COLOR_CARD_BG);
        cardWelcome.setBorder(new EmptyBorder(24, 24, 24, 24));
        cardWelcome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        cardWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblWelcome = new JLabel("Selamat Datang, " + currentUser.getFullName() + "!");
        lblWelcome.setFont(UIUtils.FONT_TITLE);
        lblWelcome.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        JLabel lblSub = new JLabel("Status Akun Anda: " + currentUser.getStatus() + " | Role: " + (currentUser.isAdmin() ? "Admin" : "Pengguna Biasa"));
        lblSub.setFont(UIUtils.FONT_SUBTITLE);
        lblSub.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JPanel textGroup = new JPanel();
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));
        textGroup.setOpaque(false);
        textGroup.add(lblWelcome);
        textGroup.add(Box.createRigidArea(new Dimension(0, 4)));
        textGroup.add(lblSub);

        cardWelcome.add(textGroup, BorderLayout.WEST);

        // User Details Card
        JPanel cardInfo = new JPanel();
        cardInfo.setLayout(new GridLayout(4, 2, 12, 12));
        cardInfo.setBackground(UIUtils.COLOR_CARD_BG);
        cardInfo.setBorder(new EmptyBorder(24, 24, 24, 24));
        cardInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        addDetailRow(cardInfo, "Nama Lengkap:", currentUser.getFullName());
        addDetailRow(cardInfo, "Username:", currentUser.getUsername());
        addDetailRow(cardInfo, "Email:", currentUser.getEmail());
        addDetailRow(cardInfo, "Status Persetujuan:", currentUser.getStatus());

        mainPanel.add(cardWelcome);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        mainPanel.add(cardInfo);

        return mainPanel;
    }

    private JPanel createAdminApprovalPanel() {
        JPanel adminPanel = new JPanel(new BorderLayout(16, 16));
        adminPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        adminPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Title Header
        JLabel lblHeader = new JLabel("Daftar Pengguna & Persetujuan Akun");
        lblHeader.setFont(UIUtils.FONT_TITLE);
        lblHeader.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        // Table Setup
        String[] columnNames = {"No", "Nama Lengkap", "Username", "Email", "Status Persetujuan"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };

        userTable = new JTable(tableModel);
        userTable.setFont(UIUtils.FONT_INPUT);
        userTable.setRowHeight(30);
        userTable.getTableHeader().setFont(UIUtils.FONT_LABEL);
        userTable.getTableHeader().setBackground(UIUtils.COLOR_PRIMARY);
        userTable.getTableHeader().setForeground(Color.WHITE);

        // Center align status column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        userTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(userTable);

        // Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        JButton btnApprove = new JButton("Setujui (Approve)");
        btnApprove.setFont(UIUtils.FONT_BUTTON);
        btnApprove.setBackground(UIUtils.COLOR_SUCCESS);
        btnApprove.setForeground(Color.WHITE);
        btnApprove.setFocusPainted(false);
        btnApprove.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnApprove.addActionListener(e -> handleApproveUser());

        JButton btnReject = new JButton("Tolak (Reject)");
        btnReject.setFont(UIUtils.FONT_BUTTON);
        btnReject.setBackground(UIUtils.COLOR_ERROR);
        btnReject.setForeground(Color.WHITE);
        btnReject.setFocusPainted(false);
        btnReject.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReject.addActionListener(e -> handleRejectUser());

        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setFont(UIUtils.FONT_BUTTON);
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.setForeground(UIUtils.COLOR_PRIMARY);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadUserData());

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnReject);
        buttonPanel.add(btnApprove);

        adminPanel.add(lblHeader, BorderLayout.NORTH);
        adminPanel.add(scrollPane, BorderLayout.CENTER);
        adminPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load data on init
        loadUserData();

        return adminPanel;
    }

    private void loadUserData() {
        tableModel.setRowCount(0);
        AuthService authService = AuthService.getInstance();
        userListCache = authService.getAllUsers();

        int index = 1;
        for (User u : userListCache) {
            tableModel.addRow(new Object[]{
                    index++,
                    u.getFullName(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getStatus()
            });
        }
    }

    private void handleApproveUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih pengguna dari tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User selectedUser = userListCache.get(selectedRow);
        if (selectedUser.isApproved()) {
            JOptionPane.showMessageDialog(this, "Akun ini sudah dalam status APPROVED.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin MENYETUJUI akun " + selectedUser.getUsername() + "?",
                "Konfirmasi Persetujuan",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = AuthService.getInstance().updateUserStatus(selectedUser.getUsername(), User.STATUS_APPROVED);
            if (success) {
                JOptionPane.showMessageDialog(this, "Akun " + selectedUser.getUsername() + " berhasil disetujui (APPROVED)!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate status akun.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRejectUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih pengguna dari tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User selectedUser = userListCache.get(selectedRow);

        if (selectedUser.isAdmin()) {
            JOptionPane.showMessageDialog(this, "Tidak dapat menolak akun Administrator utama!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin MENOLAK akun " + selectedUser.getUsername() + "?",
                "Konfirmasi Penolakan",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = AuthService.getInstance().updateUserStatus(selectedUser.getUsername(), User.STATUS_REJECTED);
            if (success) {
                JOptionPane.showMessageDialog(this, "Akun " + selectedUser.getUsername() + " telah ditolak (REJECTED).", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate status akun.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addDetailRow(JPanel container, String labelStr, String valueStr) {
        JLabel lblName = new JLabel(labelStr);
        lblName.setFont(UIUtils.FONT_LABEL);
        lblName.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblVal = new JLabel(valueStr);
        lblVal.setFont(UIUtils.FONT_INPUT);
        lblVal.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        container.add(lblName);
        container.add(lblVal);
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin keluar?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
