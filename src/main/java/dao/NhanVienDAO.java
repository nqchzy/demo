package dao;

import model.NhanVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    /* ===================== GET ALL ===================== */
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE TrangThai = 1 ORDER BY MaNV";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ptst = con.prepareStatement(sql);
             ResultSet rs = ptst.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getString("TenNV"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setChucVu(rs.getString("ChucVu"));

                Date d = rs.getDate("NgayVaoLam");
                if (d != null) nv.setNgayVaoLam(d.toLocalDate());

                // Lấy tên đăng nhập (nếu có), nếu không thì dùng MaNV
                String tenDangNhap = rs.getString("TenDangNhap");
                nv.setTenDangNhap(tenDangNhap != null ? tenDangNhap : rs.getString("MaNV"));

                nv.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(nv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ===================== INSERT ===================== */
    public boolean themNhanVien(NhanVien nv) {
        String sql = """
                INSERT INTO NhanVien
                (MaNV, TenNV, SDT, Email, DiaChi, ChucVu, NgayVaoLam, TenDangNhap, TrangThai)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ptst = con.prepareStatement(sql)) {

            ptst.setString(1, nv.getMaNV());
            ptst.setString(2, nv.getTenNV());
            ptst.setString(3, nv.getSdt());
            ptst.setString(4, nv.getEmail());
            ptst.setString(5, nv.getDiaChi());
            ptst.setString(6, nv.getChucVu());
            ptst.setDate(7, Date.valueOf(nv.getNgayVaoLam()));
            ptst.setString(8, nv.getTenDangNhap() != null ? nv.getTenDangNhap() : nv.getMaNV());
            ptst.setBoolean(9, nv.isTrangThai());

            return ptst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ===================== UPDATE ===================== */
    public boolean capNhatNhanVien(NhanVien nv) {
        String sql = """
                UPDATE NhanVien
                SET TenNV=?, SDT=?, Email=?, DiaChi=?, ChucVu=?, NgayVaoLam=?, TenDangNhap=?
                WHERE MaNV=?
                """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ptst = con.prepareStatement(sql)) {

            ptst.setString(1, nv.getTenNV());
            ptst.setString(2, nv.getSdt());
            ptst.setString(3, nv.getEmail());
            ptst.setString(4, nv.getDiaChi());
            ptst.setString(5, nv.getChucVu());
            ptst.setDate(6, Date.valueOf(nv.getNgayVaoLam()));
            ptst.setString(7, nv.getTenDangNhap() != null ? nv.getTenDangNhap() : nv.getMaNV());
            ptst.setString(8, nv.getMaNV());

            return ptst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ===================== SOFT DELETE ===================== */
    public boolean xoaNhanVien(String maNV) {
        String sql = "UPDATE NhanVien SET TrangThai = 0 WHERE MaNV = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ptst = con.prepareStatement(sql)) {

            ptst.setString(1, maNV);
            return ptst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ===================== SEARCH ===================== */
    public NhanVien timNhanVienTheoMa(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV=? AND TrangThai=1";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ptst = con.prepareStatement(sql)) {

            ptst.setString(1, maNV);
            try (ResultSet rs = ptst.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getString("MaNV"));
                    nv.setTenNV(rs.getString("TenNV"));
                    nv.setSdt(rs.getString("SDT"));
                    nv.setEmail(rs.getString("Email"));
                    nv.setDiaChi(rs.getString("DiaChi"));
                    nv.setChucVu(rs.getString("ChucVu"));

                    Date d = rs.getDate("NgayVaoLam");
                    if (d != null) nv.setNgayVaoLam(d.toLocalDate());

                    String tenDangNhap = rs.getString("TenDangNhap");
                    nv.setTenDangNhap(tenDangNhap != null ? tenDangNhap : rs.getString("MaNV"));

                    nv.setTrangThai(rs.getBoolean("TrangThai"));
                    return nv;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhanVien> timNhanVienTheoTen(String tenNV) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE TenNV LIKE ? AND TrangThai=1";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ptst = con.prepareStatement(sql)) {

            ptst.setString(1, "%" + tenNV + "%");
            try (ResultSet rs = ptst.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getString("MaNV"));
                    nv.setTenNV(rs.getString("TenNV"));
                    nv.setSdt(rs.getString("SDT"));
                    nv.setEmail(rs.getString("Email"));
                    nv.setDiaChi(rs.getString("DiaChi"));
                    nv.setChucVu(rs.getString("ChucVu"));

                    Date d = rs.getDate("NgayVaoLam");
                    if (d != null) nv.setNgayVaoLam(d.toLocalDate());

                    String tenDangNhap = rs.getString("TenDangNhap");
                    nv.setTenDangNhap(tenDangNhap != null ? tenDangNhap : rs.getString("MaNV"));

                    nv.setTrangThai(rs.getBoolean("TrangThai"));
                    list.add(nv);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ===================== GET BY CHUC VU ===================== */
    public List<NhanVien> getNhanVienTheoChucVu(String chucVu) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE ChucVu = ? AND TrangThai = 1";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ptst = con.prepareStatement(sql)) {

            ptst.setString(1, chucVu);
            ResultSet rs = ptst.executeQuery();

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getString("TenNV"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setChucVu(rs.getString("ChucVu"));

                Date ngayVaoLam = rs.getDate("NgayVaoLam");
                if (ngayVaoLam != null) {
                    nv.setNgayVaoLam(ngayVaoLam.toLocalDate());
                }

                String tenDangNhap = rs.getString("TenDangNhap");
                nv.setTenDangNhap(tenDangNhap != null ? tenDangNhap : rs.getString("MaNV"));

                nv.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}