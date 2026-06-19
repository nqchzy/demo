package model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "SanPham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tenSP", nullable = false)
    private String tenSP;

    @Column(name = "giaBan", nullable = false)
    private double giaBan;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;

    @Column(name = "ngayNhap", nullable = false)
    private LocalDate ngayNhap;

    public SanPham() {}

    public SanPham(String tenSP, double giaBan, int soLuong, LocalDate ngayNhap) {
        this.tenSP = tenSP;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.ngayNhap = ngayNhap;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public LocalDate getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(LocalDate ngayNhap) { this.ngayNhap = ngayNhap; }
}

