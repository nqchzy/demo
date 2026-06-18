package dao;

import model.NhaCungCap;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapDAO {

    // Lấy tất cả nhà cung cấp
    public List<NhaCungCap> getAllNhaCungCap() {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE TrangThai = 1 ORDER BY MaNCC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNCC(rs.getString("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setSdt(rs.getString("SDT"));
                ncc.setEmail(rs.getString("Email"));
                ncc.setDiaChi(rs.getString("DiaChi"));
                ncc.setNguoiLienHe(rs.getString("NguoiLienHe"));

                Date ngayHopTac = rs.getDate("NgayHopTac");
                if (ngayHopTac != null) {
                    ncc.setNgayHopTac(ngayHopTac.toLocalDate());
                }

                ncc.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(ncc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm nhà cung cấp
    public boolean themNhaCungCap(NhaCungCap ncc) {
        String sql = "INSERT INTO NhaCungCap (MaNCC, TenNCC, SDT, Email, DiaChi, " +
                "NguoiLienHe, NgayHopTac, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getSdt());
            ps.setString(4, ncc.getEmail());
            ps.setString(5, ncc.getDiaChi());
            ps.setString(6, ncc.getNguoiLienHe());

            if (ncc.getNgayHopTac() != null) {
                ps.setDate(7, Date.valueOf(ncc.getNgayHopTac()));
            } else {
                ps.setDate(7, Date.valueOf(LocalDate.now()));
            }

            ps.setBoolean(8, true);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật nhà cung cấp
    public boolean capNhatNhaCungCap(NhaCungCap ncc) {
        String sql = "UPDATE NhaCungCap SET TenNCC=?, SDT=?, Email=?, DiaChi=?, " +
                "NguoiLienHe=?, NgayHopTac=? WHERE MaNCC=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getSdt());
            ps.setString(3, ncc.getEmail());
            ps.setString(4, ncc.getDiaChi());
            ps.setString(5, ncc.getNguoiLienHe());

            if (ncc.getNgayHopTac() != null) {
                ps.setDate(6, Date.valueOf(ncc.getNgayHopTac()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, ncc.getMaNCC());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa nhà cung cấp (soft delete)
    public boolean xoaNhaCungCap(String maNCC) {
        String sql = "UPDATE NhaCungCap SET TrangThai = 0 WHERE MaNCC = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNCC);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm nhà cung cấp theo mã
    public NhaCungCap timNhaCungCapTheoMa(String maNCC) {
        String sql = "SELECT * FROM NhaCungCap WHERE MaNCC = ? AND TrangThai = 1";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNCC);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNCC(rs.getString("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setSdt(rs.getString("SDT"));
                ncc.setEmail(rs.getString("Email"));
                ncc.setDiaChi(rs.getString("DiaChi"));
                ncc.setNguoiLienHe(rs.getString("NguoiLienHe"));

                Date ngayHopTac = rs.getDate("NgayHopTac");
                if (ngayHopTac != null) {
                    ncc.setNgayHopTac(ngayHopTac.toLocalDate());
                }

                ncc.setTrangThai(rs.getBoolean("TrangThai"));
                return ncc;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Tìm nhà cung cấp theo tên
    public List<NhaCungCap> timNhaCungCapTheoTen(String tenNCC) {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE TenNCC LIKE ? AND TrangThai = 1";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + tenNCC + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNCC(rs.getString("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setSdt(rs.getString("SDT"));
                ncc.setEmail(rs.getString("Email"));
                ncc.setDiaChi(rs.getString("DiaChi"));
                ncc.setNguoiLienHe(rs.getString("NguoiLienHe"));

                Date ngayHopTac = rs.getDate("NgayHopTac");
                if (ngayHopTac != null) {
                    ncc.setNgayHopTac(ngayHopTac.toLocalDate());
                }

                ncc.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(ncc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Kiểm tra mã nhà cung cấp đã tồn tại
}