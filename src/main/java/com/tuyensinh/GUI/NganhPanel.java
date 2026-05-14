package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createStatisticPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadData();
    }

    // ================= STATISTIC =================
    private JPanel createStatisticPanel() {

    JPanel panel = new JPanel(new GridLayout(1, 4, 15, 15));
    panel.setOpaque(false);

    List<NganhDTO> list = nganhBUS.getAll();

    int tongNganh = 0;
    int tongChiTieu = 0;
    int tongDGNL = 0;
    int tongTHPT = 0;

    if (list != null) {

        tongNganh = list.size();

        for (NganhDTO n : list) {

            tongChiTieu += n.getChiTieu();

            if ("1".equals(n.getDgnl())) {
                tongDGNL++;
            }

            if ("1".equals(n.getThpt())) {
                tongTHPT++;
            }
        }
    }

    panel.add(createStatCard(
            "Tổng ngành",
            String.valueOf(tongNganh),
            new Color(52, 152, 219)
    ));

    panel.add(createStatCard(
            "Chỉ tiêu",
            String.valueOf(tongChiTieu),
            new Color(46, 204, 113)
    ));

    panel.add(createStatCard(
            "ĐGNL",
            String.valueOf(tongDGNL),
            new Color(155, 89, 182)
    ));

    panel.add(createStatCard(
            "THPT",
            String.valueOf(tongTHPT),
            new Color(230, 126, 34)
    ));

    return panel;
}

    private JPanel createStatCard(String title, String value, Color color) {

        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(color);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 30));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    // ================= TABLE =================
    private JPanel createTablePanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // ===== TOP =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Danh sách ngành");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        topPanel.add(lblTitle, BorderLayout.WEST);

        // ===== TOOLBAR =====
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolBar.setBackground(Color.WHITE);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(220, 38));

        JButton btnAdd = createToolbarButton("Thêm");
        JButton btnUpdate = createToolbarButton("Sửa");
        JButton btnDelete = createToolbarButton("Xóa");
        JButton btnRefresh = createToolbarButton("Làm mới");

        btnAdd.addActionListener(e -> openForm(null));

        btnUpdate.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn ngành cần sửa!");
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
                "ID",
                "Mã ngành",
                "Tên ngành",
                "Tổ hợp",
                "Chỉ tiêu",
                "Điểm sàn"
        };

        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);

        table.setRowHeight(38);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        table.getTableHeader().setBackground(
                new Color(245, 246, 250)
        );

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ================= BUTTON =================
    private JButton createToolbarButton(String text) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btn.setPreferredSize(new Dimension(100, 38));

        return btn;
    }

    // ================= LOAD =================
    private void loadData() {

        model.setRowCount(0);

        List<NganhDTO> list = nganhBUS.getAll();

        if (list == null) return;

        for (NganhDTO n : list) {

            model.addRow(new Object[]{
                    n.getIdNganh(),
                    n.getMaNganh(),
                    n.getTenNganh(),
                    n.getToHopGoc(),
                    n.getChiTieu(),
                    n.getDiemSan()
            });
        }
    }

    // ================= FORM POPUP =================
    private void openForm(NganhDTO nganh) {

        JDialog dialog = new JDialog();

        dialog.setTitle(
                nganh == null
                        ? "Thêm ngành"
                        : "Cập nhật ngành"
        );

        dialog.setSize(420, 520);

        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtMa = new JTextField();
        JTextField txtTen = new JTextField();
        JTextField txtToHop = new JTextField();
        JTextField txtChiTieu = new JTextField();
        JTextField txtDiemSan = new JTextField();

        JCheckBox chkTHPT = new JCheckBox("THPT");
        JCheckBox chkDGNL = new JCheckBox("ĐGNL");
        JCheckBox chkVSAT = new JCheckBox("VSAT");
        JCheckBox chkTT = new JCheckBox("Tuyển thẳng");

        addField(panel, "Mã ngành", txtMa);
        addField(panel, "Tên ngành", txtTen);
        addField(panel, "Tổ hợp gốc", txtToHop);
        addField(panel, "Chỉ tiêu", txtChiTieu);
        addField(panel, "Điểm sàn", txtDiemSan);

        JPanel methodPanel = new JPanel(new GridLayout(2, 2));

        methodPanel.add(chkTHPT);
        methodPanel.add(chkDGNL);
        methodPanel.add(chkVSAT);
        methodPanel.add(chkTT);

        panel.add(new JLabel("Phương thức xét tuyển"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(methodPanel);

        // ===== DATA =====
        if (nganh != null) {

            txtMa.setText(nganh.getMaNganh());
            txtTen.setText(nganh.getTenNganh());
            txtToHop.setText(nganh.getToHopGoc());
        }

        JButton btnSave = new JButton("Lưu");

        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);

        btnSave.setFocusPainted(false);

        btnSave.setPreferredSize(new Dimension(100, 40));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        bottom.add(btnSave);

        panel.add(Box.createVerticalStrut(20));
        panel.add(bottom);

        // ===== SAVE =====
        btnSave.addActionListener(e -> {

            try {

                NganhDTO n = new NganhDTO();

                if (nganh != null) {
                    n.setIdNganh(nganh.getIdNganh());
                }

                n.setMaNganh(txtMa.getText());
                n.setTenNganh(txtTen.getText());
                n.setToHopGoc(txtToHop.getText());

                n.setChiTieu(
                        Integer.parseInt(txtChiTieu.getText())
                );

                n.setDiemSan(
                        Double.parseDouble(txtDiemSan.getText())
                );

                n.setThpt(chkTHPT.isSelected() ? "1" : "0");
                n.setDgnl(chkDGNL.isSelected() ? "1" : "0");
                n.setVsat(chkVSAT.isSelected() ? "1" : "0");
                n.setTuyenThang(chkTT.isSelected() ? "1" : "0");

                String result;

                if (nganh == null) {
                    result = nganhBUS.addNganh(n);
                } else {
                    result = nganhBUS.updateNganh(n);
                }

                JOptionPane.showMessageDialog(dialog, result);

                loadData();

                dialog.dispose();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(dialog,
                        "Dữ liệu không hợp lệ!");
            }
        });

        dialog.add(panel);

        dialog.setVisible(true);
    }

    // ================= DELETE =================
    private void deleteNganh() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn ngành cần xóa!");

            return;
        }

        int id = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa ngành này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            String result = nganhBUS.deleteNganh(id);

            JOptionPane.showMessageDialog(this, result);

            loadData();
        }
    }

    // ================= FIELD =================
    private void addField(JPanel panel, String label, JTextField txt) {

        JLabel lbl = new JLabel(label);

        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txt);
        panel.add(Box.createVerticalStrut(15));
    }
}