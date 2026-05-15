package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.tuyensinh.BUS.NganhToHopBUS;
import com.tuyensinh.DTO.NganhToHopDTO;

public class NganhToHopPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private NganhToHopBUS bus;

    public NganhToHopPanel() {

        bus = new NganhToHopBUS();

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadData();
    }

    // ================= HEADER =================
    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ NGÀNH - TỔ HỢP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JButton btnAdd = new JButton("+ Thêm liên kết");

        btnAdd.setFocusPainted(false);
        btnAdd.setBackground(new Color(52, 152, 219));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setPreferredSize(new Dimension(160, 40));

        btnAdd.addActionListener(e -> openForm(null));

        panel.add(lblTitle, BorderLayout.WEST);
        panel.add(btnAdd, BorderLayout.EAST);

        return panel;
    }

    // ================= TABLE =================
    private JPanel createTablePanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        String[] columns = {
                "ID",
                "Mã ngành",
                "Mã tổ hợp",
                "Môn 1",
                "Môn 2",
                "Môn 3",
                "Độ lệch"
        };

        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);

        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        table.getTableHeader().setBackground(
                new Color(245, 246, 250)
        );

        table.setSelectionBackground(
                new Color(52, 152, 219)
        );

        JScrollPane scrollPane = new JScrollPane(table);

        // ===== ACTION BUTTON =====
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);

        JButton btnEdit = createButton("Sửa", new Color(46, 204, 113));
        JButton btnDelete = createButton("Xóa", new Color(231, 76, 60));
        JButton btnRefresh = createButton("Làm mới", new Color(52, 152, 219));

        btnEdit.addActionListener(e -> editSelected());

        btnDelete.addActionListener(e -> deleteSelected());

        btnRefresh.addActionListener(e -> loadData());

        actionPanel.add(btnRefresh);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);

        panel.add(actionPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ================= BUTTON =================
    private JButton createButton(String text, Color color) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btn.setPreferredSize(new Dimension(110, 38));

        return btn;
    }

    // ================= LOAD =================
    private void loadData() {

        model.setRowCount(0);

        List<NganhToHopDTO> list = bus.getAll();

        if (list == null) return;

        for (NganhToHopDTO n : list) {

            model.addRow(new Object[]{
                    n.getId(),
                    n.getMaNganh(),
                    n.getMaToHop(),
                    n.getThMon1(),
                    n.getThMon2(),
                    n.getThMon3(),
                    n.getDoLech()
            });
        }
    }

    // ================= FORM =================
    private void openForm(NganhToHopDTO data) {

        JDialog dialog = new JDialog();

        dialog.setTitle(
                data == null
                        ? "Thêm ngành - tổ hợp"
                        : "Cập nhật ngành - tổ hợp"
        );

        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel(
                data == null
                        ? "THÊM LIÊN KẾT"
                        : "CẬP NHẬT LIÊN KẾT"
        );

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(25));

        // ===== INPUT =====
        JTextField txtMaNganh = createInput(panel, "Mã ngành");
        JTextField txtMaToHop = createInput(panel, "Mã tổ hợp");

        JTextField txtMon1 = createInput(panel, "Môn 1");
        JTextField txtMon2 = createInput(panel, "Môn 2");
        JTextField txtMon3 = createInput(panel, "Môn 3");

        JTextField txtDoLech = createInput(panel, "Độ lệch");

        // ===== DATA =====
        if (data != null) {

            txtMaNganh.setText(data.getMaNganh());
            txtMaToHop.setText(data.getMaToHop());

            txtMon1.setText(data.getThMon1());
            txtMon2.setText(data.getThMon2());
            txtMon3.setText(data.getThMon3());

            if (data.getDoLech() != null) {
                txtDoLech.setText(String.valueOf(data.getDoLech()));
            }
        }

        // ===== BUTTON =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);

        JButton btnSave = new JButton(
                data == null ? "Thêm mới" : "Cập nhật"
        );

        btnSave.setFocusPainted(false);

        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);

        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSave.setPreferredSize(new Dimension(140, 42));

        bottom.add(btnSave);

        panel.add(Box.createVerticalStrut(20));
        panel.add(bottom);

        // ===== SAVE =====
        btnSave.addActionListener(e -> {

            try {

                NganhToHopDTO dto = new NganhToHopDTO();

                if (data != null) {
                    dto.setId(data.getId());
                }

                dto.setMaNganh(txtMaNganh.getText());
                dto.setMaToHop(txtMaToHop.getText());

                dto.setThMon1(txtMon1.getText());
                dto.setThMon2(txtMon2.getText());
                dto.setThMon3(txtMon3.getText());

                dto.setDoLech(
                        Double.parseDouble(txtDoLech.getText())
                );

                String result;

                if (data == null) {
                    result = bus.addMapping(dto);
                } else {
                    result = "Đã cập nhật!";
                }

                JOptionPane.showMessageDialog(dialog, result);

                loadData();

                dialog.dispose();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Dữ liệu không hợp lệ!"
                );
            }
        });

        dialog.add(panel);

        dialog.setVisible(true);
    }

    // ================= INPUT =================
    private JTextField createInput(JPanel panel, String label) {

        JLabel lbl = new JLabel(label);

        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txt = new JTextField();

        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
        panel.add(txt);
        panel.add(Box.createVerticalStrut(18));

        return txt;
    }

    // ================= EDIT =================
    private void editSelected() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn dữ liệu!"
            );

            return;
        }

        NganhToHopDTO dto = new NganhToHopDTO();

        dto.setId((int) model.getValueAt(row, 0));

        dto.setMaNganh(model.getValueAt(row, 1).toString());
        dto.setMaToHop(model.getValueAt(row, 2).toString());

        dto.setThMon1(model.getValueAt(row, 3).toString());
        dto.setThMon2(model.getValueAt(row, 4).toString());
        dto.setThMon3(model.getValueAt(row, 5).toString());

        Object value = model.getValueAt(row, 6);

        if (value != null) {
            dto.setDoLech(Double.parseDouble(value.toString()));
        }

        openForm(dto);
    }

    // ================= DELETE =================
    private void deleteSelected() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn dữ liệu!"
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            int id = (int) model.getValueAt(row, 0);

            String result = bus.deleteMapping(id);

            JOptionPane.showMessageDialog(this, result);

            loadData();
        }
    }
}