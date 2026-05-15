package com.tuyensinh.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.tuyensinh.DTO.NganhDTO;
import com.tuyensinh.config.HibernateUtil;

public class NganhDAO {

    // ====== DTO RESULT (có số NV) ======
    public List<Object[]> getAllWithSoNV() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String sql = """
                SELECT 
                    n.idnganh,
                    n.manganh,
                    n.tennganh,
                    n.n_tohopgoc,
                    n.n_chitieu,
                    n.n_diemsan,
                    n.n_diemtrungtuyen,
                    n.n_dgnl,
                    n.n_thpt,
                    n.n_vsat,
                    n.n_tuyenthang,
                    COUNT(v.idnv) AS so_nv
                FROM xt_nganh n
                LEFT JOIN xt_nguyenvongxettuyen v 
                    ON n.manganh COLLATE utf8_unicode_ci = v.nv_manganh
                GROUP BY n.idnganh
            """;

            return session.createNativeQuery(sql).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ====== Hibernate CRUD giữ nguyên ======
    public List<NganhDTO> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM NganhDTO", NganhDTO.class).list();
        }
    }

    public boolean save(NganhDTO nganh) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(nganh);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public boolean update(NganhDTO nganh) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(nganh);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public boolean delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            NganhDTO n = session.get(NganhDTO.class, id);
            if (n != null) session.remove(n);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }
    /**
     * Lấy ngành theo mã
     */
    public NganhDTO getByMa(String maNganh) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM NganhDTO WHERE maNganh = :ma", NganhDTO.class)
                    .setParameter("ma", maNganh)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}