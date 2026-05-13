package com.tuyensinh.GUI;

import com.tuyensinh.BUS.DiemThiBUS;
import com.tuyensinh.BUS.ThiSinhBUS;
import com.tuyensinh.DTO.DiemThiDTO;
import com.tuyensinh.DTO.ThiSinhDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ThiSinhForm - Form quản lý thông tin thí sinh
 */
public class ThiSinhForm extends JPanel {

    private ThiSinhBUS thiSinhBUS = new ThiSinhBUS();
    private DiemThiBUS diemThiBUS = new DiemThiBUS();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch = new JTextField(20);
    private JButton btnSearch, btnAdd, btnEdit, btnDelete, btnRefresh, btnThongKe, btnChiTiet, btnDiem;

    public ThiSinhForm() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        JLabel lblTitle = new JLabel("📚 QUẢN LÝ THÍ SINH");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Center - Table
        String[] columns = {"ID", "Số Báo Danh", "Họ", "Tên", "CCCD", "Ngày Sinh", "Giới Tính", "Điện Thoại", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // South - Search & Buttons
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("🔍 Tìm kiếm"));
        searchPanel.add(new JLabel("Từ khóa:"));
        searchPanel.add(txtSearch);
        btnSearch = new JButton("Tìm");
        btnSearch.setIcon(new ImageIcon());
        searchPanel.add(btnSearch);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Thêm Mới");
        btnAdd.setIcon(new ImageIcon());
        btnEdit = new JButton("Sửa");
        btnEdit.setIcon(new ImageIcon());
        btnDelete = new JButton("Xóa");
        btnDelete.setIcon(new ImageIcon());
        btnChiTiet = new JButton("Chi Tiết");
        btnChiTiet.setIcon(new ImageIcon());
        btnDiem = new JButton("Điểm");
        btnDiem.setIcon(new ImageIcon());
        btnThongKe = new JButton("Thống Kê");
        btnThongKe.setIcon(new ImageIcon());
        btnRefresh = new JButton("Làm Mới");
        btnRefresh.setIcon(new ImageIcon());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnChiTiet);
        buttonPanel.add(btnDiem);
        buttonPanel.add(btnThongKe);
        buttonPanel.add(btnRefresh);

        southPanel.add(searchPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnSearch.addActionListener(e -> search());
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> delete());
        btnRefresh.addActionListener(e -> loadData());
        btnThongKe.addActionListener(e -> showThongKe());
        btnChiTiet.addActionListener(e -> showChiTietThiSinh());
        btnDiem.addActionListener(e -> showDiemThiSinh());
        txtSearch.addActionListener(e -> search());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<ThiSinhDTO> list = thiSinhBUS.getPage(0, 100);
        for (ThiSinhDTO ts : list) {
            Object[] row = {
                ts.getId(),
                ts.getSoBaoDanh(),
                ts.getHo(),
                ts.getTen(),
                ts.getCccd(),
                ts.getNgaySinh(),
                ts.getGioiTinh(),
                ts.getDienThoai(),
                ts.getEmail()
            };
            tableModel.addRow(row);
        }
    }

    private void search() {
        String key = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<ThiSinhDTO> list = key.isEmpty() 
            ? thiSinhBUS.getPage(0, 100)
            : thiSinhBUS.search(key, 0, 100);
        for (ThiSinhDTO ts : list) {
            Object[] row = {
                ts.getId(),
                ts.getSoBaoDanh(),
                ts.getHo(),
                ts.getTen(),
                ts.getCccd(),
                ts.getNgaySinh(),
                ts.getGioiTinh(),
                ts.getDienThoai(),
                ts.getEmail()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Thí Sinh Mới", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = createFormPanel(null);
        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            // Save logic here
            JOptionPane.showMessageDialog(dialog, "Thêm thí sinh thành công!");
            dialog.dispose();
            loadData();
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thí sinh cần sửa!");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        ThiSinhDTO ts = thiSinhBUS.getById(id);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Thông Tin Thí Sinh", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = createFormPanel(ts);
        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            // Update logic here
            JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
            dialog.dispose();
            loadData();
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private JPanel createFormPanel(ThiSinhDTO ts) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtSBD = new JTextField(20);
        JTextField txtHo = new JTextField(20);
        JTextField txtTen = new JTextField(20);
        JTextField txtCCCD = new JTextField(20);
        JTextField txtDienThoai = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JTextField txtNoiSinh = new JTextField(20);
        JComboBox<ThiSinhDTO.GioiTinh> cboGioiTinh = new JComboBox<>(ThiSinhDTO.GioiTinh.values());
        JComboBox<String> cboDoiTuong = new JComboBox<>(new String[]{"HS Lớp 12", "Tốt nghiệp THPT", "Khác"});
        JComboBox<String> cboKhuVuc = new JComboBox<>(new String[]{"KV1", "KV2", "KV2-NT", "KV3"});

        if (ts != null) {
            txtSBD.setText(ts.getSoBaoDanh());
            txtHo.setText(ts.getHo());
            txtTen.setText(ts.getTen());
            txtCCCD.setText(ts.getCccd());
            txtDienThoai.setText(ts.getDienThoai());
            txtEmail.setText(ts.getEmail());
            txtNoiSinh.setText(ts.getNoiSinh());
            cboGioiTinh.setSelectedItem(ts.getGioiTinh());
        }

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Số Báo Danh:"), gbc);
        gbc.gridx = 1;
        panel.add(txtSBD, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Họ:"), gbc);
        gbc.gridx = 1;
        panel.add(txtHo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1;
        panel.add(txtTen, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("CCCD:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCCCD, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Giới Tính:"), gbc);
        gbc.gridx = 1;
        panel.add(cboGioiTinh, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Điện Thoại:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDienThoai, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nơi Sinh:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNoiSinh, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Đối Tượng:"), gbc);
        gbc.gridx = 1;
        panel.add(cboDoiTuong, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Khu Vực:"), gbc);
        gbc.gridx = 1;
        panel.add(cboKhuVuc, gbc);

        return panel;
    }

    private void showThongKe() {
        long tong = thiSinhBUS.count();
        java.util.Map<String, Long> theoDoiTuong = thiSinhBUS.countByDoiTuong();
        java.util.Map<String, Long> theoKhuVuc = thiSinhBUS.countByKhuVuc();

        StringBuilder sb = new StringBuilder();
        sb.append("📊 THỐNG KÊ THÍ SINH\n\n");
        sb.append("Tổng số thí sinh: ").append(tong).append("\n\n");

        sb.append("Theo đối tượng:\n");
        if (theoDoiTuong.isEmpty()) {
            sb.append("- Chưa có dữ liệu\n");
        } else {
            for (java.util.Map.Entry<String, Long> entry : theoDoiTuong.entrySet()) {
                sb.append("- ").append(entry.getKey()).append(": ")
                        .append(entry.getValue()).append("\n");
            }
        }

        sb.append("\nTheo khu vực:\n");
        if (theoKhuVuc.isEmpty()) {
            sb.append("- Chưa có dữ liệu\n");
        } else {
            for (java.util.Map.Entry<String, Long> entry : theoKhuVuc.entrySet()) {
                sb.append("- ").append(entry.getKey()).append(": ")
                        .append(entry.getValue()).append("\n");
            }
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Thống Kê", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showChiTietThiSinh() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thí sinh!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        ThiSinhDTO ts = thiSinhBUS.getById(id);
        if (ts == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thí sinh!");
            return;
        }

        String message = String.format(
                "THÔNG TIN THÍ SINH\n\n" +
                "Số báo danh: %s\n" +
                "Họ tên: %s %s\n" +
                "CCCD: %s\n" +
                "Ngày sinh: %s\n" +
                "Giới tính: %s\n" +
                "Điện thoại: %s\n" +
                "Email: %s\n" +
                "Nơi sinh: %s\n" +
                "Đối tượng: %s\n" +
                "Khu vực: %s",
                ts.getSoBaoDanh(),
                ts.getHo(),
                ts.getTen(),
                ts.getCccd(),
                ts.getNgaySinh(),
                ts.getGioiTinh(),
                ts.getDienThoai(),
                ts.getEmail(),
                ts.getNoiSinh(),
                ts.getDoiTuong(),
                ts.getKhuVuc()
        );

        JOptionPane.showMessageDialog(this, message, "Chi Tiết Thí Sinh", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showDiemThiSinh() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thí sinh!");
            return;
        }

        String cccd = (String) tableModel.getValueAt(selectedRow, 4);
        DiemThiDTO diem = diemThiBUS.getByCCCD(cccd);
        if (diem == null) {
            JOptionPane.showMessageDialog(this, "Chưa có dữ liệu điểm cho thí sinh này!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ĐIỂM THI THPT\n");
        sb.append("Toán: ").append(diem.getTo()).append(" | Văn: ").append(diem.getVa())
                .append(" | Lý: ").append(diem.getLi()).append(" | Hóa: ").append(diem.getHo()).append("\n");
        sb.append("Sinh: ").append(diem.getSi()).append(" | Sử: ").append(diem.getSu())
                .append(" | Địa: ").append(diem.getDi()).append(" | Ngoại ngữ: ").append(diem.getN1_thi()).append("\n");
        sb.append("KTPL: ").append(diem.getKtpl()).append(" | Tin: ").append(diem.getTi())
                .append(" | CNCN: ").append(diem.getCncn()).append(" | CNNN: ").append(diem.getCnnn()).append("\n\n");

        sb.append("ĐIỂM ĐGNL\n");
        if (diem.getNl1() > 0) {
            sb.append("NL1: ").append(diem.getNl1()).append("\n\n");
        } else {
            sb.append("Chưa có\n\n");
        }

        sb.append("ĐIỂM VSAT\n");
        if (diem.getNk1() > 0 || diem.getNk2() > 0) {
            sb.append("NK1: ").append(diem.getNk1()).append(" | NK2: ").append(diem.getNk2()).append("\n");
        } else {
            sb.append("Chưa có\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Điểm Thí Sinh", JOptionPane.INFORMATION_MESSAGE);
    }

    private void delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thí sinh cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa thí sinh này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            boolean success = thiSinhBUS.delete(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
}