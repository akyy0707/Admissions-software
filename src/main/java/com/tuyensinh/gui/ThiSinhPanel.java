package com.tuyensinh.GUI;

import com.tuyensinh.BUS.ThiSinhBUS;
import com.tuyensinh.DTO.ThiSinhDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ThiSinhPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private ThiSinhBUS tsBUS = new ThiSinhBUS();

    private JTextField txtSearch;

    public ThiSinhPanel() {
        setLayout(new BorderLayout());

        String[] cols = {"ID", "Họ", "Tên", "CCCD"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        JPanel top = new JPanel();

        txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Search");
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");

        top.add(txtSearch);
        top.add(btnSearch);
        top.add(btnAdd);
        top.add(btnDelete);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

        btnSearch.addActionListener(e -> search());
        btnAdd.addActionListener(e -> addThiSinh());
        btnDelete.addActionListener(e -> deleteThiSinh());
    }

    private void loadData() {
        model.setRowCount(0);

        List<ThiSinhDTO> list = tsBUS.getPage(1, 20);

        for (ThiSinhDTO ts : list) {
            model.addRow(new Object[]{
                    ts.getId(),
                    ts.getHo(),
                    ts.getTen(),
                    ts.getCccd()
            });
        }
    }

    private void search() {
        String key = txtSearch.getText();

        model.setRowCount(0);

        List<ThiSinhDTO> list = tsBUS.search(key, 1, 50);

        for (ThiSinhDTO ts : list) {
            model.addRow(new Object[]{
                    ts.getId(),
                    ts.getHo(),
                    ts.getTen(),
                    ts.getCccd()
            });
        }
    }

    private void addThiSinh() {
        String ho = JOptionPane.showInputDialog(this, "Nhập họ:");
        String ten = JOptionPane.showInputDialog(this, "Nhập tên:");
        String cccd = JOptionPane.showInputDialog(this, "Nhập CCCD:");

        if (ho == null || ten == null) return;

        ThiSinhDTO ts = new ThiSinhDTO();
        ts.setHo(ho);
        ts.setTen(ten);
        ts.setCccd(cccd);

        if (tsBUS.insert(ts)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }

    private void deleteThiSinh() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần xóa!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        if (tsBUS.delete(id)) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!");
        }
    }
}