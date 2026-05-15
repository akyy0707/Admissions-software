package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.tuyensinh.BUS.DiemThiBUS;
import com.tuyensinh.DTO.DiemThiDTO;

public class DiemThiPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JLabel lblTongThiSinh;
    private JLabel lblAvgToan;
    private JLabel lblAvgVan;

    private DiemThiBUS diemThiBUS;

    public DiemThiPanel() {

        diemThiBUS = new DiemThiBUS();

        setLayout(new BorderLayout(15, 15));

        setBackground(new Color(245, 247, 250));

        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(createTopPanel(), BorderLayout.NORTH);

        add(createTablePanel(), BorderLayout.CENTER);

        loadDataFromDB();
    }

    // ================= TOP =================

    private JPanel createTopPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ ĐIỂM THI");

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));

        panel.add(lblTitle, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        right.setOpaque(false);

        JButton btnImport = createButton(
                "Import Excel",
                new Color(46, 204, 113)
        );

        JButton btnRefresh = createButton(
                "Làm mới",
                new Color(52, 152, 219)
        );

        btnImport.addActionListener(e -> importExcel());

        btnRefresh.addActionListener(e -> loadDataFromDB());

        right.add(btnImport);

        right.add(btnRefresh);

        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    // ================= TABLE =================

    private JPanel createTablePanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // ===== INFO =====

        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 15, 15));

        infoPanel.setBackground(Color.WHITE);

        lblTongThiSinh = createStatCardValue("0");

        lblAvgToan = createStatCardValue("0");

        lblAvgVan = createStatCardValue("0");

        infoPanel.add(createStatCard(
                "Tổng thí sinh",
                lblTongThiSinh,
                new Color(52, 152, 219)
        ));

        infoPanel.add(createStatCard(
                "Điểm TB Toán",
                lblAvgToan,
                new Color(46, 204, 113)
        ));

        infoPanel.add(createStatCard(
                "Điểm TB Văn",
                lblAvgVan,
                new Color(155, 89, 182)
        ));

        panel.add(infoPanel, BorderLayout.NORTH);

        // ===== TABLE =====

        String[] columns = {
                "CCCD",
                "Toán",
                "Văn",
                "Lý",
                "Hóa",
                "Sinh",
                "Sử",
                "Địa",
                "KTPL",
                "Tin",
                "NK1",
                "NK2"
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

        table.setShowGrid(false);

        table.setIntercellSpacing(new Dimension(0, 0));

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ================= CARD =================

    private JPanel createStatCard(
            String title,
            JLabel valueLabel,
            Color color
    ) {

        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitle = new JLabel(title);

        lblTitle.setForeground(Color.GRAY);

        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        valueLabel.setForeground(color);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));

        card.add(lblTitle, BorderLayout.NORTH);

        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createStatCardValue(String value) {

        return new JLabel(value);
    }

    // ================= BUTTON =================

    private JButton createButton(String text, Color color) {

        JButton btn = new JButton(text);

        btn.setBackground(color);

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btn.setPreferredSize(new Dimension(140, 40));

        return btn;
    }

    // ================= IMPORT =================

    private void importExcel() {

        JFileChooser chooser = new JFileChooser();

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();

            try {

                diemThiBUS.importFromExcel(file);

                JOptionPane.showMessageDialog(
                        this,
                        "Import Excel thành công!"
                );

                loadDataFromDB();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Import thất bại!"
                );

                e.printStackTrace();
            }
        }
    }

    // ================= LOAD DB =================

    private void loadDataFromDB() {

        try {

            model.setRowCount(0);

            List<DiemThiDTO> list = diemThiBUS.getAll();

            double tongToan = 0;

            double tongVan = 0;

            for (DiemThiDTO d : list) {

                tongToan += d.getTo();

                tongVan += d.getVa();

                model.addRow(new Object[]{
                        d.getCccd(),
                        d.getTo(),
                        d.getVa(),
                        d.getLi(),
                        d.getHo(),
                        d.getSi(),
                        d.getSu(),
                        d.getDi(),
                        d.getKtpl(),
                        d.getTi(),
                        d.getNk1(),
                        d.getNk2()
                });
            }

            DecimalFormat df = new DecimalFormat("#.##");

            lblTongThiSinh.setText(String.valueOf(list.size()));

            if (list.size() > 0) {

                lblAvgToan.setText(
                        df.format(tongToan / list.size())
                );

                lblAvgVan.setText(
                        df.format(tongVan / list.size())
                );

            } else {

                lblAvgToan.setText("0");

                lblAvgVan.setText("0");
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi load dữ liệu!"
            );
        }
    }
}