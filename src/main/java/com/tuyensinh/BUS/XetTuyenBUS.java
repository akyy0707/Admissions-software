package com.tuyensinh.BUS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuyensinh.DAO.DiemThiDAO;
import com.tuyensinh.DAO.NganhDAO;
import com.tuyensinh.DAO.NganhToHopDAO;
import com.tuyensinh.DAO.QuyDoiDAO;
import com.tuyensinh.DAO.ToHopDAO;
import com.tuyensinh.DTO.DiemThiDTO;
import com.tuyensinh.DTO.NganhDTO;
import com.tuyensinh.DTO.QuyDoiDTO;
import com.tuyensinh.DTO.ThiSinhDTO;
import com.tuyensinh.DTO.ToHopDTO;

/**
 * XetTuyenBUS - Business Logic cho xét tuyển
 * Tính điểm xét tuyển và so sánh chọn ngành trúng tuyển
 */
public class XetTuyenBUS {

    private DiemThiDAO diemDAO = new DiemThiDAO();
    private NganhDAO nganhDAO = new NganhDAO();
    private NganhToHopDAO nganhToHopDAO = new NganhToHopDAO();
    private ToHopDAO toHopDAO = new ToHopDAO();
    private QuyDoiDAO quyDoiDAO = new QuyDoiDAO();

    /**
     * Kết quả xét tuyển cho một thí sinh
     */
    public static class KetQuaXetTuyen {
        public String soBaoDanh;
        public String hoTen;
        public String cccd;
        public double diemXetTuyen;
        public String maNganh;
        public String tenNganh;
        public boolean trungTuyen;
        public int thuTuNguyenVong;
        public String lyDo;

        public KetQuaXetTuyen(String soBaoDanh, String hoTen, String cccd) {
            this.soBaoDanh = soBaoDanh;
            this.hoTen = hoTen;
            this.cccd = cccd;
        }
    }

    /**
     * Tính điểm xét tuyển theo tổ hợp
     * @param cccd CCCD của thí sinh
     * @param maToHop Mã tổ hợp (A00, A01, C00, D01...)
     * @return Điểm xét tuyển hoặc -1 nếu không hợp lệ
     */
    public double tinhDiemXetTuyen(String cccd, String maToHop) {
        DiemThiDTO diem = diemDAO.getByCCCD(cccd);
        if (diem == null) {
            return -1;
        }

        ToHopDTO toHop = toHopDAO.getByMa(maToHop);
        if (toHop == null) {
            return -1;
        }

        // Lấy hệ số từ bảng quy đổi
        double hs1 = getHeSo(toHop.getMon1());
        double hs2 = getHeSo(toHop.getMon2());
        double hs3 = getHeSo(toHop.getMon3());

        // Lấy điểm các môn
        double m1 = getDiemMon(diem, toHop.getMon1());
        double m2 = getDiemMon(diem, toHop.getMon2());
        double m3 = getDiemMon(diem, toHop.getMon3());

        // Tính điểm với hệ số
        return m1 * hs1 + m2 * hs2 + m3 * hs3;
    }

    private double getHeSo(String maMon) {
        // Mặc định hệ số 1.0 cho tất cả môn
        // Có thể mở rộng sau bằng cách đọc từ bảng quy đổi
        QuyDoiDTO qd = quyDoiDAO.getByMaMon(maMon);
        if (qd != null) {
            // Tính hệ số quy đổi theo công thức
            // Giả định: hệ số = điểm A / điểm quy đổi
            if (qd.getDiemA() != null && qd.getDiemA() > 0) {
                return 30.0 / qd.getDiemA(); // Quy đổi về thang 30
            }
        }
        return 1.0;
    }

    private double getDiemMon(DiemThiDTO diem, String maMon) {
        if (maMon == null) return 0;
        switch (maMon.toUpperCase()) {
            case "TO": return diem.getTo();
            case "VA": return diem.getVa();
            case "LI": return diem.getLi();
            case "HO": return diem.getHo();
            case "SI": return diem.getSi();
            case "SU": return diem.getSu();
            case "DI": return diem.getDi();
            case "NN": return diem.getN1_thi();
            case "KTPL": return diem.getKtpl();
            case "TI": return diem.getTi();
            default: return 0;
        }
    }

