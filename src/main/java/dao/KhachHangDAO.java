package dao;
import model.KhachHang;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang ORDER BY MaKH";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("Email"));
                kh.setDiaChi(rs.getString("DiaChi"));

                Date ngayDK = rs.getDate("NgayDangKy");
                if (ngayDK != null) {
                    kh.setNgayDangKy(ngayDK.toLocalDate());
                }

                kh.setDiemTichLuy(rs.getInt("DiemTichLuy"));
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (MaKH, TenKH, SDT, Email, DiaChi, " +
                "NgayDangKy, DiemTichLuy) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, kh.getMaKH());
            pstmt.setString(2, kh.getTenKH());
            pstmt.setString(3, kh.getSdt());
            pstmt.setString(4, kh.getEmail());
            pstmt.setString(5, kh.getDiaChi());
            pstmt.setDate(6, Date.valueOf(LocalDate.now()));
            pstmt.setInt(7, 0);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET TenKH = ?, SDT = ?, Email = ?, " +
                "DiaChi = ? WHERE MaKH = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, kh.getTenKH());
            pstmt.setString(2, kh.getSdt());
            pstmt.setString(3, kh.getEmail());
            pstmt.setString(4, kh.getDiaChi());
            pstmt.setString(5, kh.getMaKH());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhachHang(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE MaKH = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, maKH);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhachHang timKhachHangTheoMa(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE MaKH = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, maKH);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("Email"));
                kh.setDiaChi(rs.getString("DiaChi"));

                Date ngayDK = rs.getDate("NgayDangKy");
                if (ngayDK != null) {
                    kh.setNgayDangKy(ngayDK.toLocalDate());
                }

                kh.setDiemTichLuy(rs.getInt("DiemTichLuy"));
                return kh;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public KhachHang timKhachHangTheoSDT(String sdt) {
        String sql = "SELECT * FROM KhachHang WHERE SDT = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, sdt);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("Email"));
                kh.setDiaChi(rs.getString("DiaChi"));

                Date ngayDK = rs.getDate("NgayDangKy");
                if (ngayDK != null) {
                    kh.setNgayDangKy(ngayDK.toLocalDate());
                }

                kh.setDiemTichLuy(rs.getInt("DiemTichLuy"));
                return kh;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<KhachHang> timKhachHangTheoTen(String tenKH) {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE TenKH LIKE ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, "%" + tenKH + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("Email"));
                kh.setDiaChi(rs.getString("DiaChi"));

                Date ngayDK = rs.getDate("NgayDangKy");
                if (ngayDK != null) {
                    kh.setNgayDangKy(ngayDK.toLocalDate());
                }

                kh.setDiemTichLuy(rs.getInt("DiemTichLuy"));
                list.add(kh);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean capNhatDiemTichLuy(String maKH, int diem) {
        String sql = "UPDATE KhachHang SET DiemTichLuy = DiemTichLuy + ? WHERE MaKH = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, diem);
            pstmt.setString(2, maKH);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
