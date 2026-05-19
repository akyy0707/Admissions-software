package com.tuyensinh.GUI;

import com.tuyensinh.BUS.ImportDiemCC;
import com.tuyensinh.config.DB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;

public class ImportDiemCCPanel extends JPanel {

    private Connection conn;
    private JButton btnImport;
    private JLabel lblStatus;

    public ImportDiemCCPanel() {
        // Kết nối CSDL
        try {
            conn = DB.getConn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cài đặt giao diện tổng thể
        setLayout(new GridBagLayout()); // Dùng GridBagLayout để căn giữa
        setBackground(new Color(248, 249, 250)); // Nền xám sáng

        // Khởi tạo thẻ (Card) Upload bo góc
        RoundedPanel uploadCard = new RoundedPanel(25, Color.WHITE);
        uploadCard.setPreferredSize(new Dimension(550, 350));
        uploadCard.setLayout(new BoxLayout(uploadCard, BoxLayout.Y_AXIS));
        uploadCard.setBorder(new EmptyBorder(40, 40, 40, 40));

        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("NHẬP ĐIỂM CHỨNG CHỈ NGOẠI NGỮ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(40, 40, 40));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Mô tả phụ
        JLabel lblDesc = new JLabel("Vui lòng chọn file Excel (.xls, .xlsx) chứa điểm chứng chỉ để nạp vào hệ thống");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(new Color(130, 130, 130));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Nút Import
        btnImport = createFlatButton("Chọn File Excel", new Color(41, 128, 185), new Color(52, 152, 219));
        btnImport.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImport.setPreferredSize(new Dimension(200, 45));
        btnImport.setMaximumSize(new Dimension(200, 45));

        // 4. Trạng thái
        lblStatus = new JLabel("Trạng thái: Chưa có file nào được chọn");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblStatus.setForeground(new Color(150, 150, 150));
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Lắp ráp các thành phần vào Card
        uploadCard.add(Box.createVerticalStrut(10));
        uploadCard.add(lblTitle);
        uploadCard.add(Box.createVerticalStrut(15));
        uploadCard.add(lblDesc);
        uploadCard.add(Box.createVerticalStrut(40));
        uploadCard.add(btnImport);
        uploadCard.add(Box.createVerticalStrut(30));
        uploadCard.add(lblStatus);

        add(uploadCard); // Thêm Card vào giữa màn hình

        // ================= SỰ KIỆN NÚT IMPORT =================
        btnImport.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            // Lọc chỉ cho phép chọn file Excel
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                lblStatus.setText("Đang xử lý: " + file.getName() + "...");
                lblStatus.setForeground(new Color(230, 126, 34)); // Màu cam chờ xử lý
                
                // Dùng Thread để tránh đơ giao diện khi import file
                new Thread(() -> {
                    try {
                        ImportDiemCC importer = new ImportDiemCC(conn);
                        importer.importExcel(file);

                        // Cập nhật lại giao diện sau khi chạy xong
                        SwingUtilities.invokeLater(() -> {
                            lblStatus.setText("Import thành công: " + file.getName());
                            lblStatus.setForeground(new Color(39, 174, 96)); // Màu xanh lá báo thành công
                            JOptionPane.showMessageDialog(this, "Import điểm chứng chỉ thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(() -> {
                            lblStatus.setText("Lỗi: Import thất bại!");
                            lblStatus.setForeground(new Color(231, 76, 60)); // Màu đỏ báo lỗi
                            JOptionPane.showMessageDialog(this, "Import thất bại! Vui lòng kiểm tra lại cấu trúc file.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                }).start();
            }
        });
    }

    // ================= CUSTOM BUTTON =================
    private JButton createFlatButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));

        // Hiệu ứng Hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
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
            
            // Đổ bóng mờ nhẹ
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Vẽ màu nền trắng
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            // Vẽ viền thanh mảnh
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            g2.dispose();
        }
    }
}