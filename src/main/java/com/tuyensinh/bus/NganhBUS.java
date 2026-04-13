package com.tuyensinh.bus;

import com.tuyensinh.dao.NganhDAO;
import com.tuyensinh.dto.NganhDTO;
import java.util.List;

public class NganhBUS {
    private NganhDAO nganhDAO;

    public NganhBUS() {
        this.nganhDAO = new NganhDAO();
    }

    public List<NganhDTO> getAll() {
        return nganhDAO.getAll();
    }

    public String addNganh(NganhDTO nganh) {
        if (nganh.getMaNganh() == null || nganh.getMaNganh().trim().isEmpty()) {
            return "Mã ngành không được để trống!";
        }
        if (nganh.getTenNganh() == null || nganh.getTenNganh().trim().isEmpty()) {
            return "Tên ngành không được để trống!";
        }
        boolean isSuccess = nganhDAO.save(nganh);
        return isSuccess ? "Thêm ngành thành công!" : "Thêm thất bại. Mã ngành có thể đã tồn tại!";
    }

    public String updateNganh(NganhDTO nganh) {
        boolean isSuccess = nganhDAO.update(nganh);
        return isSuccess ? "Cập nhật ngành thành công!" : "Cập nhật thất bại!";
    }

    public String deleteNganh(int idNganh) {
        boolean isSuccess = nganhDAO.delete(idNganh);
        return isSuccess ? "Xóa ngành thành công!" : "Xóa thất bại (ngành đang được sử dụng)!";
    }
}