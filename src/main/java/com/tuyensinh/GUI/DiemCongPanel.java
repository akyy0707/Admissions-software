package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.tuyensinh.BUS.DiemCongBUS;
import com.tuyensinh.DTO.DiemCongDTO;

public class DiemCongPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private DiemCongBUS bus;

    private JLabel lblTongHoSo;
    private JLabel lblTongDiem;
    private JLabel lblPT4;

    public DiemCongPanel() {

        bus = new DiemCongBUS();

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadData();
    }

    // ================= TOP =================
    private JPanel createTopPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ ĐIỂM CỘNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));

        panel.add(lblTitle, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JButton btnImport = new JButton("Import Excel");
        JButton btnRefresh = new JButton("Làm mới");

        btnImport.setBackground(new Color(46, 204, 113));
        btnRefresh.setBackground(new Color(52, 152, 219));

        btnImport.setForeground(Color.WHITE);
        btnRefresh.setForeground(Color.WHITE);

        btnImport.addActionListener(e -> importExcel());
        btnRefresh.addActionListener(e -> loadData());

        right.add(btnImport);
        right.add(btnRefresh);

        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    // ================= TABLE =================
    private JPanel createTablePanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        infoPanel.setBackground(Color.WHITE);

        lblTongHoSo = new JLabel("0");
        lblTongDiem = new JLabel("0");
        lblPT4 = new JLabel("0");

        infoPanel.add(createCard("Tổng hồ sơ", lblTongHoSo, Color.BLUE));
        infoPanel.add(createCard("Tổng điểm", lblTongDiem, Color.GREEN));
        infoPanel.add(createCard("PT4", lblPT4, Color.MAGENTA));

        panel.add(infoPanel, BorderLayout.NORTH);

        String[] columns = {
                "CCCD",
                "Mã ngành",
                "Mã tổ hợp",
                "Phương thức",
                "Điểm CC",
                "Điểm UT",
                "Điểm tổng"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane sp = new JScrollPane(table);
        panel.add(sp, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCard(String title, JLabel value, Color color) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lbl = new JLabel(title);
        lbl.setForeground(Color.GRAY);

        value.setForeground(color);
        value.setFont(new Font("Segoe UI", Font.BOLD, 20));

        card.add(lbl, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);

        return card;
    }

    // ================= LOAD DATA =================
    private void loadData() {

        model.setRowCount(0);

        List<DiemCongDTO> list = bus.getAll();

        double tong = 0;

        for (DiemCongDTO d : list) {

            model.addRow(new Object[]{
                    d.getCccd(),
                    d.getMaNganh(),
                    d.getMaToHop(),
                    d.getPhuongThuc(),
                    d.getDiemCC(),
                    d.getDiemUuTien(),
                    d.getDiemTong()
            });

            tong += d.getDiemTong();
        }

        lblTongHoSo.setText(String.valueOf(list.size()));
        lblTongDiem.setText(String.format("%.2f", tong));
        lblPT4.setText(String.valueOf(bus.countPT4()));
    }

    // ================= IMPORT =================
    private void importExcel() {

        JFileChooser chooser = new JFileChooser();

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();

            try {
                bus.importFromExcel(file);

                JOptionPane.showMessageDialog(this, "Import thành công!");
                loadData();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Import thất bại!");
            }
        }
    }
}