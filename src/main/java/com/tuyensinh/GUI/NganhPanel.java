package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.tuyensinh.BUS.NganhBUS;

public class NganhPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
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

        panel.add(createStatCard("Tổng ngành",
                String.valueOf(nganhBUS.getTongNganh()),
                new Color(52, 152, 219)));

        panel.add(createStatCard("Chỉ tiêu",
                String.valueOf(nganhBUS.getTongChiTieu()),
                new Color(46, 204, 113)));

        panel.add(createStatCard("ĐGNL",
                String.valueOf(nganhBUS.getTongDGNL()),
                new Color(155, 89, 182)));

        panel.add(createStatCard("Tuyển thẳng",
                String.valueOf(nganhBUS.getTongTuyenThang()),
                new Color(230, 126, 182)));
        panel.add(createStatCard("THPT",
                String.valueOf(nganhBUS.getTongTHPT()),
                new Color(230, 126, 182)));
        panel.add(createStatCard("VSAT",
                String.valueOf(nganhBUS.getTongVSAT()),
                new Color(230, 126, 182)));

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(color);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    // ================= TABLE =================
    private JPanel createTablePanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Danh sách ngành tuyển sinh");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        panel.add(title, BorderLayout.NORTH);

        String[] columns = {
                "ID",
                "Mã ngành",
                "Tên ngành",
                "Tổ hợp",
                "Chỉ tiêu",
                "Điểm sàn",
                "Điểm trúng tuyển",
                "Phương thức",
                "Số NV"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
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

        for (Object[] n : list) {

            model.addRow(new Object[]{
                    n[0], // id
                    n[1], // ma nganh
                    n[2], // ten nganh
                    n[3], // to hop
                    n[4], // chi tieu
                    n[5], // diem san
                    n[6], // diem trung tuyen

                    formatPhuongThuc(n[7], n[8], n[9], n[10]),

                    n[11] // so NV
            });
        }
    }
}