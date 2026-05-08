package com.tuyensinh.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Connection;

import com.tuyensinh.BUS.DiemCongBUS;
import com.tuyensinh.config.DB;

public class ImprtExDiemCong extends JPanel {

    private Connection conn;
    private DiemCongBUS bus;

    private JButton btnImport;
    private JProgressBar progressBar;

    public ImprtExDiemCong() {

        // 🔥 FIX: kết nối DB trong constructor
        try {
            conn = DB.getConn();
            bus = new DiemCongBUS(conn);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối database!");
        }

        btnImport = new JButton("Chọn file Excel");
        progressBar = new JProgressBar();

        btnImport.addActionListener((ActionEvent e) -> chooseFile());

        setLayout(null);
        btnImport.setBounds(100, 30, 200, 40);
        progressBar.setBounds(50, 100, 300, 25);

        add(btnImport);
        add(progressBar);
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            new Thread(() -> {
                try {

                    // 🔥 FIX: gọi đúng method
                    bus.importFromExcel(file);

                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Import thành công!"));

                } catch (Exception e) {
                    e.printStackTrace();

                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Import lỗi!"));
                }
            }).start();
        }
    }
}