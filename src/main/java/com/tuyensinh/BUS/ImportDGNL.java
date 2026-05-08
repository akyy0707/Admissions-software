package com.tuyensinh.BUS;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportDGNL {

    private Connection conn;

    public ImportDGNL(Connection conn) {
        this.conn = conn;
    }

    public void importExcel(File file) {

        try {

            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fis);

            Sheet sheet = workbook.getSheetAt(0);

            String sqlCheck = "SELECT iddiemthi " +
                    "FROM xt_diemthixettuyen " +
                    "WHERE cccd = ?";

            String sqlInsert = "INSERT INTO xt_diemthixettuyen " +
                    "(cccd, d_phuongthuc, NL1) " +
                    "VALUES (?, 'DGNL', ?)";

            String sqlUpdate = "UPDATE xt_diemthixettuyen " +
                    "SET NL1 = ?, d_phuongthuc = 'DGNL' " +
                    "WHERE cccd = ?";

            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);

            DataFormatter formatter = new DataFormatter();

            // bỏ dòng tiêu đề
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null)
                    continue;

                // cột B = CMND
                String cccd = formatter.formatCellValue(row.getCell(1)).trim();

                // cột I = DIEM
                String diemStr = formatter.formatCellValue(row.getCell(8)).trim();

                if (cccd.isEmpty() || diemStr.isEmpty())
                    continue;

                double diem = Double.parseDouble(diemStr);

                // kiểm tra tồn tại
                psCheck.setString(1, cccd);

                ResultSet rs = psCheck.executeQuery();

                if (rs.next()) {

                    // UPDATE
                    psUpdate.setDouble(1, diem);
                    psUpdate.setString(2, cccd);

                    psUpdate.executeUpdate();

                } else {

                    // INSERT
                    psInsert.setString(1, cccd);
                    psInsert.setDouble(2, diem);

                    psInsert.executeUpdate();
                }

                rs.close();
            }

            workbook.close();
            fis.close();

            psCheck.close();
            psInsert.close();
            psUpdate.close();

            System.out.println("Import ĐGNL thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}