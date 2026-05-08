package com.tuyensinh.DAO;

import com.tuyensinh.DTO.QuyDoiDTO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.tuyensinh.config.HibernateUtil;

import java.util.List;

public class QuyDoiDAO {
    public List<QuyDoiDTO> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM QuyDoiDTO", QuyDoiDTO.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lấy quy đổi theo mã môn
     */
    public QuyDoiDTO getByMaMon(String maMon) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM QuyDoiDTO WHERE maMon = :ma", QuyDoiDTO.class)
                    .setParameter("ma", maMon)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean save(QuyDoiDTO quyDoi) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(quyDoi);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(QuyDoiDTO quyDoi) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(quyDoi);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int idQd) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            QuyDoiDTO quyDoi = session.get(QuyDoiDTO.class, idQd);
            if (quyDoi != null) {
                session.remove(quyDoi);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
}