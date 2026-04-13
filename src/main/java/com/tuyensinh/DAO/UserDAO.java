package com.tuyensinh.DAO;

import com.tuyensinh.DTO.UserDTO;
import com.tuyensinh.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDAO {

    public boolean insert(UserDTO user) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.persist(user);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            System.out.println("Insert User lỗi: " + e.getMessage());
            return false;
        }
    }

    public List<UserDTO> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM UserDTO", UserDTO.class).list();
        }
    }

    public UserDTO getByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "SELECT u FROM UserDTO u LEFT JOIN FETCH u.thiSinh WHERE u.username = :username";

            return session.createQuery(hql, UserDTO.class)
                    .setParameter("username", username)
                    .uniqueResult();

        } catch (Exception e) {
            System.out.println("Lỗi getByUsername: " + e.getMessage());
            return null;
        }
    }

    public UserDTO login(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "FROM UserDTO u WHERE u.username = :username AND u.password = :password";

            UserDTO user = session.createQuery(hql, UserDTO.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();

            if (user != null && user.getPassword().equals(password)) {
                return user;
            }

            return null;

        } catch (Exception e) {
            System.out.println("Login lỗi: " + e.getMessage());
            return null;
        }
    }

    public boolean update(UserDTO user) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.merge(user);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            System.out.println("Update User lỗi: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            UserDTO user = session.get(UserDTO.class, id);
            if (user != null) {

                if (user.getThiSinh() != null) {
                    user.getThiSinh().setUser(null);
                    user.setThiSinh(null);
                }

                session.remove(user);
            }

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            System.out.println("Delete User lỗi: " + e.getMessage());
            return false;
        }
    }
}