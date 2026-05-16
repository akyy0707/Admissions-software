package com.tuyensinh.BUS;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tuyensinh.DTO.DiemCongDTO;
import com.tuyensinh.config.DB;

public class DiemCongBUS {

    private Connection conn;

    public DiemCongBUS() {
        try {
            this.conn = DB.getConn(); // hoặc HibernateUtil
        } catch (Exception ex) {
            System.getLogger(DiemCongBUS.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    // ================= GET ALL =================
    public List<DiemCongDTO> getAll() {

        List<DiemCongDTO> list = new ArrayList<>();

        try {

            String sql = "SELECT ts_cccd, manganh, matohop, phuongthuc, " +
                    "       diemCC, diemUtxt, diemTong " +
                    "FROM xt_diemcongxetuyen " +
                    "";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                DiemCongDTO d = new DiemCongDTO();

                d.setCccd(rs.getString("ts_cccd"));
                d.setMaNganh(rs.getString("manganh"));
                d.setMaToHop(rs.getString("matohop"));
                d.setPhuongThuc(rs.getString("phuongthuc"));
                d.setDiemCC(rs.getDouble("diemCC"));
                d.setDiemUuTien(rs.getDouble("diemUtxt"));
                d.setDiemTong(rs.getDouble("diemTong"));

                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= COUNT PT4 =================
    public int countPT4() {

        int count = 0;

        try {

            String sql = "SELECT COUNT(*) FROM xt_diemcongxetuyen WHERE phuongthuc = 'PT4'";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    // ================= IMPORT EXCEL =================
    public void importFromExcel(File file) {

        try (Workbook wb = new XSSFWorkbook(file)) {

            Sheet sheet = wb.getSheetAt(0);

            String sql = "INSERT INTO xt_diemcongxetuyen " +
                    "(ts_cccd, manganh, matohop, phuongthuc, diemCC, diemUtxt, diemTong, dc_keys) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                String cccd = row.getCell(1).toString();
                String mon = row.getCell(3).toString().toLowerCase();
                String tenNganh = row.getCell(6).toString();

                double diemCC = Double.parseDouble(row.getCell(7).toString());
                double diemUT = Double.parseDouble(row.getCell(8).toString());

                String manganh = getMaNganh(tenNganh);
                if (manganh == null)
                    continue;

                List<String> toHop = getToHop(manganh, mon);

                double tong = diemCC + diemUT;

                for (String th : toHop) {

                    ps.setString(1, cccd);
                    ps.setString(2, manganh);
                    ps.setString(3, th);
                    ps.setString(4, "PT4");
                    ps.setDouble(5, diemCC);
                    ps.setDouble(6, diemUT);
                    ps.setDouble(7, tong);
                    ps.setString(8, cccd + "_" + manganh + "_" + th);

                    ps.addBatch();
                }
            }

            ps.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= HELPERS =================
    private String getMaNganh(String ten) {

        try {

            String sql = "SELECT manganh FROM xt_nganh WHERE tennganh = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ten);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> getToHop(String ma, String mon) {

        List<String> list = new ArrayList<>();

        try {

            String sql = "SELECT matohop FROM xt_nganh_tohop " +
                    "WHERE manganh = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ma);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}