package com.tuyensinh.GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tuyensinh.BUS.NganhBUS;
import com.tuyensinh.BUS.ThiSinhBUS;
import com.tuyensinh.DAO.NganhToHopDAO;
import com.tuyensinh.DAO.NguyenVongDAO;
import com.tuyensinh.DTO.NganhDTO;
import com.tuyensinh.DTO.NguyenVongDTO;
import com.tuyensinh.DTO.ThiSinhDTO;
import com.tuyensinh.config.DB;

public class NganhTrungTuyenPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cboNganh;
    private JLabel lblTongTrung;
    private JLabel lblDiemSan;
    private JLabel lblDiemCao;
    private JLabel lblDiemThap;

    private final NganhBUS nganhBUS;
    private final ThiSinhBUS thiSinhBUS;
    private final NganhToHopDAO nganhToHopDAO;

    public NganhTrungTuyenPanel() {
        this.nganhBUS = new NganhBUS();
        this.thiSinhBUS = new ThiSinhBUS();
        this.nganhToHopDAO = new NganhToHopDAO();

        initComponents();
        loadNganhList();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 246, 250));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblTitle = new JLabel("DANH SÁCH TRÚNG TUYỂN CHI TIẾT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(lblTitle, BorderLayout.NORTH);

        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createMainContent() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 20));
        wrapper.setOpaque(false);

        // Panel thống kê
        RoundedPanel statsPanel = new RoundedPanel(20, Color.WHITE);
        statsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 15));
        statsPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        lblTongTrung = new JLabel("0");
        lblDiemSan = new JLabel("--");
        lblDiemCao = new JLabel("--");
        lblDiemThap = new JLabel("--");

        lblTongTrung.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDiemSan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDiemCao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDiemThap.setFont(new Font("Segoe UI", Font.BOLD, 16));

        statsPanel.add(createStatLabel("Tổng Trúng:", lblTongTrung));
        statsPanel.add(createStatLabel("Điểm Sàn:", lblDiemSan));
        statsPanel.add(createStatLabel("Điểm Cao:", lblDiemCao));
        statsPanel.add(createStatLabel("Điểm Thấp:", lblDiemThap));

        wrapper.add(statsPanel, BorderLayout.NORTH);

        // Panel bảng
        RoundedPanel tablePanel = new RoundedPanel(20, Color.WHITE);
        tablePanel.setLayout(new BorderLayout(10, 15));
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);

        JLabel lblNganh = new JLabel("Chọn Ngành:");
        cboNganh = new JComboBox<>();
        cboNganh.setPreferredSize(new Dimension(250, 38));

        JButton btnXem = createButton("Xem Chi Tiết", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton btnXuatExcel = createButton("Xuất Excel", new Color(46, 204, 113), new Color(39, 174, 96));

        btnXem.addActionListener(e -> loadData());
        btnXuatExcel.addActionListener(e -> xuatExcel());

        toolbar.add(lblNganh);
        toolbar.add(cboNganh);
        toolbar.add(btnXem);
        toolbar.add(btnXuatExcel);

        tablePanel.add(toolbar, BorderLayout.NORTH);

        // Bảng
        String[] columns = {
            "STT",
            "CCCD",
            "Họ Tên",
            "Điểm Xét Tuyển",
            "Tổ Hợp"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        wrapper.add(tablePanel, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createStatLabel(String title, JLabel value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(lbl, BorderLayout.WEST);
        panel.add(value, BorderLayout.EAST);

        return panel;
    }

    private JButton createButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 38));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    private void loadNganhList() {
        cboNganh.removeAllItems();
        cboNganh.addItem("-- Chọn Ngành --");

        try {
            List<NganhDTO> dsNganh = nganhBUS.getAll();
            if (dsNganh != null) {
                for (NganhDTO nganh : dsNganh) {
                    cboNganh.addItem(nganh.getMaNganh() + " - " + nganh.getTenNganh());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách ngành");
        }
    }

    private void loadData() {
        int index = cboNganh.getSelectedIndex();
        if (index <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngành");
            return;
        }

        String selectedItem = (String) cboNganh.getSelectedItem();
        String maNganh = selectedItem.split(" - ")[0].trim();

        model.setRowCount(0);

        try (Connection conn = DB.getConn()) {
            NguyenVongDAO nvDAO = new NguyenVongDAO(conn);
            List<NguyenVongDTO> nvList = nvDAO.getByMaNganhAndKetQua(maNganh, "TRUNG_TUYEN");

            Map<String, ThiSinhDTO> tsByCccd = new HashMap<>();
            List<ThiSinhDTO> tsList = thiSinhBUS.getAll();
            if (tsList != null) {
                for (ThiSinhDTO ts : tsList) {
                    tsByCccd.put(ts.getCccd(), ts);
                }
            }

            int stt = 1;
            double diemCao = Double.NEGATIVE_INFINITY;
            double diemThap = Double.POSITIVE_INFINITY;
            List<Double> diemList = new ArrayList<>();

            for (NguyenVongDTO nv : nvList) {
                ThiSinhDTO ts = tsByCccd.get(nv.getCccd());
                String hoTen = ts != null ? (ts.getHo() + " " + ts.getTen()).trim() : "N/A";

                double diem = nv.getDiemXetTuyen();
                diemList.add(diem);

                if (diem > diemCao) {
                    diemCao = diem;
                }
                if (diem < diemThap) {
                    diemThap = diem;
                }

                model.addRow(new Object[]{
                    stt++,
                    nv.getCccd(),
                    hoTen,
                    String.format("%.2f", diem),
                    nv.getToHopMon()
                });
            }

            // Cập nhật thống kê
            NganhDTO nganh = nganhBUS.getById(maNganh);
            Double diemSan = nganh != null ? nganh.getDiemSan() : null;

            lblTongTrung.setText(String.valueOf(nvList.size()));
            lblDiemSan.setText(diemSan != null ? String.format("%.2f", diemSan) : "--");
            lblDiemCao.setText(diemCao != Double.NEGATIVE_INFINITY ? String.format("%.2f", diemCao) : "--");
            lblDiemThap.setText(diemThap != Double.POSITIVE_INFINITY ? String.format("%.2f", diemThap) : "--");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu");
        }
    }

    private void xuatExcel() {

    int index = cboNganh.getSelectedIndex();

    if (index <= 0 || model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this,
                "Vui lòng chọn ngành và tải dữ liệu trước");
        return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setSelectedFile(new File("ketqua_xettuyen.xlsx"));

    int option = fileChooser.showSaveDialog(this);
    if (option != JFileChooser.APPROVE_OPTION) {
        return;
    }

    File file = fileChooser.getSelectedFile();

    try (Workbook workbook = new XSSFWorkbook()) {

        Sheet sheet = workbook.createSheet("Kết quả xét tuyển");

        // ===== HEADER =====
        Row header = sheet.createRow(0);

        for (int i = 0; i < model.getColumnCount(); i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(model.getColumnName(i));
        }

        // ===== DATA =====
        for (int i = 0; i < model.getRowCount(); i++) {
            Row row = sheet.createRow(i + 1);

            for (int j = 0; j < model.getColumnCount(); j++) {
                Object value = model.getValueAt(i, j);
                row.createCell(j).setCellValue(
                        value == null ? "" : value.toString()
                );
            }
        }

        // Auto size column
        for (int i = 0; i < model.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }

        JOptionPane.showMessageDialog(this,
                "Xuất Excel thành công!\n" + file.getAbsolutePath());

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Lỗi xuất Excel: " + e.getMessage());
    }
}

    // RoundedPanel helper class
    class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg;

        public RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
