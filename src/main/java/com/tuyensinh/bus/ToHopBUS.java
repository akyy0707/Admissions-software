package com.tuyensinh.bus;

import com.tuyensinh.dao.ToHopDAO;
import com.tuyensinh.dto.ToHopDTO;
import java.util.List;

public class ToHopBUS {
    private ToHopDAO toHopDAO;

    public ToHopBUS() {
        this.toHopDAO = new ToHopDAO();
    }

    public List<ToHopDTO> getAll() {
        return toHopDAO.getAll();
    }

    public String addToHop(ToHopDTO toHop) {
        if (toHop.getMaToHop() == null || toHop.getMaToHop().trim().isEmpty()) {
            return "Mã tổ hợp không được để trống!";
        }
        boolean isSuccess = toHopDAO.save(toHop);
        return isSuccess ? "Thêm tổ hợp thành công!" : "Thêm thất bại!";
    }

    public String updateToHop(ToHopDTO toHop) {
        boolean isSuccess = toHopDAO.update(toHop);
        return isSuccess ? "Cập nhật thành công!" : "Cập nhật thất bại!";
    }

    public String deleteToHop(int idToHop) {
        boolean isSuccess = toHopDAO.delete(idToHop);
        return isSuccess ? "Xóa thành công!" : "Xóa thất bại!";
    }
}