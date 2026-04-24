package com.tuyensinh.GUI;

import com.tuyensinh.DTO.UserDTO;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private UserDTO currentUser;
    private JTabbedPane tabbedPane;

    public MainFrame(UserDTO user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Phần Mềm Tuyển Sinh - " + currentUser.getUsername());
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ===== FONT EMOJI =====
        UIManager.put("Menu.font", new Font("Segoe UI Emoji", Font.PLAIN, 14));
        UIManager.put("MenuItem.font", new Font("Segoe UI Emoji", Font.PLAIN, 14));

        // Menu Bar
        setJMenuBar(createMenuBar());

        // ===== TAB =====
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

        tabbedPane.addTab("👨‍🎓 Thi Sinh", new ThiSinhPanel());
        tabbedPane.addTab("📋 Nguyen Vong", new NguyenVongPanel());
        tabbedPane.addTab("✅ Xet Tuyen", new XetTuyenPanel());
        tabbedPane.addTab("📥 Import Excel", new ImportExcel());

        if (currentUser.getRole() == UserDTO.Role.ADMIN) {
            tabbedPane.addTab("👤 Quan Ly User", new UserPanel());
        }

        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tabbedPane, BorderLayout.CENTER);

        // ===== STATUS BAR =====
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        JLabel lblLeft = new JLabel("  👤 " + currentUser.getUsername() + " | 🔑 " + currentUser.getRole());
        JLabel lblRight = new JLabel(new java.util.Date().toString() + "  ");

        statusBar.add(lblLeft, BorderLayout.WEST);
        statusBar.add(lblRight, BorderLayout.EAST);

        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // ===== MENU HỆ THỐNG =====
        JMenu menuSystem = new JMenu("⚙️ He thong");
        menuSystem.setMnemonic('H');

        JMenuItem itemLogout = new JMenuItem("🚪 Dang xuat");
        itemLogout.setAccelerator(KeyStroke.getKeyStroke('L',
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                        | java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        itemLogout.addActionListener(e -> logout());

        JMenuItem itemExit = new JMenuItem("❌ Thoat");
        itemExit.setAccelerator(KeyStroke.getKeyStroke('Q',
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                        | java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        itemExit.addActionListener(e -> System.exit(0));

        menuSystem.add(itemLogout);
        menuSystem.addSeparator();
        menuSystem.add(itemExit);

        // ===== MENU TRA CỨU =====
        JMenu menuSearch = new JMenu("🔍 Tra cuu");
        menuSearch.setMnemonic('T');

        JMenuItem itemSearchTS = new JMenuItem("👨‍🎓 Tim thi sinh");
        itemSearchTS.addActionListener(e -> tabbedPane.setSelectedIndex(0));

        JMenuItem itemSearchNV = new JMenuItem("📋 Tim nguyen vong");
        itemSearchNV.addActionListener(e -> tabbedPane.setSelectedIndex(1));

        menuSearch.add(itemSearchTS);
        menuSearch.add(itemSearchNV);

        // ===== MENU HELP =====
        JMenu menuHelp = new JMenu("❓ Giup do");
        menuHelp.setMnemonic('G');

        JMenuItem itemAbout = new JMenuItem("ℹ️ Gioi thieu");
        itemAbout.addActionListener(e -> showAbout());

        menuHelp.add(itemAbout);

        menuBar.add(menuSystem);
        menuBar.add(menuSearch);
        menuBar.add(menuHelp);

        return menuBar;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có muốn đăng xuất không?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm();
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "🎓 Phần Mềm Tuyển Sinh 2025\n" +
                        "Version 1.0\n" +
                        "Developed by Team",
                "Giới thiệu",
                JOptionPane.INFORMATION_MESSAGE);
    }
}