package com.tuyensinh.DTO;

public class DiemCongDTO {
    private int id;
    private String cccd;
    private String maNganh;
    private String maToHop;
    private String phuongThuc;
    private double diemCC;
    private double diemUuTien;
    private double diemTong;
    private String dcKeys;

    public DiemCongDTO() {
    }

    public DiemCongDTO(String cccd, String maNganh, String maToHop,
            String phuongThuc, double diemCC, double diemUuTien) {
        this.cccd = cccd;
        this.maNganh = maNganh;
        this.maToHop = maToHop;
        this.phuongThuc = phuongThuc;
        this.diemCC = diemCC;
        this.diemUuTien = diemUuTien;
        this.diemTong = diemCC + diemUuTien;
        this.dcKeys = cccd + "_" + maNganh + "_" + maToHop;
    }

    // Getter & Setter
    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getMaNganh() {
        return maNganh;
    }

    public void setMaNganh(String maNganh) {
        this.maNganh = maNganh;
    }

    public String getMaToHop() {
        return maToHop;
    }

    public void setMaToHop(String maToHop) {
        this.maToHop = maToHop;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public double getDiemCC() {
        return diemCC;
    }

    public void setDiemCC(double diemCC) {
        this.diemCC = diemCC;
    }

    public double getDiemUuTien() {
        return diemUuTien;
    }

    public void setDiemUuTien(double diemUuTien) {
        this.diemUuTien = diemUuTien;
    }

    public double getDiemTong() {
        return diemTong;
    }

    public void setDiemTong(double diemTong) {
        this.diemTong = diemTong;
    }

    public String getDcKeys() {
        return dcKeys;
    }

    public void setDcKeys(String dcKeys) {
        this.dcKeys = dcKeys;
    }
}