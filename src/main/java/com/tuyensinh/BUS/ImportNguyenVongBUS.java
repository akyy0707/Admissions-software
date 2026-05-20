package com.tuyensinh.BUS;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.tuyensinh.config.DB;

public class ImportNguyenVongBUS {

    public int importFromExcel(File file) throws Exception {
        int inserted = 0;

        try (
                Connection conn = DB.getConn();
                FileInputStream fis = new FileInputStream(file);
                Workbook wb = WorkbookFactory.create(fis)
        ) {
            conn.setAutoCommit(false);

            Set<String> existingKeys = loadExistingKeys(conn);

            String sqlInsert = """
                INSERT IGNORE INTO xt_nguyenvongxettuyen
                (
                    nn_cccd,
                    nv_manganh,
                    nv_tt,
                    nv_keys
                )
                VALUES (?, ?, ?, ?)
            """;

            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                DataFormatter formatter = new DataFormatter();

                int[] sheetIndexes = {1, 2};

                for (int sheetIndex : sheetIndexes) {
                    Sheet sheet = wb.getNumberOfSheets() > sheetIndex
                            ? wb.getSheetAt(sheetIndex)
                            : null;

                    if (sheet == null) {
                        continue;
                    }

                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        if (row == null) {
                            continue;
                        }

                        String cccd = formatter.formatCellValue(row.getCell(1)).trim();
                        String nvTtStr = formatter.formatCellValue(row.getCell(2)).trim();
                        String maNganh = formatter.formatCellValue(row.getCell(5)).trim();

                        if (cccd.isEmpty() || nvTtStr.isEmpty() || maNganh.isEmpty()) {
                            continue;
                        }

                        int nvTt;
                        try {
                            nvTt = Integer.parseInt(nvTtStr);
                        } catch (NumberFormatException ex) {
                            continue;
                        }

                        String nvKeys = cccd + "_" + nvTt;

                        if (existingKeys.contains(nvKeys)) {
                            continue;
                        }

                        existingKeys.add(nvKeys);

                        psInsert.setString(1, cccd);
                        psInsert.setString(2, maNganh);
                        psInsert.setInt(3, nvTt);
                        psInsert.setString(4, nvKeys);
                        psInsert.addBatch();
                        inserted++;

                        if (inserted % 500 == 0) {
                            psInsert.executeBatch();
                            conn.commit();
                        }
                    }
                }

                psInsert.executeBatch();
                conn.commit();
            }
        }

        return inserted;
    }

    private Set<String> loadExistingKeys(Connection conn) throws Exception {
        Set<String> keys = new HashSet<>();

        String sql = """
            SELECT nv_keys
            FROM xt_nguyenvongxettuyen
            WHERE nv_keys IS NOT NULL
        """;

        try (
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                keys.add(rs.getString("nv_keys"));
            }
        }

        return keys;
    }

    public static void main(String[] args) throws Exception {
        String path = args.length > 0 ? args[0] : "D:\\Doc\\Nguyenvong.xlsx";
        int inserted = new ImportNguyenVongBUS().importFromExcel(new File(path));
        System.out.println("Imported: " + inserted);
    }
}
