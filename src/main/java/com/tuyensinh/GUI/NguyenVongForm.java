package com.tuyensinh.GUI;

import com.tuyensinh.BUS.NganhBUS;
import com.tuyensinh.DAO.NguyenVongDAO;
import com.tuyensinh.DTO.NganhDTO;
import com.tuyensinh.DTO.NguyenVongDTO;
import com.tuyensinh.config.DB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NguyenVongForm extends JPanel {

    private final NganhBUS nganhBUS = new NganhBUS();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnEdit, btnDelete, btnRefresh;
    private JLabel lblTong;
    private JLabel lblTrung;
    private JLabel lblRot;

    public NguyenVongForm() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 249, 250)); // Nền xám sáng
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Tiêu đề trang
        JLabel lblTitle = new JLabel("QUẢN LÝ NGUYỆN VỌNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        // Nội dung chính (Table + Toolbar)
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        RoundedPanel mainPanel = new RoundedPanel(20, Color.WHITE);
        mainPanel.setLayout(new BorderLayout(10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // ===== TOOLBAR =====
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setOpaque(false);

        // Search Panel (Trái)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm SBD:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(new Color(80, 80, 80));

        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(180, 38));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));

        btnSearch = createFlatButton("Tìm kiếm", new Color(149, 165, 166), new Color(127, 140, 141));
        btnSearch.setPreferredSize(new Dimension(100, 38));

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // Action Panel (Phải)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        btnAdd = createFlatButton("Thêm mới", new Color(46, 204, 113), new Color(39, 174, 96));
        btnEdit = createFlatButton("Cập nhật", new Color(52, 152, 219), new Color(41, 128, 185));
        btnDelete = createFlatButton("Xóa", new Color(231, 76, 60), new Color(192, 57, 43));
        btnRefresh = createFlatButton("Làm Mới", new Color(240, 240, 240), new Color(220, 220, 220));
        btnRefresh.setForeground(new Color(80, 80, 80));

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);

        toolBar.add(searchPanel, BorderLayout.WEST);
        toolBar.add(actionPanel, BorderLayout.EAST);

        JPanel statsPanel = new JPanel(new BorderLayout(0, 10));
        statsPanel.setOpaque(false);

        JPanel cardRow = new JPanel(new GridLayout(1, 3, 15, 15));
        cardRow.setOpaque(false);

        lblTong = new JLabel("0");
        lblTrung = new JLabel("0");
        lblRot = new JLabel("0");

        cardRow.add(createStatCard("Tổng NV", lblTong, new Color(52, 152, 219)));
        cardRow.add(createStatCard("Trúng", lblTrung, new Color(46, 204, 113)));
        cardRow.add(createStatCard("Rớt", lblRot, new Color(231, 76, 60)));

        statsPanel.add(cardRow, BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setOpaque(false);
        topPanel.add(statsPanel, BorderLayout.NORTH);
        topPanel.add(toolBar, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {
            "STT",
            "ID",
            "CCCD",
            "Mã ngành",
            "Thứ tự NV",
            "Điểm THXT",
            "Điểm cộng",
            "Điểm UTQD",
            "Điểm xét tuyển",
            "Kết quả",
            "NV Keys",
            "Phương thức",
            "Tổ hợp"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
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

        // Giới hạn độ rộng cột STT cho đẹp
        table.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== EVENTS =====
        btnSearch.addActionListener(e -> search());
        txtSearch.addActionListener(e -> search()); // Nhấn Enter để tìm
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> delete());
        btnRefresh.addActionListener(e -> loadData());

        return mainPanel;
    }

    // ================= CUSTOM BUTTON =================
    private JButton createFlatButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(110, 38));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if(btn.isEnabled()) btn.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent evt) {
                if(btn.isEnabled()) btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    // ================= DATA LOGIC =================
    private void loadData() {
        loadData(null);
    }

    private void loadData(String key) {
        tableModel.setRowCount(0);

        DataResult data = fetchData();
        List<Object[]> rows = data.rows;
        int stt = 1;

        String searchKey =
                key == null
                        ? ""
                        : key.trim().toLowerCase();

        for (Object[] row : rows) {
            String cccd = row[1] == null ? "" : row[1].toString();
            String maNganh = row[2] == null ? "" : row[2].toString();

            if (!searchKey.isEmpty()) {
                if (!cccd.toLowerCase().contains(searchKey)
                        && !maNganh.toLowerCase().contains(searchKey)) {
                    continue;
                }
            }

            Object[] newRow = new Object[row.length + 1];
            newRow[0] = stt++;
            System.arraycopy(row, 0, newRow, 1, row.length);
            tableModel.addRow(newRow);
        }

        updateStats(data);
    }

    private DataResult fetchData() {
        List<Object[]> rows = new ArrayList<>();
        int tong = 0;
        int trung = 0;
        int rot = 0;
        Map<String, Integer> theoNganh = new HashMap<>();
        Map<String, Integer> theoPhuongThuc = new HashMap<>();

        try (Connection conn = DB.getConn()) {
            NguyenVongDAO nvDAO = new NguyenVongDAO(conn);
            List<NguyenVongDTO> ds = nvDAO.getAll();

            for (NguyenVongDTO nv : ds) {
                tong++;

                String ketQua = nv.getKetQua() == null
                        ? ""
                        : nv.getKetQua().trim().toUpperCase();

                if ("TRUNG_TUYEN".equals(ketQua)) {
                    trung++;
                } else if ("ROT".equals(ketQua)) {
                    rot++;
                }

                String maNganh = nv.getMaNganh();
                if (maNganh == null || maNganh.isEmpty()) {
                    maNganh = "(Trống)";
                }
                theoNganh.merge(maNganh, 1, Integer::sum);

                String phuongThuc = nv.getPhuongThuc();
                if (phuongThuc == null || phuongThuc.isEmpty()) {
                    phuongThuc = "(Trống)";
                }
                theoPhuongThuc.merge(phuongThuc, 1, Integer::sum);

                rows.add(new Object[]{
                        nv.getIdnv(),
                        nv.getCccd(),
                        nv.getMaNganh(),
                        nv.getThuTuNV(),
                        nv.getDiemTHXT(),
                        nv.getDiemCong(),
                        nv.getDiemUTQD(),
                        nv.getDiemXetTuyen(),
                        nv.getKetQua(),
                        nv.getKeys(),
                        nv.getPhuongThuc(),
                        nv.getToHopMon()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Khong the tai du lieu nguyen vong tu DB",
                    "Loi",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        return new DataResult(rows, tong, trung, rot, theoNganh, theoPhuongThuc);
    }

    private void updateStats(DataResult data) {
        lblTong.setText(String.valueOf(data.tong));
        lblTrung.setText(String.valueOf(data.trung));
        lblRot.setText(String.valueOf(data.rot));
    }

    private JPanel createStatCard(String title, JLabel value, Color color) {
        RoundedPanel panel = new RoundedPanel(16, Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));

        value.setFont(new Font("Segoe UI", Font.BOLD, 26));
        value.setForeground(color);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(value, BorderLayout.CENTER);
        return panel;
    }


    private static class DataResult {
        private final List<Object[]> rows;
        private final int tong;
        private final int trung;
        private final int rot;
        private final Map<String, Integer> theoNganh;
        private final Map<String, Integer> theoPhuongThuc;

        private DataResult(
                List<Object[]> rows,
                int tong,
                int trung,
                int rot,
                Map<String, Integer> theoNganh,
                Map<String, Integer> theoPhuongThuc
        ) {
            this.rows = rows;
            this.tong = tong;
            this.trung = trung;
            this.rot = rot;
            this.theoNganh = theoNganh;
            this.theoPhuongThuc = theoPhuongThuc;
        }
    }

    private void search() {
        String key = txtSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        loadData(key);
    }

    private void delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng dữ liệu cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa nguyện vọng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData();
        }
    }

    // ================= FORMS (DIALOGS) =================
    private void showAddDialog() {
        openFormDialog(null, "Thêm Nguyện Vọng Mới");
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nguyện vọng cần cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        openFormDialog(tableModel.getValueAt(selectedRow, 0), "Cập Nhật Nguyện Vọng");
    }

    private void openFormDialog(Object dataId, String title) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(650, 480);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());

        // Header Form
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        
        JLabel lblFormTitle = new JLabel(title.toUpperCase());
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblFormTitle.setForeground(new Color(50, 50, 50));
        headerPanel.add(lblFormTitle, BorderLayout.WEST);
        
        dialog.add(headerPanel, BorderLayout.NORTH);

        // Body Form (Chia 2 cột)
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 20, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 30, 20, 30));

        JTextField txtSBD = createStyledTextField();
        JTextField txtHoTen = createStyledTextField();

        JComboBox<String> cboNganh1 = createStyledComboBox();
        JComboBox<String> cboNganh2 = createStyledComboBox();
        JComboBox<String> cboNganh3 = createStyledComboBox();
        JComboBox<String> cboNganh4 = createStyledComboBox();
        JComboBox<String> cboNganh5 = createStyledComboBox();
        JComboBox<String> cboNganh6 = createStyledComboBox();

        // Thêm dữ liệu combo box
        List<NganhDTO> dsNganh = nganhBUS.getAll();
        String defaultCombo = "-- Chọn ngành --";
        JComboBox[] cbos = {cboNganh1, cboNganh2, cboNganh3, cboNganh4, cboNganh5, cboNganh6};
        
        for(JComboBox cbo : cbos) {
            cbo.addItem(defaultCombo);
            if(dsNganh != null) {
                for (NganhDTO nganh : dsNganh) {
                    cbo.addItem(nganh.getMaNganh() + " - " + nganh.getTenNganh());
                }
            }
        }

        formPanel.add(createLabeledComponent("Số Báo Danh:", txtSBD));
        formPanel.add(createLabeledComponent("Họ Tên Thí Sinh:", txtHoTen));
        formPanel.add(createLabeledComponent("Nguyện vọng 1:", cboNganh1));
        formPanel.add(createLabeledComponent("Nguyện vọng 2:", cboNganh2));
        formPanel.add(createLabeledComponent("Nguyện vọng 3:", cboNganh3));
        formPanel.add(createLabeledComponent("Nguyện vọng 4:", cboNganh4));
        formPanel.add(createLabeledComponent("Nguyện vọng 5:", cboNganh5));
        formPanel.add(createLabeledComponent("Nguyện vọng 6:", cboNganh6));

        dialog.add(formPanel, BorderLayout.CENTER);

        // Footer Form (Buttons)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 30, 20, 30));

        JButton btnSave = createFlatButton("Lưu Dữ Liệu", new Color(46, 204, 113), new Color(39, 174, 96));
        btnSave.setPreferredSize(new Dimension(130, 40));
        
        JButton btnCancel = createFlatButton("Hủy bỏ", new Color(230, 230, 230), new Color(210, 210, 210));
        btnCancel.setForeground(new Color(80, 80, 80));

        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Lưu dữ liệu thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            loadData();
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JPanel createLabeledComponent(String labelText, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 100, 100));
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));
        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JTextField createStyledTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> cbo = new JComboBox<>();
        cbo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbo.setBackground(Color.WHITE);
        return cbo;
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
            
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }
}