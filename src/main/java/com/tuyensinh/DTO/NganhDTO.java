package com.tuyensinh.DTO;

import jakarta.persistence.*;

@Entity
@Table(name = "xt_nganh")
public class NganhDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idnganh")
    private int idNganh;

    @Column(name = "manganh", length = 45, nullable = false)
    private String maNganh;

    @Column(name = "tennganh", length = 100, nullable = false)
    private String tenNganh;

    @Column(name = "n_tohopgoc", length = 3)
    private String toHopGoc;

    @Column(name = "n_chitieu")
    private int chiTieu;

    @Column(name = "n_diemsan")
    private Double diemSan;

    @Column(name = "n_diemtrungtuyen")
    private Double diemTrungTuyen;

    @Column(name = "n_tuyenthang", length = 1) private String tuyenThang;
    @Column(name = "n_dgnl", length = 1) private String dgnl;
    @Column(name = "n_thpt", length = 1) private String thpt;
    @Column(name = "n_vsat", length = 1) private String vsat;

    @Column(name = "sl_xtt") private Integer slXtt;
    @Column(name = "sl_dgnl") private Integer slDgnl;
    @Column(name = "sl_vsat") private Integer slVsat;
    @Column(name = "sl_thpt", length = 45) private String slThpt;

    public NganhDTO() {}


    public int getIdNganh() { return idNganh; }
    public void setIdNganh(int idNganh) { this.idNganh = idNganh; }

    public String getMaNganh() { return maNganh; }
    public void setMaNganh(String maNganh) { this.maNganh = maNganh; }

    public String getTenNganh() { return tenNganh; }
    public void setTenNganh(String tenNganh) { this.tenNganh = tenNganh; }

    public String getToHopGoc() { return toHopGoc; }
    public void setToHopGoc(String toHopGoc) { this.toHopGoc = toHopGoc; }

    public int getChiTieu() { return chiTieu; }
    public void setChiTieu(int chiTieu) { this.chiTieu = chiTieu; }

    public Double getDiemSan() { return diemSan; }
    public void setDiemSan(Double diemSan) { this.diemSan = diemSan; }

    public Double getDiemTrungTuyen() { return diemTrungTuyen; }
    public void setDiemTrungTuyen(Double diemTrungTuyen) { this.diemTrungTuyen = diemTrungTuyen; }

    public String getTuyenThang() { return tuyenThang; }
    public void setTuyenThang(String tuyenThang) { this.tuyenThang = tuyenThang; }

    public String getDgnl() { return dgnl; }
    public void setDgnl(String dgnl) { this.dgnl = dgnl; }

    public String getThpt() { return thpt; }
    public void setThpt(String thpt) { this.thpt = thpt; }

    public String getVsat() { return vsat; }
    public void setVsat(String vsat) { this.vsat = vsat; }

    public Integer getSlXtt() { return slXtt; }
    public void setSlXtt(Integer slXtt) { this.slXtt = slXtt; }

    public Integer getSlDgnl() { return slDgnl; }
    public void setSlDgnl(Integer slDgnl) { this.slDgnl = slDgnl; }

    public Integer getSlVsat() { return slVsat; }
    public void setSlVsat(Integer slVsat) { this.slVsat = slVsat; }

    public String getSlThpt() { return slThpt; }
    public void setSlThpt(String slThpt) { this.slThpt = slThpt; }
}