package com.tuyensinh.GUI;

import com.tuyensinh.BUS.DiemThiBUS;
import com.tuyensinh.DTO.DiemThiDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DiemForm extends JPanel {

    private DiemThiBUS diemThiBUS = new DiemThiBUS();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnEdit, btnDelete, btnRefresh;

    public DiemForm() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 249, 250)); // Nền xám sáng hiện đại
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Tiêu đề trang
        JLabel lblTitle = new JLabel("QUẢN LÝ ĐIỂM THI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        // Nội dung chính (Table + Toolbar) bọc trong khung bo góc
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        RoundedPanel mainPanel = new RoundedPanel(20, Color.WHITE);
        mainPanel.setLayout(new BorderLayout(10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // ===== TOOLBAR =====
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setOpaque(false);

        // Search Box (Bên trái)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        
        JLabel lblSearch = new JLabel("Tìm CCCD:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(new Color(80, 80, 80));
        
        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(180, 38));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));

        btnSearch = createFlatButton("Tìm kiếm", new Color(149, 165, 166), new Color(127, 140, 141));
        btnSearch.setPreferredSize(new Dimension(100, 38));

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Actions (Bên phải)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        btnAdd = createFlatButton("Nhập Điểm", new Color(46, 204, 113), new Color(39, 174, 96));
        btnEdit = createFlatButton("Cập nhật", new Color(52, 152, 219), new Color(41, 128, 185));
        btnDelete = createFlatButton("Xóa", new Color(231, 76, 60), new Color(192, 57, 43));
        btnRefresh = createFlatButton("Làm Mới", new Color(240, 240, 240), new Color(220, 220, 220));
        btnRefresh.setForeground(new Color(80, 80, 80)); // Đổi màu chữ cho nút nền sáng

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);

        toolBar.add(searchPanel, BorderLayout.WEST);
        toolBar.add(actionPanel, BorderLayout.EAST);
        mainPanel.add(toolBar, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"CCCD", "Toán", "Văn", "Lý", "Hóa", "Sinh", "Sử", "Địa", "Ngoại ngữ", "Điểm TB"};
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== EVENTS =====
        btnSearch.addActionListener(e -> search());
        txtSearch.addActionListener(e -> search()); // Hỗ trợ nhấn Enter
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> delete());
        btnRefresh.addActionListener(e -> loadData());

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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(110, 38));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    // ================= LOGIC =================
    private void loadData() {
        tableModel.setRowCount(0);
        List<DiemThiDTO> list = diemThiBUS.getAll();
        if (list == null) return;
        
        for (DiemThiDTO diem : list) {
            double tb = (diem.getTo() + diem.getVa() + diem.getLi() + diem.getHo() + diem.getSi() + diem.getSu() + diem.getDi()) / 7;
            Object[] row = {
                diem.getCccd(), diem.getTo(), diem.getVa(), diem.getLi(), diem.getHo(),
                diem.getSi(), diem.getSu(), diem.getDi(), diem.getN1_thi(), String.format("%.2f", tb)
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
                    diem.getCccd(), diem.getTo(), diem.getVa(), diem.getLi(), diem.getHo(),
                    diem.getSi(), diem.getSu(), diem.getDi(), diem.getN1_thi(), String.format("%.2f", tb)
                };
                tableModel.addRow(row);
            }
        }
    }

    private void delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng dữ liệu cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa điểm này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
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

    // ================= FORMS (DIALOGS) =================
    private void showAddDialog() {
        openFormDialog(null, "Nhập Điểm Thi Mới");
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dữ liệu cần cập nhật!");
            return;
        }
        String cccd = (String) tableModel.getValueAt(selectedRow, 0);
        DiemThiDTO diem = diemThiBUS.getByCCCD(cccd);
        openFormDialog(diem, "Cập Nhật Điểm Thi");
    }

    private void openFormDialog(DiemThiDTO diem, String title) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(550, 480);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(25, 30, 10, 30));

        // Tiêu đề form
        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(50, 50, 50));
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        formPanel.add(lblTitle, BorderLayout.NORTH);

        // Các field nhập liệu
        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 20, 15));
        fieldsPanel.setBackground(Color.WHITE);

        JTextField txtCCCD = createStyledTextField();
        JTextField txtTo = createStyledTextField();
        JTextField txtVa = createStyledTextField();
        JTextField txtLi = createStyledTextField();
        JTextField txtHo = createStyledTextField();
        JTextField txtSi = createStyledTextField();
        JTextField txtSu = createStyledTextField();
        JTextField txtDi = createStyledTextField();
        JTextField txtNN = createStyledTextField();

        if (diem != null) {
            txtCCCD.setText(diem.getCccd());
            txtCCCD.setEditable(false);
            txtCCCD.setBackground(new Color(245, 245, 245));
            txtTo.setText(String.valueOf(diem.getTo()));
            txtVa.setText(String.valueOf(diem.getVa()));
            txtLi.setText(String.valueOf(diem.getLi()));
            txtHo.setText(String.valueOf(diem.getHo()));
            txtSi.setText(String.valueOf(diem.getSi()));
            txtSu.setText(String.valueOf(diem.getSu()));
            txtDi.setText(String.valueOf(diem.getDi()));
            txtNN.setText(String.valueOf(diem.getN1_thi()));
        }

        // Cột 1: CCCD chiếm cả 2 ô dòng đầu
        JPanel pnlCCCD = new JPanel(new BorderLayout());
        pnlCCCD.setOpaque(false);
        pnlCCCD.add(createFieldLabel("Căn cước công dân:"), BorderLayout.NORTH);
        pnlCCCD.add(txtCCCD, BorderLayout.CENTER);
        
        fieldsPanel.add(pnlCCCD);
        fieldsPanel.add(new JLabel()); // Ô trống để căn chỉnh grid

        fieldsPanel.add(createLabeledField("Điểm Toán:", txtTo));
        fieldsPanel.add(createLabeledField("Điểm Văn:", txtVa));
        fieldsPanel.add(createLabeledField("Điểm Lý:", txtLi));
        fieldsPanel.add(createLabeledField("Điểm Hóa:", txtHo));
        fieldsPanel.add(createLabeledField("Điểm Sinh:", txtSi));
        fieldsPanel.add(createLabeledField("Điểm Sử:", txtSu));
        fieldsPanel.add(createLabeledField("Điểm Địa:", txtDi));
        fieldsPanel.add(createLabeledField("Điểm Ngoại ngữ:", txtNN));

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        dialog.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 30, 20, 30));

        JButton btnSave = createFlatButton("Lưu Dữ Liệu", new Color(46, 204, 113), new Color(39, 174, 96));
        btnSave.setPreferredSize(new Dimension(130, 40));
        
        JButton btnCancel = createFlatButton("Hủy bỏ", new Color(230, 230, 230), new Color(210, 210, 210));
        btnCancel.setForeground(new Color(80, 80, 80));

        btnSave.addActionListener(e -> {
            // (Code lưu/cập nhật xuống Database của bạn xử lý ở đây)
            JOptionPane.showMessageDialog(dialog, "Lưu dữ liệu thành công!");
            dialog.dispose();
            loadData();
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JPanel createLabeledField(String labelText, JTextField textField) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(createFieldLabel(labelText), BorderLayout.NORTH);
        p.add(textField, BorderLayout.CENTER);
        return p;
    }

    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 100, 100));
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));
        return lbl;
    }

    private JTextField createStyledTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(6, 10, 6, 10)
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