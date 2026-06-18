// ========== HoaDonDAO.java - Full Code với phương thức nhập mã thủ công ==========
package dao;

import model.HoaDon;
import model.ChiTietHoaDon;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {


    public boolean kiemTraMaHoaDonTonTai(String maHD) {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE MaHD = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, maHD);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Lấy mã hóa đơn lớn nhất hiện có (để gợi ý cho người dùng)
     * @return Mã hóa đơn lớn nhất, hoặc null nếu chưa có hóa đơn nào
     */
    public String getMaHoaDonLonNhat() {
        String sql = "SELECT TOP 1 MaHD FROM HoaDon ORDER BY MaHD DESC";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getString("MaHD");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Đếm tổng số hóa đơn trong hệ thống
     * @return Số lượng hóa đơn
     */
    public int demSoLuongHoaDon() {
        String sql = "SELECT COUNT(*) FROM HoaDon";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY NgayLap DESC";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("MaHD"));
                hd.setMaNV(rs.getString("MaNV"));
                hd.setMaKH(rs.getString("MaKH"));

                Timestamp ngayLap = rs.getTimestamp("NgayLap");
                if (ngayLap != null) {
                    hd.setNgayLap(ngayLap.toLocalDateTime());
                }

                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setGiamGia(rs.getDouble("GiamGia"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));
                hd.setGhiChu(rs.getString("GhiChu"));

                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean themHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (MaHD, MaNV, MaKH, NgayLap, TongTien, " +
                "GiamGia, ThanhTien, GhiChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, hd.getMaHD());
            pstmt.setString(2, hd.getMaNV());
            pstmt.setString(3, hd.getMaKH());
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setDouble(5, hd.getTongTien());
            pstmt.setDouble(6, hd.getGiamGia());
            pstmt.setDouble(7, hd.getThanhTien());
            pstmt.setString(8, hd.getGhiChu());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaHoaDon(String maHD) {
        Connection con = null;
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            String sqlCT = "DELETE FROM ChiTietHoaDon WHERE MaHD = ?";
            PreparedStatement pstmtCT = con.prepareStatement(sqlCT);
            pstmtCT.setString(1, maHD);
            pstmtCT.executeUpdate();

            String sqlHD = "DELETE FROM HoaDon WHERE MaHD = ?";
            PreparedStatement pstmtHD = con.prepareStatement(sqlHD);
            pstmtHD.setString(1, maHD);
            pstmtHD.executeUpdate();

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public HoaDon timHoaDonTheoMa(String maHD) {
        String sql = "SELECT * FROM HoaDon WHERE MaHD = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, maHD);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("MaHD"));
                hd.setMaNV(rs.getString("MaNV"));
                hd.setMaKH(rs.getString("MaKH"));

                Timestamp ngayLap = rs.getTimestamp("NgayLap");
                if (ngayLap != null) {
                    hd.setNgayLap(ngayLap.toLocalDateTime());
                }

                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setGiamGia(rs.getDouble("GiamGia"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));
                hd.setGhiChu(rs.getString("GhiChu"));

                return hd;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<HoaDon> timHoaDonTheoKhachHang(String maKH) {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MaKH = ? ORDER BY NgayLap DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, maKH);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("MaHD"));
                hd.setMaNV(rs.getString("MaNV"));
                hd.setMaKH(rs.getString("MaKH"));

                Timestamp ngayLap = rs.getTimestamp("NgayLap");
                if (ngayLap != null) {
                    hd.setNgayLap(ngayLap.toLocalDateTime());
                }

                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setGiamGia(rs.getDouble("GiamGia"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));
                hd.setGhiChu(rs.getString("GhiChu"));

                list.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<HoaDon> getHoaDonTheoNgay(LocalDateTime tuNgay, LocalDateTime denNgay) {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE NgayLap BETWEEN ? AND ? ORDER BY NgayLap DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(tuNgay));
            pstmt.setTimestamp(2, Timestamp.valueOf(denNgay));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("MaHD"));
                hd.setMaNV(rs.getString("MaNV"));
                hd.setMaKH(rs.getString("MaKH"));

                Timestamp ngayLap = rs.getTimestamp("NgayLap");
                if (ngayLap != null) {
                    hd.setNgayLap(ngayLap.toLocalDateTime());
                }

                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setGiamGia(rs.getDouble("GiamGia"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));
                hd.setGhiChu(rs.getString("GhiChu"));

                list.add(hd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public double getTongDoanhThuTheoNgay(LocalDateTime tuNgay, LocalDateTime denNgay) {
        String sql = "SELECT SUM(ThanhTien) AS TongDoanhThu FROM HoaDon " +
                "WHERE NgayLap BETWEEN ? AND ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(tuNgay));
            pstmt.setTimestamp(2, Timestamp.valueOf(denNgay));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("TongDoanhThu");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<ChiTietHoaDon> getChiTietHoaDon(String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT ct.*, sp.TenSP " +
                "FROM ChiTietHoaDon ct " +
                "LEFT JOIN SanPham sp ON ct.MaSP = sp.MaSP " +
                "WHERE ct.MaHD = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, maHD);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setMaHD(rs.getString("MaHD"));
                ct.setMaSP(rs.getString("MaSP"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setDonGia(rs.getDouble("DonGia"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));

                list.add(ct);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean themChiTietHoaDon(ChiTietHoaDon ct) {
        String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaSP, SoLuong, DonGia, ThanhTien) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, ct.getMaHD());
            pstmt.setString(2, ct.getMaSP());
            pstmt.setInt(3, ct.getSoLuong());
            pstmt.setDouble(4, ct.getDonGia());
            pstmt.setDouble(5, ct.getThanhTien());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaChiTietHoaDon(String maHD) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaHD = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, maHD);
            return pstmt.executeUpdate() >= 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getTenSanPham(String maSP) {
        String sql = "SELECT TenSP FROM SanPham WHERE MaSP = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, maSP);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("TenSP");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Không tìm thấy";
    }
}