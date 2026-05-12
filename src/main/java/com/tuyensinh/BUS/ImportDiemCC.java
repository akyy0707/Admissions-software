package com.tuyensinh.BUS;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportDiemCC {

    private Connection conn;

    public ImportDiemCC(Connection conn) {
        this.conn = conn;
    }

    public void importExcel(File file) {

        try {

            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            String sqlCheck = "SELECT iddiemthi " +
                    "FROM xt_diemthixettuyen " +
                    "WHERE cccd = ?";

            String sqlInsert = "INSERT INTO xt_diemthixettuyen " +
                    "(cccd, N1_CC) " +
                    "VALUES (?, ?)";

            String sqlUpdate = "UPDATE xt_diemthixettuyen " +
                    "SET N1_CC = ? " +
                    "WHERE cccd = ?";

            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);

            DataFormatter formatter = new DataFormatter();

            // Bỏ dòng tiêu đề
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null)
                    continue;

                // Cột B = CCCD
                String cccd = formatter.formatCellValue(row.getCell(1)).trim();

                // Cột E = Điểm quy đổi
                String diemQDStr = formatter.formatCellValue(row.getCell(4)).trim();

                if (cccd.isEmpty() || diemQDStr.isEmpty())
                    continue;

                double diemQD = Double.parseDouble(diemQDStr);

                // Kiểm tra tồn tại
                psCheck.setString(1, cccd);

                ResultSet rs = psCheck.executeQuery();

                if (rs.next()) {

                    // UPDATE
                    psUpdate.setDouble(1, diemQD);
                    psUpdate.setString(2, cccd);

                    psUpdate.executeUpdate();

                } else {

                    // INSERT
                    psInsert.setString(1, cccd);
                    psInsert.setDouble(2, diemQD);

                    psInsert.executeUpdate();
                }

                rs.close();
            }

            workbook.close();
            fis.close();

            psCheck.close();
            psInsert.close();
            psUpdate.close();

            System.out.println("Import điểm chứng chỉ thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
