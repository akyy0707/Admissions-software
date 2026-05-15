package com.tuyensinh.BUS;

import java.sql.Connection;
import java.util.List;

import com.tuyensinh.DAO.NguyenVongDAO;
import com.tuyensinh.DTO.NguyenVongDTO;

public class NguyenVongBUS {

    private NguyenVongDAO dao;

    public NguyenVongBUS(Connection conn) {
        dao = new NguyenVongDAO(conn);
    }

    // ================= GET =================
    public List<NguyenVongDTO> getAll() {
        return dao.getAll();
    }

    // ================= ADD =================
    public String add(NguyenVongDTO nv) {

        if (nv.getCccd() == null || nv.getCccd().isEmpty()) {
            return "CCCD không được rỗng!";
        }

        if (nv.getMaNganh() == null || nv.getMaNganh().isEmpty()) {
            return "Mã ngành không được rỗng!";
        }

        if (nv.getThuTuNV() <= 0) {
            return "Thứ tự nguyện vọng không hợp lệ!";
        }

        boolean ok = dao.insert(nv);

        return ok ? "Thêm nguyện vọng thành công!" : "Thêm thất bại!";
    }

    // ================= DELETE =================
    public String delete(int id) {

        boolean ok = dao.delete(id);

        return ok ? "Xóa thành công!" : "Xóa thất bại!";
    }
}