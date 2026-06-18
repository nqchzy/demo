package dao;

import model.SanPham;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {
    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE trangThai = 1";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPham sp = new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getString("maLoai"),
                        rs.getDouble("giaNhap"),
                        rs.getDouble("giaBan"),
                        rs.getString("size"),
                        rs.getInt("soLuong"),
                        rs.getDate("ngaySanXuat") != null
                                ? rs.getDate("ngaySanXuat").toLocalDate()
                                : null,
                        rs.getString("moTa")
                );
                sp.setTrangThai(rs.getBoolean("trangThai"));

                list.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean themSanPham(SanPham sp) {
        String sql = "INSERT INTO SanPham (maSP, tenSP, maLoai, giaNhap, giaBan, " +
                "size, soLuong, ngaySanXuat, moTa, trangThai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setString(3, sp.getMaLoai());
            ps.setDouble(4, sp.getGiaNhap());
            ps.setDouble(5, sp.getGiaBan());
            ps.setString(6, sp.getSize());
            ps.setInt(7, sp.getSoluong());

            if (sp.getNgaySanXuat() != null) {
                ps.setDate(8, Date.valueOf(sp.getNgaySanXuat()));
            } else {
                ps.setNull(8, Types.DATE);
            }

            ps.setString(9, sp.getMoTa());
            ps.setBoolean(10, true);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật sản phẩm
    public boolean capNhatSanPham(SanPham sp) {
        String sql = "UPDATE SanPham SET tenSP=?, maLoai=?, giaNhap=?, giaBan=?, " +
                "size=?, soLuong=?, ngaySanXuat=?, moTa=? WHERE maSP=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sp.getTenSP());
            ps.setString(2, sp.getMaLoai());
            ps.setDouble(3, sp.getGiaNhap());
            ps.setDouble(4, sp.getGiaBan());
            ps.setString(5, sp.getSize());
            ps.setInt(6, sp.getSoluong());

            if (sp.getNgaySanXuat() != null) {
                ps.setDate(7, Date.valueOf(sp.getNgaySanXuat()));
            } else {
                ps.setNull(7, Types.DATE);
            }

            ps.setString(8, sp.getMoTa());
            ps.setString(9, sp.getMaSP());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean xoaSanPham(String maSP) {
        String sql = "UPDATE SanPham SET trangThai = 0 WHERE maSP = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public SanPham timSanPhamTheoMa(String maSP) {
        String sql = "SELECT * FROM SanPham WHERE maSP = ? AND trangThai = 1";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getString("maLoai"),
                        rs.getDouble("giaNhap"),
                        rs.getDouble("giaBan"),
                        rs.getString("size"),
                        rs.getInt("soLuong"),
                        rs.getDate("ngaySanXuat") != null
                                ? rs.getDate("ngaySanXuat").toLocalDate()
                                : null,
                        rs.getString("moTa")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<SanPham> timSanPhamTheoTen(String tenSP) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE tenSP LIKE ? AND trangThai = 1";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + tenSP + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SanPham sp = new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getString("maLoai"),
                        rs.getDouble("giaNhap"),
                        rs.getDouble("giaBan"),
                        rs.getString("size"),
                        rs.getInt("soLuong"),
                        rs.getDate("ngaySanXuat") != null
                                ? rs.getDate("ngaySanXuat").toLocalDate()
                                : null,
                        rs.getString("moTa")
                );
                list.add(sp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Giảm số lượng sản phẩm khi bán
    public boolean giamSoLuong(String maSP, int soLuongBan) {
        String sql = "UPDATE SanPham SET soLuong = soLuong - ? WHERE maSP = ? AND soLuong >= ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, soLuongBan);
            ps.setString(2, maSP);
            ps.setInt(3, soLuongBan);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}