package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
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

import com.tuyensinh.BUS.ToHopBUS;
import com.tuyensinh.DTO.ToHopDTO;

public class ToHopPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private ToHopBUS bus;

    public ToHopPanel() {

        bus = new ToHopBUS();

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

        JLabel lblTitle = new JLabel("QUẢN LÝ TỔ HỢP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JButton btnAdd = new JButton("+ Thêm tổ hợp");

        btnAdd.setFocusPainted(false);
        btnAdd.setBackground(new Color(52, 152, 219));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setPreferredSize(new Dimension(150, 40));

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

        // ===== ACTION =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(Color.WHITE);

        JButton btnRefresh = createButton(
                "Làm mới",
                new Color(52, 152, 219)
        );

        JButton btnEdit = createButton(
                "Sửa",
                new Color(46, 204, 113)
        );

        JButton btnDelete = createButton(
                "Xóa",
                new Color(231, 76, 60)
        );

        btnRefresh.addActionListener(e -> loadData());

        btnEdit.addActionListener(e -> editSelected());

        btnDelete.addActionListener(e -> deleteSelected());

        topPanel.add(btnRefresh);
        topPanel.add(btnEdit);
        topPanel.add(btnDelete);

        // ===== TABLE =====
        String[] columns = {
                "ID",
                "Mã tổ hợp",
                "Môn 1",
                "Môn 2",
                "Môn 3",
                "Tên tổ hợp"
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

        table.setShowGrid(false);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(topPanel, BorderLayout.NORTH);
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

        List<ToHopDTO> list = bus.getAll();

        if (list == null) return;

        for (ToHopDTO t : list) {

            model.addRow(new Object[]{
                    t.getIdToHop(),
                    t.getMaToHop(),
                    t.getMon1(),
                    t.getMon2(),
                    t.getMon3(),
                    t.getTenToHop()
            });
        }
    }

    // ================= FORM =================
    private void openForm(ToHopDTO data) {

        JDialog dialog = new JDialog();

        dialog.setTitle(
                data == null
                        ? "Thêm tổ hợp"
                        : "Cập nhật tổ hợp"
        );

        dialog.setSize(400, 580);

        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();

        panel.setBackground(Color.WHITE);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel(
                data == null
                        ? "THÊM TỔ HỢP"
                        : "CẬP NHẬT TỔ HỢP"
        );

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(25));

        // ===== INPUT =====
        JTextField txtMa = createInput(panel, "Mã tổ hợp");

        JTextField txtMon1 = createInput(panel, "Môn 1");
        JTextField txtMon2 = createInput(panel, "Môn 2");
        JTextField txtMon3 = createInput(panel, "Môn 3");

        JTextField txtTen = createInput(panel, "Tên tổ hợp");

        // ===== DATA =====
        if (data != null) {

            txtMa.setText(data.getMaToHop());

            txtMon1.setText(data.getMon1());
            txtMon2.setText(data.getMon2());
            txtMon3.setText(data.getMon3());

            txtTen.setText(data.getTenToHop());
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

                ToHopDTO dto = new ToHopDTO();

                if (data != null) {
                    dto.setIdToHop(data.getIdToHop());
                }

                dto.setMaToHop(txtMa.getText());

                dto.setMon1(txtMon1.getText());
                dto.setMon2(txtMon2.getText());
                dto.setMon3(txtMon3.getText());

                dto.setTenToHop(txtTen.getText());

                String result;

                if (data == null) {
                    result = bus.addToHop(dto);
                } else {
                    result = bus.updateToHop(dto);
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

        JTextField txt = new JTextField();

        txt.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42)
        );

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

        ToHopDTO dto = new ToHopDTO();

        dto.setIdToHop(
                (int) model.getValueAt(row, 0)
        );

        dto.setMaToHop(
                model.getValueAt(row, 1).toString()
        );

        dto.setMon1(
                model.getValueAt(row, 2).toString()
        );

        dto.setMon2(
                model.getValueAt(row, 3).toString()
        );

        dto.setMon3(
                model.getValueAt(row, 4).toString()
        );

        dto.setTenToHop(
                model.getValueAt(row, 5).toString()
        );

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

            String result = bus.deleteToHop(id);

            JOptionPane.showMessageDialog(this, result);

            loadData();
        }
    }
}