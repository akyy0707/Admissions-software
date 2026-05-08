package com.tuyensinh.DAO;

import com.tuyensinh.DTO.NganhToHopDTO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.tuyensinh.config.HibernateUtil;

import java.util.List;

public class NganhToHopDAO {
    public List<NganhToHopDTO> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM NganhToHopDTO", NganhToHopDTO.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean save(NganhToHopDTO mapping) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(mapping);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int idMapping) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            NganhToHopDTO mapping = session.get(NganhToHopDTO.class, idMapping);
            if (mapping != null) {
                session.delete(mapping);
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