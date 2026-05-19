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

                COALESCE(dc.diemCC, 0) AS diemCC,
                COALESCE(dc.diemUtxt, 0) AS diemUtxt,
                COALESCE(dc.diemTong, 0) AS diemTong,

                nv.nv_ketqua,
                nv.nv_keys,
                nv.tt_phuongthuc,
                nv.tt_thm

            FROM xt_nguyenvongxettuyen nv

            LEFT JOIN xt_diemcongxettuyen dc

                ON nv.nn_cccd = dc.ts_cccd

                AND nv.nv_manganh = dc.manganh

                AND nv.tt_thm = dc.matohop

            ORDER BY nv.idnv ASC
        """;

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ResultSet rs =
                ps.executeQuery();

        while (rs.next()) {

            NguyenVongDTO nv =
                    new NguyenVongDTO();

            nv.setIdnv(
                    rs.getInt("idnv")
            );

            nv.setCccd(
                    rs.getString("nn_cccd")
            );

            nv.setMaNganh(
                    rs.getString("nv_manganh")
            );

            nv.setThuTuNV(
                    rs.getInt("nv_tt")
            );

            // =========================
            // ĐIỂM THXT
            // =========================
            nv.setDiemTHXT(
                    rs.getDouble("diem_thxt")
            );

            // =========================
            // ĐIỂM CỘNG
            // =========================
            nv.setDiemCong(
                    rs.getDouble("diemCC")
            );

            // =========================
            // ĐIỂM ƯU TIÊN
            // =========================
            nv.setDiemUTQD(
                    rs.getDouble("diemUtxt")
            );

            // =========================
            // ĐIỂM XÉT TUYỂN
            // =========================
            nv.setDiemXetTuyen(
                    rs.getDouble("diemTong")
            );

            nv.setKetQua(
                    rs.getString("nv_ketqua")
            );

            nv.setKeys(
                    rs.getString("nv_keys")
            );

            nv.setPhuongThuc(
                    rs.getString("tt_phuongthuc")
            );

            nv.setToHopMon(
                    rs.getString("tt_thm")
            );

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

            PreparedStatement ps =
                    conn.prepareStatement(sql);

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

            // DEBUG
            System.out.println("========== SAVE NV ==========");
            System.out.println("CCCD: " + nv.getCccd());
            System.out.println("Ngành: " + nv.getMaNganh());
            System.out.println("THXT: " + nv.getDiemTHXT());
            System.out.println("UTQD: " + nv.getDiemUTQD());
            System.out.println("Cộng: " + nv.getDiemCong());
            System.out.println("XT: " + nv.getDiemXetTuyen());
            System.out.println("=============================");

            boolean ok =
                    ps.executeUpdate() > 0;

            ps.close();

            return ok;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // DELETE
    // =========================================================
    public boolean delete(int id) {

        try {

            String sql =
                    """
                    DELETE FROM xt_nguyenvongxettuyen
                    WHERE idnv = ?
                    """;

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, id);

            boolean ok =
                    ps.executeUpdate() > 0;

            ps.close();

            return ok;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}