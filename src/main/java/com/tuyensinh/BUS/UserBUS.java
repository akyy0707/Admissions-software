package com.tuyensinh.BUS;

import com.tuyensinh.DAO.UserDAO;
import com.tuyensinh.DTO.UserDTO;

import java.util.List;

public class UserBUS {

    private UserDAO userDAO = new UserDAO();

    public UserDTO login(String username, String password) {
        UserDTO user = userDAO.login(username, password);

        if (user == null) {
            System.out.println("Sai tài khoản hoặc mật khẩu!");
            return null;
        }

        if (user.getRole() != UserDTO.Role.ADMIN) {
            System.out.println("Bạn không có quyền đăng nhập!");
            return null;
        }

        return user;
    }

    public List<UserDTO> getAll() {
        return userDAO.getAll();
    }

    public List<UserDTO> getPage(int page, int size) {
        return userDAO.getPage(page, size);
    }

    public long countUsers() {
        return userDAO.count();
    }

    // Đã gỡ bỏ chặn currentUser == null
    public boolean insert(UserDTO user) {
        if (userDAO.getByUsername(user.getUsername()) != null) {
            System.out.println("Username đã tồn tại!");
            return false;
        }
        return userDAO.insert(user);
    }

    // Đã gỡ bỏ chặn currentUser == null
    public UserDTO getUser(String username) {
        return userDAO.getByUsername(username);
    }

    // Đã gỡ bỏ chặn currentUser == null
    public boolean update(UserDTO user) {
        return userDAO.update(user);
    }

    // Đã gỡ bỏ chặn currentUser == null
    public boolean delete(int id) {
        return userDAO.delete(id);
    }

    // Đã gỡ bỏ chặn currentUser == null
    public boolean changePassword(int id, String newPassword) {
        UserDTO user = userDAO.getById(id);
        if (user == null) {
            return false;
        }
        user.setPassword(newPassword);
        return userDAO.update(user);
    }

    // Đã gỡ bỏ chặn currentUser == null
    public boolean changeRole(int id, UserDTO.Role newRole) {
        UserDTO user = userDAO.getById(id);
        if (user == null) {
            return false;
        }
        user.setRole(newRole);
        return userDAO.update(user);
    }

    // Đã gỡ bỏ chặn currentUser == null
    public boolean toggleStatus(int id) {
        UserDTO user = userDAO.getById(id);
        if (user == null) {
            return false;
        }
        user.setStatus(user.getStatus() == null || !user.getStatus());
        return userDAO.update(user);
    }

    public List<UserDTO> search(String keyword) {
        return userDAO.search(keyword);
    }
}