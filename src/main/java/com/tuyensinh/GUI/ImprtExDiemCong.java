package com.tuyensinh.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.util.List;

import com.tuyensinh.BUS.DiemCongBUS;
import com.tuyensinh.config.DB;

public class ImprtExDiemCong extends JPanel {

    private Connection conn;
    private DiemCongBUS bus;

    private JPanel dropZonePanel;
    private JLabel lblInstruction;
    private JLabel lblFileName;
    private JProgressBar progressBar;
    private JButton btnChooseFile;
    private File selectedFile;
    private JLabel lblStatus;

    public ImprtExDiemCong() {
        // Kết nối DB trong constructor
        try {
            conn = DB.getConn();
            bus = new DiemCongBUS(conn);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout()); // Tự động căn giữa toàn bộ Form
        setBackground(new Color(248, 249, 250)); // Nền xám sáng hiện đại

        // Khởi tạo thẻ (Card) trắng bo góc chứa toàn bộ nội dung
        RoundedPanel mainCard = new RoundedPanel(25, Color.WHITE);
        mainCard.setPreferredSize(new Dimension(650, 480));
        mainCard.setLayout(new BoxLayout(mainCard, BoxLayout.Y_AXIS));
        mainCard.setBorder(new EmptyBorder(35, 40, 35, 40));

        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("NHẬP ĐIỂM CỘNG TỪ EXCEL");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 40, 40));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Hỗ trợ kéo thả file (.xls, .xlsx) trực tiếp vào khu vực bên dưới");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(130, 130, 130));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Khu vực Drop Zone (Nét đứt)
        dropZonePanel = new DashedDropZone();
        dropZonePanel.setLayout(new BoxLayout(dropZonePanel, BoxLayout.Y_AXIS));
        dropZonePanel.setPreferredSize(new Dimension(500, 220));
        dropZonePanel.setMaximumSize(new Dimension(500, 220));
        dropZonePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dropZonePanel.setBackground(new Color(245, 247, 250));

        dropZonePanel.add(Box.createVerticalStrut(40));

        lblInstruction = new JLabel("KÉO & THẢ FILE VÀO ĐÂY");
        lblInstruction.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblInstruction.setForeground(new Color(100, 150, 200));
        lblInstruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblFileName = new JLabel("Chưa có file nào được chọn");
        lblFileName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFileName.setForeground(new Color(150, 150, 150));
        lblFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnChooseFile = createFlatButton("Chọn File Thủ Công", new Color(41, 128, 185), new Color(52, 152, 219));
        btnChooseFile.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChooseFile.addActionListener(e -> chooseFile());

        dropZonePanel.add(lblInstruction);
        dropZonePanel.add(Box.createVerticalStrut(15));
        dropZonePanel.add(lblFileName);
        dropZonePanel.add(Box.createVerticalStrut(25));
        dropZonePanel.add(btnChooseFile);

        // Cài đặt tính năng Drag & Drop
        setupDragAndDrop(dropZonePanel);

        // 3. Progress Bar & Status
        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(500, 8));
        progressBar.setMaximumSize(new Dimension(500, 8));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setVisible(false);
        progressBar.setBorderPainted(false);
        progressBar.setForeground(new Color(46, 204, 113)); // Màu xanh lá khi chạy
        progressBar.setBackground(new Color(235, 235, 235));

        lblStatus = new JLabel("Trạng thái: Sẵn sàng");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblStatus.setForeground(new Color(120, 120, 120));
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Lắp ráp các thành phần vào Card
        mainCard.add(lblTitle);
        mainCard.add(Box.createVerticalStrut(8));
        mainCard.add(lblSub);
        mainCard.add(Box.createVerticalStrut(30));
        mainCard.add(dropZonePanel);
        mainCard.add(Box.createVerticalStrut(20));
        mainCard.add(progressBar);
        mainCard.add(Box.createVerticalStrut(10));
        mainCard.add(lblStatus);

        add(mainCard);
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
        btn.setPreferredSize(new Dimension(180, 42));
        btn.setMaximumSize(new Dimension(180, 42));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    // ================= DRAG & DROP LOGIC =================
    private void setupDragAndDrop(JPanel dropZone) {
        new DropTarget(dropZone, DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = dtde.getTransferable();

                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        @SuppressWarnings("unchecked")
                        List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                        if (!files.isEmpty()) {
                            File file = files.get(0);
                            if (file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls")) {
                                handleFileSelected(file);
                            } else {
                                JOptionPane.showMessageDialog(ImprtExDiemCong.this,
                                        "Vui lòng chỉ chọn định dạng file Excel (.xlsx hoặc .xls)",
                                        "Định dạng không hợp lệ",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ImprtExDiemCong.this, "Lỗi đọc file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            }
        }, true);
    }

    // ================= CHOOSE FILE LOGIC =================
    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".xlsx") || f.getName().endsWith(".xls");
            }
            @Override
            public String getDescription() {
                return "Excel Files (*.xlsx, *.xls)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            handleFileSelected(file);
        }
    }

    private void handleFileSelected(File file) {
        if (bus == null) {
            JOptionPane.showMessageDialog(this, "Kết nối Database chưa sẵn sàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedFile = file;
        lblFileName.setText(file.getName());
        lblFileName.setForeground(new Color(39, 174, 96)); // Chữ xanh lá báo hiệu đã chọn
        lblFileName.setFont(new Font("Segoe UI", Font.BOLD, 14));

        int option = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn nạp điểm cộng từ file: " + file.getName() + " ?",
                "Xác nhận Import",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            importFile(file);
        } else {
            resetUI();
        }
    }

    // ================= IMPORT THREAD =================
    private void importFile(File file) {
        btnChooseFile.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true); // Hiệu ứng tiến trình chạy vô tận
        lblStatus.setText("Trạng thái: Đang xử lý dữ liệu...");
        lblStatus.setForeground(new Color(230, 126, 34)); // Cam cảnh báo đang xử lý

        new Thread(() -> {
            try {
                // Gọi BUS để import điểm cộng
                bus.importFromExcel(file);

                SwingUtilities.invokeLater(() -> {
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(100);
                    lblStatus.setText("Trạng thái: Import dữ liệu thành công!");
                    lblStatus.setForeground(new Color(39, 174, 96));

                    JOptionPane.showMessageDialog(this,
                            "Nạp điểm cộng thành công!\nTệp: " + file.getName(),
                            "Hoàn tất",
                            JOptionPane.INFORMATION_MESSAGE);
                    resetUI();
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(false);
                    lblStatus.setText("Trạng thái: Lỗi trong quá trình import!");
                    lblStatus.setForeground(new Color(231, 76, 60));
                    btnChooseFile.setEnabled(true);

                    JOptionPane.showMessageDialog(this,
                            "Quá trình import thất bại!\nChi tiết: " + e.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void resetUI() {
        btnChooseFile.setEnabled(true);
        lblFileName.setText("Chưa có file nào được chọn");
        lblFileName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFileName.setForeground(new Color(150, 150, 150));
        progressBar.setVisible(false);
        lblStatus.setText("Trạng thái: Sẵn sàng");
        lblStatus.setForeground(new Color(120, 120, 120));
        selectedFile = null;
    }

    // ================= CLASSES CUSTOM ĐỒ HOẠ =================
    
    // Class tạo thẻ bo góc trắng
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
            
            // Nền trắng
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            // Viền nhạt
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }

    // Class tạo vùng Drop Zone với nét đứt
    class DashedDropZone extends JPanel {
        public DashedDropZone() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            int radius = 20;

            // Nền màu xám/xanh nhạt bên trong
            g2.setColor(getBackground());
            g2.fillRoundRect(2, 2, w - 5, h - 5, radius, radius);

            // Nét đứt (Dashed stroke) viền ngoài
            Stroke dashed = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{12}, 0);
            g2.setStroke(dashed);
            g2.setColor(new Color(170, 190, 220)); // Xanh lơ nhạt
            g2.drawRoundRect(2, 2, w - 5, h - 5, radius, radius);

            g2.dispose();
        }
    }
}