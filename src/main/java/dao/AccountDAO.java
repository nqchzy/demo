package dao;

import java.sql.*;
import model.Account;

public class AccountDAO {

    public Account login(String username, String password) {

        if ("admin".equals(username) && "admin".equals(password)) {
            Account admin = new Account();
            admin.setUsername("admin");
            admin.setFullName("Quản trị viên");
            admin.setRole("Admin");
            admin.setActive(true);
            return admin;
        }

        String sql = "SELECT * FROM NhanVien WHERE MaNV=? AND MatKhau=? AND TrangThai=1";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account acc = new Account();
                    acc.setUsername(rs.getString("MaNV"));
                    acc.setFullName(rs.getString("TenNV"));
                    acc.setRole("Nhân viên");
                    acc.setActive(true);
                    return acc;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(String maNV, String tenNV, String sdt, String email,
                            String diaChi, String matKhau) {

        if (usernameExists(maNV)) {
            return false;
        }

        String sql = """
            INSERT INTO NhanVien(MaNV, TenNV, SDT, Email, DiaChi, ChucVu, 
                                NgayVaoLam, MatKhau, TenDangNhap, TrangThai)
            VALUES (?, ?, ?, ?, ?, 'Nhân viên', CURDATE(), ?, ?, 1)
            """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            ps.setString(2, tenNV);
            ps.setString(3, sdt);
            ps.setString(4, email);
            ps.setString(5, diaChi);
            ps.setString(6, matKhau);
            ps.setString(7, maNV);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean usernameExists(String maNV) {
        String sql = "SELECT 1 FROM NhanVien WHERE MaNV=?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}