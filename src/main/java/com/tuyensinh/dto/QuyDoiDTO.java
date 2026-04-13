package com.tuyensinh.dto;

import javax.persistence.*;

@Entity
@Table(name = "xt_bangquydoi")
public class QuyDoiDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idqd")
    private int idQd;

    @Column(name = "d_phuongthuc", length = 45) private String phuongThuc;
    @Column(name = "d_tohop", length = 45) private String toHop;
    @Column(name = "d_mon", length = 45) private String mon;

    @Column(name = "d_diema", precision = 6, scale = 2) private Double diemA;
    @Column(name = "d_diemb", precision = 6, scale = 2) private Double diemB;
    @Column(name = "d_diemc", precision = 6, scale = 2) private Double diemC;
    @Column(name = "d_diemd", precision = 6, scale = 2) private Double diemD;

    @Column(name = "d_maquydoi", length = 45, unique = true)
    private String maQuyDoi;

    @Column(name = "d_phanvi", length = 45)
    private String phanVi;

    public QuyDoiDTO() {}


    public int getIdQd() { return idQd; }
    public void setIdQd(int idQd) { this.idQd = idQd; }

    public String getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }

    public String getToHop() { return toHop; }
    public void setToHop(String toHop) { this.toHop = toHop; }

    public String getMon() { return mon; }
    public void setMon(String mon) { this.mon = mon; }

    public Double getDiemA() { return diemA; }
    public void setDiemA(Double diemA) { this.diemA = diemA; }

    public Double getDiemB() { return diemB; }
    public void setDiemB(Double diemB) { this.diemB = diemB; }

    public Double getDiemC() { return diemC; }
    public void setDiemC(Double diemC) { this.diemC = diemC; }

    public Double getDiemD() { return diemD; }
    public void setDiemD(Double diemD) { this.diemD = diemD; }

    public String getMaQuyDoi() { return maQuyDoi; }
    public void setMaQuyDoi(String maQuyDoi) { this.maQuyDoi = maQuyDoi; }

    public String getPhanVi() { return phanVi; }
    public void setPhanVi(String phanVi) { this.phanVi = phanVi; }
}