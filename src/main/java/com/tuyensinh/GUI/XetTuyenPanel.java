package com.tuyensinh.GUI;

import com.tuyensinh.BUS.XetTuyenBUS;
import com.tuyensinh.BUS.NganhBUS;
import com.tuyensinh.DTO.NganhDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * XetTuyenPanel - Panel xét tuyển và hiển thị kết quả
 */
public class XetTuyenPanel extends JPanel {

    private XetTuyenBUS xetTuyenBUS = new XetTuyenBUS();
    private NganhBUS nganhBUS = new NganhBUS();
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtCCCD = new JTextField(15);
    private JButton btnXetTuyen, btnXetTuyenDoi, btnThongKe, btnExport;

    public XetTuyenPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title
        JLabel lblTitle = new JLabel(" XÉT TUYỂN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Center - Table
        String[] columns = {"STT", "Số Báo Danh", "Họ Tên", "CCCD", "Ngành", "Điểm Xét Tuyển", "Kết Quả", "Thứ Tự NV", "Ghi Chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // South - Controls
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("🔍 Xét tuyển theo CCCD"));
        searchPanel.add(new JLabel("CCCD:"));
        searchPanel.add(txtCCCD);
        btnXetTuyen = new JButton("Xét Tuyển");
        searchPanel.add(btnXetTuyen);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnXetTuyenDoi = new JButton("Xét Tuyển Đợt");
        btnThongKe = new JButton("📊 Thống Kê");
        btnExport = new JButton("📤 Export Excel");

        buttonPanel.add(btnXetTuyenDoi);
        buttonPanel.add(btnThongKe);
        buttonPanel.add(btnExport);

        southPanel.add(searchPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnXetTuyen.addActionListener(e -> xetTuyenTheoCCCD());
        btnXetTuyenDoi.addActionListener(e -> xetTuyenDoi());
        btnThongKe.addActionListener(e -> hienThiThongKe());
        btnExport.addActionListener(e -> exportExcel());
        txtCCCD.addActionListener(e -> xetTuyenTheoCCCD());
    }

    private void xetTuyenTheoCCCD() {
        String cccd = txtCCCD.getText().trim();
        if (cccd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập CCCD!");
            return;
        }

        // Lấy danh sách ngành để xét
        List<NganhDTO> dsNganh = nganhBUS.getAll();
        List<String> dsMaNganh = new ArrayList<>();
        for (NganhDTO n : dsNganh) {
            dsMaNganh.add(n.getMaNganh());
        }

        List<XetTuyenBUS.KetQuaXetTuyen> ketQuaList = xetTuyenBUS.xetTuyen(cccd, dsMaNganh);
        hienThiKetQua(ketQuaList);
    }

    private void xetTuyenDoi() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có muốn xét tuyển đợt cho tất cả thí sinh?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Hiển thị loading
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            Map<String, List<XetTuyenBUS.KetQuaXetTuyen>> ketQuaMap = xetTuyenBUS.xetTuyenDoi();
            
            tableModel.setRowCount(0);
            int stt = 1;
            for (List<XetTuyenBUS.KetQuaXetTuyen> list : ketQuaMap.values()) {
                for (XetTuyenBUS.KetQuaXetTuyen kq : list) {
                    Object[] row = {
                        stt++,
                        kq.soBaoDanh,
                        kq.hoTen,
                        kq.cccd,
                        kq.tenNganh,
                        String.format("%.2f", kq.diemXetTuyen),
                        kq.trungTuyen ? "✅ Trúng tuyển" : "❌ Không trúng",
                        kq.thuTuNguyenVong,
                        kq.lyDo
                    };
                    tableModel.addRow(row);
                }
            }
            
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            JOptionPane.showMessageDialog(this, "Đã xét tuyển xong! Tổng: " + ketQuaMap.size() + " thí sinh");
        }
    }

    private void hienThiKetQua(List<XetTuyenBUS.KetQuaXetTuyen> ketQuaList) {
        tableModel.setRowCount(0);
        int stt = 1;
        for (XetTuyenBUS.KetQuaXetTuyen kq : ketQuaList) {
            Object[] row = {
                stt++,
                kq.soBaoDanh,
                kq.hoTen,
                kq.cccd,
                kq.tenNganh,
                String.format("%.2f", kq.diemXetTuyen),
                kq.trungTuyen ? "✅ Trúng tuyển" : "❌ Không trúng",
                kq.thuTuNguyenVong,
                kq.lyDo
            };
            tableModel.addRow(row);
        }
    }

    private void hienThiThongKe() {
        XetTuyenBUS.ThongKeXetTuyen tk = xetTuyenBUS.thongKe();
        
        String message = String.format(
            "📊 THỐNG KÊ XÉT TUYỂN\n\n" +
            "Tổng số thí sinh: %d\n" +
            "Số trúng tuyển: %d\n" +
            "Số không trúng: %d\n" +
            "Tỷ lệ trúng tuyển: %.2f%%",
            tk.tongSoThiSinh,
            tk.soTrungTuyen,
            tk.soKhongTrungTuyen,
            tk.tiLeTrungTuyen
        );
        
        JOptionPane.showMessageDialog(this, message, "Thống Kê", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportExcel() {
        JOptionPane.showMessageDialog(this, "Chức năng export Excel đang được phát triển!");
    }
}