package com.tuyensinh.GUI;

import com.tuyensinh.BUS.ThiSinhBUS;
import com.tuyensinh.DTO.ThiSinhDTO;
import com.tuyensinh.DTO.ThiSinhDTO.GioiTinh;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThiSinhPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private ThiSinhBUS tsBUS = new ThiSinhBUS();

    private JTextField txtSearch;

    public ThiSinhPanel() {
        setLayout(new BorderLayout());

        // ===== COLUMNS =====
        String[] columns = {
                "ID", "Số Báo Danh", "Họ", "Tên",
                "CCCD", "Ngày Sinh", "Giới Tính",
                "Điện Thoại", "Email"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);

        // ===== TOP PANEL =====
        JPanel top = new JPanel();

        txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("🔍 Tìm");
        JButton btnAdd = new JButton("➕ Thêm");
        JButton btnDelete = new JButton("🗑 Xóa");

        top.add(new JLabel("Tìm kiếm:"));
        top.add(txtSearch);
        top.add(btnSearch);
        top.add(btnAdd);
        top.add(btnDelete);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

        // ===== EVENTS =====
        btnSearch.addActionListener(e -> search());
        btnAdd.addActionListener(e -> addThiSinh());
        btnDelete.addActionListener(e -> deleteThiSinh());
    }

    // ===== LOAD DATA =====
    private void loadData() {
        tableModel.setRowCount(0);

        List<ThiSinhDTO> list = tsBUS.getPage(1, 50);

        for (ThiSinhDTO ts : list) {
            tableModel.addRow(new Object[]{
                    ts.getId(),
                    ts.getSoBaoDanh(),
                    ts.getHo(),
                    ts.getTen(),
                    ts.getCccd(),
                    ts.getNgaySinh(),
                    ts.getGioiTinh(),
                    ts.getDienThoai(),
                    ts.getEmail()
            });
        }
    }

    // ===== SEARCH =====
    private void search() {
        String key = txtSearch.getText().trim();

        tableModel.setRowCount(0);

        List<ThiSinhDTO> list = tsBUS.search(key, 1, 50);

        for (ThiSinhDTO ts : list) {
            tableModel.addRow(new Object[]{
                    ts.getId(),
                    ts.getSoBaoDanh(),
                    ts.getHo(),
                    ts.getTen(),
                    ts.getCccd(),
                    ts.getNgaySinh(),
                    ts.getGioiTinh(),
                    ts.getDienThoai(),
                    ts.getEmail()
            });
        }
    }

    // ===== ADD =====
    private void addThiSinh() {
    try {
        String ho = JOptionPane.showInputDialog(this, "Nhập họ:");
        String ten = JOptionPane.showInputDialog(this, "Nhập tên:");
        String cccd = JOptionPane.showInputDialog(this, "Nhập CCCD:");
        String sbd = JOptionPane.showInputDialog(this, "Nhập số báo danh:");

        String ngaySinhStr = JOptionPane.showInputDialog(this, "Nhập ngày sinh (dd/MM/yyyy):");

        // chọn giới tính bằng dropdown (chuẩn)
        GioiTinh gioiTinh = (GioiTinh) JOptionPane.showInputDialog(
                this,
                "Chọn giới tính:",
                "Giới tính",
                JOptionPane.QUESTION_MESSAGE,
                null,
                GioiTinh.values(),
                GioiTinh.Nam
        );

        String sdt = JOptionPane.showInputDialog(this, "Nhập điện thoại:");
        String email = JOptionPane.showInputDialog(this, "Nhập email:");

        if (ho == null || ten == null) return;

        // ===== PARSE DATE =====
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date ngaySinh = sdf.parse(ngaySinhStr);

        // ===== SET DTO =====
        ThiSinhDTO ts = new ThiSinhDTO();
        ts.setHo(ho);
        ts.setTen(ten);
        ts.setCccd(cccd);
        ts.setSoBaoDanh(sbd);
        ts.setNgaySinh(ngaySinh); // ✅ đúng kiểu Date
        ts.setGioiTinh(gioiTinh); // ✅ đúng enum
        ts.setDienThoai(sdt);
        ts.setEmail(email);

        if (tsBUS.insert(ts)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Sai định dạng ngày! (dd/MM/yyyy)");
    }
}
    // ===== DELETE =====
    private void deleteThiSinh() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần xóa!");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        if (tsBUS.delete(id)) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!");
        }
    }
}