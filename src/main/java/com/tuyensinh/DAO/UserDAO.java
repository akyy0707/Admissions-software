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

    public List<UserDTO> getPage(int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM UserDTO", UserDTO.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .list();
        } catch (Exception e) {
            System.out.println("Lỗi getPage: " + e.getMessage());
            return List.of();
        }
    }

    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long result = session.createQuery(
                    "SELECT COUNT(u.id) FROM UserDTO u", Long.class).uniqueResult();
            return result != null ? result : 0;
        } catch (Exception e) {
            System.out.println("Lỗi count: " + e.getMessage());
            return 0;
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

    public UserDTO getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(UserDTO.class, id);
        } catch (Exception e) {
            System.out.println("Lỗi getById: " + e.getMessage());
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

            if (user == null || !user.getPassword().equals(password)) {
                return null;
            }

            if (user.getRole() != UserDTO.Role.ADMIN) {
                System.out.println("Chỉ ADMIN mới có thể đăng nhập!");
                return null;
            }

            if (user.getStatus() == null || !user.getStatus()) {
                System.out.println("Tài khoản đã bị vô hiệu hóa!");
                return null;
            }

            return user;

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