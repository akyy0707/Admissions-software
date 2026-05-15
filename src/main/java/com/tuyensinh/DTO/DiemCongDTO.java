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

    // ================= GETTERS & SETTERS =================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
        updateKeys();
    }

    public String getMaNganh() {
        return maNganh;
    }

    public void setMaNganh(String maNganh) {
        this.maNganh = maNganh;
        updateKeys();
    }

    public String getMaToHop() {
        return maToHop;
    }

    public void setMaToHop(String maToHop) {
        this.maToHop = maToHop;
        updateKeys();
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
        updateTong();
    }

    public double getDiemUuTien() {
        return diemUuTien;
    }
    

    public void setDiemUuTien(double diemUuTien) {
        this.diemUuTien = diemUuTien;
        updateTong();
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

    // ================= LOGIC INTERNAL =================

    private void updateTong() {
        this.diemTong = this.diemCC + this.diemUuTien;
    }

    private void updateKeys() {
        if (cccd != null && maNganh != null && maToHop != null) {
            this.dcKeys = cccd + "_" + maNganh + "_" + maToHop;
        }
    }
}