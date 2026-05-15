package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tuyensinh.DTO.UserDTO;
import com.tuyensinh.config.HibernateUtil;

public class MainFrame extends JFrame {

    private UserDTO currentUser;
    private JPanel mainContent;
    private CardLayout cardLayout;

    public MainFrame(UserDTO user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {

        setTitle("Tuyển Sinh 2025");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(33, 43, 54));
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BorderLayout());

        // ===== LOGO =====
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(25, 32, 40));
        logoPanel.setPreferredSize(new Dimension(230, 80));

        JLabel lblLogo = new JLabel("TUYỂN SINH");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        logoPanel.add(lblLogo);

        sidebar.add(logoPanel, BorderLayout.NORTH);

        // ===== MENU =====
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(33, 43, 54));
        menuPanel.setLayout(new GridLayout(0, 1, 0, 8));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        String[] menus = {
                "Trang chủ",
                "Ngành",
                "Tổ hợp",
                "Ngành - Tổ hợp",
                "Thí sinh",
                "Điểm thi",
                "Điểm cộng",
                "Nguyện vọng",
                "Xét tuyển"
        };

        for (String m : menus) {

            JButton btn = new JButton(m);

            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setBackground(new Color(33, 43, 54));
            btn.setForeground(Color.WHITE);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.setPreferredSize(new Dimension(200, 45));

            // ===== HOVER =====
            btn.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(52, 73, 94));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(33, 43, 54));
                }
            });

            // ===== CHANGE PANEL =====
            btn.addActionListener(e -> cardLayout.show(mainContent, m));

            menuPanel.add(btn);
        }

        sidebar.add(menuPanel, BorderLayout.CENTER);

        // ===== TOPBAR =====
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setPreferredSize(new Dimension(0, 60));
        topbar.setBackground(Color.WHITE);
        topbar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblWelcome = new JLabel(
                "Xin chào, " + currentUser.getUsername()
        );

        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);

        btnLogout.addActionListener(e -> logout());

        topbar.add(lblWelcome, BorderLayout.WEST);
        topbar.add(btnLogout, BorderLayout.EAST);

        // ===== CONTENT =====
        cardLayout = new CardLayout();

        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(new Color(245, 246, 250));
        mainContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainContent.add(createHomePanel(), "Trang chủ");
        mainContent.add(new NganhPanel(), "Ngành");
        mainContent.add(new ToHopPanel(), "Tổ hợp");
        mainContent.add(new NganhToHopPanel(), "Ngành - Tổ hợp");
        mainContent.add(new ThiSinhPanel(), "Thí sinh");
        mainContent.add(new DiemThiPanel(), "Điểm thi");
        mainContent.add(new DiemCongPanel(), "Điểm cộng");
        mainContent.add(new NguyenVongPanel(), "Nguyện vọng");
        mainContent.add(new XetTuyenPanel(), "Xét tuyển");

        // ===== RIGHT PANEL =====
        JPanel rightPanel = new JPanel(new BorderLayout());

        rightPanel.add(topbar, BorderLayout.NORTH);
        rightPanel.add(mainContent, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // ===== HOME PANEL =====
    private JPanel createHomePanel() {

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new Color(245, 247, 250));
    panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    // ================= THỐNG KÊ TỪ DB =================

    int tongThiSinh = 0;
    int tongNganh = 0;
    int tongXetTuyen = 0;

    try {

        Connection conn = HibernateUtil
                .getSessionFactory()
                .openSession()
                .doReturningWork(c -> c);

        // ===== Tổng thí sinh =====
        String sqlThiSinh = "SELECT COUNT(*) FROM xt_thisinhxettuyen25";

        PreparedStatement ps1 = conn.prepareStatement(sqlThiSinh);

        ResultSet rs1 = ps1.executeQuery();

        if (rs1.next()) {
            tongThiSinh = rs1.getInt(1);
        }

        // ===== Tổng ngành =====
        String sqlNganh = "SELECT COUNT(*) FROM xt_nganh";

        PreparedStatement ps2 = conn.prepareStatement(sqlNganh);

        ResultSet rs2 = ps2.executeQuery();

        if (rs2.next()) {
            tongNganh = rs2.getInt(1);
        }

        // ===== Tổng hồ sơ xét tuyển =====
        String sqlXT = "SELECT COUNT(*) FROM xt_diemthixettuyen";

        PreparedStatement ps3 = conn.prepareStatement(sqlXT);

        ResultSet rs3 = ps3.executeQuery();

        if (rs3.next()) {
            tongXetTuyen = rs3.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    // ================= HEADER =================

    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);

    JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ TUYỂN SINH 2025");

    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
    lblTitle.setForeground(new Color(44, 62, 80));

    JLabel lblSub = new JLabel(
            "Chào mừng bạn đến với hệ thống quản lý tuyển sinh đại học"
    );

    lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 18));
    lblSub.setForeground(Color.GRAY);

    JPanel titleBox = new JPanel();

    titleBox.setOpaque(false);

    titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

    titleBox.add(lblTitle);
    titleBox.add(Box.createVerticalStrut(10));
    titleBox.add(lblSub);

    header.add(titleBox, BorderLayout.WEST);

    panel.add(header, BorderLayout.NORTH);

    // ================= CENTER =================

    JPanel center = new JPanel(new GridLayout(1, 3, 20, 20));

    center.setOpaque(false);

    center.setBorder(BorderFactory.createEmptyBorder(
            40,
            0,
            0,
            0
    ));

    center.add(createDashboardCard(
            " Tổng thí sinh",
            String.valueOf(tongThiSinh),
            new Color(52, 152, 219)
    ));

    center.add(createDashboardCard(
            " Tổng ngành",
            String.valueOf(tongNganh),
            new Color(46, 204, 113)
    ));

    center.add(createDashboardCard(
            " Hồ sơ xét tuyển",
            String.valueOf(tongXetTuyen),
            new Color(155, 89, 182)
    ));

    panel.add(center, BorderLayout.CENTER);

    // ================= FOOTER =================

    JLabel footer = new JLabel(
            "Admissions Management System © 2025",
            SwingConstants.CENTER
    );

    footer.setFont(new Font("Segoe UI", Font.PLAIN, 13));

    footer.setForeground(Color.GRAY);

    footer.setBorder(BorderFactory.createEmptyBorder(
            20,
            0,
            0,
            0
    ));

    panel.add(footer, BorderLayout.SOUTH);

    return panel;
}

/**
 * Dashboard Card
 */
private JPanel createDashboardCard(
        String title,
        String value,
        Color color
) {

    JPanel card = new JPanel(new BorderLayout());

    card.setBackground(Color.WHITE);

    card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
    ));

    JLabel lblTitle = new JLabel(title);

    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

    lblTitle.setForeground(new Color(80, 80, 80));

    JLabel lblValue = new JLabel(value);

    lblValue.setFont(new Font("Segoe UI", Font.BOLD, 42));

    lblValue.setForeground(color);

    JLabel lblDesc = new JLabel("Dữ liệu realtime từ database");

    lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

    lblDesc.setForeground(Color.GRAY);

    card.add(lblTitle, BorderLayout.NORTH);

    card.add(lblValue, BorderLayout.CENTER);

    card.add(lblDesc, BorderLayout.SOUTH);

    return card;
}

    // ===== LOGOUT =====
    private void logout() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có muốn đăng xuất?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm();
        }
    }
}