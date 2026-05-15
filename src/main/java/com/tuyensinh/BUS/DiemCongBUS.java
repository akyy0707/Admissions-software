package com.tuyensinh.BUS;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
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

            String sql = """
                    SELECT ts_cccd, manganh, matohop, phuongthuc,
                           diemCC, diemUtxt, diemTong
                    FROM xt_diemcongxetuyen
                    """;

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

    // ================= SEARCH BY CCCD =================
    public List<DiemCongDTO> searchByCCCD(String cccd) {

        List<DiemCongDTO> list = new ArrayList<>();

        try {

            String sql = """
                    SELECT ts_cccd, manganh, matohop, phuongthuc,
                           diemCC, diemUtxt, diemTong
                    FROM xt_diemcongxetuyen
                    WHERE ts_cccd LIKE ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + cccd + "%");
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
    public void importDiemCC(File file) {

        try (Workbook wb = new XSSFWorkbook(file)) {

            Sheet sheet = wb.getSheetAt(0);

            String sql = """
                        UPDATE xt_diemcongxetuyen

                        SET
                            diemCC = ?,

                            diemTong =
                                ? + IFNULL(diemUtxt, 0)

                        WHERE ts_cccd = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);

            int batchSize = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null)
                    continue;

                Cell cccdCell = row.getCell(1);
                Cell diemCell = row.getCell(5);

                if (cccdCell == null || diemCell == null)
                    continue;

                String cccd = cccdCell.toString().trim();

                if (cccd.isEmpty())
                    continue;

                double diemCC;

                try {

                    diemCC = Double.parseDouble(
                            diemCell.toString().trim());

                } catch (Exception e) {
                    continue;
                }

                ps.setDouble(1, diemCC);

                ps.setDouble(2, diemCC);

                ps.setString(3, cccd);

                ps.addBatch();

                batchSize++;

                if (batchSize >= 1000) {

                    ps.executeBatch();

                    ps.clearBatch();

                    batchSize = 0;
                }
            }

            ps.executeBatch();

            ps.clearBatch();

            System.out.println("Import điểm CC thành công");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importDiemUuTien(File file) {

        try (Workbook wb = new XSSFWorkbook(file)) {

            Sheet sheet = wb.getSheetAt(0);

            String sql = """
                        INSERT INTO xt_diemcongxetuyen
                        (
                            ts_cccd,
                            manganh,
                            matohop,
                            phuongthuc,
                            diemUtxt,
                            diemTong,
                            dc_keys
                        )

                        VALUES (?, ?, ?, ?, ?, ?, ?)

                        ON DUPLICATE KEY UPDATE

                            diemUtxt = VALUES(diemUtxt),

                            diemTong =
                                IFNULL(diemCC,0)
                                + VALUES(diemUtxt)
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);

            int batchSize = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null)
                    continue;

                Cell cccdCell = row.getCell(1);
                Cell monCell = row.getCell(3);
                Cell nganhCell = row.getCell(6);

                if (cccdCell == null
                        || monCell == null
                        || nganhCell == null)
                    continue;

                String cccd = cccdCell.toString().trim();

                String monExcel = monCell.toString().trim();

                String tenNganh = nganhCell.toString().trim();

                if (cccd.isEmpty()
                        || tenNganh.isEmpty())
                    continue;

                String maMon = chuyenMaMon(monExcel);

                if (maMon.isEmpty())
                    continue;

                String manganh = getMaNganh(tenNganh);

                if (manganh == null)
                    continue;

                List<String> dsToHop = getToHopTheoNganh(manganh);

                for (String toHop : dsToHop) {

                    double diemUT = monThuocToHop(maMon, toHop)
                            ? 1.5
                            : 0.5;

                    ps.setString(1, cccd);

                    ps.setString(2, manganh);

                    ps.setString(3, toHop);

                    ps.setString(4, null);

                    ps.setDouble(5, diemUT);

                    ps.setDouble(6, diemUT);

                    ps.setString(7,
                            cccd + "_" + manganh + "_" + toHop);

                    ps.addBatch();

                    batchSize++;

                    if (batchSize >= 1000) {

                        ps.executeBatch();

                        ps.clearBatch();

                        batchSize = 0;
                    }
                }
            }

            ps.executeBatch();

            ps.clearBatch();

            System.out.println("Import điểm ưu tiên thành công");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= HELPERS =================
    private String getMaNganh(String tenNganh) {

        try {

            String sql = """
                        SELECT manganh
                        FROM xt_nganh
                        WHERE tennganh = ?
                    """;

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

    private List<String> getToHopTheoNganh(String manganh) {

        List<String> list = new ArrayList<>();

        try {

            String sql = """
                        SELECT matohop
                        FROM xt_nganh_tohop
                        WHERE manganh = ?
                    """;

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

    private String chuyenMaMon(String mon) {

        mon = mon.trim().toLowerCase();

        switch (mon) {

            case "toán":
            case "toan":
                return "TO";

            case "vật lý":
            case "vat ly":
            case "lý":
            case "ly":
                return "LI";

            case "hóa":
            case "hoa":
            case "hóa học":
                return "HO";

            case "sinh":
            case "sinh học":
                return "SI";

            case "tiếng anh":
            case "tieng anh":
            case "anh":
                return "N1";

            case "lịch sử":
            case "su":
                return "SU";

            case "địa lý":
            case "dia ly":
                return "DI";

            case "ngữ văn":
            case "văn":
            case "van":
                return "VA";

            case "tin":
            case "tin học":
                return "TI";

            case "công nghệ công nghiệp":
                return "CNCN";

            case "công nghệ nông nghiệp":
                return "CNNN";

            case "giáo dục kinh tế và pháp luật":
                return "KTPL";
        }

        return "";
    }

    private boolean monThuocToHop(String maMon, String maToHop) {

        try {

            String sql = """
                        SELECT *
                        FROM xt_tohop_monthi
                        WHERE matohop = ?
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, maToHop);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String mon1 = rs.getString("mon1");
                String mon2 = rs.getString("mon2");
                String mon3 = rs.getString("mon3");

                return maMon.equalsIgnoreCase(mon1)
                        || maMon.equalsIgnoreCase(mon2)
                        || maMon.equalsIgnoreCase(mon3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}