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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
import com.tuyensinh.BUS.ThiSinhBUS;
import com.tuyensinh.BUS.XetTuyenBUS;
import com.tuyensinh.DAO.NganhToHopDAO;
import com.tuyensinh.DAO.NguyenVongDAO;
import com.tuyensinh.DTO.NganhDTO;
import com.tuyensinh.DTO.NganhToHopDTO;
import com.tuyensinh.DTO.NguyenVongDTO;
import com.tuyensinh.DTO.ThiSinhDTO;
import com.tuyensinh.config.DB;

public class XetTuyenPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtCCCD;

    private JLabel lblTong;
    private JLabel lblTrungTuyen;
    private JLabel lblKhongTrung;

    private final XetTuyenBUS xetTuyenBUS;
    private final NganhBUS nganhBUS;
    private final ThiSinhBUS thiSinhBUS;
    private final NganhToHopDAO nganhToHopDAO;

    public XetTuyenPanel() {

        this.xetTuyenBUS = new XetTuyenBUS();
        this.nganhBUS = new NganhBUS();
        this.thiSinhBUS = new ThiSinhBUS();
        this.nganhToHopDAO = new NganhToHopDAO();

        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 246, 250));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblTitle =
                new JLabel("QUẢN LÝ XÉT TUYỂN");

        lblTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        add(lblTitle, BorderLayout.NORTH);

        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createMainContent() {

        JPanel wrapper =
                new JPanel(new BorderLayout(0, 20));

        wrapper.setOpaque(false);

        JPanel cardPanel =
                new JPanel(new GridLayout(1, 3, 20, 20));

        cardPanel.setOpaque(false);

        lblTong = new JLabel("0");
        lblTrungTuyen = new JLabel("0");
        lblKhongTrung = new JLabel("0");

        cardPanel.add(createStatCard(
                "Tổng Hồ Sơ",
                lblTong,
                new Color(52, 152, 219)
        ));

        cardPanel.add(createStatCard(
                "Trúng Tuyển",
                lblTrungTuyen,
                new Color(46, 204, 113)
        ));

        cardPanel.add(createStatCard(
                "Không Trúng",
                lblKhongTrung,
                new Color(231, 76, 60)
        ));

        wrapper.add(cardPanel, BorderLayout.NORTH);

        RoundedPanel tablePanel =
                new RoundedPanel(20, Color.WHITE);

        tablePanel.setLayout(
                new BorderLayout(10, 15)
        );

        tablePanel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        JPanel toolbar =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        toolbar.setOpaque(false);

        JLabel lblCCCD =
                new JLabel("CCCD:");

        txtCCCD = new JTextField(18);

        txtCCCD.setPreferredSize(
                new Dimension(220, 38)
        );

        JButton btnXet =
                createButton(
                        "Xét Tuyển",
                        new Color(52, 152, 219),
                        new Color(41, 128, 185)
                );

        toolbar.add(lblCCCD);
        toolbar.add(txtCCCD);
        toolbar.add(btnXet);

        tablePanel.add(toolbar, BorderLayout.NORTH);

        String[] columns = {
            "STT",
            "CCCD",
            "Ngành",
            "Tổ hợp",
            "Điểm THXT",
            "Điểm cộng",
            "Điểm UT",
            "Điểm XT",
            "Kết quả",
            "Ghi chú"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(35);

        JScrollPane scrollPane =
                new JScrollPane(table);

        tablePanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        wrapper.add(
                tablePanel,
                BorderLayout.CENTER
        );

        btnXet.addActionListener(
                e -> xetMotThiSinh()
        );

        return wrapper;
    }

    // =====================================================
    // XÉT 1 THÍ SINH
    // =====================================================

   // CHỈ THAY METHOD xetMotThiSinh()

private void xetMotThiSinh() {

    String cccd =
            txtCCCD.getText().trim();

    if (cccd.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Nhập CCCD"
        );

        return;
    }

    ThiSinhDTO ts =
            thiSinhBUS.getByCCCD(cccd);

    if (ts == null) {

        JOptionPane.showMessageDialog(
                this,
                "Không tìm thấy thí sinh"
        );

        return;
    }

    model.setRowCount(0);

    int stt = 1;

    try {

        Connection conn =
                DB.getConn();

        NguyenVongDAO nvDAO =
                new NguyenVongDAO(conn);

        List<NganhDTO> dsNganh =
                nganhBUS.getAll();

        int trung = 0;
        int rot = 0;

        for (NganhDTO nganh : dsNganh) {

            List<NganhToHopDTO> dsToHop =
                    nganhToHopDAO.getAll();

            List<XetTuyenBUS.KetQuaXetTuyen> ketQuaNganh =
                    new ArrayList<>();

            int nv = 1;

            // =====================================
            // TÍNH TẤT CẢ TỔ HỢP
            // =====================================

            for (NganhToHopDTO th : dsToHop) {

                if (!th.getMaNganh().equalsIgnoreCase(
                        nganh.getMaNganh()
                )) {
                    continue;
                }

                XetTuyenBUS.KetQuaXetTuyen kq =
                        xetTuyenBUS.tinhDiem(
                                ts,
                                nganh.getMaNganh(),
                                th.getMaToHop(),
                                "THPT",
                                nv++
                        );

                ketQuaNganh.add(kq);
            }

            // =====================================
            // LẤY TỔ HỢP CAO NHẤT
            // =====================================

            XetTuyenBUS.KetQuaXetTuyen best =
                    xetTuyenBUS.getToHopCaoNhat(
                            ketQuaNganh
                    );

            if (best == null) {
                continue;
            }

            // =====================================
            // CHỈ GIỮ NV CAO NHẤT TRÚNG TUYỂN
            // =====================================

            if (best.trungTuyen) {
                trung++;
            } else {
                rot++;
            }

            model.addRow(new Object[]{
                stt++,
                best.cccd,
                best.tenNganh,
                best.toHop,
                best.diemTHXT,
                best.diemCong,
                best.diemUT,
                best.diemXetTuyen,
                best.trungTuyen
                        ? "Trúng tuyển"
                        : "Không trúng",
                best.lyDo
            });

            NguyenVongDTO nvDTO =
                    new NguyenVongDTO();

            nvDTO.setCccd(best.cccd);

            nvDTO.setMaNganh(
                    best.maNganh
            );

            nvDTO.setThuTuNV(
                    best.thuTuNguyenVong
            );

            nvDTO.setDiemTHXT(
                    best.diemTHXT
            );

            nvDTO.setDiemUTQD(
                    best.diemUT
            );

            nvDTO.setDiemCong(
                    best.diemCong
            );

            nvDTO.setDiemXetTuyen(
                    best.diemXetTuyen
            );

            nvDTO.setKetQua(
                    best.trungTuyen
                            ? "TRUNG_TUYEN"
                            : "ROT"
            );

            nvDTO.setKeys(
                    best.cccd
                            + best.maNganh
                            + best.toHop
            );

            nvDTO.setPhuongThuc(
                    best.phuongThuc
            );

            nvDTO.setToHopMon(
                    best.toHop
            );

            nvDAO.insert(nvDTO);
        }

        lblTong.setText("1");

        lblTrungTuyen.setText(
                String.valueOf(trung)
        );

        lblKhongTrung.setText(
                String.valueOf(rot)
        );

        conn.close();

    } catch (Exception e) {

        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Lỗi xét tuyển"
        );
    }
}

    // =====================================================
    // CARD
    // =====================================================

    private JPanel createStatCard(
            String title,
            JLabel value,
            Color color
    ) {

        RoundedPanel panel =
                new RoundedPanel(20, Color.WHITE);

        panel.setLayout(new BorderLayout());

        panel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        JLabel lblTitle =
                new JLabel(title);

        lblTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        value.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        36
                )
        );

        value.setForeground(color);

        panel.add(lblTitle, BorderLayout.NORTH);

        panel.add(value, BorderLayout.CENTER);

        return panel;
    }

    // =====================================================
    // BUTTON
    // =====================================================

    private JButton createButton(
            String text,
            Color bg,
            Color hover
    ) {

        JButton btn = new JButton(text);

        btn.setBackground(bg);

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.addMouseListener(
                new MouseAdapter() {

            @Override
            public void mouseEntered(
                    MouseEvent e
            ) {
                btn.setBackground(hover);
            }

            @Override
            public void mouseExited(
                    MouseEvent e
            ) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    // =====================================================
    // ROUNDED PANEL
    // =====================================================

    class RoundedPanel extends JPanel {

        private final int radius;
        private final Color bg;

        public RoundedPanel(
                int radius,
                Color bg
        ) {

            this.radius = radius;
            this.bg = bg;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(bg);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}