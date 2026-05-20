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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.tuyensinh.BUS.DiemThiBUS;
import com.tuyensinh.BUS.NganhBUS;
import com.tuyensinh.BUS.ThiSinhBUS;
import com.tuyensinh.BUS.XetTuyenBUS;
import com.tuyensinh.DAO.NganhToHopDAO;
import com.tuyensinh.DAO.NguyenVongDAO;
import com.tuyensinh.DTO.DiemThiDTO;
import com.tuyensinh.DTO.NganhDTO;
import com.tuyensinh.DTO.NganhToHopDTO;
import com.tuyensinh.DTO.NguyenVongDTO;
import com.tuyensinh.DTO.ThiSinhDTO;
import com.tuyensinh.config.DB;

public class XetTuyenPanel extends JPanel {

        private JTable table;
        private DefaultTableModel model;

        private JTextField txtCCCD;
        private javax.swing.JComboBox<String> cboPhuongThuc;
        private JLabel lblStatus;
        private JButton btnXet;
        private JButton btnXetAll;

        private JLabel lblTong;
        private JLabel lblTrungTuyen;
        private JLabel lblKhongTrung;

        private final XetTuyenBUS xetTuyenBUS;
        private final NganhBUS nganhBUS;
        private final DiemThiBUS diemThiBUS;
        private final ThiSinhBUS thiSinhBUS;
        private final NganhToHopDAO nganhToHopDAO;

        public XetTuyenPanel() {

                this.xetTuyenBUS = new XetTuyenBUS();
                this.nganhBUS = new NganhBUS();
                this.diemThiBUS = new DiemThiBUS();
                this.thiSinhBUS = new ThiSinhBUS();
                this.nganhToHopDAO = new NganhToHopDAO();

                initComponents();
        }

        private void initComponents() {

                setLayout(new BorderLayout(20, 20));
                setBackground(new Color(245, 246, 250));
                setBorder(new EmptyBorder(25, 25, 25, 25));

                JLabel lblTitle = new JLabel("QUẢN LÝ XÉT TUYỂN");

                lblTitle.setFont(
                                new Font(
                                                "Segoe UI",
                                                Font.BOLD,
                                                28));

                add(lblTitle, BorderLayout.NORTH);

                add(createMainContent(), BorderLayout.CENTER);
        }

