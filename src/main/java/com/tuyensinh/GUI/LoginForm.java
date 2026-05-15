package com.tuyensinh.GUI;

import com.tuyensinh.BUS.UserBUS;
import com.tuyensinh.DTO.UserDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;

public class LoginForm extends JFrame {

    private JTextField txtUser = new JTextField();
    private JPasswordField txtPass = new JPasswordField();
    private JCheckBox chkRemember = new JCheckBox("Ghi nhớ đăng nhập");
    private JButton btnLogin, btnForgot;
    private UserBUS userBUS = new UserBUS();

    // Biến hỗ trợ di chuyển Form (khi không có thanh tiêu đề)
    private int mouseX, mouseY;

    public LoginForm() {
        setTitle("Đăng Nhập Hệ Thống");
        setSize(420, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true); 
        // Bo góc toàn bộ JFrame
        setShape(new RoundRectangle2D.Double(0, 0, 420, 560, 25, 25));

        initComponents();
        setupDraggable();
        setVisible(true);
    }

    private void initComponents() {
        // ===== BACKGROUND GRADIENT =====
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(41, 128, 185), 
                        0, getHeight(), new Color(109, 192, 230)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // ===== TOP BAR (Chứa nút Tắt) =====
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        topBar.setOpaque(false);

        JButton btnClose = new JButton("X");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> System.exit(0));

        btnClose.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnClose.setForeground(new Color(231, 76, 60)); }
            public void mouseExited(MouseEvent e) { btnClose.setForeground(Color.WHITE); }
        });
        topBar.add(btnClose);

        // ===== CENTER CARD (Form Trắng) =====
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        RoundedPanel card = new RoundedPanel(25, Color.WHITE);
        card.setPreferredSize(new Dimension(340, 460)); // Rộng hơn 1 xíu để thoáng input
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(35, 30, 35, 30));

        // --- TITLE ---
        JLabel lblTitle = new JLabel("TUYỂN SINH 2026");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(40, 40, 40));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Đăng nhập hệ thống quản lý");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(130, 130, 130));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- INPUTS ---
        styleInput(txtUser);
        styleInput(txtPass);

        // --- CHECKBOX ---
        chkRemember.setOpaque(false);
        chkRemember.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkRemember.setForeground(new Color(100, 100, 100));
        chkRemember.setFocusPainted(false);

        // Đưa Checkbox vào 1 panel để ép nó nằm bên trái khung giữa 280px
        JPanel pnlRemember = new JPanel(new BorderLayout());
        pnlRemember.setOpaque(false);
        pnlRemember.setMaximumSize(new Dimension(280, 25));
        pnlRemember.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlRemember.add(chkRemember, BorderLayout.WEST);

        // --- LOGIN BUTTON ---
        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(280, 45)); // Khớp độ rộng với Textfield
        
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { btnLogin.setBackground(new Color(39, 174, 96)); }
            public void mouseExited(MouseEvent evt) { btnLogin.setBackground(new Color(46, 204, 113)); }
        });

        // --- FORGOT PASSWORD ---
        btnForgot = new JButton("Quên mật khẩu?");
        btnForgot.setBorderPainted(false);
        btnForgot.setContentAreaFilled(false);
        btnForgot.setFocusPainted(false);
        btnForgot.setForeground(new Color(150, 150, 150));
        btnForgot.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgot.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnForgot.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { btnForgot.setForeground(new Color(52, 152, 219)); }
            public void mouseExited(MouseEvent evt) { btnForgot.setForeground(new Color(150, 150, 150)); }
        });

        // --- LẮP RÁP FORM ---
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(5));
        card.add(lblSub);
        card.add(Box.createVerticalStrut(30));

        card.add(createLeftAlignedLabel("Tên đăng nhập"));
        card.add(Box.createVerticalStrut(5));
        card.add(txtUser);
        card.add(Box.createVerticalStrut(15));

        card.add(createLeftAlignedLabel("Mật khẩu"));
        card.add(Box.createVerticalStrut(5));
        card.add(txtPass);
        card.add(Box.createVerticalStrut(10));

        card.add(pnlRemember); // Thêm Panel bọc Checkbox vào thay vì Checkbox trần
        card.add(Box.createVerticalStrut(25));

        card.add(btnLogin);
        card.add(Box.createVerticalStrut(15));

        card.add(btnForgot);

        centerWrapper.add(card); // Đưa card vào GridBag để tự động canh giữa

        // Gắn vào Main Panel
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        add(mainPanel);

        // ===== EVENT =====
        btnLogin.addActionListener(e -> login());
        txtPass.addActionListener(e -> login()); 
        txtUser.addActionListener(e -> txtPass.requestFocus()); 
    }

    // Hàm bọc nhãn vào panel để vừa canh giữa tổng thể, vừa ép text sát lề trái
    private JPanel createLeftAlignedLabel(String text) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        pnl.setMaximumSize(new Dimension(280, 20)); // Cùng độ rộng với TextField
        pnl.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn Panel ở giữa màn hình
        
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 100, 100));
        pnl.add(lbl, BorderLayout.WEST); // Đẩy Label về góc trái của Panel
        
        return pnl;
    }

    // Hàm style cho Textfield
    private void styleInput(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setAlignmentX(Component.CENTER_ALIGNMENT); // Ép khung nằm giữa
        txt.setMaximumSize(new Dimension(280, 42)); // Chốt độ rộng chuẩn là 280px
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
    }

    // Hàm giúp di chuyển cửa sổ
    private void setupDraggable() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - mouseX, y - mouseY);
            }
        });
    }

    private void login() {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserDTO user = userBUS.login(u, p);

        if (user != null) {
            new MainFrame(user);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            txtPass.setText(""); 
            txtPass.requestFocus();
        }
    }

    // ================= CLASS CUSTOM BO GÓC =================
    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Đổ bóng mờ nhẹ 
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Nền thẻ Card chính
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            g2.dispose();
        }
    }
}