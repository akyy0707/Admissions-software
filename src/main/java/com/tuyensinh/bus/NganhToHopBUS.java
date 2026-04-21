package com.tuyensinh.BUS;

import com.tuyensinh.DAO.NganhToHopDAO;
import com.tuyensinh.DTO.NganhToHopDTO;
import java.util.List;

public class NganhToHopBUS {
    private NganhToHopDAO nganhToHopDAO;

    public NganhToHopBUS() {
        this.nganhToHopDAO = new NganhToHopDAO();
    }

    public List<NganhToHopDTO> getAll() {
        return nganhToHopDAO.getAll();
    }

    public String addMapping(NganhToHopDTO mapping) {
        if (mapping.getMaNganh() == null || mapping.getMaToHop() == null) {
            return "Vui lòng chọn đầy đủ Ngành và Tổ hợp!";
        }
        
        // Tự động tạo chuỗi key chống trùng lặp theo DB của bạn
        String key = mapping.getMaNganh() + "_" + mapping.getMaToHop();
        mapping.setTbKeys(key);

        boolean isSuccess = nganhToHopDAO.save(mapping);
        return isSuccess ? "Đã ghép Tổ hợp vào Ngành thành công!" : "Lỗi: Tổ hợp này đã được ghép với ngành từ trước!";
    }

    public String deleteMapping(int idMapping) {
        boolean isSuccess = nganhToHopDAO.delete(idMapping);
        return isSuccess ? "Đã xóa tổ hợp khỏi ngành!" : "Xóa thất bại!";
    }
}