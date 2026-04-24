package com.tuyensinh.GUI;

import com.tuyensinh.BUS.NganhBUS;
import com.tuyensinh.DTO.NganhDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NguyenVongForm - Form quản lý nguyện vọng của thí sinh
 */
public class NguyenVongForm extends JPanel {

    private NganhBUS nganhBUS = new NganhBUS();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch = new JTextField(20);
    private JButton btnSearch, btnAdd, btnEdit, btnDelete, btnRefresh;

    public NguyenVongForm() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        JLabel lblTitle = new JLabel("📋 QUẢN LÝ NGUYỆN VỌNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Center - Table
        String[] columns = {"STT", "Số Báo Danh", "Họ Tên", "Ngành 1", "Ngành 2", "Ngành 3", "Ngành 4", "Ngành 5", "Ngành 6"};
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
        searchPanel.add(new JLabel("Số Báo Danh:"));
        searchPanel.add(txtSearch);
        btnSearch = new JButton("Tìm");
        searchPanel.add(btnSearch);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Thêm Nguyện Vọng");
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
        // Dữ liệu mẫu - sau này load từ DB
        List<Object[]> sampleData = getSampleData();
        int stt = 1;
        for (Object[] row : sampleData) {
            Object[] newRow = new Object[row.length + 1];
            newRow[0] = stt++;
            System.arraycopy(row, 0, newRow, 1, row.length);
            tableModel.addRow(newRow);
        }
    }

    private List<Object[]> getSampleData() {
        List<Object[]> list = new ArrayList<>();
        list.add(new Object[]{"SBD001", "Nguyễn Văn A", "CNTT", "Kế toán", "Marketing", "", "", ""});
        list.add(new Object[]{"SBD002", "Trần Thị B", "Kinh tế", "Luật", "Marketing", "", "", ""});
        list.add(new Object[]{"SBD003", "Lê Văn C", "CNTT", "Khoa học máy tính", "An toàn thông tin", "", "", ""});
        return list;
    }

    private void search() {
        String key = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        if (key.isEmpty()) {
            loadData();
        } else {
            List<Object[]> sampleData = getSampleData();
            int stt = 1;
            for (Object[] row : sampleData) {
                if (row[0].toString().toLowerCase().contains(key.toLowerCase()) ||
                    row[1].toString().toLowerCase().contains(key.toLowerCase())) {
                    Object[] newRow = new Object[row.length + 1];
                    newRow[0] = stt++;
                    System.arraycopy(row, 0, newRow, 1, row.length);
                    tableModel.addRow(newRow);
                }
            }
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Nguyện Vọng", true);
        dialog.setSize(600, 450);
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
            JOptionPane.showMessageDialog(dialog, "Thêm nguyện vọng thành công!");
            dialog.dispose();
            loadData();
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nguyện vọng cần sửa!");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Nguyện Vọng", true);
        dialog.setSize(600, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = createFormPanel(tableModel.getValueAt(selectedRow, 0));
        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Cập nhật nguyện vọng thành công!");
            dialog.dispose();
            loadData();
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private JPanel createFormPanel(Object stt) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtSBD = new JTextField(15);
        JTextField txtHoTen = new JTextField(15);
        
        List<NganhDTO> dsNganh = nganhBUS.getAll();
        JComboBox<String> cboNganh1 = new JComboBox<>();
        JComboBox<String> cboNganh2 = new JComboBox<>();
        JComboBox<String> cboNganh3 = new JComboBox<>();
        JComboBox<String> cboNganh4 = new JComboBox<>();
        JComboBox<String> cboNganh5 = new JComboBox<>();
        JComboBox<String> cboNganh6 = new JComboBox<>();
        
        cboNganh1.addItem("-- Chọn ngành --");
        cboNganh2.addItem("-- Chọn ngành --");
        cboNganh3.addItem("-- Chọn ngành --");
        cboNganh4.addItem("-- Chọn ngành --");
        cboNganh5.addItem("-- Chọn ngành --");
        cboNganh6.addItem("-- Chọn ngành --");
        
        for (NganhDTO nganh : dsNganh) {
            String item = nganh.getMaNganh() + " - " + nganh.getTenNganh();
            cboNganh1.addItem(item);
            cboNganh2.addItem(item);
            cboNganh3.addItem(item);
            cboNganh4.addItem(item);
            cboNganh5.addItem(item);
            cboNganh6.addItem(item);
        }

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Số Báo Danh:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(txtSBD, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(txtHoTen, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nguyện vọng 1:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(cboNganh1, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nguyện vọng 2:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(cboNganh2, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nguyện vọng 3:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(cboNganh3, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nguyện vọng 4:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(cboNganh4, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nguyện vọng 5:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(cboNganh5, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nguyện vọng 6:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(cboNganh6, gbc);

        return panel;
    }

    private void delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nguyện vọng cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa nguyện vọng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData();
        }
    }
}