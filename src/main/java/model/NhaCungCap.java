package model;

import java.time.LocalDate;

public class NhaCungCap {
    private String maNCC;
    private String tenNCC;
    private String sdt;
    private String email;
    private String diaChi;
    private String nguoiLienHe;
    private LocalDate ngayHopTac;
    private boolean trangThai;

    // Constructor đầy đủ
    public NhaCungCap(String maNCC, String tenNCC, String sdt, String email,
                      String diaChi, String nguoiLienHe, LocalDate ngayHopTac) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.sdt = sdt;
        this.email = email;
        this.diaChi = diaChi;
        this.nguoiLienHe = nguoiLienHe;
        this.ngayHopTac = ngayHopTac;
        this.trangThai = true;
    }

    // Constructor rỗng
    public NhaCungCap() {
    }

    // Getters and Setters
    public String getMaNCC() { return maNCC; }
    public void setMaNCC(String maNCC) { this.maNCC = maNCC; }

    public String getTenNCC() { return tenNCC; }
    public void setTenNCC(String tenNCC) { this.tenNCC = tenNCC; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getNguoiLienHe() { return nguoiLienHe; }
    public void setNguoiLienHe(String nguoiLienHe) { this.nguoiLienHe = nguoiLienHe; }

    public LocalDate getNgayHopTac() { return ngayHopTac; }
    public void setNgayHopTac(LocalDate ngayHopTac) { this.ngayHopTac = ngayHopTac; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return "NhaCungCap{" +
                "maNCC='" + maNCC + '\'' +
                ", tenNCC='" + tenNCC + '\'' +
                ", sdt='" + sdt + '\'' +
                ", email='" + email + '\'' +
                ", nguoiLienHe='" + nguoiLienHe + '\'' +
                ", trangThai=" + trangThai +
                '}';
    }
}