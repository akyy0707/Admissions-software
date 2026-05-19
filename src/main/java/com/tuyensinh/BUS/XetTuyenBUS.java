package com.tuyensinh.BUS;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuyensinh.DAO.DiemThiDAO;
import com.tuyensinh.DAO.NganhDAO;
import com.tuyensinh.DAO.NganhToHopDAO;
import com.tuyensinh.DTO.DiemThiDTO;
import com.tuyensinh.DTO.NganhDTO;
import com.tuyensinh.DTO.NganhToHopDTO;
import com.tuyensinh.DTO.ThiSinhDTO;

/**
 * XetTuyenBUS - Business xét tuyển chuẩn 2025
 */
public class XetTuyenBUS {

    private final DiemThiDAO diemDAO = new DiemThiDAO();
    private final NganhDAO nganhDAO = new NganhDAO();
    private final NganhToHopDAO nganhToHopDAO = new NganhToHopDAO();

    // =========================================================
    // DTO KẾT QUẢ
    // =========================================================
    public static class KetQuaXetTuyen {

        public String cccd;
        public String hoTen;

        public String maNganh;
        public String tenNganh;

        public String toHop;
        public String phuongThuc;

        public int thuTuNguyenVong;

        public double diemTHXT;
        public double diemCong;
        public double diemUT;
        public double diemXetTuyen;

        public boolean trungTuyen;
        public String lyDo;
    }

    // =========================================================
    // TÍNH ĐIỂM XÉT TUYỂN
    // =========================================================
    public KetQuaXetTuyen tinhDiem(
            ThiSinhDTO ts,
            String maNganh,
            String maToHop,
            String phuongThuc,
            int thuTuNV
    ) {

        KetQuaXetTuyen kq = new KetQuaXetTuyen();

        kq.cccd = ts.getCccd();
        kq.hoTen = ts.getHo() + " " + ts.getTen();

        kq.maNganh = maNganh;
        kq.toHop = maToHop;
        kq.phuongThuc = phuongThuc;
        kq.thuTuNguyenVong = thuTuNV;

        // =====================================================
        // LẤY NGÀNH
        // =====================================================
        NganhDTO nganh = nganhDAO.getByMa(maNganh);

        if (nganh == null) {
            kq.lyDo = "Không tìm thấy ngành";
            return kq;
        }

        kq.tenNganh = nganh.getTenNganh();

        // =====================================================
        // LẤY TỔ HỢP
        // =====================================================
        NganhToHopDTO toHop =
                nganhToHopDAO.getByNganhAndToHop(
                        maNganh,
                        maToHop
                );

        if (toHop == null) {

            kq.lyDo = "Ngành không có tổ hợp này";

            return kq;
        }

        // =====================================================
        // LẤY ĐIỂM THI
        // =====================================================
        DiemThiDTO diem =
                diemDAO.getByCCCD(ts.getCccd());
        System.out.println("========== OBJECT DIEM ==========");
System.out.println("CCCD: " + diem.getCccd());

System.out.println("TO: " + diem.getTo());
System.out.println("VA: " + diem.getVa());
System.out.println("LI: " + diem.getLi());
System.out.println("HO: " + diem.getHo());
        if (diem == null) {

            kq.lyDo = "Không có điểm thi";

            return kq;
        }

        // =====================================================
        // TÍNH ĐIỂM 3 MÔN
        // =====================================================
        double d1 = getDiemMon(diem, toHop.getThMon1());
        double d2 = getDiemMon(diem, toHop.getThMon2());
        double d3 = getDiemMon(diem, toHop.getThMon3());
        System.out.println("==========");
System.out.println("Tổ hợp: " + maToHop);

System.out.println(toHop.getThMon1() + " = " + d1);
System.out.println(toHop.getThMon2() + " = " + d2);
System.out.println(toHop.getThMon3() + " = " + d3);
        double hs1 =
                toHop.getHsMon1() == null
                        ? 1
                        : toHop.getHsMon1();

        double hs2 =
                toHop.getHsMon2() == null
                        ? 1
                        : toHop.getHsMon2();

        double hs3 =
                toHop.getHsMon3() == null
                        ? 1
                        : toHop.getHsMon3();

        double tongHeSo = hs1 + hs2 + hs3;

        // =====================================================
        // ĐIỂM THXT
        // =====================================================
        double diemTHXT =
                (
                        (d1 * hs1)
                                + (d2 * hs2)
                                + (d3 * hs3)
                )
                        / tongHeSo
                        * 3;

        // =====================================================
        // QUY ĐỔI TỔ HỢP
        // =====================================================
        double doLech =
                toHop.getDoLech() == null
                        ? 0
                        : toHop.getDoLech();

        double diemTHGXT = diemTHXT - doLech;

        // =====================================================
        // ĐIỂM CỘNG
        // =====================================================
        double diemCong = tinhDiemCong(diem);

        if (diemCong > 3) {
            diemCong = 3;
        }

        // =====================================================
        // ĐIỂM ƯU TIÊN
        // =====================================================
        double mucUT = tinhMucUuTien(ts);

        double diemUT;

        if ((diemTHGXT + diemCong) < 22.5) {

            diemUT = mucUT;

        } else {

            diemUT =
                    (
                            (30 - diemTHGXT - diemCong)
                                    / 7.5
                    )
                            * mucUT;

            if (diemUT < 0) {
                diemUT = 0;
            }
        }

        // =====================================================
        // ĐIỂM XÉT TUYỂN
        // =====================================================
        double diemXT =
                diemTHGXT
                        + diemCong
                        + diemUT;

        // =====================================================
        // GÁN KẾT QUẢ
        // =====================================================
        kq.diemTHXT = lamTron(diemTHGXT);
        kq.diemCong = lamTron(diemCong);
        kq.diemUT = lamTron(diemUT);
        kq.diemXetTuyen = lamTron(diemXT);

        // =====================================================
        // SO ĐIỂM SÀN
        // =====================================================
        double diemSan =
                nganh.getDiemSan() == null
                        ? 0
                        : nganh.getDiemSan();

        if (diemXT >= diemSan) {

            kq.trungTuyen = true;
            kq.lyDo = "Đủ điều kiện";

        } else {

            kq.trungTuyen = false;
            kq.lyDo = "Không đạt điểm sàn";
        }

        return kq;
    }

