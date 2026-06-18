package model;

import java.time.LocalDate;

public class SanPham {
    private String maSP;
    private String tenSP;
    private String maLoai;
    private double giaNhap;
    private double giaBan;
    private String size;
    private int soluong;
    private LocalDate ngaySanXuat;
    private String moTa;
    private boolean trangThai;

    public SanPham(String maSP, String tenSP, String maLoai, double giaNhap,
                   double giaBan, String size, int soluong, LocalDate ngaySanXuat, String moTa) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.maLoai = maLoai;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.size = size;
        this.soluong = soluong;
        this.ngaySanXuat = ngaySanXuat;
        this.moTa = moTa;
        this.trangThai = true;
    }

    public SanPham() {
    }

    // Getters and Setters
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    public double getGiaNhap() { return giaNhap; }
    public void setGiaNhap(double giaNhap) { this.giaNhap = giaNhap; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public int getSoluong() { return soluong; }
    public void setSoluong(int soluong) { this.soluong = soluong; }

    public LocalDate getNgaySanXuat() { return ngaySanXuat; }
    public void setNgaySanXuat(LocalDate ngaySanXuat) { this.ngaySanXuat = ngaySanXuat; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}