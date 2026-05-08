package com.tuyensinh.GUI;

import com.tuyensinh.BUS.UserBUS;
import com.tuyensinh.DTO.UserDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginForm extends JFrame {

    private JTextField txtUser = new JTextField();
    private JPasswordField txtPass = new JPasswordField();
    private JCheckBox chkRemember = new JCheckBox("Ghi nhớ đăng nhập");
    private JButton btnLogin, btnForgot;
    private UserBUS userBUS = new UserBUS();

    public LoginForm() {
        setTitle("Đăng Nhập - Tuyển Sinh");
        setSize(420, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 420, 520, 20, 20));

        initComponents();
        setVisible(true);
    }

    private void initComponents() {

        // ===== BACKGROUND GRADIENT =====
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // FIX lỗi mờ
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(52, 152, 219),
                        0, getHeight(), new Color(41, 128, 185)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // ===== CARD TRẮNG =====
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(300, 360));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("TUYỂN SINH 2025");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Đăng nhập hệ thống");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== INPUT =====
        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        styleInput(txtUser);
        styleInput(txtPass);

        // ===== CHECKBOX =====
        chkRemember.setOpaque(false);
        chkRemember.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // ===== BUTTON =====
        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // hover
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(39, 174, 96));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(46, 204, 113));
            }
        });

        // ===== FORGOT =====
        btnForgot = new JButton("Quên mật khẩu?");
        btnForgot.setBorderPainted(false);
        btnForgot.setContentAreaFilled(false);
        btnForgot.setForeground(Color.GRAY);
        btnForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgot.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== ADD =====
lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

card.add(lblTitle);
card.add(Box.createVerticalStrut(5));
card.add(lblSub);
card.add(Box.createVerticalStrut(20));

// USER
JLabel lblUser = new JLabel("Tên đăng nhập");
lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);

card.add(lblUser);
card.add(Box.createVerticalStrut(5));
card.add(txtUser);

// PASS
card.add(Box.createVerticalStrut(10));

JLabel lblPass = new JLabel("Mật khẩu");
lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
txtPass.setAlignmentX(Component.LEFT_ALIGNMENT);

card.add(lblPass);
card.add(Box.createVerticalStrut(5));
card.add(txtPass);

// CHECKBOX
card.add(Box.createVerticalStrut(10));
chkRemember.setAlignmentX(Component.LEFT_ALIGNMENT);
card.add(chkRemember);

// BUTTON
card.add(Box.createVerticalStrut(15));
btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
card.add(btnLogin);

// FORGOT
card.add(Box.createVerticalStrut(10));
btnForgot.setAlignmentX(Component.LEFT_ALIGNMENT);
card.add(btnForgot);

        // ===== CLOSE BUTTON =====
        JButton btnClose = new JButton("x");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setBounds(380, 10, 30, 30);
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setForeground(Color.WHITE);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> System.exit(0));

        mainPanel.setLayout(null);
        card.setBounds(60, 80, 300, 360);
        mainPanel.add(card);
        mainPanel.add(btnClose);

        add(mainPanel);

        // ===== EVENT =====
        btnLogin.addActionListener(e -> login());
        txtPass.addActionListener(e -> login());
    }

    private void styleInput(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

    private void login() {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập đầy đủ thông tin!");
            return;
        }

        UserDTO user = userBUS.login(u, p);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Xin chào " + user.getUsername());
            new MainFrame(user);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
            txtPass.setText("");
        }
    }
}