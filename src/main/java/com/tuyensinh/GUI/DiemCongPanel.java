package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

import com.tuyensinh.BUS.DiemCongBUS;
import com.tuyensinh.config.HibernateUtil;

public class DiemCongPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private DiemCongBUS diemCongBUS;

    private Connection conn;

    // thống kê
    private JLabel lblTongHoSo;
    private JLabel lblTongDiem;
    private JLabel lblPT4;

    public DiemCongPanel() {

        conn = HibernateUtil
                .getSessionFactory()
                .openSession()
                .doReturningWork(c -> c);

        diemCongBUS = new DiemCongBUS(conn);

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

        JLabel lblTitle = new JLabel("QUẢN LÝ ĐIỂM CỘNG");

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

    // ================= TABLE PANEL =================

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

        lblTongHoSo = new JLabel("0");
        lblTongDiem = new JLabel("0");
        lblPT4 = new JLabel("0");

        infoPanel.add(createStatCard(
                "Tổng hồ sơ",
                lblTongHoSo,
                new Color(52, 152, 219)
        ));

        infoPanel.add(createStatCard(
                "Tổng điểm cộng",
                lblTongDiem,
                new Color(46, 204, 113)
        ));

        infoPanel.add(createStatCard(
                "PT4",
                lblPT4,
                new Color(155, 89, 182)
        ));

        panel.add(infoPanel, BorderLayout.NORTH);

        // ===== TABLE =====

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

                diemCongBUS.importFromExcel(file);

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

        model.setRowCount(0);

        int tongHoSo = 0;
        double tongDiem = 0;
        int tongPT4 = 0;

        try {

            String sql = """
                    SELECT ts_cccd,
                           manganh,
                           matohop,
                           phuongthuc,
                           diemCC,
                           diemUtxt,
                           diemTong
                    FROM xt_diemcongxetuyen
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String cccd = rs.getString("ts_cccd");
                String manganh = rs.getString("manganh");
                String matohop = rs.getString("matohop");
                String phuongthuc = rs.getString("phuongthuc");

                double diemCC = rs.getDouble("diemCC");
                double diemUT = rs.getDouble("diemUtxt");
                double diemTong1 = rs.getDouble("diemTong");

                model.addRow(new Object[]{
                        cccd,
                        manganh,
                        matohop,
                        phuongthuc,
                        diemCC,
                        diemUT,
                        diemTong1
                });

                tongHoSo++;

                tongDiem += diemTong1;

                if ("PT4".equals(phuongthuc)) {
                    tongPT4++;
                }
            }

            lblTongHoSo.setText(String.valueOf(tongHoSo));

            lblTongDiem.setText(String.format("%.2f", tongDiem));

            lblPT4.setText(String.valueOf(tongPT4));

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải dữ liệu từ database!"
            );
        }
    }
}