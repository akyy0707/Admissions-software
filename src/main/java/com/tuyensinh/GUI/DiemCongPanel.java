package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.tuyensinh.BUS.DiemCongBUS;
import com.tuyensinh.config.HibernateUtil;

public class DiemCongPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private DiemCongBUS diemCongBUS;
    private Connection conn;

    // Các nhãn thống kê
    private JLabel lblTongHoSo;
    private JLabel lblTongDiem;
    private JLabel lblPT4;

    public DiemCongPanel() {
        conn = HibernateUtil
                .getSessionFactory()
                .openSession()
                .doReturningWork(c -> c);

        diemCongBUS = new DiemCongBUS(conn);

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 249, 250)); // Nền xám sáng hiện đại
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Khởi tạo các label thống kê trước khi nạp vào Panel
        lblTongHoSo = new JLabel("0");
        lblTongDiem = new JLabel("0");
        lblPT4 = new JLabel("0");

        add(createStatisticPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadDataFromDB();
    }

    // ================= STATISTIC =================
    private JPanel createStatisticPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 30, 30));
        panel.setOpaque(false);

        panel.add(createStatCard("Tổng Hồ Sơ Nhập", lblTongHoSo, new Color(41, 128, 185))); // Xanh dương
        panel.add(createStatCard("Tổng Điểm Cộng", lblTongDiem, new Color(39, 174, 96)));    // Xanh lá
        panel.add(createStatCard("Phương Thức 4 (PT4)", lblPT4, new Color(142, 68, 173)));     // Tím

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(130, 130, 130));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));

        valueLabel.setForeground(color);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ================= TABLE & TOOLBAR =================
    private JPanel createTablePanel() {
        RoundedPanel panel = new RoundedPanel(20, Color.WHITE);
        panel.setLayout(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // ===== TOP BAR (Title + Actions) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Dữ Liệu Điểm Cộng Xét Tuyển");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(50, 50, 50));
        topPanel.add(lblTitle, BorderLayout.WEST);

        // ===== TOOLBAR (Buttons) =====
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        toolBar.setOpaque(false);

        JButton btnImport = createFlatButton("Import Excel", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton btnRefresh = createFlatButton("Làm mới", new Color(52, 152, 219), new Color(41, 128, 185));

        btnImport.addActionListener(e -> importExcel());
        btnRefresh.addActionListener(e -> loadDataFromDB());

        toolBar.add(btnImport);
        toolBar.add(btnRefresh);

        topPanel.add(toolBar, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"CCCD", "Mã ngành", "Mã tổ hợp", "Phương thức", "Điểm CC", "Điểm UT", "Điểm tổng"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Chống sửa dữ liệu trực tiếp trên bảng
            }
        };

        table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setSelectionBackground(new Color(220, 235, 252));
        table.setSelectionForeground(new Color(30, 30, 30));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(240, 240, 240));

        // Custom Header
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(new Color(80, 80, 80));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ================= CUSTOM BUTTON =================
    private JButton createFlatButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(130, 40));

        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    // ================= IMPORT =================
    private void importExcel() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                diemCongBUS.importFromExcel(file);
                JOptionPane.showMessageDialog(this, "Import dữ liệu từ Excel thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDB();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Import thất bại! Vui lòng kiểm tra lại định dạng file.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ================= LOAD DB =================
    private void loadDataFromDB() {
        model.setRowCount(0);
        int tongHoSo = 0;
        double tongDiem = 0;
        int tongPT4 = 0;

        try {
            String sql = """
                    SELECT ts_cccd,
                           manganh,
                           matohop,
                           phuongthuc,
                           diemCC,
                           diemUtxt,
                           diemTong
                    FROM xt_diemcongxetuyen
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String cccd = rs.getString("ts_cccd");
                String manganh = rs.getString("manganh");
                String matohop = rs.getString("matohop");
                String phuongthuc = rs.getString("phuongthuc");

                double diemCC = rs.getDouble("diemCC");
                double diemUT = rs.getDouble("diemUtxt");
                double diemTong1 = rs.getDouble("diemTong");

                model.addRow(new Object[]{
                        cccd, manganh, matohop, phuongthuc, diemCC, diemUT, diemTong1
                });

                tongHoSo++;
                tongDiem += diemTong1;

                if ("PT4".equals(phuongthuc)) {
                    tongPT4++;
                }
            }

            lblTongHoSo.setText(String.format("%,d", tongHoSo));
            lblTongDiem.setText(String.format("%,.2f", tongDiem));
            lblPT4.setText(String.format("%,d", tongPT4));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu từ cơ sở dữ liệu!", "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
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
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Vẽ màu nền
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            // Vẽ viền thanh mảnh
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            g2.dispose();
        }
    }
}