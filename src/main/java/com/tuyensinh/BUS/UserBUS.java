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
}