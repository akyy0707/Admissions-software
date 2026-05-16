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

    // ================= GET ALL =================
    public List<NguyenVongDTO> getAll() {

        List<NguyenVongDTO> list = new ArrayList<>();

        try {

            String sql = "SELECT * FROM xt_nguyenvong";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                NguyenVongDTO nv = new NguyenVongDTO();

                nv.setIdnv(rs.getInt("idnv"));
                nv.setCccd(rs.getString("nn_cccd"));
                nv.setMaNganh(rs.getString("nv_manganh"));
                nv.setThuTuNV(rs.getInt("nv_tt"));

                nv.setDiemTHXT(rs.getDouble("diem_thxt"));
                nv.setDiemUTQD(rs.getDouble("diem_utqd"));
                nv.setDiemCong(rs.getDouble("diem_cong"));
                nv.setDiemXetTuyen(rs.getDouble("diem_xettuyen"));

                nv.setKetQua(rs.getString("nv_ketqua"));
                nv.setKeys(rs.getString("nv_keys"));

                nv.setPhuongThuc(rs.getString("tt_phuongthuc"));
                nv.setToHopMon(rs.getString("tt_thm"));

                list.add(nv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= INSERT =================
    public boolean insert(NguyenVongDTO nv) {

        try {

            String sql = "INSERT INTO xt_nguyenvong " +
                    "(nn_cccd, nv_manganh, nv_tt, " +
                    "diem_thxt, diem_utqd, diem_cong, diem_xettuyen, " +
                    "nv_ketqua, nv_keys, tt_phuongthuc, tt_thm) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= DELETE =================
    public boolean delete(int id) {

        try {

            String sql = "DELETE FROM xt_nguyenvong WHERE idnv = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}