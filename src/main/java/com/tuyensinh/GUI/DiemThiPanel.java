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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.tuyensinh.BUS.DiemThiBUS;
import com.tuyensinh.DTO.DiemThiDTO;

public class DiemThiPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearchCCCD;
    private JComboBox<String> cboMethod;

    // Các nhãn thống kê
    private JLabel lblTongThiSinh;
    private JLabel lblAvgToan;
    private JLabel lblAvgVan;

    // Phân trang
    private int currentPage = 1;
    private int recordsPerPage = 10;
    private List<DiemThiDTO> allDiemThiList = new java.util.ArrayList<>();
    private JLabel lblPageInfo;
    private JButton btnPrevious;
    private JButton btnNext;

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
        panel.add(createStatCard("Điểm Trung Bình Toán", lblAvgToan, new Color(39, 174, 96))); // Xanh lá
        panel.add(createStatCard("Điểm Trung Bình Văn", lblAvgVan, new Color(142, 68, 173))); // Tím

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

        JLabel lblTitle = new JLabel(" Điểm Thi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(50, 50, 50));
        topPanel.add(lblTitle, BorderLayout.WEST);

        // ===== TOOLBAR (Buttons) =====
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        toolBar.setOpaque(false);

        // Tìm kiếm CCCD
        JLabel lblCCCD = new JLabel("Tìm CCCD:");
        lblCCCD.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCCCD.setForeground(new Color(80, 80, 80));
        
        txtSearchCCCD = new JTextField(15);
        txtSearchCCCD.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearchCCCD.setPreferredSize(new Dimension(150, 35));
        
        // Lọc theo phương thức
        JLabel lblMethod = new JLabel("Phương thức:");
        lblMethod.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMethod.setForeground(new Color(80, 80, 80));
        
        cboMethod = new JComboBox<>(new String[]{"Tất cả", "DGNL", "THPT", "VSAT"});
        cboMethod.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboMethod.setPreferredSize(new Dimension(120, 35));
        cboMethod.addActionListener(e -> filterByMethod());
        
        JButton btnSearch = createFlatButton("Tìm kiếm", new Color(155, 89, 182), new Color(142, 68, 173));
        JButton btnImport = createFlatButton("Import Excel", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton btnRefresh = createFlatButton("Làm mới", new Color(52, 152, 219), new Color(41, 128, 185));

        btnSearch.addActionListener(e -> searchByCCCD());
        btnImport.addActionListener(e -> importExcel());
        btnRefresh.addActionListener(e -> loadDataFromDB());

        toolBar.add(lblCCCD);
        toolBar.add(txtSearchCCCD);
        toolBar.add(btnSearch);
        toolBar.add(lblMethod);
        toolBar.add(cboMethod);
        toolBar.add(btnImport);
        toolBar.add(btnRefresh);

        topPanel.add(toolBar, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {
                "ID", "CCCD", "Số báo danh", "Phương thức",
                "Toán", "Lý", "Hóa", "Sinh", "Sử", "Địa", "Văn",
                "N1_THI", "N1_CC", "CNCN", "CNNN", "Tin", "KTPL", "NL1", "NK1", "NK2"
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

        // ===== PAGINATION PANEL =====
        JPanel paginationPanel = createPaginationPanel();
        panel.add(paginationPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= PAGINATION PANEL =================
    private JPanel createPaginationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setOpaque(false);

        btnPrevious = createFlatButton("< Trước", new Color(52, 152, 219), new Color(41, 128, 185));
        btnPrevious.setPreferredSize(new Dimension(100, 35));
        
        lblPageInfo = new JLabel("Trang 1 / 1");
        lblPageInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPageInfo.setForeground(new Color(80, 80, 80));
        lblPageInfo.setPreferredSize(new Dimension(150, 35));
        
        btnNext = createFlatButton("Tiếp >", new Color(52, 152, 219), new Color(41, 128, 185));
        btnNext.setPreferredSize(new Dimension(100, 35));

        btnPrevious.addActionListener(e -> previousPage());
        btnNext.addActionListener(e -> nextPage());

        panel.add(btnPrevious);
        panel.add(lblPageInfo);
        panel.add(btnNext);

        return panel;
    }

    private void displayPage() {
        model.setRowCount(0);
        
        int totalRecords = allDiemThiList.size();
        int totalPages = (totalRecords + recordsPerPage - 1) / recordsPerPage;
        
        if (currentPage < 1) currentPage = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        int startIndex = (currentPage - 1) * recordsPerPage;
        int endIndex = Math.min(startIndex + recordsPerPage, totalRecords);

        double tongToan = 0;
        double tongVan = 0;
        int countOnPage = 0;

        for (int i = startIndex; i < endIndex; i++) {
            DiemThiDTO d = allDiemThiList.get(i);
            tongToan += d.getTo();
            tongVan += d.getVa();
            countOnPage++;

            model.addRow(new Object[] {
                    d.getIddiemthi(), d.getCccd(), d.getSobaodanh(), d.getD_phuongthuc(),
                    d.getTo(), d.getLi(), d.getHo(), d.getSi(), d.getSu(), d.getDi(), d.getVa(),
                    d.getN1_thi(), d.getN1_cc(), d.getCncn(), d.getCnnn(), d.getTi(), d.getKtpl(), d.getNl1(),
                    d.getNk1(), d.getNk2()
            });
        }

        // Cập nhật thông tin trang
        lblPageInfo.setText(String.format("Trang %d / %d", currentPage, totalPages > 0 ? totalPages : 1));
        btnPrevious.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);

        // Cập nhật thống kê
        DecimalFormat df = new DecimalFormat("0.00");
        lblTongThiSinh.setText(String.format("%,d", totalRecords));

        if (countOnPage > 0) {
            lblAvgToan.setText(df.format(tongToan / countOnPage));
            lblAvgVan.setText(df.format(tongVan / countOnPage));
        } else {
            lblAvgToan.setText("0.00");
            lblAvgVan.setText("0.00");
        }
    }

    private void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            displayPage();
        }
    }

    private void nextPage() {
        int totalPages = (allDiemThiList.size() + recordsPerPage - 1) / recordsPerPage;
        if (currentPage < totalPages) {
            currentPage++;
            displayPage();
        }
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
                JOptionPane.showMessageDialog(this, "Import dữ liệu từ Excel thành công!", "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDB();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Import thất bại! Vui lòng kiểm tra lại định dạng file.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ================= LOAD DB =================
    private void loadDataFromDB() {
        try {
            currentPage = 1;
            allDiemThiList = diemThiBUS.getAll();
            displayPage();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu từ cơ sở dữ liệu!", "Lỗi kết nối",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= SEARCH BY CCCD =================
    private void searchByCCCD() {
        String cccdSearch = txtSearchCCCD.getText().trim();
        
        if (cccdSearch.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập CCCD để tìm kiếm!", "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            currentPage = 1;
            allDiemThiList = new java.util.ArrayList<>();
            List<DiemThiDTO> list = diemThiBUS.getAll();

            for (DiemThiDTO d : list) {
                if (d.getCccd() != null && d.getCccd().contains(cccdSearch)) {
                    allDiemThiList.add(d);
                }
            }

            if (allDiemThiList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy CCCD: " + cccdSearch, "Không có kết quả",
                        JOptionPane.INFORMATION_MESSAGE);
                lblTongThiSinh.setText("0");
                lblAvgToan.setText("0.00");
                lblAvgVan.setText("0.00");
                model.setRowCount(0);
            } else {
                displayPage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= FILTER BY METHOD =================
    private void filterByMethod() {
        String selectedMethod = (String) cboMethod.getSelectedItem();
        
        try {
            currentPage = 1;
            allDiemThiList = new java.util.ArrayList<>();
            List<DiemThiDTO> list = diemThiBUS.getAll();

            if ("Tất cả".equals(selectedMethod)) {
                allDiemThiList.addAll(list);
            } else {
                for (DiemThiDTO d : list) {
                    if (d.getD_phuongthuc() != null && d.getD_phuongthuc().equals(selectedMethod)) {
                        allDiemThiList.add(d);
                    }
                }
            }

            if (allDiemThiList.isEmpty()) {
                lblTongThiSinh.setText("0");
                lblAvgToan.setText("0.00");
                lblAvgVan.setText("0.00");
                model.setRowCount(0);
            } else {
                displayPage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lọc dữ liệu!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
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