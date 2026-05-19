package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.tuyensinh.DTO.UserDTO;

public class Dashboard extends JFrame {

    private UserDTO currentUser;

    public Dashboard(UserDTO user) {
        this.currentUser = user;
        setupUITheme(); // Tùy chỉnh theme cơ bản cho tab
        initUI();
    }

    private void setupUITheme() {
        // Tối giản hóa giao diện của JTabbedPane
        UIManager.put("TabbedPane.font", new Font("Segoe UI", Font.PLAIN, 15));
        UIManager.put("TabbedPane.tabInsets", new Insets(10, 20, 10, 20));
        UIManager.put("TabbedPane.selected", new Color(220, 235, 252)); // Màu xanh nhạt khi chọn tab
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
    }

    private void initUI() {
        setTitle("Hệ Thống Quản Lý Xét Tuyển");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. Tạo thanh Header (Phần đầu trang) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185)); // Màu xanh dương hiện đại và dịu mắt
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("HỆ THỐNG XÉT TUYỂN 2026");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("👤 Xin chào, " + currentUser.getUsername());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Tạo khu vực Tabs (Nội dung chính) ---
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFocusable(false); // Bỏ khung viền nét đứt (focus ring) khi click vào tab
        tabs.setBackground(Color.WHITE);

        // Thêm các panel chức năng
        tabs.addTab("👨‍🎓 Thí Sinh", new ThiSinhPanel());
        tabs.addTab("📥 Import Excel điểm", new ImprtExDiem());
        tabs.addTab("📥 Import Excel điểm cộng", new ImprtExDiemCong());

        // Phân quyền: Chỉ ADMIN mới thấy tab User
        if (currentUser.getRole() == UserDTO.Role.ADMIN) {
            tabs.addTab("🛡️ Quản lý User", new UserPanel());
        }

        // Bọc tabs trong một panel để tạo khoảng trống (padding) với viền cửa sổ
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245)); // Màu nền xám nhạt
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.add(tabs, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}