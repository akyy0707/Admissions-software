package com.tuyensinh.DTO;

public class NguyenVongDTO {

    private int idnv;

    private String cccd;

    private String maNganh;

    private int thuTuNV;

    // Điểm tổ hợp xét tuyển
    private double diemTHXT;

    // Điểm tổ hợp gốc xét tuyển
    private double diemTHGXT;

    // Điểm ưu tiên quy đổi
    private double diemUTQD;

    // Điểm cộng
    private double diemCong;

    // Điểm xét tuyển cuối
    private double diemXetTuyen;

    private String ketQua;

    private String keys;

    private String phuongThuc;

    private String toHopMon;

    // =========================================================
    // GETTER SETTER
    // =========================================================

    public int getIdnv() {
        return idnv;
    }

    public void setIdnv(int idnv) {
        this.idnv = idnv;
    }

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

    public int getThuTuNV() {
        return thuTuNV;
    }

    public void setThuTuNV(int thuTuNV) {
        this.thuTuNV = thuTuNV;
    }

    public double getDiemTHXT() {
        return diemTHXT;
    }

    public void setDiemTHXT(double diemTHXT) {
        this.diemTHXT = diemTHXT;
    }

    public double getDiemTHGXT() {
        return diemTHGXT;
    }

    public void setDiemTHGXT(double diemTHGXT) {
        this.diemTHGXT = diemTHGXT;
    }

    public double getDiemUTQD() {
        return diemUTQD;
    }

    public void setDiemUTQD(double diemUTQD) {
        this.diemUTQD = diemUTQD;
    }

    public double getDiemCong() {
        return diemCong;
    }

    public void setDiemCong(double diemCong) {
        this.diemCong = diemCong;
    }

    public double getDiemXetTuyen() {
        return diemXetTuyen;
    }

    public void setDiemXetTuyen(double diemXetTuyen) {
        this.diemXetTuyen = diemXetTuyen;
    }

    public String getKetQua() {
        return ketQua;
    }

    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public String getToHopMon() {
        return toHopMon;
    }

    public void setToHopMon(String toHopMon) {
        this.toHopMon = toHopMon;
    }
}