package com.tuyensinh.bus;

import com.tuyensinh.dao.QuyDoiDAO;
import com.tuyensinh.dto.QuyDoiDTO;
import java.util.List;

public class QuyDoiBUS {
    private QuyDoiDAO quyDoiDAO;

    public QuyDoiBUS() {
        this.quyDoiDAO = new QuyDoiDAO();
    }

    public List<QuyDoiDTO> getAll() {
        return quyDoiDAO.getAll();
    }

    public String addQuyDoi(QuyDoiDTO quyDoi) {
        if (quyDoi.getMaQuyDoi() == null || quyDoi.getMaQuyDoi().trim().isEmpty()) {
            return "Mã quy đổi không được để trống!";
        }
        boolean isSuccess = quyDoiDAO.save(quyDoi);
        return isSuccess ? "Thêm bảng quy đổi thành công!" : "Thêm thất bại!";
    }

    public String updateQuyDoi(QuyDoiDTO quyDoi) {
        boolean isSuccess = quyDoiDAO.update(quyDoi);
        return isSuccess ? "Cập nhật thành công!" : "Cập nhật thất bại!";
    }

    public String deleteQuyDoi(int idQd) {
        boolean isSuccess = quyDoiDAO.delete(idQd);
        return isSuccess ? "Xóa thành công!" : "Xóa thất bại!";
    }
}