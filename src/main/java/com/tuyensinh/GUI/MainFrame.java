package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tuyensinh.DTO.UserDTO;

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

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());

        JLabel lbl = new JLabel(
                "<html><h1>HỆ THỐNG TUYỂN SINH 2025</h1>"
                        + "<p>Chào mừng bạn đến với hệ thống quản lý tuyển sinh.</p></html>"
        );

        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lbl.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(lbl, BorderLayout.NORTH);

        return panel;
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