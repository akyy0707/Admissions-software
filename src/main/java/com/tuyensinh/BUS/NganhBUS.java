package com.tuyensinh.BUS;

import java.util.List;

import com.tuyensinh.DAO.NganhDAO;
import com.tuyensinh.DTO.NganhDTO;

public class NganhBUS {

    private NganhDAO nganhDAO;

    public NganhBUS() {
        nganhDAO = new NganhDAO();
    }

    // ================= GET =================
    public List<NganhDTO> getAll() {
        return nganhDAO.getAll();
    }

    // ================= STATISTIC =================
    public int getTongNganh() {

        List<NganhDTO> list = getAll();

        return list.size();
    }

    public int getTongChiTieu() {

        int tong = 0;

        List<NganhDTO> list = getAll();

        for (NganhDTO n : list) {
            tong += n.getChiTieu();
        }

        return tong;
    }

    public int getTongDGNL() {

        int tong = 0;

        List<NganhDTO> list = getAll();

        for (NganhDTO n : list) {

            if ("1".equals(n.getDgnl())) {
                tong++;
            }
        }

        return tong;
    }

    public int getTongTuyenThang() {

        int tong = 0;

        List<NganhDTO> list = getAll();

        for (NganhDTO n : list) {

            if ("1".equals(n.getTuyenThang())) {
                tong++;
            }
        }

        return tong;
    }

    public int getTongVSAT() {

        int tong = 0;

        List<NganhDTO> list = getAll();

        for (NganhDTO n : list) {

            if ("1".equals(n.getVsat())) {
                tong++;
            }
        }

        return tong;
    }

    public int getTongTHPT() {

        int tong = 0;

        List<NganhDTO> list = getAll();

        for (NganhDTO n : list) {

            if ("1".equals(n.getThpt())) {
                tong++;
            }
        }

        return tong;
    }

    // ================= ADD =================
    public String addNganh(NganhDTO nganh) {

        if (nganh.getMaNganh() == null
                || nganh.getMaNganh().trim().isEmpty()) {

            return "Mã ngành không được để trống!";
        }

        if (nganh.getTenNganh() == null
                || nganh.getTenNganh().trim().isEmpty()) {

            return "Tên ngành không được để trống!";
        }

        boolean isSuccess = nganhDAO.save(nganh);

        return isSuccess
                ? "Thêm ngành thành công!"
                : "Thêm thất bại!";
    }

    // ================= UPDATE =================
    public String updateNganh(NganhDTO nganh) {

        boolean isSuccess = nganhDAO.update(nganh);

        return isSuccess
                ? "Cập nhật ngành thành công!"
                : "Cập nhật thất bại!";
    }

    // ================= DELETE =================
    public String deleteNganh(int idNganh) {

        boolean isSuccess = nganhDAO.delete(idNganh);

        return isSuccess
                ? "Xóa ngành thành công!"
                : "Xóa thất bại!";
    }

    public List<Object[]> getAllWithSoNV() {
        return nganhDAO.getAllWithSoNV();
    }
}
