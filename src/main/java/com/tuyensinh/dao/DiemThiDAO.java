package com.tuyensinh.dao;

import java.sql.*;
import java.util.List;

import com.tuyensinh.config.DB;
import com.tuyensinh.dto.*;;

public class DiemThiDAO {

    public void insertBatch(List<DiemThiDTO> list) {
        String sql = "INSERT INTO xt_diemthixettuyen " +
                "(cccd, `TO`, VA, LI, HO, SI, SU, DI, N1_THI, KTPL, TI, CNCN, CNNN, NK1, NK2) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConn();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            int batchSize = 500;
            int count = 0;

            for (DiemThiDTO d : list) {

                ps.setString(1, d.getCccd());
                ps.setDouble(2, d.getTo());
                ps.setDouble(3, d.getVa());
                ps.setDouble(4, d.getLi());
                ps.setDouble(5, d.getHo());
                ps.setDouble(6, d.getSi());
                ps.setDouble(7, d.getSu());
                ps.setDouble(8, d.getDi());

                ps.setDouble(9, d.getN1_thi());

                ps.setDouble(10, d.getKtpl());
                ps.setDouble(11, d.getTi());
                ps.setDouble(12, d.getCncn());
                ps.setDouble(13, d.getCnnn());

                ps.setDouble(14, d.getNk1());
                ps.setDouble(15, d.getNk2());

                ps.addBatch();
                count++;

                if (count % batchSize == 0) {
                    ps.executeBatch();
                }
            }

            ps.executeBatch();
            conn.commit();

            System.out.println("DAO: Insert thành công " + count + " dòng");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}