package com.tuyensinh.BUS;

import com.tuyensinh.DAO.UserDAO;
import com.tuyensinh.DTO.UserDTO;

import java.util.List;

public class UserBUS {

    private UserDAO userDAO = new UserDAO();

    private UserDTO currentUser;

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

    public boolean insert(UserDTO user) {

        if (currentUser == null) {
            System.out.println("Bạn chưa đăng nhập!");
            return false;
        }

        if (userDAO.getByUsername(user.getUsername()) != null) {
            System.out.println("Username đã tồn tại!");
            return false;
        }

        return userDAO.insert(user);
    }

    public UserDTO getUser(String username) {
        if (currentUser == null) {
            System.out.println("Bạn chưa đăng nhập!");
            return null;
        }

        return userDAO.getByUsername(username);
    }

    public boolean update(UserDTO user) {
        if (currentUser == null) {
            System.out.println("Bạn chưa đăng nhập!");
            return false;
        }
        return userDAO.update(user);
    }

    public boolean delete(int id) {
        if (currentUser == null) {
            System.out.println("Bạn chưa đăng nhập!");
            return false;
        }
        return userDAO.delete(id);
    }

    public boolean changePassword(int id, String newPassword) {
        if (currentUser == null) {
            System.out.println("Bạn chưa đăng nhập!");
            return false;
        }
        UserDTO user = userDAO.getById(id);
        if (user == null) {
            return false;
        }
        user.setPassword(newPassword);
        return userDAO.update(user);
    }

    public boolean changeRole(int id, UserDTO.Role newRole) {
        if (currentUser == null) {
            System.out.println("Bạn chưa đăng nhập!");
            return false;
        }
        UserDTO user = userDAO.getById(id);
        if (user == null) {
            return false;
        }
        user.setRole(newRole);
        return userDAO.update(user);
    }

    public boolean toggleStatus(int id) {
        if (currentUser == null) {
            System.out.println("Bạn chưa đăng nhập!");
            return false;
        }
        UserDTO user = userDAO.getById(id);
        if (user == null) {
            return false;
        }
        user.setStatus(user.getStatus() == null || !user.getStatus());
        return userDAO.update(user);
    }
}