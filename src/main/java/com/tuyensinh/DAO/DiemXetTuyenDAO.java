package com.tuyensinh.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.tuyensinh.DTO.DiemXetTuyenDTO;
import com.tuyensinh.config.HibernateUtil;

public class DiemXetTuyenDAO {

    public List<DiemXetTuyenDTO> getAll() {

        List<DiemXetTuyenDTO> list = new ArrayList<>();

        String sql = """
            SELECT 
                t.cccd,
                t.ho,
                t.ten,
                v.nv_tt,
                v.tt_thm,
                v.diem_thxt,
                v.diem_cong,
                v.diem_utqd,
                v.diem_xettuyen
            FROM xt_nguyenvongxettuyen v
            STRAIGHT_JOIN xt_thisinhxettuyen25 t 
                ON t.cccd = v.nn_cccd
            ORDER BY v.diem_xettuyen DESC
            LIMIT 100
        """;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            @SuppressWarnings("unchecked")
            List<Object[]> rows = session.createNativeQuery(sql).list();

            for (Object[] r : rows) {

                list.add(new DiemXetTuyenDTO(
                        r[0] == null ? "" : r[0].toString(),
                        r[1] == null ? "" : r[1].toString(),
                        r[2] == null ? "" : r[2].toString(),
                        r[3] == null ? "" : r[3].toString(),
                        r[4] == null ? "" : r[4].toString(),
                        r[5] == null ? 0 : Double.parseDouble(r[5].toString()),
                        r[6] == null ? 0 : Double.parseDouble(r[6].toString()),
                        r[7] == null ? 0 : Double.parseDouble(r[7].toString()),
                        r[8] == null ? 0 : Double.parseDouble(r[8].toString())
                ));
            }
        }

        return list;
    }
}