    /**
     * Xét tuyển cho một thí sinh với nhiều nguyện vọng
     * @param cccd CCCD của thí sinh
     * @param dsNguyenVong Danh sách mã ngành theo thứ tự ưu tiên
     * @return Danh sách kết quả xét tuyển
     */
    public List<KetQuaXetTuyen> xetTuyen(String cccd, List<String> dsNguyenVong) {
        List<KetQuaXetTuyen> ketQuaList = new ArrayList<>();
        
        ThiSinhDTO ts = new ThiSinhDTO();
        ts.setCccd(cccd);
        
        // Lấy thông tin thí sinh (giả định)
        String soBaoDanh = "SBD_" + cccd;
        String hoTen = "Thí sinh " + cccd;

        for (int i = 0; i < dsNguyenVong.size(); i++) {
            String maNganh = dsNguyenVong.get(i);
            NganhDTO nganh = nganhDAO.getByMa(maNganh);
            
            if (nganh == null) continue;

            KetQuaXetTuyen kq = new KetQuaXetTuyen(soBaoDanh, hoTen, cccd);
            kq.maNganh = maNganh;
            kq.tenNganh = nganh.getTenNganh();
            kq.thuTuNguyenVong = i + 1;

            // Lấy tổ hợp gốc của ngành
            String toHopGoc = nganh.getToHopGoc();
            if (toHopGoc == null || toHopGoc.isEmpty()) {
                kq.lyDo = "Ngành chưa có tổ hợp";
                kq.trungTuyen = false;
                ketQuaList.add(kq);
                continue;
            }

            // Tính điểm xét tuyển
            double diemXT = tinhDiemXetTuyen(cccd, toHopGoc);
            kq.diemXetTuyen = diemXT;

            // So sánh với điểm sàn
            Double diemSan = nganh.getDiemSan();
            if (diemSan == null) diemSan = 0.0;
            
            if (diemXT >= diemSan) {
                kq.trungTuyen = true;
                kq.lyDo = "Đạt điểm sàn";
            } else {
                kq.trungTuyen = false;
                kq.lyDo = String.format("Không đạt điểm sàn (%.2f < %.2f)", diemXT, diemSan);
            }

            ketQuaList.add(kq);
        }

        return ketQuaList;
    }

    /**
     * Xét tuyển đợt - xét tất cả thí sinh
     * @return Map với key là CCCD, value là danh sách kết quả
     */
    public Map<String, List<KetQuaXetTuyen>> xetTuyenDoi() {
        Map<String, List<KetQuaXetTuyen>> ketQuaMap = new HashMap<>();
        
        // Lấy danh sách tất cả thí sinh có điểm
        List<DiemThiDTO> dsDiem = diemDAO.getAll();
        
        for (DiemThiDTO diem : dsDiem) {
            // Giả định mỗi thí sinh có 6 nguyện vọng
            List<String> dsNV = Arrays.asList("CNTT", "KETOAN", "MARKETING", "LUAT", "KINHTE", "TAICHINH");
            List<KetQuaXetTuyen> kq = xetTuyen(diem.getCccd(), dsNV);
            ketQuaMap.put(diem.getCccd(), kq);
        }
        
        return ketQuaMap;
    }

    /**
     * Lấy danh sách ngành trúng tuyển cao nhất cho mỗi thí sinh
     * @param cccd CCCD thí sinh
     * @return Ngành trúng tuyển cao nhất hoặc null
     */
    public KetQuaXetTuyen getNguyenVongTrungTuyenCaoNhat(String cccd, List<String> dsNguyenVong) {
        List<KetQuaXetTuyen> ketQuaList = xetTuyen(cccd, dsNguyenVong);
        
        for (KetQuaXetTuyen kq : ketQuaList) {
            if (kq.trungTuyen) {
                return kq;
            }
        }
        return null;
    }

    /**
     * Thống kê xét tuyển
     */
    public static class ThongKeXetTuyen {
        public int tongSoThiSinh;
        public int soTrungTuyen;
        public int soKhongTrungTuyen;
        public double tiLeTrungTuyen;
        public Map<String, Integer> thongKeTheoNganh;
    }

    public ThongKeXetTuyen thongKe() {
        ThongKeXetTuyen tk = new ThongKeXetTuyen();
        tk.thongKeTheoNganh = new HashMap<>();
        
        Map<String, List<KetQuaXetTuyen>> ketQuaMap = xetTuyenDoi();
        tk.tongSoThiSinh = ketQuaMap.size();
        
        for (List<KetQuaXetTuyen> list : ketQuaMap.values()) {
            boolean daTrungTuyen = false;
            for (KetQuaXetTuyen kq : list) {
                if (kq.trungTuyen && !daTrungTuyen) {
                    tk.soTrungTuyen++;
                    daTrungTuyen = true;
                    
                    // Thống kê theo ngành
                    tk.thongKeTheoNganh.merge(kq.maNganh, 1, Integer::sum);
                }
            }
            if (!daTrungTuyen) {
                tk.soKhongTrungTuyen++;
            }
        }
        
        tk.tiLeTrungTuyen = tk.tongSoThiSinh > 0 
            ? (double) tk.soTrungTuyen / tk.tongSoThiSinh * 100 
            : 0;
        
        return tk;
    }
}