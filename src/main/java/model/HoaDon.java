package model;
import java.time.LocalDateTime;

public class HoaDon {
    private String maHD;
    private String maNV;
    private String maKH;
    private LocalDateTime ngayLap;
    private double tongTien;
    private double giamGia;
    private double thanhTien;
    private String ghiChu;

    public HoaDon() {}

    public HoaDon(String maHD, String maNV, String maKH) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maKH = maKH;
        this.ngayLap = LocalDateTime.now();
        this.giamGia = 0;
    }

    
    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public LocalDateTime getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDateTime ngayLap) { this.ngayLap = ngayLap; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
        this.thanhTien = tongTien - giamGia;
    }

    public double getGiamGia() { return giamGia; }
    public void setGiamGia(double giamGia) {
        this.giamGia = giamGia;
        this.thanhTien = tongTien - giamGia;
    }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    public HoaDon(String maHD,
                  String maNV,
                  String maKH,
                  LocalDateTime ngayLap,
                  double tongTien,
                  double giamGia,
                  double thanhTien,
                  String ghiChu) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maKH = maKH;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.giamGia = giamGia;
        this.thanhTien = thanhTien;
        this.ghiChu = ghiChu;
    }

}
