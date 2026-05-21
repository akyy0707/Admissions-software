package com.tuyensinh.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tuyensinh.DTO.NguyenVongDTO;

public class NguyenVongDAO {

        private Connection conn;

        public NguyenVongDAO(Connection conn) {
                this.conn = conn;
        }

        // =========================================================
        // GET ALL
        // =========================================================
        // =========================================================
        // GET ALL
        // =========================================================
        public List<NguyenVongDTO> getAll() {

                List<NguyenVongDTO> list = new ArrayList<>();

                try {

                        String sql = """
                                                SELECT
                                                        nv.idnv,
                                                        nv.nn_cccd,
                                                        nv.nv_manganh,
                                                        nv.nv_tt,

                                                        nv.diem_thxt,
                                                        nv.diem_cong,
                                                        nv.diem_utqd,
                                                        nv.diem_xettuyen,

                                                        nv.nv_ketqua,
                                                        nv.nv_keys,
                                                        nv.tt_phuongthuc,
                                                        nv.tt_thm

                                                FROM xt_nguyenvongxettuyen nv

                                                ORDER BY nv.idnv ASC
                                        """;

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {

                                NguyenVongDTO nv = new NguyenVongDTO();

                                nv.setIdnv(
                                                rs.getInt("idnv"));

                                nv.setCccd(
                                                rs.getString("nn_cccd"));

                                nv.setMaNganh(
                                                rs.getString("nv_manganh"));

                                nv.setThuTuNV(
                                                rs.getInt("nv_tt"));

                                // =========================
                                // ĐIỂM THXT
                                // =========================
                                nv.setDiemTHXT(
                                                rs.getDouble("diem_thxt"));

                                // =========================
                                // ĐIỂM CỘNG
                                // =========================
                                nv.setDiemCong(
                                                rs.getDouble("diem_cong"));

                                // =========================
                                // ĐIỂM ƯU TIÊN
                                // =========================
                                nv.setDiemUTQD(
                                                rs.getDouble("diem_utqd"));

                                // =========================
                                // ĐIỂM XÉT TUYỂN
                                // =========================
                                nv.setDiemXetTuyen(
                                                rs.getDouble("diem_xettuyen"));

                                nv.setKetQua(
                                                rs.getString("nv_ketqua"));

                                nv.setKeys(
                                                rs.getString("nv_keys"));

                                nv.setPhuongThuc(
                                                rs.getString("tt_phuongthuc"));

                                nv.setToHopMon(
                                                rs.getString("tt_thm"));

                                list.add(nv);
                        }

                        rs.close();
                        ps.close();

                } catch (Exception e) {

                        e.printStackTrace();
                }

                return list;
        }

        // =========================================================
        // GET CCCD DISTINCT
        // =========================================================
        public List<String> getDistinctCCCDs() {

                List<String> list = new ArrayList<>();

                try {

                        String sql = """
                                                SELECT DISTINCT nn_cccd
                                                FROM xt_nguyenvongxettuyen
                                                WHERE nn_cccd IS NOT NULL
                                                ORDER BY nn_cccd ASC
                                        """;

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                                list.add(rs.getString("nn_cccd"));
                        }

                        rs.close();
                        ps.close();

                } catch (Exception e) {

                        e.printStackTrace();
                }

