
package com.tuyensinh.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Connection;

import com.tuyensinh.BUS.ImportDiemCC;
import com.tuyensinh.config.DB;

public class ImportDiemCCPanel extends JPanel {

    private Connection conn;
    private JButton btnImport;
    private JLabel lblStatus;

    public ImportDiemCCPanel() {

        setLayout(new FlowLayout());

        try {
            conn = DB.getConn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnImport = new JButton("Import Điểm Chứng Chỉ");
        lblStatus = new JLabel("Chưa import file");

        add(btnImport);
        add(lblStatus);

        btnImport.addActionListener((ActionEvent e) -> {

            JFileChooser fileChooser = new JFileChooser();

            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {

                File file = fileChooser.getSelectedFile();

                try {

                    ImportDiemCC importer = new ImportDiemCC(conn);

                    importer.importExcel(file);

                    lblStatus.setText("Import thành công: " + file.getName());

                    JOptionPane.showMessageDialog(
                            null,
                            "Import điểm chứng chỉ thành công!");

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
