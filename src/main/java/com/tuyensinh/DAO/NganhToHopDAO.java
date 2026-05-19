package com.tuyensinh.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.tuyensinh.DTO.NganhToHopDTO;
import com.tuyensinh.config.HibernateUtil;

public class NganhToHopDAO {

    // =========================================================
    // GET ALL
    // =========================================================
    public List<NganhToHopDTO> getAll() {

        try (
                Session session =
                        HibernateUtil
                                .getSessionFactory()
                                .openSession()
        ) {

            return session.createQuery(
                    "FROM NganhToHopDTO",
                    NganhToHopDTO.class
            ).list();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // GET BY MA NGANH
    // =========================================================
    public List<NganhToHopDTO> getByMaNganh(String maNganh) {

        try (
                Session session =
                        HibernateUtil
                                .getSessionFactory()
                                .openSession()
        ) {

            String hql = """
                FROM NganhToHopDTO
                WHERE maNganh = :mn
            """;

            return session.createQuery(
                    hql,
                    NganhToHopDTO.class
            )
            .setParameter("mn", maNganh)
            .list();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // GET BY NGANH + TO HOP
    // =========================================================
    public NganhToHopDTO getByNganhAndToHop(
            String maNganh,
            String maToHop
    ) {

        try (
                Session session =
                        HibernateUtil
                                .getSessionFactory()
                                .openSession()
        ) {

            String hql = """
                FROM NganhToHopDTO
                WHERE maNganh = :mn
                AND maToHop = :mt
            """;

            return session.createQuery(
                            hql,
                            NganhToHopDTO.class
                    )
                    .setParameter("mn", maNganh)
                    .setParameter("mt", maToHop)
                    .uniqueResult();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // SAVE
    // =========================================================
    public boolean save(NganhToHopDTO dto) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtil
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            session.persist(dto);

            tx.commit();

            return true;

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // UPDATE
    // =========================================================
    public boolean update(NganhToHopDTO dto) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtil
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            session.merge(dto);

            tx.commit();

            return true;

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // DELETE
    // =========================================================
    public boolean delete(int id) {

        Transaction tx = null;

        try (
                Session session =
                        HibernateUtil
                                .getSessionFactory()
                                .openSession()
        ) {

            tx = session.beginTransaction();

            NganhToHopDTO dto =
                    session.get(NganhToHopDTO.class, id);

            if (dto != null) {

                session.remove(dto);

                tx.commit();

                return true;
            }

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            e.printStackTrace();
        }

        return false;
    }
}