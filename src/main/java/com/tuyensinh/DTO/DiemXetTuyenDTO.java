package com.tuyensinh.DTO;

public class DiemXetTuyenDTO {

    private String cccd;
    private String ho;
    private String ten;

    private String nvtt;
    private String thm;

    private double diemThxt;
    private double diemCong;
    private double diemUtqd;
    private double diemXetTuyen;

    public DiemXetTuyenDTO() {}

    public DiemXetTuyenDTO(String cccd, String ho, String ten,
                           String nvtt, String thm,
                           double diemThxt, double diemCong,
                           double diemUtqd, double diemXetTuyen) {
        this.cccd = cccd;
        this.ho = ho;
        this.ten = ten;
        this.nvtt = nvtt;
        this.thm = thm;
        this.diemThxt = diemThxt;
        this.diemCong = diemCong;
        this.diemUtqd = diemUtqd;
        this.diemXetTuyen = diemXetTuyen;
    }

    public String getCccd() { return cccd; }
    public String getHo() { return ho; }
    public String getTen() { return ten; }
    public String getNvtt() { return nvtt; }
    public String getThm() { return thm; }
    public double getDiemThxt() { return diemThxt; }
    public double getDiemCong() { return diemCong; }
    public double getDiemUtqd() { return diemUtqd; }
    public double getDiemXetTuyen() { return diemXetTuyen; }
}