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
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 249, 250)); // Nền xám sáng hiện đại
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ XÉT TUYỂN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        // Khối trung tâm (Card Thống kê + Bảng dữ liệu)
        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createMainContent() {
        JPanel mainWrapper = new JPanel(new BorderLayout(0, 20));
        mainWrapper.setOpaque(false);

        // 1. Khối thẻ Thống kê (Nằm trên cùng)
        JPanel cardPanel = new JPanel(new GridLayout(1, 3, 30, 30));
        cardPanel.setOpaque(false);

        lblTong = new JLabel("0");
        lblTrungTuyen = new JLabel("0");
        lblKhongTrung = new JLabel("0");

        cardPanel.add(createStatCard("Tổng Thí Sinh Xét", lblTong, new Color(41, 128, 185)));
        cardPanel.add(createStatCard("Đã Trúng Tuyển", lblTrungTuyen, new Color(39, 174, 96)));
        cardPanel.add(createStatCard("Không Trúng Tuyển", lblKhongTrung, new Color(231, 76, 60)));

        mainWrapper.add(cardPanel, BorderLayout.NORTH);

        // 2. Khối Control (Tìm kiếm & Nút thao tác) + Table
        RoundedPanel tableWrapper = new RoundedPanel(20, Color.WHITE);
        tableWrapper.setLayout(new BorderLayout(10, 15));
        tableWrapper.setBorder(new EmptyBorder(20, 25, 20, 25));

        // --- Toolbar (Chứa input CCCD & các nút) ---
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setOpaque(false);

        // Left Toolbar (Xét 1 thí sinh)
        JPanel leftTool = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftTool.setOpaque(false);

        JLabel lblCCCD = new JLabel("Tra CCCD:");
        lblCCCD.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCCCD.setForeground(new Color(80, 80, 80));

        txtCCCD = new JTextField(15);
        txtCCCD.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCCCD.setPreferredSize(new Dimension(160, 38));
        txtCCCD.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));

        JButton btnXet1 = createFlatButton("Xét 1 Thí Sinh", new Color(52, 152, 219), new Color(41, 128, 185));
        btnXet1.setPreferredSize(new Dimension(140, 38));

        leftTool.add(lblCCCD);
        leftTool.add(txtCCCD);
        leftTool.add(btnXet1);

        // Right Toolbar (Xét tất cả & Thống kê)
        JPanel rightTool = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightTool.setOpaque(false);

        JButton btnXetAll = createFlatButton("Chạy Xét Toàn Bộ", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton btnThongKe = createFlatButton("Bảng Thống Kê", new Color(155, 89, 182), new Color(142, 68, 173));

        btnXetAll.setPreferredSize(new Dimension(160, 38));
        btnThongKe.setPreferredSize(new Dimension(140, 38));

        rightTool.add(btnXetAll);
        rightTool.add(btnThongKe);

        toolBar.add(leftTool, BorderLayout.WEST);
        toolBar.add(rightTool, BorderLayout.EAST);

        tableWrapper.add(toolBar, BorderLayout.NORTH);

        // --- Bảng kết quả ---
        String[] columns = {"STT", "CCCD", "Ngành", "Điểm XT", "Kết quả", "NV", "Ghi chú"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
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

        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        mainWrapper.add(tableWrapper, BorderLayout.CENTER);

        // Events
        btnXet1.addActionListener(e -> xetMotThiSinh());
        btnXetAll.addActionListener(e -> xetTatCa());
        btnThongKe.addActionListener(e -> thongKe());
        txtCCCD.addActionListener(e -> xetMotThiSinh()); // Enter để chạy

        return mainWrapper;
    }

    // ================= COMPONENTS CUSTOM =================

    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(130, 130, 130));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));

        valueLabel.setForeground(color);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JButton createFlatButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { btn.setBackground(hoverColor); }
            public void mouseExited(MouseEvent evt) { btn.setBackground(bgColor); }
        });

        return btn;
    }

    // ================= XÉT 1 THÍ SINH =================
    private void xetMotThiSinh() {
        String cccd = txtCCCD.getText().trim();
        if (cccd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập CCCD cần xét!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<NganhDTO> dsNganh = nganhBUS.getAll();
        List<String> dsMaNganh = new ArrayList<>();
        for (NganhDTO n : dsNganh) {
            dsMaNganh.add(n.getMaNganh());
        }

        List<XetTuyenBUS.KetQuaXetTuyen> list = xetTuyenBUS.xetTuyen(cccd, dsMaNganh);
        loadTable(list);
    }

    // ================= XÉT TẤT CẢ =================
    private void xetTatCa() {
        model.setRowCount(0);
        Map<String, List<XetTuyenBUS.KetQuaXetTuyen>> map = xetTuyenBUS.xetTuyenDoi();

        int stt = 1;
        int trung = 0;
        int khong = 0;

        for (List<XetTuyenBUS.KetQuaXetTuyen> list : map.values()) {
            for (XetTuyenBUS.KetQuaXetTuyen kq : list) {
                if (kq.trungTuyen) trung++;
                else khong++;

                model.addRow(new Object[]{
                        stt++, kq.cccd, kq.tenNganh,
                        String.format("%.2f", kq.diemXetTuyen),
                        kq.trungTuyen ? "Trúng tuyển" : "Không trúng",
                        kq.thuTuNguyenVong, kq.lyDo
                });
            }
        }

        lblTong.setText(String.valueOf(map.size()));
        lblTrungTuyen.setText(String.valueOf(trung));
        lblKhongTrung.setText(String.valueOf(khong));

        JOptionPane.showMessageDialog(this, "Đã chạy xét tuyển toàn bộ hồ sơ thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= LOAD BẢNG THEO LIST =================
    private void loadTable(List<XetTuyenBUS.KetQuaXetTuyen> list) {
        model.setRowCount(0);
        int stt = 1;
        int trung = 0;
        int khong = 0;

        for (XetTuyenBUS.KetQuaXetTuyen kq : list) {
            if (kq.trungTuyen) trung++;
            else khong++;

            model.addRow(new Object[]{
                    stt++, kq.cccd, kq.tenNganh,
                    String.format("%.2f", kq.diemXetTuyen),
                    kq.trungTuyen ? "Trúng tuyển" : "Không trúng",
                    kq.thuTuNguyenVong, kq.lyDo
            });
        }

        // Với 1 thí sinh, tổng thí sinh là 1
        lblTong.setText(list.isEmpty() ? "0" : "1");
        lblTrungTuyen.setText(String.valueOf(trung));
        lblKhongTrung.setText(String.valueOf(khong));
    }

    // ================= THỐNG KÊ CHI TIẾT =================
    private void thongKe() {
        XetTuyenBUS.ThongKeXetTuyen tk = xetTuyenBUS.thongKe();
        StringBuilder sb = new StringBuilder();

        sb.append("TỔNG THÍ SINH: ").append(tk.tongSoThiSinh).append("\n");
        sb.append("TRÚNG TUYỂN: ").append(tk.soTrungTuyen).append("\n");
        sb.append("KHÔNG TRÚNG: ").append(tk.soKhongTrungTuyen).append("\n");
        sb.append("TỶ LỆ: ").append(String.format("%.2f", tk.tiLeTrungTuyen)).append("%\n\n");
        sb.append("THEO NGÀNH:\n");

        for (String ma : tk.thongKeTheoNganh.keySet()) {
            sb.append("- ").append(ma).append(": ").append(tk.thongKeTheoNganh.get(ma)).append(" thí sinh\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Bảng Thống Kê Tổng Hợp", JOptionPane.INFORMATION_MESSAGE);
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
            
            // Bóng đổ
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Nền trắng
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            // Viền nhạt
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            g2.dispose();
        }
    }
}