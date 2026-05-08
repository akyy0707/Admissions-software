package com.tuyensinh.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

import com.tuyensinh.BUS.*;

/**
 * ImportExcelFrm - Giao diện import Excel với kéo thả (Drag & Drop)
 */
public class ImportExcelFrm extends JPanel {

    private DiemThiBUS bus = new DiemThiBUS();
    private JLabel lblDropZone;
    private JLabel lblFileName;
    private JProgressBar progressBar;
    private JButton btnChooseFile;
    private File selectedFile;
    private JLabel lblStatus;

    public ImportExcelFrm() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel lblTitle = new JLabel("📥 IMPORT ĐIỂM THI TỪ EXCEL");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Center - Drop Zone
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Drop Zone Panel
        lblDropZone = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Draw dashed border
                g2.setColor(new Color(100, 150, 200));
                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{10}, 0));
                g2.drawRoundRect(10, 10, w - 20, h - 20, 20, 20);
                
                // Draw background
                g2.setColor(new Color(240, 248, 255));
                g2.fillRoundRect(11, 11, w - 22, h - 22, 20, 20);
                
                super.paintComponent(g);
            }
        };
        lblDropZone.setLayout(new BoxLayout(lblDropZone, BoxLayout.Y_AXIS));
        lblDropZone.setBorder(new EmptyBorder(40, 20, 40, 20));
        lblDropZone.setPreferredSize(new Dimension(400, 250));

        JLabel lblIcon = new JLabel("📁");
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblInstruction = new JLabel("Kéo file Excel vào đây");
        lblInstruction.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblInstruction.setForeground(new Color(70, 130, 180));
        lblInstruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblFileName = new JLabel("Chưa chọn file");
        lblFileName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblFileName.setForeground(new Color(100, 100, 100));
        lblFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnChooseFile = new JButton("Hoặc Click Để Chọn File");
        btnChooseFile.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnChooseFile.setBackground(new Color(70, 130, 180));
        btnChooseFile.setForeground(Color.WHITE);
        btnChooseFile.setFocusPainted(false);
        btnChooseFile.setPreferredSize(new Dimension(250, 40));
        btnChooseFile.setMaximumSize(new Dimension(250, 40));
        btnChooseFile.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChooseFile.addActionListener(e -> chooseFile());

        lblDropZone.add(lblIcon);
        lblDropZone.add(Box.createVerticalStrut(15));
        lblDropZone.add(lblInstruction);
        lblDropZone.add(Box.createVerticalStrut(10));
        lblDropZone.add(lblFileName);
        lblDropZone.add(Box.createVerticalStrut(20));
        lblDropZone.add(btnChooseFile);

        // Setup Drag & Drop
        setupDragAndDrop(lblDropZone);

        centerPanel.add(lblDropZone, BorderLayout.CENTER);

        // Progress & Status
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        
        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(400, 25));
        progressBar.setVisible(false);

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(100, 100, 100));

        bottomPanel.add(progressBar, BorderLayout.NORTH);
        bottomPanel.add(lblStatus, BorderLayout.CENTER);

        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void setupDragAndDrop(JLabel dropZone) {
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
                                JOptionPane.showMessageDialog(ImportExcelFrm.this,
                                        "Vui lòng chọn file Excel (.xlsx hoặc .xls)",
                                        "Lỗi",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ImportExcelFrm.this,
                            "Lỗi: " + e.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            }
        }, true);
    }

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
        selectedFile = file;
        lblFileName.setText("✓ " + file.getName());
        lblFileName.setForeground(new Color(34, 139, 34));

        // Ask to import
        int option = JOptionPane.showConfirmDialog(this,
                "Bạn muốn import file: " + file.getName() + " ?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            importFile(file);
        }
    }

    private void importFile(File file) {
        // Disable controls
        btnChooseFile.setEnabled(false);

        // Show progress
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        lblStatus.setText("⏳ Đang import...");

        new Thread(() -> {
            try {
                bus.importFromExcel(file);

                SwingUtilities.invokeLater(() -> {
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(100);
                    lblStatus.setText("✅ Import thành công!");
                    
                    JOptionPane.showMessageDialog(this,
                            "Import thành công!\nFile: " + file.getName(),
                            "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Reset
                    resetUI();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(false);
                    lblStatus.setText("❌ Import thất bại: " + e.getMessage());
                    btnChooseFile.setEnabled(true);

                    JOptionPane.showMessageDialog(this,
                            "Lỗi: " + e.getMessage(),
                            "Lỗi import",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void resetUI() {
        btnChooseFile.setEnabled(true);
        lblFileName.setText("Chưa chọn file");
        lblFileName.setForeground(new Color(100, 100, 100));
        progressBar.setVisible(false);
        lblStatus.setText(" ");
        selectedFile = null;
    }
}
