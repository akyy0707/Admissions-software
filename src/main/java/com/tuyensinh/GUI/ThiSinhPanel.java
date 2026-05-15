package com.tuyensinh.GUI;

import com.tuyensinh.BUS.ThiSinhBUS;
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

public class ThiSinhPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private ThiSinhBUS tsBUS = new ThiSinhBUS();

    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnDelete, btnRefresh;

    public ThiSinhPanel() {
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

        // Nội dung chính (Toolbar + Table)
        add(createMainPanel(), BorderLayout.CENTER);
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

        btnAdd = createFlatButton("Thêm mới", new Color(46, 204, 113), new Color(39, 174, 96));
        btnDelete = createFlatButton("Xóa", new Color(231, 76, 60), new Color(192, 57, 43));
        btnRefresh = createFlatButton("Làm Mới", new Color(240, 240, 240), new Color(220, 220, 220));
        btnRefresh.setForeground(new Color(80, 80, 80));

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

        // ===== SỰ KIỆN =====
        btnSearch.addActionListener(e -> search());
        txtSearch.addActionListener(e -> search()); // Nhấn Enter ở ô tìm kiếm
        btnAdd.addActionListener(e -> openAddDialog());
        btnDelete.addActionListener(e -> deleteThiSinh());
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

    // ================= XỬ LÝ DỮ LIỆU =================
    private void loadData() {
        tableModel.setRowCount(0);
        List<ThiSinhDTO> list = tsBUS.getPage(1, 50);

        for (ThiSinhDTO ts : list) {
            tableModel.addRow(new Object[]{
                    ts.getId(), ts.getSoBaoDanh(), ts.getHo(), ts.getTen(),
                    ts.getCccd(), ts.getNgaySinh() != null ? new SimpleDateFormat("dd/MM/yyyy").format(ts.getNgaySinh()) : "", 
                    ts.getGioiTinh(), ts.getDienThoai(), ts.getEmail()
            });
        }
    }

    private void search() {
        String key = txtSearch.getText().trim();
        if (key.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        List<ThiSinhDTO> list = tsBUS.search(key, 1, 50);

        for (ThiSinhDTO ts : list) {
            tableModel.addRow(new Object[]{
                    ts.getId(), ts.getSoBaoDanh(), ts.getHo(), ts.getTen(),
                    ts.getCccd(), ts.getNgaySinh() != null ? new SimpleDateFormat("dd/MM/yyyy").format(ts.getNgaySinh()) : "", 
                    ts.getGioiTinh(), ts.getDienThoai(), ts.getEmail()
            });
        }
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