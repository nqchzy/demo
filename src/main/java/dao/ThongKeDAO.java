package dao;

import java.sql.*;
import java.util.*;

public class ThongKeDAO {
    public Map<Integer, Double> getDoanhThuTheoThang() {
        Map<Integer, Double> result = new LinkedHashMap<>();

        for (int i = 1; i <= 12; i++) {
            result.put(i, 0.0);
        }

        String sql = """
            SELECT 
                MONTH(NgayLap) AS Thang,
                SUM(ThanhTien) AS DoanhThu
            FROM HoaDon
            WHERE YEAR(NgayLap) = YEAR(CURDATE())
            GROUP BY MONTH(NgayLap)
            ORDER BY Thang
            """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.put(
                        rs.getInt("Thang"),
                        rs.getDouble("DoanhThu")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
