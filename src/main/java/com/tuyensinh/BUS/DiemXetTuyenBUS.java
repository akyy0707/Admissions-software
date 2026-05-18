package com.tuyensinh.BUS;

import java.util.List;

import com.tuyensinh.DAO.DiemXetTuyenDAO;
import com.tuyensinh.DTO.DiemXetTuyenDTO;

public class DiemXetTuyenBUS {

    private DiemXetTuyenDAO dao = new DiemXetTuyenDAO();

    // CACHE để giảm lag UI
    private List<DiemXetTuyenDTO> cache;

    public List<DiemXetTuyenDTO> getAll() {

        if (cache == null) {
            cache = dao.getAll();
        }

        return cache;
    }

    // refresh khi cần
    public void refresh() {
        cache = dao.getAll();
    }
}