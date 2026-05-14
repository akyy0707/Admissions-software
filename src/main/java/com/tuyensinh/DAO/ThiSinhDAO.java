package com.tuyensinh.DAO;

import com.tuyensinh.DTO.ThiSinhDTO;
import com.tuyensinh.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ThiSinhDAO {

    public boolean insert(ThiSinhDTO ts) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.persist(ts);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            System.out.println("Insert ThiSinh lỗi: " + e.getMessage());
            return false;
        }
    }

    public List<ThiSinhDTO> getPage(int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "SELECT t FROM ThiSinhDTO t LEFT JOIN FETCH t.user";

            return session.createQuery(hql, ThiSinhDTO.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .list();

        } catch (Exception e) {
            System.out.println("Lỗi getPage: " + e.getMessage());
            return List.of();
        }
    }

    public List<ThiSinhDTO> search(String key, int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "SELECT t FROM ThiSinhDTO t LEFT JOIN FETCH t.user WHERE " +
                    "LOWER(t.cccd) LIKE :key OR " +
                    "LOWER(t.ho) LIKE :key OR " +
                    "LOWER(t.ten) LIKE :key";

            return session.createQuery(hql, ThiSinhDTO.class)
                    .setParameter("key", "%" + key.toLowerCase() + "%")
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .list();

        } catch (Exception e) {
            System.out.println("Lỗi search: " + e.getMessage());
            return List.of();
        }
    }

    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Long result = session.createQuery(
                    "SELECT COUNT(t.id) FROM ThiSinhDTO t", Long.class).uniqueResult();

            return result != null ? result : 0;

        } catch (Exception e) {
            System.out.println("Lỗi count: " + e.getMessage());
            return 0;
        }
    }

    // 🔹
    public boolean update(ThiSinhDTO ts) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            session.merge(ts);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            System.out.println("Lỗi update: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            ThiSinhDTO ts = session.get(ThiSinhDTO.class, id);
            if (ts != null) {

                // 🔥 xử lý OneToOne
                if (ts.getUser() != null) {
                    ts.getUser().setThiSinh(null);
                    ts.setUser(null);
                }

                session.remove(ts);
            }

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            System.out.println("Lỗi delete: " + e.getMessage());
            return false;
        }
    }

    public ThiSinhDTO getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "SELECT t FROM ThiSinhDTO t LEFT JOIN FETCH t.user WHERE t.id = :id";

            return session.createQuery(hql, ThiSinhDTO.class)
                    .setParameter("id", id)
                    .uniqueResult();

        } catch (Exception e) {
            System.out.println("Lỗi getById: " + e.getMessage());
            return null;
        }
    }

    public ThiSinhDTO getByCCCD(String cccd) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "SELECT t FROM ThiSinhDTO t LEFT JOIN FETCH t.user WHERE t.cccd = :cccd";

            return session.createQuery(hql, ThiSinhDTO.class)
                    .setParameter("cccd", cccd)
                    .uniqueResult();

        } catch (Exception e) {
            System.out.println("Lỗi getByCCCD: " + e.getMessage());
            return null;
        }
    }

    public long countSearch(String key) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "SELECT COUNT(t.id) FROM ThiSinhDTO t WHERE " +
                    "LOWER(t.cccd) LIKE :key OR " +
                    "LOWER(t.ho) LIKE :key OR " +
                    "LOWER(t.ten) LIKE :key";

            Long result = session.createQuery(hql, Long.class)
                    .setParameter("key", "%" + key.toLowerCase() + "%")
                    .uniqueResult();

            return result != null ? result : 0;

        } catch (Exception e) {
            System.out.println("Lỗi countSearch: " + e.getMessage());
            return 0;
        }
    }

    public Map<String, Long> countByDoiTuong() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> rows = session.createQuery(
                    "SELECT t.doiTuong, COUNT(t.id) FROM ThiSinhDTO t GROUP BY t.doiTuong",
                    Object[].class).list();

            Map<String, Long> result = new LinkedHashMap<>();
            for (Object[] row : rows) {
                String key = row[0] != null ? row[0].toString() : "Chua ro";
                Long value = row[1] != null ? (Long) row[1] : 0L;
                result.put(key, value);
            }
            return result;
        } catch (Exception e) {
            System.out.println("Loi countByDoiTuong: " + e.getMessage());
            return Map.of();
        }
    }

    public Map<String, Long> countByKhuVuc() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> rows = session.createQuery(
                    "SELECT t.khuVuc, COUNT(t.id) FROM ThiSinhDTO t GROUP BY t.khuVuc",
                    Object[].class).list();

            Map<String, Long> result = new LinkedHashMap<>();
            for (Object[] row : rows) {
                String key = row[0] != null ? row[0].toString() : "Chua ro";
                Long value = row[1] != null ? (Long) row[1] : 0L;
                result.put(key, value);
            }
            return result;
        } catch (Exception e) {
            System.out.println("Loi countByKhuVuc: " + e.getMessage());
            return Map.of();
        }
    }
}