package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 249, 250)); // Nền xám sáng hiện đại
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Nội dung chính: Đã bao gồm Tiêu đề, Toolbar và Bảng dữ liệu
        add(createTablePanel(), BorderLayout.CENTER);

        loadData();
    }

    // ================= TABLE & TOOLBAR =================
    private JPanel createTablePanel() {
        RoundedPanel panel = new RoundedPanel(20, Color.WHITE);
        panel.setLayout(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // ===== TOP BAR (Title + Actions) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Danh Sách Tổ Hợp Môn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(50, 50, 50));
        topPanel.add(lblTitle, BorderLayout.WEST);

        // ===== TOOLBAR (Buttons) =====
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        toolBar.setOpaque(false);

        JButton btnAdd = createFlatButton("Thêm mới", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton btnEdit = createFlatButton("Cập nhật", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton btnDelete = createFlatButton("Xóa", new Color(231, 76, 60), new Color(192, 57, 43));
        JButton btnRefresh = createFlatButton("Làm mới", new Color(149, 165, 166), new Color(127, 140, 141));

        btnAdd.addActionListener(e -> openForm(null));
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> loadData());

        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.add(btnRefresh);

        topPanel.add(toolBar, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"ID", "Mã tổ hợp", "Môn 1", "Môn 2", "Môn 3", "Tên tổ hợp"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Chống click đúp sửa trực tiếp
            }
        };

        table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setSelectionBackground(new Color(220, 235, 252));
        table.setSelectionForeground(new Color(30, 30, 30));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(240, 240, 240));

        // Custom Header
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(new Color(80, 80, 80));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
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
        btn.setPreferredSize(new Dimension(110, 40));

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

    // ================= LOAD DATA =================
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

    // ================= FORM POPUP =================
    private void openForm(ToHopDTO data) {
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle(data == null ? "Thêm Tổ Hợp Mới" : "Cập Nhật Tổ Hợp");
        dialog.setSize(450, 580);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel(data == null ? "THÊM TỔ HỢP MỚI" : "CẬP NHẬT TỔ HỢP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(40, 40, 40));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(30));

        // ===== INPUT =====
        JTextField txtMa = createInput(panel, "Mã tổ hợp (VD: A00)");
        JTextField txtMon1 = createInput(panel, "Môn 1");
        JTextField txtMon2 = createInput(panel, "Môn 2");
        JTextField txtMon3 = createInput(panel, "Môn 3");
        JTextField txtTen = createInput(panel, "Tên tổ hợp");

        // ===== BIND DATA =====
        if (data != null) {
            txtMa.setText(data.getMaToHop());
            txtMon1.setText(data.getMon1());
            txtMon2.setText(data.getMon2());
            txtMon3.setText(data.getMon3());
            txtTen.setText(data.getTenToHop());
        }

        // ===== BUTTON XÁC NHẬN =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottom.setOpaque(false);

        JButton btnSave = createFlatButton(data == null ? "Thêm Dữ Liệu" : "Lưu Thay Đổi", new Color(46, 204, 113), new Color(39, 174, 96));
        btnSave.setPreferredSize(new Dimension(140, 42));
        bottom.add(btnSave);

        panel.add(Box.createVerticalStrut(20));
        panel.add(bottom);

        // ===== SỰ KIỆN LƯU =====
        btnSave.addActionListener(e -> {
            try {
                ToHopDTO dto = new ToHopDTO();
                if (data != null) {
                    dto.setIdToHop(data.getIdToHop());
                }

                dto.setMaToHop(txtMa.getText().trim());
                dto.setMon1(txtMon1.getText().trim());
                dto.setMon2(txtMon2.getText().trim());
                dto.setMon3(txtMon3.getText().trim());
                dto.setTenToHop(txtTen.getText().trim());

                if (dto.getMaToHop().isEmpty() || dto.getMon1().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền các thông tin bắt buộc!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String result = (data == null) ? bus.addToHop(dto) : bus.updateToHop(dto);
                JOptionPane.showMessageDialog(dialog, result, "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                loadData();
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu nhập vào không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // ================= HELPER INPUT =================
    private JTextField createInput(JPanel panel, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(80, 80, 80));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng dữ liệu để cập nhật!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ToHopDTO dto = new ToHopDTO();
        dto.setIdToHop((int) model.getValueAt(row, 0));
        dto.setMaToHop(model.getValueAt(row, 1).toString());
        dto.setMon1(model.getValueAt(row, 2).toString());
        dto.setMon2(model.getValueAt(row, 3).toString());
        dto.setMon3(model.getValueAt(row, 4).toString());
        dto.setTenToHop(model.getValueAt(row, 5).toString());

        openForm(dto);
    }

    // ================= DELETE =================
    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng dữ liệu để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Hành động này không thể hoàn tác. Bạn có chắc muốn xóa?", "Cảnh báo xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) model.getValueAt(row, 0);
            String result = bus.deleteToHop(id);
            JOptionPane.showMessageDialog(this, result, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadData();
        }
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
            
            // Đổ bóng mờ nhẹ
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Vẽ nền Panel chính
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            // Vẽ viền thanh mảnh bọc xung quanh
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            g2.dispose();
        }
    }
}