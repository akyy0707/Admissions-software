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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

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

import com.tuyensinh.BUS.DiemThiBUS;
import com.tuyensinh.DTO.DiemThiDTO;

public class DiemThiPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    // Các nhãn thống kê
    private JLabel lblTongThiSinh;
    private JLabel lblAvgToan;
    private JLabel lblAvgVan;

    private DiemThiBUS diemThiBUS;

    public DiemThiPanel() {
        diemThiBUS = new DiemThiBUS();

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 249, 250)); // Nền xám sáng hiện đại
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Khởi tạo các label thống kê trước khi nạp vào Panel
        lblTongThiSinh = new JLabel("0");
        lblAvgToan = new JLabel("0");
        lblAvgVan = new JLabel("0");

        add(createStatisticPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadDataFromDB();
    }

    // ================= STATISTIC =================
    private JPanel createStatisticPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 30, 30));
        panel.setOpaque(false);

        panel.add(createStatCard("Tổng Thí Sinh Có Điểm", lblTongThiSinh, new Color(41, 128, 185))); // Xanh dương
        panel.add(createStatCard("Điểm Trung Bình Toán", lblAvgToan, new Color(39, 174, 96)));    // Xanh lá
        panel.add(createStatCard("Điểm Trung Bình Văn", lblAvgVan, new Color(142, 68, 173)));     // Tím

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

        JLabel lblTitle = new JLabel("Dữ Liệu Điểm Thi Chi Tiết");
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
        String[] columns = {
                "CCCD", "Toán", "Văn", "Lý", "Hóa", "Sinh", 
                "Sử", "Địa", "KTPL", "Tin", "NK1", "NK2"
        };
        
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

        // Hiệu ứng Hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent evt) {
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
                diemThiBUS.importFromExcel(file);
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
        try {
            model.setRowCount(0);
            List<DiemThiDTO> list = diemThiBUS.getAll();

            double tongToan = 0;
            double tongVan = 0;

            for (DiemThiDTO d : list) {
                tongToan += d.getTo();
                tongVan += d.getVa();

                model.addRow(new Object[]{
                        d.getCccd(), d.getTo(), d.getVa(), d.getLi(), d.getHo(),
                        d.getSi(), d.getSu(), d.getDi(), d.getKtpl(), d.getTi(),
                        d.getNk1(), d.getNk2()
                });
            }

            DecimalFormat df = new DecimalFormat("0.00");

            lblTongThiSinh.setText(String.format("%,d", list.size()));

            if (list.size() > 0) {
                lblAvgToan.setText(df.format(tongToan / list.size()));
                lblAvgVan.setText(df.format(tongVan / list.size()));
            } else {
                lblAvgToan.setText("0.00");
                lblAvgVan.setText("0.00");
            }

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