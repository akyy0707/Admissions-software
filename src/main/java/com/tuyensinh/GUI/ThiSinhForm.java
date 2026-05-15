package com.tuyensinh.GUI;

import com.tuyensinh.BUS.DiemThiBUS;
import com.tuyensinh.BUS.ThiSinhBUS;
import com.tuyensinh.DTO.DiemThiDTO;
import com.tuyensinh.DTO.ThiSinhDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class ThiSinhForm extends JPanel {

    private ThiSinhBUS thiSinhBUS = new ThiSinhBUS();
    private DiemThiBUS diemThiBUS = new DiemThiBUS();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnEdit, btnDelete, btnRefresh, btnThongKe, btnChiTiet, btnDiem;

    public ThiSinhForm() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 249, 250)); // Nền xám sáng hiện đại
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Tiêu đề trang
        JLabel lblTitle = new JLabel("QUẢN LÝ THÍ SINH");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        // Nội dung chính (Table + Toolbar)
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        RoundedPanel mainPanel = new RoundedPanel(20, Color.WHITE);
        mainPanel.setLayout(new BorderLayout(10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // ===== TOOLBAR =====
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setOpaque(false);

        // Search Panel (Bên trái)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(new Color(80, 80, 80));

        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(180, 38));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));

        btnSearch = createFlatButton("Tìm", new Color(149, 165, 166), new Color(127, 140, 141));
        btnSearch.setPreferredSize(new Dimension(80, 38));

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Action Panel (Bên phải - Tự động xuống dòng nếu khung hẹp)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        btnAdd = createFlatButton("Thêm", new Color(46, 204, 113), new Color(39, 174, 96));
        btnEdit = createFlatButton("Sửa", new Color(52, 152, 219), new Color(41, 128, 185));
        btnDelete = createFlatButton("Xóa", new Color(231, 76, 60), new Color(192, 57, 43));
        btnChiTiet = createFlatButton("Chi tiết", new Color(155, 89, 182), new Color(142, 68, 173));
        btnDiem = createFlatButton("Xem Điểm", new Color(230, 126, 34), new Color(211, 84, 0));
        btnThongKe = createFlatButton("Thống kê", new Color(22, 160, 133), new Color(26, 188, 156));
        btnRefresh = createFlatButton("Làm Mới", new Color(240, 240, 240), new Color(220, 220, 220));
        btnRefresh.setForeground(new Color(80, 80, 80));

        // Tinh chỉnh chiều rộng để vừa với màn hình
        Dimension btnSize = new Dimension(100, 38);
        btnAdd.setPreferredSize(btnSize); btnEdit.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize); btnChiTiet.setPreferredSize(btnSize);
        btnDiem.setPreferredSize(btnSize); btnThongKe.setPreferredSize(btnSize);
        btnRefresh.setPreferredSize(btnSize);

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnChiTiet);
        actionPanel.add(btnDiem);
        actionPanel.add(btnThongKe);
        actionPanel.add(btnRefresh);

        toolBar.add(searchPanel, BorderLayout.WEST);
        toolBar.add(actionPanel, BorderLayout.CENTER);
        mainPanel.add(toolBar, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"ID", "Số Báo Danh", "Họ", "Tên", "CCCD", "Ngày Sinh", "Giới Tính", "Điện Thoại", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(220, 235, 252));
        table.setSelectionForeground(new Color(30, 30, 30));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(240, 240, 240));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(new Color(80, 80, 80));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        table.getColumnModel().getColumn(0).setMaxWidth(60); // Thu gọn cột ID

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== EVENTS =====
        btnSearch.addActionListener(e -> search());
        txtSearch.addActionListener(e -> search()); // Nhấn Enter tìm kiếm
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> delete());
        btnRefresh.addActionListener(e -> loadData());
        btnThongKe.addActionListener(e -> showThongKe());
        btnChiTiet.addActionListener(e -> showChiTietThiSinh());
        btnDiem.addActionListener(e -> showDiemThiSinh());

        return mainPanel;
    }

    // ================= CUSTOM BUTTON =================
    private JButton createFlatButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    // ================= LOGIC DATA =================
    private void loadData() {
        tableModel.setRowCount(0);
        List<ThiSinhDTO> list = thiSinhBUS.getPage(0, 100);
        for (ThiSinhDTO ts : list) {
            Object[] row = {
                ts.getId(), ts.getSoBaoDanh(), ts.getHo(), ts.getTen(),
                ts.getCccd(), ts.getNgaySinh(), ts.getGioiTinh(),
                ts.getDienThoai(), ts.getEmail()
            };
            tableModel.addRow(row);
        }
    }

    private void search() {
        String key = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<ThiSinhDTO> list = key.isEmpty() ? thiSinhBUS.getPage(0, 100) : thiSinhBUS.search(key, 0, 100);
        for (ThiSinhDTO ts : list) {
            Object[] row = {
                ts.getId(), ts.getSoBaoDanh(), ts.getHo(), ts.getTen(),
                ts.getCccd(), ts.getNgaySinh(), ts.getGioiTinh(),
                ts.getDienThoai(), ts.getEmail()
            };
            tableModel.addRow(row);
        }
    }

    private void delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thí sinh cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa thí sinh này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            boolean success = thiSinhBUS.delete(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ================= FORMS & DIALOGS =================
    private void showAddDialog() {
        openFormDialog(null, "Thêm Thí Sinh Mới");
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thí sinh cần cập nhật!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        ThiSinhDTO ts = thiSinhBUS.getById(id);
        openFormDialog(ts, "Cập Nhật Thông Tin Thí Sinh");
    }

    private void openFormDialog(ThiSinhDTO ts, String title) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(750, 550); // Mở rộng để chứa thiết kế 2 cột
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());

        // Header Form
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        JLabel lblFormTitle = new JLabel(title.toUpperCase());
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblFormTitle.setForeground(new Color(50, 50, 50));
        headerPanel.add(lblFormTitle, BorderLayout.WEST);
        dialog.add(headerPanel, BorderLayout.NORTH);

        // Body Form (Lưới 5 dòng x 2 cột)
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 25, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 30, 20, 30));

        JTextField txtSBD = createStyledTextField();
        JTextField txtHo = createStyledTextField();
        JTextField txtTen = createStyledTextField();
        JTextField txtCCCD = createStyledTextField();
        JTextField txtDienThoai = createStyledTextField();
        JTextField txtEmail = createStyledTextField();
        JTextField txtNoiSinh = createStyledTextField();
        JComboBox<ThiSinhDTO.GioiTinh> cboGioiTinh = createStyledComboBox(ThiSinhDTO.GioiTinh.values());
        JComboBox<String> cboDoiTuong = createStyledComboBox(new String[]{"HS Lớp 12", "Tốt nghiệp THPT", "Khác"});
        JComboBox<String> cboKhuVuc = createStyledComboBox(new String[]{"KV1", "KV2", "KV2-NT", "KV3"});

        // Nạp dữ liệu cũ
        if (ts != null) {
            txtSBD.setText(ts.getSoBaoDanh());
            txtHo.setText(ts.getHo());
            txtTen.setText(ts.getTen());
            txtCCCD.setText(ts.getCccd());
            txtDienThoai.setText(ts.getDienThoai());
            txtEmail.setText(ts.getEmail());
            txtNoiSinh.setText(ts.getNoiSinh());
            cboGioiTinh.setSelectedItem(ts.getGioiTinh());
            
            // Fix code cũ: Nạp lại đối tượng và khu vực
            if(ts.getDoiTuong() != null) cboDoiTuong.setSelectedItem(ts.getDoiTuong());
            if(ts.getKhuVuc() != null) cboKhuVuc.setSelectedItem(ts.getKhuVuc());
        }

        formPanel.add(createLabeledComponent("Số Báo Danh:", txtSBD));
        formPanel.add(createLabeledComponent("CCCD:", txtCCCD));
        formPanel.add(createLabeledComponent("Họ Đệm:", txtHo));
        formPanel.add(createLabeledComponent("Tên Thí Sinh:", txtTen));
        formPanel.add(createLabeledComponent("Số Điện Thoại:", txtDienThoai));
        formPanel.add(createLabeledComponent("Email:", txtEmail));
        formPanel.add(createLabeledComponent("Giới Tính:", cboGioiTinh));
        formPanel.add(createLabeledComponent("Nơi Sinh:", txtNoiSinh));
        formPanel.add(createLabeledComponent("Đối Tượng:", cboDoiTuong));
        formPanel.add(createLabeledComponent("Khu Vực:", cboKhuVuc));

        dialog.add(formPanel, BorderLayout.CENTER);

        // Footer Form
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 30, 20, 30));

        JButton btnSave = createFlatButton("Lưu Dữ Liệu", new Color(46, 204, 113), new Color(39, 174, 96));
        btnSave.setPreferredSize(new Dimension(130, 40));
        JButton btnCancel = createFlatButton("Hủy bỏ", new Color(230, 230, 230), new Color(210, 210, 210));
        btnCancel.setForeground(new Color(80, 80, 80));

        btnSave.addActionListener(e -> {
            // (Thêm logic lưu/cập nhật xuống Database ở đây)
            JOptionPane.showMessageDialog(dialog, "Lưu thông tin thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            loadData();
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

    private <T> JComboBox<T> createStyledComboBox(T[] items) {
        JComboBox<T> cbo = new JComboBox<>(items);
        cbo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbo.setBackground(Color.WHITE);
        return cbo;
    }

    // ================= CHỨC NĂNG PHỤ (THỐNG KÊ, CHI TIẾT, ĐIỂM) =================
    private void showThongKe() {
        long tong = thiSinhBUS.count();
        Map<String, Long> theoDoiTuong = thiSinhBUS.countByDoiTuong();
        Map<String, Long> theoKhuVuc = thiSinhBUS.countByKhuVuc();

        StringBuilder sb = new StringBuilder();
        sb.append("THỐNG KÊ THÍ SINH\n\n");
        sb.append("Tổng số thí sinh: ").append(tong).append("\n\n");

        sb.append("Theo đối tượng:\n");
        if (theoDoiTuong.isEmpty()) sb.append("- Chưa có dữ liệu\n");
        else theoDoiTuong.forEach((k, v) -> sb.append("- ").append(k).append(": ").append(v).append("\n"));

        sb.append("\nTheo khu vực:\n");
        if (theoKhuVuc.isEmpty()) sb.append("- Chưa có dữ liệu\n");
        else theoKhuVuc.forEach((k, v) -> sb.append("- ").append(k).append(": ").append(v).append("\n"));

        JOptionPane.showMessageDialog(this, sb.toString(), "Bảng Thống Kê", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showChiTietThiSinh() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thí sinh!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        ThiSinhDTO ts = thiSinhBUS.getById(id);
        if (ts == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thí sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String message = String.format(
                "THÔNG TIN CHI TIẾT\n\n" +
                "Số báo danh:\t%s\n" +
                "Họ tên:\t%s %s\n" +
                "CCCD:\t%s\n" +
                "Ngày sinh:\t%s\n" +
                "Giới tính:\t%s\n" +
                "Điện thoại:\t%s\n" +
                "Email:\t%s\n" +
                "Nơi sinh:\t%s\n" +
                "Đối tượng:\t%s\n" +
                "Khu vực:\t%s",
                ts.getSoBaoDanh(), ts.getHo(), ts.getTen(), ts.getCccd(),
                ts.getNgaySinh(), ts.getGioiTinh(), ts.getDienThoai(),
                ts.getEmail(), ts.getNoiSinh(), ts.getDoiTuong(), ts.getKhuVuc()
        );

        JOptionPane.showMessageDialog(this, message, "Chi Tiết Thí Sinh", JOptionPane.INFORMATION_MESSAGE);
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
        sb.append("ĐIỂM VSAT: ").append(diem.getNk1() > 0 || diem.getNk2() > 0 ? "NK1: " + diem.getNk1() + "  |  NK2: " + diem.getNk2() : "Chưa có").append("\n");

        JOptionPane.showMessageDialog(this, sb.toString(), "Kết Quả Điểm Thi", JOptionPane.INFORMATION_MESSAGE);
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
            
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }
}