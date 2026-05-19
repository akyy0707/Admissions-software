package com.tuyensinh.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuyensinh.DTO.DiemThiDTO;
import com.tuyensinh.config.DB;

public class DiemThiDAO {

    // CACHE tránh query DB nhiều lần
    private static final Map<String, DiemThiDTO> CACHE = new HashMap<>();

    // =========================================================
    // INSERT BATCH
    // =========================================================
    public void insertBatch(List<DiemThiDTO> list) {

        String sql = """
            INSERT INTO xt_diemthixettuyen
            (
                cccd,
                TO,
                VA,
                LI,
                HO,
                SI,
                SU,
                DI,
                N1_THI,
                N1_CC,
                NL1,
                KTPL,
                TI,
                CNCN,
                CNNN,
                NK1,
                NK2
            )
            VALUES
            (
                ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
            )
        """;

        try (
                Connection conn = DB.getConn();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            conn.setAutoCommit(false);

            int batchSize = 1000;
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
                ps.setDouble(10, d.getN1_cc());

                ps.setDouble(11, d.getNl1());

                ps.setDouble(12, d.getKtpl());
                ps.setDouble(13, d.getTi());

                ps.setDouble(14, d.getCncn());
                ps.setDouble(15, d.getCnnn());

                ps.setDouble(16, d.getNk1());
                ps.setDouble(17, d.getNk2());

                ps.addBatch();

                count++;

                if (count % batchSize == 0) {

                    ps.executeBatch();
                    conn.commit();

                    System.out.println("Inserted: " + count);
                }
            }

            ps.executeBatch();
            conn.commit();

            System.out.println("DONE: " + count);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // GET BY CCCD
    // =========================================================
    public DiemThiDTO getByCCCD(String cccd) {

        // CACHE
        if (CACHE.containsKey(cccd)) {
            return CACHE.get(cccd);
        }

        String sql = """
            SELECT *
            FROM xt_diemthixettuyen
            WHERE cccd = ?
            LIMIT 1
        """;

        try (
                Connection conn = DB.getConn();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, cccd);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    DiemThiDTO d = map(rs);

                    CACHE.put(cccd, d);

                    return d;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // GET ALL
    // =========================================================
    public List<DiemThiDTO> getAll() {

        List<DiemThiDTO> list = new ArrayList<>();

        String sql = """
            SELECT *
            FROM xt_diemthixettuyen
        """;

        try (
                Connection conn = DB.getConn();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                DiemThiDTO d = map(rs);

                list.add(d);

                CACHE.put(d.getCccd(), d);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // DELETE
    // =========================================================
    public boolean delete(String cccd) {

        String sql = """
            DELETE FROM xt_diemthixettuyen
            WHERE cccd = ?
        """;

        try (
                Connection conn = DB.getConn();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, cccd);

            CACHE.remove(cccd);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // MAP RESULTSET -> DTO
    // =========================================================
   // =========================================================
// MAP RESULTSET -> DTO
// =========================================================
private DiemThiDTO map(ResultSet rs) throws Exception {

    DiemThiDTO d = new DiemThiDTO();

    d.setIddiemthi(rs.getInt("iddiemthi"));

    d.setSobaodanh(rs.getString("sobaodanh"));

    d.setD_phuongthuc(rs.getString("d_phuongthuc"));

    d.setCccd(rs.getString("cccd"));

    // =========================
    // MON THI
    // =========================

    d.setTo(getDouble(rs, "TO"));
    d.setVa(getDouble(rs, "VA"));
    d.setLi(getDouble(rs, "LI"));
    d.setHo(getDouble(rs, "HO"));
    d.setSi(getDouble(rs, "SI"));
    d.setSu(getDouble(rs, "SU"));
    d.setDi(getDouble(rs, "DI"));

    d.setN1_thi(getDouble(rs, "N1_THI"));
    d.setN1_cc(getDouble(rs, "N1_CC"));

    d.setNl1(getDouble(rs, "NL1"));

    d.setKtpl(getDouble(rs, "KTPL"));
    d.setTi(getDouble(rs, "TI"));

    d.setCncn(getDouble(rs, "CNCN"));
    d.setCnnn(getDouble(rs, "CNNN"));

    d.setNk1(getDouble(rs, "NK1"));
    d.setNk2(getDouble(rs, "NK2"));

   

    return d;
}
// =========================================================
// SAFE GET DOUBLE
// =========================================================
private double getDouble(ResultSet rs, String col) {

    try {

        Object obj = rs.getObject(col);

        if (obj == null) {
            return 0;
        }

        return Double.parseDouble(obj.toString());

    } catch (Exception e) {

        System.out.println("LOI DOC COT: " + col);

        return 0;
    }
}
}