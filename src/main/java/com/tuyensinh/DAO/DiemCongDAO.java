package com.tuyensinh.DAO;

import com.tuyensinh.DTO.DiemCongDTO;
import java.sql.*;
import java.util.List;

public class DiemCongDAO {

    private Connection conn;

    public DiemCongDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertBatch(List<DiemCongDTO> list) {
        String sql = "INSERT INTO xt_diemcongxettuyen(ts_cccd, manganh, mathop, phuongthuc, diemCC, diemUtxt, diemTong, dc_keys) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            for (DiemCongDTO dc : list) {
                ps.setString(1, dc.getCccd());
                ps.setString(2, dc.getMaNganh());
                ps.setString(3, dc.getMaToHop());
                ps.setString(4, dc.getPhuongThuc());
                ps.setDouble(5, dc.getDiemCC());
                ps.setDouble(6, dc.getDiemUuTien());
                ps.setDouble(7, dc.getDiemTong());
                ps.setString(8, dc.getDcKeys());

                ps.addBatch();
            }

            ps.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}