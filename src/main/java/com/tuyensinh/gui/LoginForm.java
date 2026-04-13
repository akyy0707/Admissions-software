package com.tuyensinh.GUI;

import com.tuyensinh.BUS.UserBUS;
import com.tuyensinh.DTO.UserDTO;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    JTextField txtUser = new JTextField();
    JPasswordField txtPass = new JPasswordField();

    private UserBUS userBUS = new UserBUS();

    public LoginForm() {
        setTitle("Login");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3,2,10,10));

        add(new JLabel("Username:"));
        add(txtUser);

        add(new JLabel("Password:"));
        add(txtPass);

        JButton btnLogin = new JButton("Login");
        add(new JLabel());
        add(btnLogin);

        btnLogin.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword());

        UserDTO user = userBUS.login(u, p);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");

            // 🔥 truyền user qua dashboard (để phân quyền)
            new Dashboard(user);

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
        }
    }
}