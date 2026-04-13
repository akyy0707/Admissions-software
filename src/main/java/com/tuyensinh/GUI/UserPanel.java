package com.tuyensinh.GUI;

import com.tuyensinh.BUS.UserBUS;
import com.tuyensinh.DTO.UserDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private UserBUS userBUS = new UserBUS();

    public UserPanel() {
        setLayout(new BorderLayout());

        // 🔥 Table
        String[] cols = {"ID", "Username", "Role"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        // 🔥 Top panel
        JPanel top = new JPanel();

        JButton btnLoad = new JButton("Load");
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");

        top.add(btnLoad);
        top.add(btnAdd);
        top.add(btnDelete);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 🔥 load dữ liệu ban đầu
        loadData();

        // 🔥 Event
        btnLoad.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> addUser());
        btnDelete.addActionListener(e -> deleteUser());
    }

    // 🔹 Load dữ liệu
    private void loadData() {
        model.setRowCount(0);

        // ❗ bạn cần có hàm getAll() trong UserDAO
        List<UserDTO> list = userBUS.getAll();

        for (UserDTO u : list) {
            model.addRow(new Object[]{
                    u.getId(),
                    u.getUsername(),
                    u.getRole()
            });
        }
    }

    // 🔹 Thêm user
    private void addUser() {
        String username = JOptionPane.showInputDialog(this, "Nhập username:");
        String password = JOptionPane.showInputDialog(this, "Nhập password:");

        if (username == null || password == null) return;

        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(UserDTO.Role.USER);

        if (userBUS.insert(user)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }

    // 🔹 Xóa user
    private void deleteUser() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn user cần xóa!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        if (userBUS.delete(id)) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!");
        }
    }
}