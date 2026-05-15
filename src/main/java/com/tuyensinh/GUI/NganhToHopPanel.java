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

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 249, 250)); // Nền xám sáng hiện đại
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Không cần panel Header riêng nữa, mình đã gộp Title và các nút vào thanh Toolbar của Table
        add(createTablePanel(), BorderLayout.CENTER);

        loadData();
    }

    // ================= TABLE & TOOLBAR =================
    private JPanel createTablePanel() {
        // Sử dụng RoundedPanel để bo góc toàn bộ khu vực bảng
        RoundedPanel panel = new RoundedPanel(20, Color.WHITE);
        panel.setLayout(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // ===== TOP BAR (Title + Actions) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Danh Sách Liên Kết Ngành - Tổ Hợp");
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
        String[] columns = {"ID", "Mã ngành", "Mã tổ hợp", "Môn 1", "Môn 2", "Môn 3", "Độ lệch"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Chống click đúp sửa trực tiếp trên bảng
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

        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

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

    // ================= FORM POPUP =================
    private void openForm(NganhToHopDTO data) {
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle(data == null ? "Thêm Ngành - Tổ Hợp" : "Cập Nhật Ngành - Tổ Hợp");
        dialog.setSize(500, 680);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel(data == null ? "THÊM LIÊN KẾT MỚI" : "CẬP NHẬT LIÊN KẾT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 40, 40));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(30));

        // ===== INPUT =====
        JTextField txtMaNganh = createInput(panel, "Mã ngành đào tạo");
        JTextField txtMaToHop = createInput(panel, "Mã tổ hợp xét tuyển");
        JTextField txtMon1 = createInput(panel, "Môn 1");
        JTextField txtMon2 = createInput(panel, "Môn 2");
        JTextField txtMon3 = createInput(panel, "Môn 3");
        JTextField txtDoLech = createInput(panel, "Độ lệch điểm");

        // ===== NẠP DATA =====
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
                NganhToHopDTO dto = new NganhToHopDTO();
                if (data != null) dto.setId(data.getId());

                dto.setMaNganh(txtMaNganh.getText());
                dto.setMaToHop(txtMaToHop.getText());
                dto.setThMon1(txtMon1.getText());
                dto.setThMon2(txtMon2.getText());
                dto.setThMon3(txtMon3.getText());
                dto.setDoLech(Double.parseDouble(txtDoLech.getText()));

                String result;
                if (data == null) {
                    result = bus.addMapping(dto);
                } else {
                    // Cập nhật lại logic bus.updateMapping nếu trong BUS của bạn có hỗ trợ hàm này
                    result = "Đã cập nhật dữ liệu!";
                }

                JOptionPane.showMessageDialog(dialog, result);
                loadData();
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: Dữ liệu nhập vào không hợp lệ!");
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // ================= HELPER FIELD =================
    private JTextField createInput(JPanel panel, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(80, 80, 80));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txt = new JTextField();
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Thêm padding cho TextField để nhập liệu thoải mái hơn
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng dữ liệu để cập nhật!");
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng dữ liệu để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this, "Hành động này không thể hoàn tác. Bạn có chắc muốn xóa?", 
                "Cảnh báo xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) model.getValueAt(row, 0);
            String result = bus.deleteMapping(id);
            JOptionPane.showMessageDialog(this, result);
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
            
            // Vẽ đổ bóng mờ ảo
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