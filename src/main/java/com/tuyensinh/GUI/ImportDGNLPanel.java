package com.tuyensinh.GUI;

import com.tuyensinh.BUS.ImportDGNL;
import com.tuyensinh.config.DB;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.awt.event.ActionEvent;

public class ImportDGNLPanel extends JPanel {

    private Connection conn;
    private JButton btnImport;
    private JLabel lblStatus;

    public ImportDGNLPanel() {

        setLayout(new FlowLayout());

        try {
            conn = DB.getConn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnImport = new JButton("Import Điểm ĐGNL");
        lblStatus = new JLabel("Chưa import file");

        add(btnImport);
        add(lblStatus);

        btnImport.addActionListener((ActionEvent e) -> {

            JFileChooser fileChooser = new JFileChooser();

            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {

                File file = fileChooser.getSelectedFile();

                try {

                    ImportDGNL importer = new ImportDGNL(conn);

                    importer.importExcel(file);

                    lblStatus.setText("Import thành công: " + file.getName());

                    JOptionPane.showMessageDialog(
                            null,
                            "Import điểm ĐGNL thành công!");

                } catch (Exception ex) {

                    ex.printStackTrace();

                    JOptionPane.showMessageDialog(
                            null,
                            "Import thất bại!");
                }
            }
        });
    }
}