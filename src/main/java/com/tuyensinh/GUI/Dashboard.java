package com.tuyensinh.GUI;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.tuyensinh.DTO.UserDTO;

public class Dashboard extends JFrame {

    private UserDTO currentUser;

    public Dashboard(UserDTO user) {
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("Dashboard - Xin chào " + currentUser.getUsername());
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 🔥 Chuyển sang MainFrame thay vì Dashboard cũ
        new MainFrame(currentUser);
        dispose();
        JTabbedPane tabs = new JTabbedPane();

        // 🔥 Tab thí sinh (ai cũng thấy)
        tabs.addTab("👨‍🎓 Thí Sinh", new ThiSinhPanel());

        tabs.addTab("📥 Import Excel điểm", new ImprtExDiem());
        tabs.addTab("📥 Import Excel điểm cộng", new ImprtExDiemCong());

        // 🔥 Tab user (chỉ admin)
        if (currentUser.getRole() == UserDTO.Role.ADMIN) {
            tabs.addTab("👤 User", new UserPanel());
        }

        tabs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }
}