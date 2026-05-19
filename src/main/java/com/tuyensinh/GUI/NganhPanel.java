package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.tuyensinh.BUS.NganhBUS;
import com.tuyensinh.DTO.NganhDTO;

public class NganhPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private NganhBUS nganhBUS;

    public NganhPanel() {
        nganhBUS = new NganhBUS();

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 249, 250)); // Nền tổng thể xám sáng hiện đại
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createStatisticPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadData();
    }

    // ================= STATISTIC =================
    private JPanel createStatisticPanel() {
        // Tăng lên 6 cột để chứa đủ các thẻ thống kê mới
        JPanel panel = new JPanel(new GridLayout(1, 6, 15, 15));
        panel.setOpaque(false);

        panel.add(createStatCard("Tổng ngành", String.valueOf(nganhBUS.getTongNganh()), new Color(52, 152, 219)));
        panel.add(createStatCard("Chỉ tiêu", String.valueOf(nganhBUS.getTongChiTieu()), new Color(46, 204, 113)));
        panel.add(createStatCard("ĐGNL", String.valueOf(nganhBUS.getTongDGNL()), new Color(155, 89, 182)));
        panel.add(createStatCard("Tuyển thẳng", String.valueOf(nganhBUS.getTongTuyenThang()), new Color(230, 126, 182)));
        panel.add(createStatCard("THPT", String.valueOf(nganhBUS.getTongTHPT()), new Color(211, 84, 0)));
        panel.add(createStatCard("VSAT", String.valueOf(nganhBUS.getTongVSAT()), new Color(243, 156, 18)));

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(130, 130, 130));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(color);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    // ================= TABLE & TOOLBAR =================
    private JPanel createTablePanel() {
        RoundedPanel panel = new RoundedPanel(20, Color.WHITE);
        panel.setLayout(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== TOP BAR (Title + Actions) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Danh Sách Ngành Tuyển Sinh");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(50, 50, 50));
        topPanel.add(lblTitle, BorderLayout.WEST);

        // ===== TOOLBAR =====
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        toolBar.setOpaque(false);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(250, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton btnAdd = createFlatButton("Thêm mới", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton btnUpdate = createFlatButton("Cập nhật", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton btnDelete = createFlatButton("Xóa", new Color(231, 76, 60), new Color(192, 57, 43));
        JButton btnRefresh = createFlatButton("Làm mới", new Color(149, 165, 166), new Color(127, 140, 141));

        btnAdd.addActionListener(e -> openForm(null));
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngành cần cập nhật!");
                return;
            }
            NganhDTO n = new NganhDTO();
            n.setIdNganh((int) model.getValueAt(row, 0));
            n.setMaNganh(model.getValueAt(row, 1).toString());
            n.setTenNganh(model.getValueAt(row, 2).toString());
            n.setToHopGoc(model.getValueAt(row, 3).toString());
            openForm(n);
        });
        btnDelete.addActionListener(e -> deleteNganh());
        btnRefresh.addActionListener(e -> loadData());

        toolBar.add(txtSearch);
        toolBar.add(btnAdd);
        toolBar.add(btnUpdate);
        toolBar.add(btnDelete);
        toolBar.add(btnRefresh);

        topPanel.add(toolBar, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {
                "ID", "Mã ngành", "Tên ngành", "Tổ hợp", "Chỉ tiêu", 
                "Điểm sàn", "Điểm trúng tuyển", "Phương thức", "Số NV"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên bảng
            }
        };

        table = new JTable(model);
        table.setRowHeight(45);
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
        btn.setPreferredSize(new Dimension(110, 38));

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

    // ================= FORMAT PHƯƠNG THỨC =================
    private String formatPhuongThuc(Object thpt, Object dgnl, Object vsat, Object tuyenThang) {
        StringBuilder sb = new StringBuilder();

        if ("1".equals(String.valueOf(thpt))) sb.append("THPT, ");
        if ("1".equals(String.valueOf(dgnl))) sb.append("ĐGNL, ");
        if ("1".equals(String.valueOf(vsat))) sb.append("VSAT, ");
        if ("1".equals(String.valueOf(tuyenThang))) sb.append("Tuyển thẳng, ");

        if (sb.length() == 0) return "Chưa có";

        return sb.substring(0, sb.length() - 2);
    }

    // ================= LOAD DATA =================
    private void loadData() {
        model.setRowCount(0);
        
        List<Object[]> list = nganhBUS.getAllWithSoNV();
        if (list == null) return;

        for (Object[] n : list) {
            model.addRow(new Object[]{
                    n[0], // id
                    n[1], // ma nganh
                    n[2], // ten nganh
                    n[3], // to hop
                    n[4], // chi tieu
                    n[5], // diem san
                    n[6], // diem trung tuyen
                    formatPhuongThuc(n[7], n[8], n[9], n[10]), // Phuong thuc
                    n[11] // so NV
            });
        }
    }

    // ================= FORM POPUP =================
    private void openForm(NganhDTO nganh) {
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle(nganh == null ? "Thêm Ngành Mới" : "Cập Nhật Thông Tin Ngành");
        dialog.setSize(450, 580);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JTextField txtMa = new JTextField();
        JTextField txtTen = new JTextField();
        JTextField txtToHop = new JTextField();
        JTextField txtChiTieu = new JTextField();
        JTextField txtDiemSan = new JTextField();

        JCheckBox chkTHPT = new JCheckBox("Điểm thi THPT");
        JCheckBox chkDGNL = new JCheckBox("Đánh giá năng lực");
        JCheckBox chkVSAT = new JCheckBox("Kỳ thi V-SAT");
        JCheckBox chkTT = new JCheckBox("Tuyển thẳng");

        Font checkFont = new Font("Segoe UI", Font.PLAIN, 14);
        chkTHPT.setFont(checkFont); chkTHPT.setBackground(Color.WHITE);
        chkDGNL.setFont(checkFont); chkDGNL.setBackground(Color.WHITE);
        chkVSAT.setFont(checkFont); chkVSAT.setBackground(Color.WHITE);
        chkTT.setFont(checkFont); chkTT.setBackground(Color.WHITE);

        addField(panel, "Mã ngành đào tạo", txtMa);
        addField(panel, "Tên ngành đào tạo", txtTen);
        addField(panel, "Tổ hợp môn gốc (Ví dụ: A00, A01)", txtToHop);
        addField(panel, "Chỉ tiêu tuyển sinh", txtChiTieu);
        addField(panel, "Điểm nhận hồ sơ (Điểm sàn)", txtDiemSan);

        JPanel methodPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        methodPanel.setBackground(Color.WHITE);
        methodPanel.add(chkTHPT);
        methodPanel.add(chkDGNL);
        methodPanel.add(chkVSAT);
        methodPanel.add(chkTT);

        JLabel lblMethod = new JLabel("Phương thức xét tuyển áp dụng");
        lblMethod.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMethod.setForeground(new Color(80, 80, 80));
        panel.add(lblMethod);
        panel.add(Box.createVerticalStrut(10));
        panel.add(methodPanel);

        // Nạp dữ liệu cũ nếu là Cập nhật
        if (nganh != null) {
            txtMa.setText(nganh.getMaNganh());
            txtTen.setText(nganh.getTenNganh());
            txtToHop.setText(nganh.getToHopGoc());
        }

        JButton btnSave = createFlatButton("Lưu Dữ Liệu", new Color(46, 204, 113), new Color(39, 174, 96));
        btnSave.setPreferredSize(new Dimension(130, 42));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);
        bottom.add(btnSave);

        panel.add(Box.createVerticalStrut(25));
        panel.add(bottom);

        // Sự kiện Lưu
        btnSave.addActionListener(e -> {
            try {
                NganhDTO n = new NganhDTO();
                if (nganh != null) n.setIdNganh(nganh.getIdNganh());
                
                n.setMaNganh(txtMa.getText());
                n.setTenNganh(txtTen.getText());
                n.setToHopGoc(txtToHop.getText());
                n.setChiTieu(Integer.parseInt(txtChiTieu.getText()));
                n.setDiemSan(Double.parseDouble(txtDiemSan.getText()));

                n.setThpt(chkTHPT.isSelected() ? "1" : "0");
                n.setDgnl(chkDGNL.isSelected() ? "1" : "0");
                n.setVsat(chkVSAT.isSelected() ? "1" : "0");
                n.setTuyenThang(chkTT.isSelected() ? "1" : "0");

                String result = (nganh == null) ? nganhBUS.addNganh(n) : nganhBUS.updateNganh(n);
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

    // ================= DELETE =================
    private void deleteNganh() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngành cần xóa!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this, "Hành động này không thể hoàn tác. Bạn có chắc muốn xóa?",
                "Cảnh báo xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String result = nganhBUS.deleteNganh(id);
            JOptionPane.showMessageDialog(this, result);
            loadData();
        }
    }

    // ================= HELPER FIELD =================
    private void addField(JPanel panel, String label, JTextField txt) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(80, 80, 80));

        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
        panel.add(txt);
        panel.add(Box.createVerticalStrut(15));
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
            
            // Vẽ đổ bóng mờ
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Nền chính
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            // Viền nhạt
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            g2.dispose();
        }
    }
}