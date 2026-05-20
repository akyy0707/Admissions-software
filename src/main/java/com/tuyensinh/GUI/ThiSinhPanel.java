package com.tuyensinh.GUI;

import com.tuyensinh.BUS.DiemThiBUS;
import com.tuyensinh.BUS.ThiSinhBUS;
import com.tuyensinh.DTO.DiemThiDTO;
import com.tuyensinh.DTO.ThiSinhDTO;
import com.tuyensinh.DTO.ThiSinhDTO.GioiTinh;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ThiSinhPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private ThiSinhBUS tsBUS = new ThiSinhBUS();
    private DiemThiBUS diemThiBUS = new DiemThiBUS();

    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnDelete, btnRefresh, btnStats, btnDetail, btnScores;

    private JLabel lblTongThiSinh;
    private JLabel lblDoiTuongSummary;
    private JLabel lblKhuVucSummary;

    private int currentPage = 1;
    private final int pageSize = 25;
    private JLabel lblPageInfo;
    private JButton btnPrevPage;
    private JButton btnNextPage;

    public ThiSinhPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 249, 250)); // Nền xám sáng hiện đại
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Tiêu đề + thống kê
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Nội dung chính (Toolbar + Table)
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("QUẢN LÝ THÍ SINH");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(40, 40, 40));

        header.add(lblTitle);
        header.add(Box.createVerticalStrut(15));
        header.add(createStatisticPanel());

        return header;
    }

    private JPanel createStatisticPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 25, 25));
        panel.setOpaque(false);

        lblTongThiSinh = new JLabel("0");
        lblDoiTuongSummary = new JLabel("...");
        lblKhuVucSummary = new JLabel("...");

        panel.add(createStatCard("Tổng Thí Sinh", lblTongThiSinh, new Color(41, 128, 185), 34));
        panel.add(createStatCard("Theo Đối Tượng", lblDoiTuongSummary, new Color(39, 174, 96), 13));
        panel.add(createStatCard("Theo Khu Vực", lblKhuVucSummary, new Color(142, 68, 173), 13));

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color color, int valueSize) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(130, 130, 130));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));

        valueLabel.setForeground(color);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, valueSize));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createMainPanel() {
        RoundedPanel mainPanel = new RoundedPanel(20, Color.WHITE);
        mainPanel.setLayout(new BorderLayout(10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // ===== TOOLBAR =====
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setOpaque(false);

        // --- Tìm kiếm (Bên trái) ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(new Color(80, 80, 80));

        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(200, 38));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));

        btnSearch = createFlatButton("Tìm", new Color(149, 165, 166), new Color(127, 140, 141));
        btnSearch.setPreferredSize(new Dimension(80, 38));

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // --- Nút thao tác (Bên phải) ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        btnDetail = createFlatButton("Chi tiết", new Color(52, 152, 219), new Color(41, 128, 185));
        btnScores = createFlatButton("Điểm", new Color(155, 89, 182), new Color(142, 68, 173));
        btnStats = createFlatButton("Thống kê", new Color(241, 196, 15), new Color(243, 156, 18));
        btnStats.setForeground(new Color(80, 80, 80));

        btnAdd = createFlatButton("Thêm mới", new Color(46, 204, 113), new Color(39, 174, 96));
        btnDelete = createFlatButton("Xóa", new Color(231, 76, 60), new Color(192, 57, 43));
        btnRefresh = createFlatButton("Làm Mới", new Color(240, 240, 240), new Color(220, 220, 220));
        btnRefresh.setForeground(new Color(80, 80, 80));

        actionPanel.add(btnDetail);
        actionPanel.add(btnScores);
        actionPanel.add(btnStats);
        actionPanel.add(btnAdd);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);

        toolBar.add(searchPanel, BorderLayout.WEST);
        toolBar.add(actionPanel, BorderLayout.EAST);
        mainPanel.add(toolBar, BorderLayout.NORTH);

        // ===== BẢNG DỮ LIỆU =====
        String[] columns = {"ID", "Số Báo Danh", "Họ", "Tên", "CCCD", "Ngày Sinh", "Giới Tính", "Điện Thoại", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40); // Nới rộng chiều cao dòng
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(220, 235, 252));
        table.setSelectionForeground(new Color(30, 30, 30));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(240, 240, 240));

        // Tùy chỉnh Header
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(new Color(80, 80, 80));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        table.getColumnModel().getColumn(0).setMaxWidth(50); // Cột ID nhỏ lại

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(createPaginationBar(), BorderLayout.SOUTH);

        // ===== SỰ KIỆN =====
        btnSearch.addActionListener(e -> search());
        txtSearch.addActionListener(e -> search()); // Nhấn Enter ở ô tìm kiếm
        btnAdd.addActionListener(e -> openAddDialog());
        btnDelete.addActionListener(e -> deleteThiSinh());
        btnDetail.addActionListener(e -> showChiTietThiSinh());
        btnScores.addActionListener(e -> showDiemThiSinh());
        btnStats.addActionListener(e -> showThongKe());
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); loadData(); });

        return mainPanel;
    }

    // ================= CUSTOM NÚT PHẲNG =================
    private JButton createFlatButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(110, 38));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { btn.setBackground(hoverColor); }
            public void mouseExited(MouseEvent evt) { btn.setBackground(bgColor); }
        });
        return btn;
    }

    private JPanel createPaginationBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        btnPrevPage = createFlatButton("Trang trước", new Color(240, 240, 240), new Color(220, 220, 220));
        btnPrevPage.setForeground(new Color(80, 80, 80));
        btnNextPage = createFlatButton("Trang sau", new Color(240, 240, 240), new Color(220, 220, 220));
        btnNextPage.setForeground(new Color(80, 80, 80));

        lblPageInfo = new JLabel("Trang 1/1");
        lblPageInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPageInfo.setForeground(new Color(120, 120, 120));

        left.add(btnPrevPage);
        left.add(btnNextPage);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(lblPageInfo);

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);

        btnPrevPage.addActionListener(e -> goToPage(currentPage - 1));
        btnNextPage.addActionListener(e -> goToPage(currentPage + 1));

        return panel;
    }

    // ================= XỬ LÝ DỮ LIỆU =================
    private void loadData() {
        currentPage = 1;
        loadDataPage(currentPage, txtSearch != null ? txtSearch.getText().trim() : "");
    }

    private void search() {
        String key = txtSearch.getText().trim();
        currentPage = 1;
        loadDataPage(currentPage, key);
    }

    private void goToPage(int page) {
        if (page < 1) {
            return;
        }
        loadDataPage(page, txtSearch.getText().trim());
    }

    private void loadDataPage(int page, String key) {
        tableModel.setRowCount(0);

        List<ThiSinhDTO> list;
        long total;
        if (key == null || key.isEmpty()) {
            list = tsBUS.getPage(page, pageSize);
            total = tsBUS.count();
        } else {
            list = tsBUS.search(key, page, pageSize);
            total = tsBUS.countSearch(key);
        }

        for (ThiSinhDTO ts : list) {
            tableModel.addRow(new Object[]{
                    ts.getId(), ts.getSoBaoDanh(), ts.getHo(), ts.getTen(),
                    ts.getCccd(), ts.getNgaySinh() != null ? new SimpleDateFormat("dd/MM/yyyy").format(ts.getNgaySinh()) : "",
                    ts.getGioiTinh(), ts.getDienThoai(), ts.getEmail()
            });
        }

        int totalPages = (int) Math.max(1, Math.ceil(total / (double) pageSize));
        currentPage = Math.min(page, totalPages);
        updatePaginationState(totalPages);

        refreshStatistics();
    }

    private void updatePaginationState(int totalPages) {
        lblPageInfo.setText("Trang " + currentPage + "/" + totalPages);
        btnPrevPage.setEnabled(currentPage > 1);
        btnNextPage.setEnabled(currentPage < totalPages);
    }

    private void refreshStatistics() {
        long total = tsBUS.count();
        Map<String, Long> theoDoiTuong = tsBUS.countByDoiTuong();
        Map<String, Long> theoKhuVuc = tsBUS.countByKhuVuc();

        lblTongThiSinh.setText(String.format("%,d", total));
        lblDoiTuongSummary.setText(formatSummary(theoDoiTuong, 3));
        lblKhuVucSummary.setText(formatSummary(theoKhuVuc, 3));
    }

    private String formatSummary(Map<String, Long> map, int maxItems) {
        if (map == null || map.isEmpty()) {
            return "Chưa có dữ liệu";
        }

        StringBuilder sb = new StringBuilder("<html>");
        int index = 0;
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            if (index >= maxItems) {
                break;
            }
            if (index > 0) {
                sb.append("<br>");
            }
            sb.append(entry.getKey()).append(": ").append(entry.getValue());
            index++;
        }

        if (map.size() > maxItems) {
            sb.append("<br>+").append(map.size() - maxItems).append(" khác");
        }

        sb.append("</html>");
        return sb.toString();
    }

    private void deleteThiSinh() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa thí sinh này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            if (tsBUS.delete(id)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showThongKe() {
        long total = tsBUS.count();
        Map<String, Long> theoDoiTuong = tsBUS.countByDoiTuong();
        Map<String, Long> theoKhuVuc = tsBUS.countByKhuVuc();

        StringBuilder sb = new StringBuilder();
        sb.append("TỔNG THÍ SINH: ").append(total).append("\n\n");

        sb.append("Theo đối tượng:\n");
        if (theoDoiTuong == null || theoDoiTuong.isEmpty()) {
            sb.append("- Chưa có dữ liệu\n");
        } else {
            for (Map.Entry<String, Long> entry : theoDoiTuong.entrySet()) {
                sb.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }

        sb.append("\nTheo khu vực:\n");
        if (theoKhuVuc == null || theoKhuVuc.isEmpty()) {
            sb.append("- Chưa có dữ liệu\n");
        } else {
            for (Map.Entry<String, Long> entry : theoKhuVuc.entrySet()) {
                sb.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Bảng Thống Kê", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showChiTietThiSinh() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thí sinh!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        ThiSinhDTO ts = tsBUS.getById(id);
        if (ts == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thí sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        openDetailDialog(ts);
    }

    private void openDetailDialog(ThiSinhDTO ts) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi Tiết Thí Sinh", true);
        dialog.setSize(750, 580);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        JLabel lblTitle = new JLabel("CHI TIẾT THÍ SINH");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        JLabel lblSubtitle = new JLabel("Số báo danh: " + ts.getSoBaoDanh());
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(200, 230, 255));
        
        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setOpaque(false);
        headerContent.add(lblTitle, BorderLayout.NORTH);
        headerContent.add(lblSubtitle, BorderLayout.SOUTH);
        headerPanel.add(headerContent, BorderLayout.WEST);
        dialog.add(headerPanel, BorderLayout.NORTH);

        // Body - Lưới thông tin
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBackground(Color.WHITE);
        bodyPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        String ngaySinh = ts.getNgaySinh() != null ? new SimpleDateFormat("dd/MM/yyyy").format(ts.getNgaySinh()) : "N/A";

        bodyPanel.add(createInfoRow("Họ:", ts.getHo()));
        bodyPanel.add(createInfoRow("Tên:", ts.getTen()));
        bodyPanel.add(createInfoRow("CCCD:", ts.getCccd()));
        bodyPanel.add(createInfoRow("Ngày sinh:", ngaySinh));
        bodyPanel.add(createInfoRow("Giới tính:", ts.getGioiTinh().toString()));
        bodyPanel.add(createInfoRow("Nơi sinh:", ts.getNoiSinh() != null ? ts.getNoiSinh() : "N/A"));
        bodyPanel.add(createInfoRow("Điện thoại:", ts.getDienThoai() != null ? ts.getDienThoai() : "N/A"));
        bodyPanel.add(createInfoRow("Email:", ts.getEmail() != null ? ts.getEmail() : "N/A"));
        bodyPanel.add(createInfoRow("Đối tượng:", ts.getDoiTuong() != null ? ts.getDoiTuong() : "N/A"));
        bodyPanel.add(createInfoRow("Khu vực:", ts.getKhuVuc() != null ? ts.getKhuVuc() : "N/A"));

        JScrollPane scrollPane = new JScrollPane(bodyPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton btnClose = createFlatButton("Đóng", new Color(240, 240, 240), new Color(220, 220, 220));
        btnClose.setForeground(new Color(80, 80, 80));
        btnClose.addActionListener(e -> dialog.dispose());
        footerPanel.add(btnClose);
        dialog.add(footerPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        row.setBorder(new EmptyBorder(5, 15, 5, 15));
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLabel.setForeground(new Color(80, 80, 80));
        lblLabel.setPreferredSize(new Dimension(120, 25));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblValue.setForeground(new Color(50, 50, 50));

        row.add(lblLabel, BorderLayout.WEST);
        row.add(lblValue, BorderLayout.CENTER);
        row.setBackground(new Color(248, 249, 250));
        
        return row;
    }

    private void showDiemThiSinh() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thí sinh!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cccd = (String) tableModel.getValueAt(selectedRow, 4);
        DiemThiDTO diem = diemThiBUS.getByCCCD(cccd);
        if (diem == null) {
            JOptionPane.showMessageDialog(this, "Chưa có dữ liệu điểm cho thí sinh này!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ĐIỂM THI THPT\n");
        sb.append("Toán: ").append(diem.getTo()).append("  |  Văn: ").append(diem.getVa())
                .append("  |  Lý: ").append(diem.getLi()).append("  |  Hóa: ").append(diem.getHo()).append("\n");
        sb.append("Sinh: ").append(diem.getSi()).append("  |  Sử: ").append(diem.getSu())
                .append("  |  Địa: ").append(diem.getDi()).append("  |  Ngoại ngữ: ").append(diem.getN1_thi()).append("\n");
        sb.append("KTPL: ").append(diem.getKtpl()).append("  |  Tin: ").append(diem.getTi())
                .append("  |  CNCN: ").append(diem.getCncn()).append("  |  CNNN: ").append(diem.getCnnn()).append("\n\n");

        sb.append("ĐIỂM ĐGNL: ").append(diem.getNl1() > 0 ? diem.getNl1() : "Chưa có").append("\n\n");
        sb.append("ĐIỂM VSAT: ").append(diem.getNk1() > 0 || diem.getNk2() > 0
                ? "NK1: " + diem.getNk1() + "  |  NK2: " + diem.getNk2()
                : "Chưa có");

        JOptionPane.showMessageDialog(this, sb.toString(), "Kết Quả Điểm Thi", JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= FORM THÊM THÍ SINH (2 Cột) =================
    private void openAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Thí Sinh Mới", true);
        dialog.setSize(650, 480);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());

        // Header Form
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        JLabel lblFormTitle = new JLabel("THÔNG TIN THÍ SINH");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblFormTitle.setForeground(new Color(50, 50, 50));
        headerPanel.add(lblFormTitle, BorderLayout.WEST);
        dialog.add(headerPanel, BorderLayout.NORTH);

        // Body Form (Lưới 4 dòng x 2 cột)
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 25, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 30, 20, 30));

        JTextField txtSBD = createStyledTextField();
        JTextField txtCCCD = createStyledTextField();
        JTextField txtHo = createStyledTextField();
        JTextField txtTen = createStyledTextField();
        JTextField txtNgaySinh = createStyledTextField(); // dd/MM/yyyy
        txtNgaySinh.setToolTipText("Ví dụ: 15/03/2005");
        JComboBox<GioiTinh> cboGioiTinh = new JComboBox<>(GioiTinh.values());
        cboGioiTinh.setBackground(Color.WHITE);
        cboGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField txtSDT = createStyledTextField();
        JTextField txtEmail = createStyledTextField();

        formPanel.add(createLabeledComponent("Số Báo Danh:", txtSBD));
        formPanel.add(createLabeledComponent("CCCD:", txtCCCD));
        formPanel.add(createLabeledComponent("Họ Đệm:", txtHo));
        formPanel.add(createLabeledComponent("Tên Thí Sinh:", txtTen));
        formPanel.add(createLabeledComponent("Ngày Sinh (dd/MM/yyyy):", txtNgaySinh));
        formPanel.add(createLabeledComponent("Giới Tính:", cboGioiTinh));
        formPanel.add(createLabeledComponent("Điện Thoại:", txtSDT));
        formPanel.add(createLabeledComponent("Email:", txtEmail));

        dialog.add(formPanel, BorderLayout.CENTER);

        // Footer Form (Buttons)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 30, 20, 30));

        JButton btnSave = createFlatButton("Lưu Dữ Liệu", new Color(46, 204, 113), new Color(39, 174, 96));
        btnSave.setPreferredSize(new Dimension(130, 40));
        JButton btnCancel = createFlatButton("Hủy bỏ", new Color(230, 230, 230), new Color(210, 210, 210));
        btnCancel.setForeground(new Color(80, 80, 80));

        btnSave.addActionListener(e -> {
            try {
                String ho = txtHo.getText().trim();
                String ten = txtTen.getText().trim();
                if (ho.isEmpty() || ten.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Họ và Tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                Date ngaySinh = null;
                if (!txtNgaySinh.getText().trim().isEmpty()) {
                    ngaySinh = sdf.parse(txtNgaySinh.getText().trim());
                }

                ThiSinhDTO ts = new ThiSinhDTO();
                ts.setHo(ho);
                ts.setTen(ten);
                ts.setCccd(txtCCCD.getText().trim());
                ts.setSoBaoDanh(txtSBD.getText().trim());
                ts.setNgaySinh(ngaySinh);
                ts.setGioiTinh((GioiTinh) cboGioiTinh.getSelectedItem());
                ts.setDienThoai(txtSDT.getText().trim());
                ts.setEmail(txtEmail.getText().trim());

                if (tsBUS.insert(ts)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm thí sinh thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm thất bại. Vui lòng kiểm tra lại thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Sai định dạng ngày sinh! Vui lòng nhập theo chuẩn dd/MM/yyyy", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JPanel createLabeledComponent(String labelText, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 100, 100));
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));
        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JTextField createStyledTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
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
            
            // Đổ bóng nhẹ
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Vẽ nền
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            // Vẽ viền
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            g2.dispose();
        }
    }
}