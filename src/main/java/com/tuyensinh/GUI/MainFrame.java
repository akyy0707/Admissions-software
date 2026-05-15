package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.tuyensinh.DTO.UserDTO;
import com.tuyensinh.config.HibernateUtil;

public class MainFrame extends JFrame {

    private UserDTO currentUser;
    private JPanel mainContent;
    private CardLayout cardLayout;

    // Các nhãn để hiển thị số liệu thống kê realtime
    private JLabel lblTongThiSinh, lblTongNganh, lblHoSoXetTuyen;
    
    // Quản lý trạng thái menu
    private List<JButton> menuButtons = new ArrayList<>();
    private final Color defaultMenuColor = new Color(30, 39, 46);
    private final Color hoverMenuColor = new Color(44, 62, 80);
    private final Color activeMenuColor = new Color(52, 152, 219); // Xanh sáng khi được chọn

    public MainFrame(UserDTO user) {
        this.currentUser = user;
        initComponents();
        loadDashboardDataAsync(); // Tải dữ liệu DB ngầm
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Tuyển Sinh 2026");
        setSize(1250, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(defaultMenuColor); 
        sidebar.setPreferredSize(new Dimension(260, 0));

        // ----- Logo Panel -----
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(22, 30, 35));
        logoPanel.setPreferredSize(new Dimension(260, 90));
        logoPanel.setLayout(new GridBagLayout()); // Căn giữa logo hoàn hảo

        JLabel lblLogo = new JLabel("TUYỂN SINH");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoPanel.add(lblLogo);
        sidebar.add(logoPanel, BorderLayout.NORTH);

        // ----- Menu Panel -----
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(defaultMenuColor);
        menuPanel.setLayout(new GridLayout(10, 1, 0, 8));
        menuPanel.setBorder(new EmptyBorder(25, 15, 20, 15));

        String[] menus = {
                "Trang chủ", "Ngành", "Tổ hợp", "Ngành - Tổ hợp",
                "Thí sinh", "Điểm thi", "Điểm cộng", "Nguyện vọng", "Xét tuyển"
        };

        for (String menuName : menus) {
            JButton btn = new JButton(menuName); 
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setBackground(defaultMenuColor);
            btn.setForeground(new Color(200, 208, 216));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(new EmptyBorder(12, 25, 12, 25));

            // Hiệu ứng Hover mượt mà, không chèn lên màu Active
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (btn.getBackground() != activeMenuColor) {
                        btn.setBackground(hoverMenuColor);
                        btn.setForeground(Color.WHITE);
                    }
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (btn.getBackground() != activeMenuColor) {
                        btn.setBackground(defaultMenuColor);
                        btn.setForeground(new Color(200, 208, 216));
                    }
                }
            });

            // Sự kiện Click: Chuyển tab và đổi màu giữ nếp
            btn.addActionListener(e -> {
                cardLayout.show(mainContent, menuName);
                for (JButton b : menuButtons) {
                    b.setBackground(defaultMenuColor);
                    b.setForeground(new Color(200, 208, 216));
                    b.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Trả về font thường
                }
                btn.setBackground(activeMenuColor);
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 16)); // In đậm menu đang chọn
            });

            menuButtons.add(btn);
            menuPanel.add(btn);
        }
        
        // Mặc định bôi đen menu "Trang chủ"
        if (!menuButtons.isEmpty()) {
            menuButtons.get(0).setBackground(activeMenuColor);
            menuButtons.get(0).setForeground(Color.WHITE);
            menuButtons.get(0).setFont(new Font("Segoe UI", Font.BOLD, 16));
        }

        sidebar.add(menuPanel, BorderLayout.CENTER);

        // ================= TOPBAR =================
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setPreferredSize(new Dimension(0, 70));
        topbar.setBackground(Color.WHITE);
        topbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 235, 235))); 

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        userInfoPanel.setOpaque(false);
        JLabel lblWelcome = new JLabel("Xin chào, " + currentUser.getUsername());
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblWelcome.setForeground(new Color(60, 60, 60));
        userInfoPanel.add(lblWelcome);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        actionPanel.setOpaque(false);
        
        // Nút đăng xuất phong cách Flat
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(new Color(245, 245, 245)); 
        btnLogout.setForeground(new Color(80, 80, 80));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(120, 38));
        btnLogout.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        // Hiệu ứng khi rê chuột vào nút Đăng xuất
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(231, 76, 60));
                btnLogout.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(245, 245, 245));
                btnLogout.setForeground(new Color(80, 80, 80));
            }
        });
        
        btnLogout.addActionListener(e -> logout());
        actionPanel.add(btnLogout);

        topbar.add(userInfoPanel, BorderLayout.WEST);
        topbar.add(actionPanel, BorderLayout.EAST);

        // ================= CONTENT =================
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(new Color(248, 249, 250)); // Màu nền xám sáng thanh lịch

        // Thêm các giao diện chức năng
        mainContent.add(createHomePanel(), "Trang chủ");
        mainContent.add(new NganhPanel(), "Ngành");
        mainContent.add(new ToHopPanel(), "Tổ hợp");
        mainContent.add(new NganhToHopPanel(), "Ngành - Tổ hợp");
        mainContent.add(new ThiSinhPanel(), "Thí sinh");
        mainContent.add(new DiemThiPanel(), "Điểm thi");
        mainContent.add(new DiemCongPanel(), "Điểm cộng");
        mainContent.add(new NguyenVongPanel(), "Nguyện vọng");
        mainContent.add(new XetTuyenPanel(), "Xét tuyển");

        // ================= LẮP RÁP =================
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(topbar, BorderLayout.NORTH);
        rightPanel.add(mainContent, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // ================= HOME PANEL =================
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(45, 55, 45, 55));

        // --- HEADER ---
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JLabel lblTitle = new JLabel("TỔNG QUAN HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(40, 40, 40));

        JLabel lblSub = new JLabel("Dữ liệu được cập nhật theo thời gian thực");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub.setForeground(new Color(140, 140, 140));

        header.add(lblTitle);
        header.add(Box.createVerticalStrut(8));
        header.add(lblSub);

        panel.add(header, BorderLayout.NORTH);

        // --- CENTER CARDS ---
        JPanel center = new JPanel(new GridLayout(1, 3, 40, 40));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(45, 0, 0, 0));

        lblTongThiSinh = new JLabel("Đang tải...");
        lblTongNganh = new JLabel("Đang tải...");
        lblHoSoXetTuyen = new JLabel("Đang tải...");

        center.add(createDashboardCard("Tổng Thí Sinh", lblTongThiSinh, new Color(41, 128, 185))); 
        center.add(createDashboardCard("Tổng Ngành", lblTongNganh, new Color(39, 174, 96)));    
        center.add(createDashboardCard("Hồ Sơ Xét Tuyển", lblHoSoXetTuyen, new Color(142, 68, 173))); 

        panel.add(center, BorderLayout.CENTER);

        // --- FOOTER ---
        JLabel footer = new JLabel("SGU Admissions Management System © 2026", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        footer.setForeground(new Color(170, 170, 170));
        footer.setBorder(new EmptyBorder(40, 0, 0, 0));
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    // Sử dụng RoundedPanel để tạo góc bo tròn cho Card
    private JPanel createDashboardCard(String title, JLabel valueLabel, Color color) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE); // Góc bo tròn bán kính 20
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(120, 120, 120));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 46));
        valueLabel.setForeground(color);
        valueLabel.setBorder(new EmptyBorder(15, 0, 0, 0));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ================= LOAD DATA BACKGROUND =================
    private void loadDashboardDataAsync() {
        SwingWorker<int[], Void> worker = new SwingWorker<>() {
            @Override
            protected int[] doInBackground() throws Exception {
                int[] results = new int[3];
                try {
                    Connection conn = HibernateUtil.getSessionFactory().openSession().doReturningWork(c -> c);

                    PreparedStatement ps1 = conn.prepareStatement("SELECT COUNT(*) FROM xt_thisinhxettuyen25");
                    ResultSet rs1 = ps1.executeQuery();
                    if (rs1.next()) results[0] = rs1.getInt(1);

                    PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM xt_nganh");
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next()) results[1] = rs2.getInt(1);

                    PreparedStatement ps3 = conn.prepareStatement("SELECT COUNT(*) FROM xt_diemthixettuyen");
                    ResultSet rs3 = ps3.executeQuery();
                    if (rs3.next()) results[2] = rs3.getInt(1);

                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return results;
            }

            @Override
            protected void done() {
                try {
                    int[] results = get();
                    lblTongThiSinh.setText(String.format("%,d", results[0]));
                    lblTongNganh.setText(String.format("%,d", results[1]));
                    lblHoSoXetTuyen.setText(String.format("%,d", results[2]));
                } catch (Exception e) {
                    lblTongThiSinh.setText("Lỗi");
                    lblTongNganh.setText("Lỗi");
                    lblHoSoXetTuyen.setText("Lỗi");
                }
            }
        };
        worker.execute();
    }

    // ================= LOGOUT =================
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm();
        }
    }

    // ================= CLASS CUSTOM BO GÓC =================
    // Lớp nội (inner class) giúp vẽ JPanel có góc bo tròn mượt mà
    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false); // Bắt buộc false để không vẽ viền vuông mặc định
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            // Bật khử răng cưa để đường cong mượt mà
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Vẽ đổ bóng mờ nhẹ (Shadow effect)
            g2.setColor(new Color(235, 235, 235));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Vẽ nền chính của Panel
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            // Vẽ viền thanh mảnh bọc bên ngoài
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            g2.dispose();
        }
    }
}