        private JPanel createMainContent() {

                JPanel wrapper = new JPanel(new BorderLayout(0, 20));

                wrapper.setOpaque(false);

                JPanel cardPanel = new JPanel(new GridLayout(1, 3, 20, 20));

                cardPanel.setOpaque(false);

                lblTong = new JLabel("0");
                lblTrungTuyen = new JLabel("0");
                lblKhongTrung = new JLabel("0");

                cardPanel.add(createStatCard(
                                "Tổng Hồ Sơ",
                                lblTong,
                                new Color(52, 152, 219)));

                cardPanel.add(createStatCard(
                                "Trúng Tuyển",
                                lblTrungTuyen,
                                new Color(46, 204, 113)));

                cardPanel.add(createStatCard(
                                "Không Trúng",
                                lblKhongTrung,
                                new Color(231, 76, 60)));

                wrapper.add(cardPanel, BorderLayout.NORTH);

                RoundedPanel tablePanel = new RoundedPanel(20, Color.WHITE);

                tablePanel.setLayout(
                                new BorderLayout(10, 15));

                tablePanel.setBorder(
                                new EmptyBorder(20, 20, 20, 20));

                JPanel toolbar = new JPanel(
                                new FlowLayout(
                                                FlowLayout.LEFT,
                                                10,
                                                0));

                toolbar.setOpaque(false);

                JLabel lblCCCD = new JLabel("CCCD:");

                JLabel lblPhuongThuc = new JLabel("Phương thức:");

                txtCCCD = new JTextField(18);

                txtCCCD.setPreferredSize(
                                new Dimension(220, 38));

                cboPhuongThuc = new javax.swing.JComboBox<>(
                                new String[] { "THPT", "DGNL", "VSAT" });

                cboPhuongThuc.setPreferredSize(
                                new Dimension(120, 38));

                btnXet = createButton(
                                "Xét Tuyển",
                                new Color(52, 152, 219),
                                new Color(41, 128, 185));

                btnXetAll = createButton(
                                "Xét Tuyển Tất Cả",
                                new Color(155, 89, 182),
                                new Color(142, 68, 173));

                lblStatus = new JLabel("");
                lblStatus.setForeground(new Color(120, 120, 120));

                toolbar.add(lblCCCD);
                toolbar.add(txtCCCD);
                toolbar.add(btnXet);
                toolbar.add(lblPhuongThuc);
                toolbar.add(cboPhuongThuc);
                toolbar.add(btnXetAll);
                toolbar.add(lblStatus);

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
                                        int column) {
                                return false;
                        }
                };

                table = new JTable(model);

                table.setRowHeight(35);

                JScrollPane scrollPane = new JScrollPane(table);

                tablePanel.add(
                                scrollPane,
                                BorderLayout.CENTER);

                wrapper.add(
                                tablePanel,
                                BorderLayout.CENTER);

                btnXet.addActionListener(
                                e -> xetMotThiSinh());

                btnXetAll.addActionListener(
                                e -> xetTatCaThiSinh());

                return wrapper;
        }

        // =====================================================
        // XÉT 1 THÍ SINH
        // =====================================================

        // CHỈ THAY METHOD xetMotThiSinh()

        private void xetMotThiSinh() {

                String cccd = txtCCCD.getText().trim();

                if (cccd.isEmpty()) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Nhập CCCD");

                        return;
                }

                ThiSinhDTO ts = thiSinhBUS.getByCCCD(cccd);

                if (ts == null) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Không tìm thấy thí sinh");

                        return;
                }

                model.setRowCount(0);

                int stt = 1;

                try {

                        Connection conn = DB.getConn();

                        NguyenVongDAO nvDAO = new NguyenVongDAO(conn);

                        List<NganhToHopDTO> dsToHop = nganhToHopDAO.getAll();

                        java.util.Map<String, List<NganhToHopDTO>> toHopByNganh = new java.util.HashMap<>();

                        if (dsToHop != null) {
                                for (NganhToHopDTO th : dsToHop) {
                                        toHopByNganh
                                                        .computeIfAbsent(
                                                                        th.getMaNganh(),
                                                                        k -> new ArrayList<>())
                                                        .add(th);
                                }
                        }

                        List<NguyenVongDTO> nvList = nvDAO.getByCCCDOrderNV(cccd);

                        int trung = 0;
                        int rot = 0;
                        boolean daTrung = false;

                        for (NguyenVongDTO nv : nvList) {
                                String maNganh = nv.getMaNganh();
                                List<NganhToHopDTO> dsToHopNganh = toHopByNganh.get(maNganh);

                                if (dsToHopNganh == null || dsToHopNganh.isEmpty()) {
                                        continue;
                                }

                               String phuongThucNV = getPhuongThucSelected();

                                if (daTrung) {
                                        model.addRow(new Object[] {
                                                        stt++,
                                                        ts.getCccd(),
                                                        maNganh,
                                                        nv.getToHopMon(),
                                                        0,
                                                        0,
                                                        0,
                                                        0,
                                                        "Bỏ",
                                                        "Nguyện vọng trước đã trúng tuyển"
                                        });

                                        NguyenVongDTO boDTO = new NguyenVongDTO();
                                        boDTO.setCccd(ts.getCccd());
                                        boDTO.setMaNganh(maNganh);
                                        boDTO.setThuTuNV(nv.getThuTuNV());
                                        boDTO.setDiemTHXT(0);
                                        boDTO.setDiemUTQD(0);
                                        boDTO.setDiemCong(0);
                                        boDTO.setDiemXetTuyen(0);
                                        boDTO.setKetQua("BO");
                                        boDTO.setKeys(nv.getKeys());
                                        boDTO.setPhuongThuc(phuongThucNV);
                                        boDTO.setToHopMon(nv.getToHopMon());
                                        nvDAO.insert(boDTO);
                                        continue;
                                }

                                List<XetTuyenBUS.KetQuaXetTuyen> ketQuaNganh = new ArrayList<>();

                                for (NganhToHopDTO th : dsToHopNganh) {
                                        XetTuyenBUS.KetQuaXetTuyen kq = xetTuyenBUS.tinhDiem(
                                                        ts,
                                                        maNganh,
                                                        th.getMaToHop(),
                                                        phuongThucNV,
                                                        nv.getThuTuNV());

                                        ketQuaNganh.add(kq);
                                }

                                XetTuyenBUS.KetQuaXetTuyen best = xetTuyenBUS.getToHopCaoNhat(
                                                ketQuaNganh);

                                if (best == null) {
                                        continue;
                                }

                                if (best.trungTuyen) {
                                        trung++;
                                        daTrung = true;
                                } else {
                                        rot++;
                                }

                                model.addRow(new Object[] {
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

                                NguyenVongDTO nvDTO = new NguyenVongDTO();

                                nvDTO.setCccd(best.cccd);
                                nvDTO.setMaNganh(best.maNganh);
                                nvDTO.setThuTuNV(best.thuTuNguyenVong);
                                nvDTO.setDiemTHXT(best.diemTHXT);
                                nvDTO.setDiemUTQD(best.diemUT);
                                nvDTO.setDiemCong(best.diemCong);
                                nvDTO.setDiemXetTuyen(best.diemXetTuyen);
                                nvDTO.setKetQua(best.trungTuyen
                                                ? "TRUNG_TUYEN"
                                                : "ROT");
                                nvDTO.setKeys(nv.getKeys());
                                nvDTO.setPhuongThuc(best.phuongThuc);
                                nvDTO.setToHopMon(best.toHop);
                                nvDAO.insert(nvDTO);
                        }

                        lblTong.setText("1");
                        lblTrungTuyen.setText(String.valueOf(trung));
                        lblKhongTrung.setText(String.valueOf(rot));

                        conn.close();

                } catch (Exception e) {

                        e.printStackTrace();

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Lỗi xét tuyển");
                }
        }

        // =====================================================
        // CARD
        // =====================================================

        private JPanel createStatCard(
                        String title,
                        JLabel value,
                        Color color) {

                RoundedPanel panel = new RoundedPanel(20, Color.WHITE);

                panel.setLayout(new BorderLayout());

                panel.setBorder(
                                new EmptyBorder(20, 20, 20, 20));

                JLabel lblTitle = new JLabel(title);

                lblTitle.setFont(
                                new Font(
                                                "Segoe UI",
                                                Font.BOLD,
                                                15));

                value.setFont(
                                new Font(
                                                "Segoe UI",
                                                Font.BOLD,
                                                36));

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
                        Color hover) {

                JButton btn = new JButton(text);

                btn.setBackground(bg);

                btn.setForeground(Color.WHITE);

                btn.setFocusPainted(false);

                btn.setBorderPainted(false);

                btn.setCursor(
                                new Cursor(Cursor.HAND_CURSOR));

                btn.addMouseListener(
                                new MouseAdapter() {

                                        @Override
                                        public void mouseEntered(
                                                        MouseEvent e) {
                                                btn.setBackground(hover);
                                        }

                                        @Override
                                        public void mouseExited(
                                                        MouseEvent e) {
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
                                Color bg) {

                        this.radius = radius;
                        this.bg = bg;

                        setOpaque(false);
                }

                @Override
                protected void paintComponent(Graphics g) {

                        Graphics2D g2 = (Graphics2D) g.create();

                        g2.setRenderingHint(
                                        RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);

                        g2.setColor(bg);

                        g2.fillRoundRect(
                                        0,
                                        0,
                                        getWidth(),
                                        getHeight(),
                                        radius,
                                        radius);

                        g2.dispose();

                        super.paintComponent(g);
                }
        }

        // =====================================================
        // XÉT TẤT CẢ THÍ SINH
        // =====================================================
        private void xetTatCaThiSinh() {

                if (btnXetAll != null && !btnXetAll.isEnabled()) {
                        return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                                this,
                                "Xét tuyển toàn bộ thí sinh và lưu DB?",
                                "Xác nhận",
                                JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION) {
                        return;
                }

                startXetTatCaWorker();
        }

        private void startXetTatCaWorker() {
                setBulkUiState(true);

                SwingWorker<XetTatCaResult, Void> worker = new SwingWorker<>() {

                        @Override
                        protected XetTatCaResult doInBackground() {
                                return runXetTatCa();
                        }

                        @Override
                        protected void done() {
                                try {
                                        XetTatCaResult result = get();
                                        if (result == null) {
                                                return;
                                        }

                                        if (result.errorMessage != null
                                                        && !result.errorMessage.isEmpty()) {
                                                JOptionPane.showMessageDialog(
                                                                XetTuyenPanel.this,
                                                                result.errorMessage);
                                                return;
                                        }

                                        model.setRowCount(0);
                                        int stt = 1;
                                        for (Object[] row : result.rows) {
                                                Object[] newRow = new Object[row.length + 1];
                                                newRow[0] = stt++;
                                                System.arraycopy(row, 0, newRow, 1, row.length);
                                                model.addRow(newRow);
                                        }

                                        lblTong.setText(String.valueOf(result.tong));
                                        lblTrungTuyen.setText(String.valueOf(result.trung));
                                        lblKhongTrung.setText(String.valueOf(result.rot));

                                } catch (Exception e) {
                                        e.printStackTrace();
                                        JOptionPane.showMessageDialog(
                                                        XetTuyenPanel.this,
                                                        "Lỗi xét tuyển");
                                } finally {
                                        setBulkUiState(false);
                                }
                        }
                };

                worker.execute();
        }

        private XetTatCaResult runXetTatCa() {
                List<Object[]> rows = new ArrayList<>();
                List<NguyenVongDTO> pendingInsert = new ArrayList<>();
                Map<String, List<CandidateNV>> nvByCccd = new HashMap<>();
                int trung = 0;
                int rot = 0;

                String phuongThuc = getPhuongThucSelected();

                try (Connection conn = DB.getConn()) {

                        NguyenVongDAO nvDAO = new NguyenVongDAO(conn);

                        List<NguyenVongDTO> nvAll = nvDAO.getAll();
                        if (nvAll == null || nvAll.isEmpty()) {
                                return XetTatCaResult.error(
                                                "Không có nguyện vọng để xét tuyển");
                        }

                        Map<String, List<NguyenVongDTO>> nvByCccdRaw = new HashMap<>();
                        for (NguyenVongDTO nv : nvAll) {
                                nvByCccdRaw
                                                .computeIfAbsent(nv.getCccd(), k -> new ArrayList<>())
                                                .add(nv);
                        }

                        for (List<NguyenVongDTO> list : nvByCccdRaw.values()) {
                                list.sort(Comparator.comparingInt(NguyenVongDTO::getThuTuNV));
                        }

                        List<NganhDTO> dsNganh = nganhBUS.getAll();

                        Map<String, NganhDTO> nganhMap = new HashMap<>();
                        if (dsNganh != null) {
                                for (NganhDTO nganh : dsNganh) {
                                        nganhMap.put(nganh.getMaNganh(), nganh);
                                }
                        }

                        List<NganhToHopDTO> dsToHop = nganhToHopDAO.getAll();

                        Map<String, List<NganhToHopDTO>> toHopByNganh = new HashMap<>();
                        if (dsToHop != null) {
                                for (NganhToHopDTO th : dsToHop) {
                                        toHopByNganh
                                                        .computeIfAbsent(
                                                                        th.getMaNganh(),
                                                                        k -> new ArrayList<>())
                                                        .add(th);
                                }
                        }

                        List<ThiSinhDTO> tsList = thiSinhBUS.getAll();
                        Map<String, ThiSinhDTO> tsByCccd = new HashMap<>();
                        if (tsList != null) {
                                for (ThiSinhDTO ts : tsList) {
                                        tsByCccd.put(ts.getCccd(), ts);
                                }
                        }

                        List<DiemThiDTO> diemList = diemThiBUS.getAll();
                        Map<String, DiemThiDTO> diemByKey = new HashMap<>();
                        Map<String, DiemThiDTO> diemByCccd = new HashMap<>();
                        if (diemList != null) {
                                for (DiemThiDTO d : diemList) {
                                        String key = d.getCccd() + "|" + d.getD_phuongthuc();
                                        diemByKey.put(key, d);
                                        diemByCccd.putIfAbsent(d.getCccd(), d);
                                }
                        }

                        List<CandidateNV> allNV = new ArrayList<>();

                        for (Map.Entry<String, List<NguyenVongDTO>> entry : nvByCccdRaw.entrySet()) {
                                String cccd = entry.getKey();
                                ThiSinhDTO ts = tsByCccd.get(cccd);
                                if (ts == null) {
                                        continue;
                                }

                                for (NguyenVongDTO nv : entry.getValue()) {
                                        String maNganh = nv.getMaNganh();
                                        NganhDTO nganh = nganhMap.get(maNganh);
                                        List<NganhToHopDTO> dsToHopNganh = toHopByNganh.get(maNganh);

                                        if (nganh == null || dsToHopNganh == null || dsToHopNganh.isEmpty()) {
                                                continue;
                                        }

                                        String phuongThucNV = phuongThuc;

                                        DiemThiDTO diem = diemByKey.get(cccd + "|" + phuongThucNV);
                                        if (diem == null) {
                                                diem = diemByCccd.get(cccd);
                                        }

                                        if (diem == null) {
                                                continue;
                                        }

                                        List<XetTuyenBUS.KetQuaXetTuyen> ketQuaNganh = new ArrayList<>();

                                        for (NganhToHopDTO th : dsToHopNganh) {
                                                XetTuyenBUS.KetQuaXetTuyen kq = xetTuyenBUS.tinhDiemCached(
                                                                ts,
                                                                maNganh,
                                                                th.getMaToHop(),
                                                                nganh,
                                                                th,
                                                                diem,
                                                                phuongThucNV,
                                                                nv.getThuTuNV());

                                                ketQuaNganh.add(kq);
                                        }

                                        XetTuyenBUS.KetQuaXetTuyen best = xetTuyenBUS.getToHopCaoNhat(
                                                        ketQuaNganh);

                                        if (best == null) {
                                                continue;
                                        }

                                        CandidateNV cand = new CandidateNV(
                                                        cccd,
                                                        nv.getThuTuNV(),
                                                        maNganh,
                                                        best.tenNganh,
                                                        best.toHop,
                                                        best.diemTHXT,
                                                        best.diemCong,
                                                        best.diemUT,
                                                        best.diemXetTuyen,
                                                        nv.getKeys(),
                                                        nv.getToHopMon(),
                                                        phuongThucNV);

                                        allNV.add(cand);
                                        nvByCccd.computeIfAbsent(cccd, k -> new ArrayList<>())
                                                        .add(cand);
                                }
                        }

                        Map<String, Double> cutoffByNganh = new HashMap<>();

                        for (Map.Entry<String, NganhDTO> entry : nganhMap.entrySet()) {
                                String maNganh = entry.getKey();
                                NganhDTO nganh = entry.getValue();

                                int chiTieu = nganh.getChiTieu();
                                List<CandidateNV> list = new ArrayList<>();
                                for (CandidateNV nv : allNV) {
                                        if (maNganh.equalsIgnoreCase(nv.maNganh)) {
                                                list.add(nv);
                                        }
                                }

                                if (chiTieu <= 0 || list.isEmpty()) {
                                        cutoffByNganh.put(maNganh, Double.POSITIVE_INFINITY);
                                        continue;
                                }

                                list.sort(
                                                Comparator.comparingDouble(
                                                                (CandidateNV o) -> o.diemXetTuyen).reversed());

                                double cutoff;
                                if (list.size() < chiTieu) {
                                        cutoff = list.get(list.size() - 1).diemXetTuyen;
                                } else {
                                        cutoff = list.get(chiTieu - 1).diemXetTuyen;
                                }

                                Double diemSan = nganh.getDiemSan();
                                if (diemSan != null && cutoff < diemSan) {
                                        cutoff = diemSan;
                                }

                                cutoffByNganh.put(maNganh, cutoff);
                        }

                        for (Map.Entry<String, Double> entry : cutoffByNganh.entrySet()) {
                                String maNganh = entry.getKey();
                                NganhDTO nganh = nganhMap.get(maNganh);
                                if (nganh == null) {
                                        continue;
                                }

                                Double cutoff = entry.getValue();
                                Double saveValue = Double.isInfinite(cutoff) ? null : cutoff;
                                nganh.setDiemTrungTuyen(saveValue);
                                nganhBUS.updateNganh(nganh);
                        }

                        for (Map.Entry<String, List<CandidateNV>> entry : nvByCccd.entrySet()) {
                                List<CandidateNV> list = entry.getValue();
                                list.sort(Comparator.comparingInt(o -> o.nvThuTu));

                                boolean daTrung = false;

                                for (CandidateNV nv : list) {
                                        double cutoff = cutoffByNganh.getOrDefault(
                                                        nv.maNganh,
                                                        Double.POSITIVE_INFINITY);

                                        String ketQua;
                                        String ghiChu = "";

                                        Double diemSan = null;
                                        NganhDTO nganh = nganhMap.get(nv.maNganh);
                                        if (nganh != null) {
                                                diemSan = nganh.getDiemSan();
                                        }

                                        if (diemSan != null && nv.diemXetTuyen < diemSan) {
                                                ketQua = "ROT";
                                                ghiChu = "Dưới điểm sàn";
                                        } else if (daTrung) {
                                                ketQua = "ROT";
                                                ghiChu = "Nguyện vọng trước đã trúng tuyển";
                                        } else if (nv.diemXetTuyen >= cutoff) {
                                                ketQua = "TRUNG_TUYEN";
                                                daTrung = true;
                                        } else {
                                                ketQua = "ROT";
                                        }

                                        NguyenVongDTO nvDTO = new NguyenVongDTO();
                                        nvDTO.setCccd(nv.cccd);
                                        nvDTO.setMaNganh(nv.maNganh);
                                        nvDTO.setThuTuNV(nv.nvThuTu);
                                        nvDTO.setDiemTHXT(nv.diemTHXT);
                                        nvDTO.setDiemUTQD(nv.diemUT);
                                        nvDTO.setDiemCong(nv.diemCong);
                                        nvDTO.setDiemXetTuyen(nv.diemXetTuyen);
                                        nvDTO.setKetQua(ketQua);
                                        nvDTO.setKeys(nv.nvKeys);
                                        nvDTO.setPhuongThuc(nv.phuongThuc);
                                        nvDTO.setToHopMon(nv.toHop);
                                        pendingInsert.add(nvDTO);
                                }
                        }

                        if (!nvDAO.insertBatch(pendingInsert)) {
                                return XetTatCaResult.error("Lỗi lưu kết quả xét tuyển");
                        }

                        // Lọc ảo lần 2 theo NV ưu tiên (sau khi đã lưu)
                        Map<String, List<NguyenVongDTO>> byCccd = new HashMap<>();
                        for (NguyenVongDTO nv : pendingInsert) {
                                byCccd.computeIfAbsent(nv.getCccd(), k -> new ArrayList<>()).add(nv);
                        }

                        rows.clear();
                        trung = 0;
                        rot = 0;

                        for (Map.Entry<String, List<NguyenVongDTO>> entry : byCccd.entrySet()) {
                                List<NguyenVongDTO> list = entry.getValue();
                                list.sort(Comparator.comparingInt(NguyenVongDTO::getThuTuNV));

                                boolean daTrung = false;

                                for (NguyenVongDTO nv : list) {
                                        String ketQua = nv.getKetQua();
                                        String ghiChu = "";

                                        Double diemSan = null;
                                        NganhDTO nganh = nganhMap.get(nv.getMaNganh());
                                        if (nganh != null) {
                                                diemSan = nganh.getDiemSan();
                                        }

                                        if (diemSan != null && nv.getDiemXetTuyen() < diemSan) {
                                                ketQua = "ROT";
                                                nv.setKetQua("ROT");
                                                ghiChu = "Dưới điểm sàn";
                                        } else if ("TRUNG_TUYEN".equals(ketQua)) {
                                                if (daTrung) {
                                                        ketQua = "ROT";
                                                        nv.setKetQua("ROT");
                                                        ghiChu = "Nguyện vọng trước đã trúng tuyển";
                                                } else {
                                                        daTrung = true;
                                                }
                                        }

                                        if ("TRUNG_TUYEN".equals(ketQua)) {
                                                trung++;
                                        } else {
                                                rot++;
                                        }

                                        String tenNganh = nv.getMaNganh();
                                        if (nganh != null && nganh.getTenNganh() != null) {
                                                tenNganh = nganh.getTenNganh();
                                        }

                                        rows.add(new Object[] {
                                                        nv.getCccd(),
                                                        tenNganh,
                                                        nv.getToHopMon(),
                                                        nv.getDiemTHXT(),
                                                        nv.getDiemCong(),
                                                        nv.getDiemUTQD(),
                                                        nv.getDiemXetTuyen(),
                                                        "TRUNG_TUYEN".equals(ketQua)
                                                                        ? "Trúng tuyển"
                                                                        : "Không trúng",
                                                        ghiChu
                                        });
                                }
                        }

                        if (!nvDAO.insertBatch(pendingInsert)) {
                                return XetTatCaResult.error("Lỗi lọc ảo sau khi lưu");
                        }

                        // Cập nhật số lượng trúng tuyển theo phương thức
                        Map<String, int[]> countByNganh = new HashMap<>();
                        for (NguyenVongDTO nv : pendingInsert) {
                                if (!"TRUNG_TUYEN".equals(nv.getKetQua())) {
                                        continue;
                                }

                                int[] counts = countByNganh.computeIfAbsent(
                                                nv.getMaNganh(),
                                                k -> new int[4]
                                );

                                counts[0]++; // tong trung

                                String pt = nv.getPhuongThuc();
                                if (pt == null) {
                                        continue;
                                }

                                if ("DGNL".equalsIgnoreCase(pt)) {
                                        counts[1]++;
                                } else if ("VSAT".equalsIgnoreCase(pt)) {
                                        counts[2]++;
                                } else if ("THPT".equalsIgnoreCase(pt)) {
                                        counts[3]++;
                                }
                        }

                        for (Map.Entry<String, int[]> entry : countByNganh.entrySet()) {
                                NganhDTO nganh = nganhMap.get(entry.getKey());
                                if (nganh == null) {
                                        continue;
                                }

                                int[] counts = entry.getValue();
                                nganh.setSlXtt(counts[0]);
                                nganh.setSlDgnl(counts[1]);
                                nganh.setSlVsat(counts[2]);
                                nganh.setSlThpt(String.valueOf(counts[3]));
                                nganhBUS.updateNganh(nganh);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                        return XetTatCaResult.error("Lỗi xét tuyển");
                }

                return new XetTatCaResult(
                                nvByCccd.size(),
                                trung,
                                rot,
                                rows);
        }

        private static class CandidateNV {
                private final String cccd;
                private final int nvThuTu;
                private final String maNganh;
                private final String tenNganh;
                private final String toHop;
                private final double diemTHXT;
                private final double diemCong;
                private final double diemUT;
                private final double diemXetTuyen;
                private final String nvKeys;
                private final String toHopMon;
                private final String phuongThuc;

                private CandidateNV(
                                String cccd,
                                int nvThuTu,
                                String maNganh,
                                String tenNganh,
                                String toHop,
                                double diemTHXT,
                                double diemCong,
                                double diemUT,
                                double diemXetTuyen,
                                String nvKeys,
                                String toHopMon,
                                String phuongThuc) {
                        this.cccd = cccd;
                        this.nvThuTu = nvThuTu;
                        this.maNganh = maNganh;
                        this.tenNganh = tenNganh;
                        this.toHop = toHop;
                        this.diemTHXT = diemTHXT;
                        this.diemCong = diemCong;
                        this.diemUT = diemUT;
                        this.diemXetTuyen = diemXetTuyen;
                        this.nvKeys = nvKeys;
                        this.toHopMon = toHopMon;
                        this.phuongThuc = phuongThuc;
                }
        }

        private void loadXetTuyenFromDb() {
                model.setRowCount(0);

                int stt = 1;
                int trung = 0;
                int rot = 0;
                int tongHoSo = 0;

                List<NganhDTO> dsNganh = nganhBUS.getAll();

                java.util.Map<String, String> tenNganhMap = new java.util.HashMap<>();

                if (dsNganh != null) {
                        for (NganhDTO nganh : dsNganh) {
                                tenNganhMap.put(
                                                nganh.getMaNganh(),
                                                nganh.getTenNganh());
                        }
                }

                java.util.Set<String> cccdSet = new java.util.HashSet<>();

                try (Connection conn = DB.getConn()) {
                        NguyenVongDAO nvDAO = new NguyenVongDAO(conn);
                        List<NguyenVongDTO> ds = nvDAO.getAll();

                        for (NguyenVongDTO nv : ds) {
                                String cccd = nv.getCccd();
                                if (cccd != null) {
                                        cccdSet.add(cccd);
                                }

                                String ketQua = nv.getKetQua() == null
                                                ? ""
                                                : nv.getKetQua().trim().toUpperCase();

                                if ("TRUNG_TUYEN".equals(ketQua)) {
                                        trung++;
                                } else if ("ROT".equals(ketQua)) {
                                        rot++;
                                }

                                String maNganh = nv.getMaNganh();
                                String tenNganh = tenNganhMap.get(maNganh);
                                String hienThiNganh = tenNganh == null
                                                ? maNganh
                                                : tenNganh;

                                String ghiChu = "";
                                if ("BO".equals(ketQua)) {
                                        ghiChu = "Nguyện vọng trước đã trúng tuyển";
                                }

                                model.addRow(new Object[] {
                                                stt++,
                                                nv.getCccd(),
                                                hienThiNganh,
                                                nv.getToHopMon(),
                                                nv.getDiemTHXT(),
                                                nv.getDiemCong(),
                                                nv.getDiemUTQD(),
                                                nv.getDiemXetTuyen(),
                                                "TRUNG_TUYEN".equals(ketQua)
                                                                ? "Trúng tuyển"
                                                                : "Không trúng",
                                                ghiChu
                                });
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(
                                        this,
                                        "Không thể tải kết quả xét tuyển từ DB");
                }

                tongHoSo = cccdSet.size();

                lblTong.setText(String.valueOf(tongHoSo));
                lblTrungTuyen.setText(String.valueOf(trung));
                lblKhongTrung.setText(String.valueOf(rot));
        }

        private void setBulkUiState(boolean running) {
                if (btnXetAll != null) {
                        btnXetAll.setEnabled(!running);
                }

                if (btnXet != null) {
                        btnXet.setEnabled(!running);
                }

                if (lblStatus != null) {
                        lblStatus.setText(running ? "Đang xét tuyển..." : "");
                }
        }

        private static class XetTatCaResult {
                private final int tong;
                private final int trung;
                private final int rot;
                private final List<Object[]> rows;
                private final String errorMessage;

                private XetTatCaResult(
                                int tong,
                                int trung,
                                int rot,
                                List<Object[]> rows) {
                        this.tong = tong;
                        this.trung = trung;
                        this.rot = rot;
                        this.rows = rows;
                        this.errorMessage = null;
                }

                private XetTatCaResult(String errorMessage) {
                        this.tong = 0;
                        this.trung = 0;
                        this.rot = 0;
                        this.rows = new ArrayList<>();
                        this.errorMessage = errorMessage;
                }

                private static XetTatCaResult error(String message) {
                        return new XetTatCaResult(message);
                }
        }

        private String getPhuongThucSelected() {
                Object value = cboPhuongThuc.getSelectedItem();
                return value == null ? "THPT" : value.toString();
        }

        private boolean isNganhMoTheoPhuongThuc(
                        NganhDTO nganh,
                        String phuongThuc) {

                if (nganh == null || phuongThuc == null) {
                        return false;
                }

                String flag;

                switch (phuongThuc) {
                        case "DGNL":
                                flag = nganh.getDgnl();
                                break;
                        case "VSAT":
                                flag = nganh.getVsat();
                                break;
                        case "THPT":
                        default:
                                flag = nganh.getThpt();
                                break;
                }

                if (flag == null) {
                        return false;
                }

                String normalized = flag.trim().toUpperCase();
                return "1".equals(normalized)
                                || "Y".equals(normalized)
                                || "TRUE".equals(normalized);
        }
}