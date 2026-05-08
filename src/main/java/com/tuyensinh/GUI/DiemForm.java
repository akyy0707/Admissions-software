package com.tuyensinh.GUI;

import com.tuyensinh.BUS.DiemThiBUS;
import com.tuyensinh.DTO.DiemThiDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * DiemForm - Form nhập và quản lý điểm thi
 */
public class DiemForm extends JPanel {

    private DiemThiBUS diemThiBUS = new DiemThiBUS();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch = new JTextField(20);
    private JButton btnSearch, btnAdd, btnEdit, btnDelete, btnRefresh;

    public DiemForm() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        JLabel lblTitle = new JLabel("📝 QUẢN LÝ ĐIỂM THI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Center - Table
        String[] columns = {"CCCD", "Toán", "Văn", "Lý", "Hóa", "Sinh", "Sử", "Địa", "Ngoại ngữ", "Điểm TB"};
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
        searchPanel.add(new JLabel("CCCD:"));
        searchPanel.add(txtSearch);
        btnSearch = new JButton("Tìm");
        searchPanel.add(btnSearch);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Nhập Điểm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnRefresh = new JButton("Làm Mới");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
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
        txtSearch.addActionListener(e -> search());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        // Giả định load tất cả điểm
        List<DiemThiDTO> list = diemThiBUS.getAll();
        for (DiemThiDTO diem : list) {
            double tb = (diem.getTo() + diem.getVa() + diem.getLi() + diem.getHo() + diem.getSi() + diem.getSu() + diem.getDi()) / 7;
            Object[] row = {
                diem.getCccd(),
                diem.getTo(),
                diem.getVa(),
                diem.getLi(),
                diem.getHo(),
                diem.getSi(),
                diem.getSu(),
                diem.getDi(),
                diem.getN1_thi(),
                String.format("%.2f", tb)
            };
            tableModel.addRow(row);
        }
    }

    private void search() {
        String key = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        if (key.isEmpty()) {
            loadData();
        } else {
            DiemThiDTO diem = diemThiBUS.getByCCCD(key);
            if (diem != null) {
                double tb = (diem.getTo() + diem.getVa() + diem.getLi() + diem.getHo() + diem.getSi() + diem.getSu() + diem.getDi()) / 7;
                Object[] row = {
                    diem.getCccd(),
                    diem.getTo(),
                    diem.getVa(),
                    diem.getLi(),
                    diem.getHo(),
                    diem.getSi(),
                    diem.getSu(),
                    diem.getDi(),
                    diem.getN1_thi(),
                    String.format("%.2f", tb)
                };
                tableModel.addRow(row);
            }
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nhập Điểm Thi", true);
        dialog.setSize(450, 500);
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
            JOptionPane.showMessageDialog(dialog, "Nhập điểm thành công!");
            dialog.dispose();
            loadData();
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa!");
            return;
        }
        String cccd = (String) tableModel.getValueAt(selectedRow, 0);
        DiemThiDTO diem = diemThiBUS.getByCCCD(cccd);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Điểm Thi", true);
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = createFormPanel(diem);
        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            // Update logic here
            JOptionPane.showMessageDialog(dialog, "Cập nhật điểm thành công!");
            dialog.dispose();
            loadData();
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private JPanel createFormPanel(DiemThiDTO diem) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtCCCD = new JTextField(15);
        JTextField txtTo = new JTextField(10);
        JTextField txtVa = new JTextField(10);
        JTextField txtLi = new JTextField(10);
        JTextField txtHo = new JTextField(10);
        JTextField txtSi = new JTextField(10);
        JTextField txtSu = new JTextField(10);
        JTextField txtDi = new JTextField(10);
        JTextField txtNN = new JTextField(10);

        if (diem != null) {
            txtCCCD.setText(diem.getCccd());
            txtCCCD.setEditable(false);
            txtTo.setText(String.valueOf(diem.getTo()));
            txtVa.setText(String.valueOf(diem.getVa()));
            txtLi.setText(String.valueOf(diem.getLi()));
            txtHo.setText(String.valueOf(diem.getHo()));
            txtSi.setText(String.valueOf(diem.getSi()));
            txtSu.setText(String.valueOf(diem.getSu()));
            txtDi.setText(String.valueOf(diem.getDi()));
            txtNN.setText(String.valueOf(diem.getN1_thi()));
        }

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("CCCD:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCCCD, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Toán:"), gbc);
        gbc.gridx = 1;
        panel.add(txtTo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Văn:"), gbc);
        gbc.gridx = 1;
        panel.add(txtVa, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Lý:"), gbc);
        gbc.gridx = 1;
        panel.add(txtLi, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Hóa:"), gbc);
        gbc.gridx = 1;
        panel.add(txtHo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Sinh:"), gbc);
        gbc.gridx = 1;
        panel.add(txtSi, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Sử:"), gbc);
        gbc.gridx = 1;
        panel.add(txtSu, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Địa:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDi, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Ngoại Ngữ:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNN, gbc);

        return panel;
    }

    private void delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa điểm này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String cccd = (String) tableModel.getValueAt(selectedRow, 0);
            boolean success = diemThiBUS.delete(cccd);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
}