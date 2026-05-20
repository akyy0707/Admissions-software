package com.tuyensinh.BUS;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tuyensinh.DTO.DiemCongDTO;
import com.tuyensinh.config.DB;

public class DiemCongBUS {

    private Connection conn;
    private List<DiemCongDTO> cacheAll;
    private Map<String, DiemCongDTO> cacheByKey;
    private boolean cacheLoaded = false;

    public DiemCongBUS() {
        try {
            this.conn = DB.getConn(); // hoặc HibernateUtil
            this.cacheAll = new ArrayList<>();
            this.cacheByKey = new HashMap<>();
            loadCache();
        } catch (Exception ex) {
            System.getLogger(DiemCongBUS.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private synchronized void loadCache() {
        if (cacheLoaded)
            return;

        try {
            String sql = "SELECT ts_cccd, manganh, matohop, phuongthuc, diemCC, diemUtxt, diemTong "
                    + "FROM xt_diemcongxetuyen";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            cacheAll.clear();
            cacheByKey.clear();

            while (rs.next()) {
                DiemCongDTO d = new DiemCongDTO();
                d.setCccd(rs.getString("ts_cccd"));
                d.setMaNganh(rs.getString("manganh"));
                d.setMaToHop(rs.getString("matohop"));
                d.setPhuongThuc(rs.getString("phuongthuc"));
                d.setDiemCC(rs.getDouble("diemCC"));
                d.setDiemUuTien(rs.getDouble("diemUtxt"));
                d.setDiemTong(rs.getDouble("diemTong"));

                cacheAll.add(d);

                String key = buildKey(
                        d.getCccd(),
                        d.getMaNganh(),
                        d.getMaToHop(),
                        d.getPhuongThuc());
                cacheByKey.put(key, d);
            }

            cacheLoaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildKey(String cccd, String manganh, String matohop, String phuongthuc) {
        return (cccd == null ? "" : cccd) + "|" +
                (manganh == null ? "" : manganh) + "|" +
                (matohop == null ? "" : matohop) + "|" +
                (phuongthuc == null ? "" : phuongthuc);
    }

    // ================= GET ALL =================
    public List<DiemCongDTO> getAll() {
        if (!cacheLoaded)
            loadCache();
        return new ArrayList<>(cacheAll);
    }

    // ================= SEARCH BY CCCD =================
    public List<DiemCongDTO> searchByCCCD(String cccd) {
        if (!cacheLoaded)
            loadCache();

        List<DiemCongDTO> list = new ArrayList<>();
        for (DiemCongDTO d : cacheAll) {
            if (d.getCccd() != null && d.getCccd().contains(cccd)) {
                list.add(d);
            }
        }
        return list;
    }

    public DiemCongDTO getByCCCDAndNganhToHopPhuongThuc(
            String cccd,
            String maNganh,
            String maToHop,
            String phuongThuc) {

        if (!cacheLoaded)
            loadCache();

        // 1. Tìm exact match: cccd + manganh + matohop + phuongthuc
        String exactKey = buildKey(cccd, maNganh, maToHop, phuongThuc);
        if (cacheByKey.containsKey(exactKey)) {
            return cacheByKey.get(exactKey);
        }

        // 2. Tìm generic: chỉ cccd, tất cả khác NULL (điểm CC chung)
        String genericKey = buildKey(cccd, null, null, null);
        if (cacheByKey.containsKey(genericKey)) {
            return cacheByKey.get(genericKey);
        }

        return null;
    }

    // ================= COUNT PT4 =================
    public int countPT4() {
        if (!cacheLoaded)
            loadCache();

        int count = 0;
        for (DiemCongDTO d : cacheAll) {
            if ("PT4".equals(d.getPhuongThuc())) {
                count++;
            }
        }
        return count;
    }

    public void reloadCache() {
        cacheLoaded = false;
        loadCache();
    }

    // ================= IMPORT EXCEL =================
    public void importDiemCC(File file) {

        try (Workbook wb = new XSSFWorkbook(file)) {

            Sheet sheet = wb.getSheetAt(0);

            String sqlUpdate = "UPDATE xt_diemcongxetuyen "
                    + "SET diemCC = ?, "
                    + "diemTong = ? + IFNULL(diemUtxt, 0) "
                    + "WHERE ts_cccd = ?";

            String sqlInsert = "INSERT INTO xt_diemcongxetuyen "
                    + "(ts_cccd, manganh, matohop, phuongthuc, diemCC, diemTong, dc_keys) "
                    + "VALUES (?, NULL, NULL, NULL, ?, ?, ?)";

            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);

            int imported = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                String cccd = getCellText(row.getCell(1));
                String diemStr = getCellText(row.getCell(5));
                if (cccd.isEmpty() || diemStr.isEmpty())
                    continue;

                double diemCC;
                try {
                    diemCC = Double.parseDouble(diemStr.replace(',', '.'));
                } catch (Exception e) {
                    continue;
                }

                psUpdate.setDouble(1, diemCC);
                psUpdate.setDouble(2, diemCC);
                psUpdate.setString(3, cccd);

                int updated = psUpdate.executeUpdate();

                if (updated == 0) {
                    psInsert.setString(1, cccd);
                    psInsert.setDouble(2, diemCC);
                    psInsert.setDouble(3, diemCC);
                    psInsert.setString(4, cccd);
                    psInsert.executeUpdate();
                }

                imported++;
            }

            psUpdate.close();
            psInsert.close();

            reloadCache();

            System.out.println("Import điểm CC thành công. Dòng xử lý: " + imported);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importDiemUuTien(File file) {

        try (Workbook wb = new XSSFWorkbook(file)) {

            Sheet sheet = wb.getSheetAt(0);

            String sqlUpdate = "UPDATE xt_diemcongxetuyen "
                    + "SET diemUtxt = ?, "
                    + "diemTong = IFNULL(diemCC, 0) + ? "
                    + "WHERE ts_cccd = ?";

            String sqlInsert = "INSERT INTO xt_diemcongxetuyen "
                    + "(ts_cccd, manganh, matohop, phuongthuc, diemUtxt, diemTong, dc_keys) "
                    + "VALUES (?, NULL, NULL, NULL, ?, ?, ?)";

            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);

            int imported = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                String cccd = getCellText(row.getCell(1));
                String diemStr = getCellText(row.getCell(7));
                if (cccd.isEmpty() || diemStr.isEmpty())
                    continue;

                double diemUT;
                try {
                    diemUT = Double.parseDouble(diemStr.replace(',', '.'));
                } catch (Exception e) {
                    continue;
                }

                psUpdate.setDouble(1, diemUT);
                psUpdate.setDouble(2, diemUT);
                psUpdate.setString(3, cccd);

                int updated = psUpdate.executeUpdate();

                if (updated == 0) {
                    psInsert.setString(1, cccd);
                    psInsert.setDouble(2, diemUT);
                    psInsert.setDouble(3, diemUT);
                    psInsert.setString(4, cccd);
                    psInsert.executeUpdate();
                }

                imported++;
            }

            psUpdate.close();
            psInsert.close();

            reloadCache();

            System.out.println("Import điểm ưu tiên thành công. Dòng xử lý: " + imported);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCellText(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue()).trim();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()).trim();
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue()).trim();
                } catch (Exception e) {
                    return cell.getCellFormula().trim();
                }
            default:
                return cell.toString().trim();
        }
    }

}
