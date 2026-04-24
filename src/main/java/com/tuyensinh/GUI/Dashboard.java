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

        // 🔥 Chuyển sang MainFrame thay vì Dashboard cũ
        new MainFrame(currentUser);
        dispose();
    }
}