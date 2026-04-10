package com.tuyensinh.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

import com.tuyensinh.bus.*;

public class ImportExcelFrm extends JFrame {

    private JButton btnImport;
    private JProgressBar progressBar;

    private DiemThiBUS bus = new DiemThiBUS();

    public ImportExcelFrm() {
        setTitle("Import Điểm Excel");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

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
                progressBar.setIndeterminate(true);

                bus.importFromExcel(file);

                progressBar.setIndeterminate(false);
                JOptionPane.showMessageDialog(this, "Import thành công!");
            }).start();
        }
    }
}
