package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.tuyensinh.BUS.NganhBUS;
import com.tuyensinh.BUS.XetTuyenBUS;
import com.tuyensinh.DTO.NganhDTO;

/**
 * XetTuyenPanel - Giao diện xét tuyển
 * KHÔNG cần XetTuyenDAO hay XetTuyenDTO
 * Vì đang dùng inner class trong XetTuyenBUS
 */
public class XetTuyenPanel extends JPanel {

    private XetTuyenBUS xetTuyenBUS;
    private NganhBUS nganhBUS;

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtCCCD;

    private JLabel lblTong;
    private JLabel lblTrungTuyen;
    private JLabel lblKhongTrung;

    public XetTuyenPanel() {

        xetTuyenBUS = new XetTuyenBUS();
        nganhBUS = new NganhBUS();

        initComponents();
    }

    // ================= INIT =================

    private void initComponents() {

        setLayout(new BorderLayout(15, 15));

        setBackground(new Color(245, 247, 250));

        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(createTopPanel(), BorderLayout.NORTH);

        add(createCenterPanel(), BorderLayout.CENTER);

        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    // ================= TOP =================

    private JPanel createTopPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ XÉT TUYỂN");

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));

        panel.add(lblTitle, BorderLayout.WEST);

        return panel;
    }

    // ================= CENTER =================

    private JPanel createCenterPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // ===== CARD =====

        JPanel cardPanel = new JPanel(new GridLayout(1, 3, 15, 15));

        cardPanel.setBackground(Color.WHITE);

        lblTong = createCardValue("0");

        lblTrungTuyen = createCardValue("0");

        lblKhongTrung = createCardValue("0");

        cardPanel.add(createCard(
                "Tổng thí sinh",
                lblTong,
                new Color(52, 152, 219)
        ));

        cardPanel.add(createCard(
                "Trúng tuyển",
                lblTrungTuyen,
                new Color(46, 204, 113)
        ));

        cardPanel.add(createCard(
                "Không trúng",
                lblKhongTrung,
                new Color(231, 76, 60)
        ));

        panel.add(cardPanel, BorderLayout.NORTH);

        // ===== TABLE =====

        String[] columns = {
                "STT",
                "CCCD",
                "Ngành",
                "Điểm XT",
                "Kết quả",
                "NV",
                "Ghi chú"
        };

        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);

        table.setRowHeight(35);

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

    // ================= BOTTOM =================

    private JPanel createBottomPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        // LEFT

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));

        left.setOpaque(false);

        left.add(new JLabel("CCCD:"));

        txtCCCD = new JTextField(20);

        left.add(txtCCCD);

        JButton btnXet1 = createButton(
                "Xét 1 thí sinh",
                new Color(52, 152, 219)
        );

        btnXet1.addActionListener(e -> xetMotThiSinh());

        left.add(btnXet1);

        // RIGHT

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        right.setOpaque(false);

        JButton btnXetAll = createButton(
                "Xét toàn bộ",
                new Color(46, 204, 113)
        );

        JButton btnThongKe = createButton(
                "Thống kê",
                new Color(155, 89, 182)
        );

        btnXetAll.addActionListener(e -> xetTatCa());

        btnThongKe.addActionListener(e -> thongKe());

        right.add(btnXetAll);

        right.add(btnThongKe);

        panel.add(left, BorderLayout.WEST);

        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    // ================= CARD =================

    private JPanel createCard(
            String title,
            JLabel value,
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

        value.setForeground(color);

        value.setFont(new Font("Segoe UI", Font.BOLD, 28));

        card.add(lblTitle, BorderLayout.NORTH);

        card.add(value, BorderLayout.CENTER);

        return card;
    }

    private JLabel createCardValue(String text) {

        return new JLabel(text);
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

        btn.setPreferredSize(new Dimension(150, 40));

        return btn;
    }

    // ================= XÉT 1 =================

    private void xetMotThiSinh() {

        String cccd = txtCCCD.getText().trim();

        if (cccd.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập CCCD!"
            );

            return;
        }

        List<NganhDTO> dsNganh = nganhBUS.getAll();

        List<String> dsMaNganh = new ArrayList<>();

        for (NganhDTO n : dsNganh) {

            dsMaNganh.add(n.getMaNganh());
        }

        List<XetTuyenBUS.KetQuaXetTuyen> list =
                xetTuyenBUS.xetTuyen(cccd, dsMaNganh);

        loadTable(list);
    }

    // ================= XÉT TẤT CẢ =================

    private void xetTatCa() {

        model.setRowCount(0);

        Map<String, List<XetTuyenBUS.KetQuaXetTuyen>> map =
                xetTuyenBUS.xetTuyenDoi();

        int stt = 1;

        int trung = 0;

        int khong = 0;

        for (List<XetTuyenBUS.KetQuaXetTuyen> list : map.values()) {

            for (XetTuyenBUS.KetQuaXetTuyen kq : list) {

                if (kq.trungTuyen) {
                    trung++;
                } else {
                    khong++;
                }

                model.addRow(new Object[]{
                        stt++,
                        kq.cccd,
                        kq.tenNganh,
                        String.format("%.2f", kq.diemXetTuyen),
                        kq.trungTuyen
                                ? "Trúng tuyển"
                                : "Không trúng",
                        kq.thuTuNguyenVong,
                        kq.lyDo
                });
            }
        }

        lblTong.setText(String.valueOf(map.size()));

        lblTrungTuyen.setText(String.valueOf(trung));

        lblKhongTrung.setText(String.valueOf(khong));

        JOptionPane.showMessageDialog(
                this,
                "Đã xét tuyển xong!"
        );
    }

    // ================= LOAD TABLE =================

    private void loadTable(
            List<XetTuyenBUS.KetQuaXetTuyen> list
    ) {

        model.setRowCount(0);

        int stt = 1;

        int trung = 0;

        int khong = 0;

        for (XetTuyenBUS.KetQuaXetTuyen kq : list) {

            if (kq.trungTuyen) {
                trung++;
            } else {
                khong++;
            }

            model.addRow(new Object[]{
                    stt++,
                    kq.cccd,
                    kq.tenNganh,
                    String.format("%.2f", kq.diemXetTuyen),
                    kq.trungTuyen
                            ? "Trúng tuyển"
                            : "Không trúng",
                    kq.thuTuNguyenVong,
                    kq.lyDo
            });
        }

        lblTong.setText("1");

        lblTrungTuyen.setText(String.valueOf(trung));

        lblKhongTrung.setText(String.valueOf(khong));
    }

    // ================= THỐNG KÊ =================

    private void thongKe() {

        XetTuyenBUS.ThongKeXetTuyen tk =
                xetTuyenBUS.thongKe();

        StringBuilder sb = new StringBuilder();

        sb.append("TỔNG THÍ SINH: ")
                .append(tk.tongSoThiSinh)
                .append("\n");

        sb.append("TRÚNG TUYỂN: ")
                .append(tk.soTrungTuyen)
                .append("\n");

        sb.append("KHÔNG TRÚNG: ")
                .append(tk.soKhongTrungTuyen)
                .append("\n");

        sb.append("TỶ LỆ: ")
                .append(String.format("%.2f", tk.tiLeTrungTuyen))
                .append("%\n\n");

        sb.append("THEO NGÀNH:\n");

        for (String ma : tk.thongKeTheoNganh.keySet()) {

            sb.append(ma)
                    .append(": ")
                    .append(tk.thongKeTheoNganh.get(ma))
                    .append("\n");
        }

        JOptionPane.showMessageDialog(
                this,
                sb.toString(),
                "Thống kê",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}