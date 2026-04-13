package com.tuyensinh.BUS;

import com.tuyensinh.DAO.ThiSinhDAO;
import com.tuyensinh.DTO.ThiSinhDTO;

import java.util.List;

public class ThiSinhBUS {

    private ThiSinhDAO thiSinhDAO = new ThiSinhDAO();

    public boolean insert(ThiSinhDTO ts) {
        return thiSinhDAO.insert(ts);
    }

    public boolean assignUser(ThiSinhDTO ts, com.tuyensinh.DTO.UserDTO user) {
        ts.setUser(user);
        user.setThiSinh(ts);

        return thiSinhDAO.update(ts);
    }

    public List<ThiSinhDTO> getPage(int page, int size) {
        return thiSinhDAO.getPage(page, size);
    }

    public List<ThiSinhDTO> search(String key, int page, int size) {
        return thiSinhDAO.search(key, page, size);
    }

    public long count() {
        return thiSinhDAO.count();
    }

    public long countSearch(String key) {
        return thiSinhDAO.countSearch(key);
    }

    public ThiSinhDTO getByCCCD(String cccd) {
        return thiSinhDAO.getByCCCD(cccd);
    }

    public boolean update(ThiSinhDTO ts) {
        return thiSinhDAO.update(ts);
    }

    public boolean delete(int id) {
        return thiSinhDAO.delete(id);
    }

    public ThiSinhDTO getById(int id) {
        return thiSinhDAO.getById(id);
    }
}