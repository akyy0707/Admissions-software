package com.tuyensinh.BUS;

import java.io.File;
import java.sql.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DiemCongBUS {

    private Connection conn;

    public DiemCongBUS(Connection conn) {
        this.conn = conn;
    }

    // ================= IMPORT =================

    public void importFromExcel(File file) {
        try {
            Workbook wb = new XSSFWorkbook(file);
            Sheet sheet = wb.getSheetAt(0);

            String insertSQL = "INSERT INTO xt_diemcongxettuyen(ts_cccd, manganh, matohop, phuongthuc, diemCC, diemUtxt, diemTong, dc_keys) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psInsert = conn.prepareStatement(insertSQL);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                String cccd = getString(row.getCell(1));
                String mon = getString(row.getCell(3)).toLowerCase();
                String tenNganh = getString(row.getCell(6));
                double diemCC = getDouble(row.getCell(7));
                double diemUT = getDouble(row.getCell(8));

                // 1. lấy mã ngành
                String manganh = getMaNganh(tenNganh);
                if (manganh == null) {
                    System.out.println("Không tìm thấy ngành: " + tenNganh);
                    continue;
                }

                // 2. lấy tổ hợp
                List<String> listToHop = getToHop(manganh, mon);
                if (listToHop.isEmpty()) {
                    System.out.println("Không có tổ hợp: " + manganh + " - " + mon);
                    continue;
                }

                double diemTong = diemCC + diemUT;

                // 3. insert
                for (String matohop : listToHop) {
                    String key = cccd + "_" + manganh + "_" + matohop;

                    psInsert.setString(1, cccd);
                    psInsert.setString(2, manganh);
                    psInsert.setString(3, matohop);
                    psInsert.setString(4, "PT4");
                    psInsert.setDouble(5, diemCC);
                    psInsert.setDouble(6, diemUT);
                    psInsert.setDouble(7, diemTong);
                    psInsert.setString(8, key);

                    psInsert.addBatch();
                }

                if (i % 500 == 0) {
                    psInsert.executeBatch();
                }
            }

            psInsert.executeBatch();
            System.out.println("IMPORT OK!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LẤY MÃ NGÀNH =================

    private String getMaNganh(String tenNganh) {
        try {
            String sql = "SELECT manganh FROM xt_nganh WHERE tennganh = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tenNganh);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("manganh");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= LẤY TỔ HỢP =================

    private List<String> getToHop(String manganh, String mon) {
        List<String> list = new ArrayList<>();

        try {
            String sql;

            // xử lý riêng tiếng anh
            if (mon.contains("anh")) {
                sql = "SELECT matohop FROM xt_nganh_tohop WHERE manganh = ? AND (th_mon1 = 'Anh' OR th_mon2 = 'Anh' OR th_mon3 = 'Anh')";
            } else {
                String flag = mapMon(mon);
                if (flag == null)
                    return list;

                sql = "SELECT matohop FROM xt_nganh_tohop WHERE manganh = ? AND " + flag + " = 1";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, manganh);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("matohop"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= MAP MÔN =================

    private String mapMon(String mon) {
        mon = mon.toLowerCase();

        if (mon.contains("toán"))
            return "TO";
        if (mon.contains("lý"))
            return "LI";
        if (mon.contains("hóa"))
            return "HO";
        if (mon.contains("sinh"))
            return "SI";
        if (mon.contains("văn"))
            return "VA";
        if (mon.contains("sử"))
            return "SU";
        if (mon.contains("địa"))
            return "DI";
        if (mon.contains("tin"))
            return "TI";

        return null;
    }

    // ================= UTILS =================

    private String getString(Cell c) {
        return c == null ? "" : c.toString().trim();
    }

    private double getDouble(Cell c) {
        try {
            return c.getNumericCellValue();
        } catch (Exception e) {
            try {
                return Double.parseDouble(c.toString());
            } catch (Exception ex) {
                return 0;
            }
        }
    }
}