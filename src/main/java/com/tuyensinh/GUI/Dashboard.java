package com.tuyensinh.GUI;

import com.tuyensinh.DTO.UserDTO;

import javax.swing.*;
import java.awt.*;

import com.tuyensinh.GUI.ImportExcel;

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

        JTabbedPane tabs = new JTabbedPane();

        // 🔥 Tab thí sinh (ai cũng thấy)
        tabs.addTab("👨‍🎓 Thí Sinh", new ThiSinhPanel());

        tabs.addTab("📥 Import Excel", new ImportExcel());
        // 🔥 Tab user (chỉ admin)
        if (currentUser.getRole() == UserDTO.Role.ADMIN) {
            tabs.addTab("👤 User", new UserPanel());
        }

        tabs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }
}