    // =========================================================
    // LẤY ĐIỂM MÔN
    // =========================================================
    private double getDiemMon(DiemThiDTO d, String mon) {

    if (d == null || mon == null) {
        return 0;
    }

    mon = mon.trim().toUpperCase();

    System.out.println("MON: " + mon);

    switch (mon) {

        case "TO":
            return d.getTo();

        case "VA":
            return d.getVa();

        case "LI":
            return d.getLi();

        case "HO":
            return d.getHo();

        case "SI":
            return d.getSi();

        case "SU":
            return d.getSu();

        case "DI":
            return d.getDi();

        case "KTPL":
            return d.getKtpl();

        case "TI":
            return d.getTi();

        case "CNCN":
            return d.getCncn();

        case "CNNN":
            return d.getCnnn();

        case "NK1":
            return d.getNk1();

        case "NK2":
            return d.getNk2();

        case "N1":
        case "NN":
            return Math.max(
                    d.getN1_thi(),
                    d.getN1_cc()
            );

        default:

            System.out.println("KHONG NHAN DIEN MON = " + mon);

            return 0;
    }
}
// =========================================================
// ĐIỂM CỘNG
// =========================================================
private double tinhDiemCong(DiemThiDTO d) {

    double cong = 0;

    // IELTS / chứng chỉ ngoại ngữ
    if (d.getN1_cc() >= 8) {
        cong += 1.5;
    }

    return cong;
}
    // =========================================================
    // ƯU TIÊN
    // =========================================================
    private double tinhMucUuTien(ThiSinhDTO ts) {

        double ut = 0;

        if ("KV1".equalsIgnoreCase(ts.getKhuVuc())) {
            ut += 0.75;
        } else if ("KV2NT".equalsIgnoreCase(ts.getKhuVuc())) {
            ut += 0.5;
        } else if ("KV2".equalsIgnoreCase(ts.getKhuVuc())) {
            ut += 0.25;
        }

        if ("01".equals(ts.getDoiTuong())) {
            ut += 2;
        } else if ("02".equals(ts.getDoiTuong())) {
            ut += 1;
        }

        return ut;
    }

    // =========================================================
    // LÀM TRÒN
    // =========================================================
    private double lamTron(double d) {

        return Math.round(d * 100.0) / 100.0;
    }

    // =========================================================
    // XÉT TUYỂN CAO NHẤT
    // =========================================================
    public KetQuaXetTuyen getNguyenVongTrungTuyenCaoNhat(
            ThiSinhDTO ts,
            List<KetQuaXetTuyen> ds
    ) {

        ds.sort(
                Comparator.comparingInt(
                        o -> o.thuTuNguyenVong
                )
        );

        for (KetQuaXetTuyen kq : ds) {

            if (kq.trungTuyen) {
                return kq;
            }
        }

        return null;
    }

    // =========================================================
    // THỐNG KÊ
    // =========================================================
    public static class ThongKeXetTuyen {

        public int tongThiSinh;
        public int trungTuyen;
        public int rot;

        public double tiLe;

        public Map<String, Integer> theoNganh =
                new HashMap<>();
    }

    public ThongKeXetTuyen thongKe(
            List<KetQuaXetTuyen> ds
    ) {

        ThongKeXetTuyen tk =
                new ThongKeXetTuyen();

        Map<String, Boolean> daDo =
                new HashMap<>();

        for (KetQuaXetTuyen kq : ds) {

            if (!daDo.containsKey(kq.cccd)) {

                tk.tongThiSinh++;

                daDo.put(kq.cccd, true);
            }

            if (kq.trungTuyen) {

                tk.trungTuyen++;

                tk.theoNganh.merge(
                        kq.maNganh,
                        1,
                        Integer::sum
                );

            } else {

                tk.rot++;
            }
        }

        if (tk.tongThiSinh > 0) {

            tk.tiLe =
                    (
                            (double) tk.trungTuyen
                                    / tk.tongThiSinh
                    )
                            * 100;
        }

        return tk;
    }
     // =========================================
    // LẤY TỔ HỢP ĐIỂM CAO NHẤT
    // =========================================

    public KetQuaXetTuyen getToHopCaoNhat(
            List<KetQuaXetTuyen> ds
    ) {

        return ds.stream()
                .max(
                        Comparator.comparingDouble(
                                o -> o.diemXetTuyen
                        )
                )
                .orElse(null);
    }

}