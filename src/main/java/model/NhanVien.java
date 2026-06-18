package model;
import java.time.LocalDate;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String sdt;
    private String email;
    private String diaChi;
    private String chucVu;
    private LocalDate ngayVaoLam;
    private String tenDangNhap;  // THÊM MỚI
    private boolean trangThai;

    public NhanVien() {}

    public NhanVien(String maNV, String tenNV, String sdt, String email,
                    String diaChi, String chucVu, LocalDate ngayVaoLam) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.sdt = sdt;
        this.email = email;
        this.diaChi = diaChi;
        this.chucVu = chucVu;
        this.ngayVaoLam = ngayVaoLam;
        this.tenDangNhap = maNV; // Mặc định tên đăng nhập = mã NV
        this.trangThai = true;
    }

    // Constructor với tên đăng nhập
    public NhanVien(String maNV, String tenNV, String sdt, String email,
                    String diaChi, String chucVu, LocalDate ngayVaoLam, String tenDangNhap) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.sdt = sdt;
        this.email = email;
        this.diaChi = diaChi;
        this.chucVu = chucVu;
        this.ngayVaoLam = ngayVaoLam;
        this.tenDangNhap = tenDangNhap;
        this.trangThai = true;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public LocalDate getNgayVaoLam() { return ngayVaoLam; }
    public void setNgayVaoLam(LocalDate ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }

    public String getTenDangNhap() { return tenDangNhap; }  // THÊM MỚI
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }  // THÊM MỚI

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}