                return list;
        }

        // =========================================================
        // GET BY CCCD ORDER NV
        // =========================================================
        public List<NguyenVongDTO> getByCCCDOrderNV(String cccd) {

                List<NguyenVongDTO> list = new ArrayList<>();

                try {

                        String sql = """
                                                SELECT
                                                        nv.idnv,
                                                        nv.nn_cccd,
                                                        nv.nv_manganh,
                                                        nv.nv_tt,

                                                        nv.diem_thxt,
                                                        nv.diem_cong,
                                                        nv.diem_utqd,
                                                        nv.diem_xettuyen,

                                                        nv.nv_ketqua,
                                                        nv.nv_keys,
                                                        nv.tt_phuongthuc,
                                                        nv.tt_thm

                                                FROM xt_nguyenvongxettuyen nv
                                                WHERE nv.nn_cccd = ?
                                                ORDER BY nv.nv_tt ASC
                                        """;

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setString(1, cccd);

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {

                                NguyenVongDTO nv = new NguyenVongDTO();

                                nv.setIdnv(rs.getInt("idnv"));
                                nv.setCccd(rs.getString("nn_cccd"));
                                nv.setMaNganh(rs.getString("nv_manganh"));
                                nv.setThuTuNV(rs.getInt("nv_tt"));
                                nv.setDiemTHXT(rs.getDouble("diem_thxt"));
                                nv.setDiemCong(rs.getDouble("diem_cong"));
                                nv.setDiemUTQD(rs.getDouble("diem_utqd"));
                                nv.setDiemXetTuyen(rs.getDouble("diem_xettuyen"));
                                nv.setKetQua(rs.getString("nv_ketqua"));
                                nv.setKeys(rs.getString("nv_keys"));
                                nv.setPhuongThuc(rs.getString("tt_phuongthuc"));
                                nv.setToHopMon(rs.getString("tt_thm"));

                                list.add(nv);
                        }

                        rs.close();
                        ps.close();

                } catch (Exception e) {

                        e.printStackTrace();
                }

                return list;
        }

        // =========================================================
        // INSERT OR UPDATE
        // =========================================================
        public boolean insert(NguyenVongDTO nv) {

                try {

                        String sql = """
                                            INSERT INTO xt_nguyenvongxettuyen
                                            (
                                                nn_cccd,
                                                nv_manganh,
                                                nv_tt,
                                                diem_thxt,
                                                diem_utqd,
                                                diem_cong,
                                                diem_xettuyen,
                                                nv_ketqua,
                                                nv_keys,
                                                tt_phuongthuc,
                                                tt_thm
                                            )
                                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

                                            ON DUPLICATE KEY UPDATE

                                                diem_thxt = VALUES(diem_thxt),
                                                diem_utqd = VALUES(diem_utqd),
                                                diem_cong = VALUES(diem_cong),
                                                diem_xettuyen = VALUES(diem_xettuyen),
                                                nv_ketqua = VALUES(nv_ketqua),
                                                tt_phuongthuc = VALUES(tt_phuongthuc),
                                                tt_thm = VALUES(tt_thm)
                                        """;

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setString(1, nv.getCccd());

                        ps.setString(2, nv.getMaNganh());

                        ps.setInt(3, nv.getThuTuNV());

                        ps.setDouble(4, nv.getDiemTHXT());

                        ps.setDouble(5, nv.getDiemUTQD());

                        ps.setDouble(6, nv.getDiemCong());

                        ps.setDouble(7, nv.getDiemXetTuyen());

                        ps.setString(8, nv.getKetQua());

                        ps.setString(9, nv.getKeys());

                        ps.setString(10, nv.getPhuongThuc());

                        ps.setString(11, nv.getToHopMon());

                        boolean ok = ps.executeUpdate() > 0;

                        ps.close();

                        return ok;

                } catch (Exception e) {

                        e.printStackTrace();
                }

                return false;
        }

        // =========================================================
        // INSERT BATCH
        // =========================================================
        public boolean insertBatch(List<NguyenVongDTO> list) {

                if (list == null || list.isEmpty()) {
                        return true;
                }

                boolean autoCommit = true;

                try {

                        String sql = """
                                                INSERT INTO xt_nguyenvongxettuyen
                                                (
                                                        nn_cccd,
                                                        nv_manganh,
                                                        nv_tt,
                                                        diem_thxt,
                                                        diem_utqd,
                                                        diem_cong,
                                                        diem_xettuyen,
                                                        nv_ketqua,
                                                        nv_keys,
                                                        tt_phuongthuc,
                                                        tt_thm
                                                )
                                                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

                                                ON DUPLICATE KEY UPDATE

                                                        diem_thxt = VALUES(diem_thxt),
                                                        diem_utqd = VALUES(diem_utqd),
                                                        diem_cong = VALUES(diem_cong),
                                                        diem_xettuyen = VALUES(diem_xettuyen),
                                                        nv_ketqua = VALUES(nv_ketqua),
                                                        tt_phuongthuc = VALUES(tt_phuongthuc),
                                                        tt_thm = VALUES(tt_thm)
                                        """;

                        autoCommit = conn.getAutoCommit();
                        if (autoCommit) {
                                conn.setAutoCommit(false);
                        }

                        PreparedStatement ps = conn.prepareStatement(sql);

                        for (NguyenVongDTO nv : list) {
                                ps.setString(1, nv.getCccd());
                                ps.setString(2, nv.getMaNganh());
                                ps.setInt(3, nv.getThuTuNV());
                                ps.setDouble(4, nv.getDiemTHXT());
                                ps.setDouble(5, nv.getDiemUTQD());
                                ps.setDouble(6, nv.getDiemCong());
                                ps.setDouble(7, nv.getDiemXetTuyen());
                                ps.setString(8, nv.getKetQua());
                                ps.setString(9, nv.getKeys());
                                ps.setString(10, nv.getPhuongThuc());
                                ps.setString(11, nv.getToHopMon());
                                ps.addBatch();
                        }

                        ps.executeBatch();
                        ps.close();

                        if (!autoCommit) {
                                return true;
                        }

                        conn.commit();

                        return true;

                } catch (Exception e) {

                        try {
                                if (autoCommit) {
                                        conn.rollback();
                                }
                        } catch (Exception ignore) {
                                // ignore rollback failures
                        }

                        e.printStackTrace();
                } finally {
                        try {
                                if (autoCommit) {
                                        conn.setAutoCommit(true);
                                }
                        } catch (Exception ignore) {
                                // ignore restore failures
                        }
                }

                return false;
        }

        // =========================================================
        // GET BY MANGANH AND KETQUA
        // =========================================================
        public List<NguyenVongDTO> getByMaNganhAndKetQua(String maNganh, String ketQua) {

                List<NguyenVongDTO> list = new ArrayList<>();

                try {

                        String sql = """
                                        SELECT
                                                nv.idnv,
                                                nv.nn_cccd,
                                                nv.nv_manganh,
                                                nv.nv_tt,
                                                nv.diem_thxt,
                                                nv.diem_cong,
                                                nv.diem_utqd,
                                                nv.diem_xettuyen,
                                                nv.nv_ketqua,
                                                nv.nv_keys,
                                                nv.tt_phuongthuc,
                                                nv.tt_thm
                                        FROM xt_nguyenvongxettuyen nv
                                        WHERE nv.nv_manganh = ? AND nv.nv_ketqua = ?
                                        ORDER BY nv.diem_xettuyen DESC, nv.nn_cccd ASC
                                        """;

                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, maNganh);
                        ps.setString(2, ketQua);

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {

                                NguyenVongDTO nv = new NguyenVongDTO();

                                nv.setIdnv(rs.getInt("idnv"));
                                nv.setCccd(rs.getString("nn_cccd"));
                                nv.setMaNganh(rs.getString("nv_manganh"));
                                nv.setThuTuNV(rs.getInt("nv_tt"));
                                nv.setDiemTHXT(rs.getDouble("diem_thxt"));
                                nv.setDiemCong(rs.getDouble("diem_cong"));
                                nv.setDiemUTQD(rs.getDouble("diem_utqd"));
                                nv.setDiemXetTuyen(rs.getDouble("diem_xettuyen"));
                                nv.setKetQua(rs.getString("nv_ketqua"));
                                nv.setKeys(rs.getString("nv_keys"));
                                nv.setPhuongThuc(rs.getString("tt_phuongthuc"));
                                nv.setToHopMon(rs.getString("tt_thm"));

                                list.add(nv);
                        }

                        ps.close();

                } catch (Exception e) {

                        e.printStackTrace();
                }

                return list;
        }

        // =========================================================
        // DELETE
        // =========================================================
        public boolean delete(int id) {

                try {

                        String sql = """
                                        DELETE FROM xt_nguyenvongxettuyen
                                        WHERE idnv = ?
                                        """;

                        PreparedStatement ps = conn.prepareStatement(sql);

                        ps.setInt(1, id);

                        boolean ok = ps.executeUpdate() > 0;

                        ps.close();

                        return ok;

                } catch (Exception e) {

                        e.printStackTrace();
                }

                return false;
        }
}