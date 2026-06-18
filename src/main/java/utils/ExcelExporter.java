package utils;

import model.HoaDon;
import model.ChiTietHoaDon;
import model.KhachHang;
import model.NhanVien;
import dao.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ExcelExporter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public static boolean exportSingleInvoice(HoaDon hoaDon, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Hóa đơn " + hoaDon.getMaHD());

            // Tạo styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle boldStyle = createBoldStyle(workbook);

            int rowNum = 0;

            // Tiêu đề
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("HÓA ĐƠN BÁN HÀNG");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            rowNum++; // Dòng trống

            // Thông tin cửa hàng
            createRow(sheet, rowNum++, "CỬA HÀNG THỜI TRANG", boldStyle);
            createRow(sheet, rowNum++, "Địa chỉ: 123 Đường Trần Đại Nghĩa, Ngũ Hành Sơn, Đà Nẵng", normalStyle);
            createRow(sheet, rowNum++, "Điện thoại: 0335656127", normalStyle);

            rowNum++; // Dòng trống

            // Thông tin hóa đơn
            createInfoRow(sheet, rowNum++, "Mã hóa đơn:", hoaDon.getMaHD(), normalStyle, boldStyle);
            createInfoRow(sheet, rowNum++, "Ngày lập:",
                    hoaDon.getNgayLap() != null ? hoaDon.getNgayLap().format(DATE_FORMATTER) : "",
                    normalStyle, normalStyle);

            // Thông tin nhân viên
            NhanVienDAO nvDAO = new NhanVienDAO();
            NhanVien nv = nvDAO.timNhanVienTheoMa(hoaDon.getMaNV());
            if (nv != null) {
                createInfoRow(sheet, rowNum++, "Nhân viên:", nv.getTenNV(), normalStyle, normalStyle);
            }

            // Thông tin khách hàng
            KhachHangDAO khDAO = new KhachHangDAO();
            KhachHang kh = khDAO.timKhachHangTheoMa(hoaDon.getMaKH());
            if (kh != null) {
                createInfoRow(sheet, rowNum++, "Khách hàng:", kh.getTenKH(), normalStyle, normalStyle);
                createInfoRow(sheet, rowNum++, "Số điện thoại:", kh.getSdt(), normalStyle, normalStyle);
            }

            rowNum++; // Dòng trống

            // Header bảng chi tiết
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"STT", "Mã SP", "Tên sản phẩm", "SL", "Đơn giá", "Thành tiền"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Chi tiết hóa đơn
            HoaDonDAO hdDAO = new HoaDonDAO();
            List<ChiTietHoaDon> chiTietList = hdDAO.getChiTietHoaDon(hoaDon.getMaHD());

            int stt = 1;
            for (ChiTietHoaDon ct : chiTietList) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(stt++);
                row.createCell(1).setCellValue(ct.getMaSP());

                String tenSP = hdDAO.getTenSanPham(ct.getMaSP());
                row.createCell(2).setCellValue(tenSP);

                row.createCell(3).setCellValue(ct.getSoLuong());

                Cell priceCell = row.createCell(4);
                priceCell.setCellValue(ct.getDonGia());
                priceCell.setCellStyle(currencyStyle);

                Cell totalCell = row.createCell(5);
                totalCell.setCellValue(ct.getThanhTien());
                totalCell.setCellStyle(currencyStyle);

                // Apply normal style to other cells
                for (int i = 0; i < 4; i++) {
                    row.getCell(i).setCellStyle(normalStyle);
                }
            }

            rowNum++; // Dòng trống

            // Tổng tiền
            createSummaryRow(sheet, rowNum++, "Tổng tiền:", hoaDon.getTongTien(), boldStyle, currencyStyle);
            createSummaryRow(sheet, rowNum++, "Giảm giá:", hoaDon.getGiamGia(), boldStyle, currencyStyle);
            createSummaryRow(sheet, rowNum++, "THANH TOÁN:", hoaDon.getThanhTien(), titleStyle, currencyStyle);

            // Ghi chú
            if (hoaDon.getGhiChu() != null && !hoaDon.getGhiChu().isEmpty()) {
                rowNum++;
                createRow(sheet, rowNum++, "Ghi chú: " + hoaDon.getGhiChu(), normalStyle);
            }

            rowNum += 2;
            createRow(sheet, rowNum++, "Cảm ơn quý khách!", boldStyle);

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xuất danh sách nhiều hóa đơn ra file Excel
     */
    public static boolean exportMultipleInvoices(List<HoaDon> hoaDonList, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách hóa đơn");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            int rowNum = 0;

            // Tiêu đề
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("DANH SÁCH HÓA ĐƠN");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

            rowNum++; // Dòng trống

            // Header
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"STT", "Mã HĐ", "Ngày lập", "Khách hàng", "Tổng tiền", "Giảm giá", "Thanh toán"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            KhachHangDAO khDAO = new KhachHangDAO();
            int stt = 1;
            double tongDoanhThu = 0;

            for (HoaDon hd : hoaDonList) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(stt++);
                row.createCell(1).setCellValue(hd.getMaHD());
                row.createCell(2).setCellValue(
                        hd.getNgayLap() != null ? hd.getNgayLap().format(DATE_FORMATTER) : ""
                );

                KhachHang kh = khDAO.timKhachHangTheoMa(hd.getMaKH());
                row.createCell(3).setCellValue(kh != null ? kh.getTenKH() : "");

                Cell tongTienCell = row.createCell(4);
                tongTienCell.setCellValue(hd.getTongTien());
                tongTienCell.setCellStyle(currencyStyle);

                Cell giamGiaCell = row.createCell(5);
                giamGiaCell.setCellValue(hd.getGiamGia());
                giamGiaCell.setCellStyle(currencyStyle);

                Cell thanhTienCell = row.createCell(6);
                thanhTienCell.setCellValue(hd.getThanhTien());
                thanhTienCell.setCellStyle(currencyStyle);

                tongDoanhThu += hd.getThanhTien();

                // Apply normal style to text cells
                for (int i = 0; i < 4; i++) {
                    row.getCell(i).setCellStyle(normalStyle);
                }
            }

            // Tổng kết
            rowNum++;
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(5);
            totalLabelCell.setCellValue("TỔNG DOANH THU:");
            totalLabelCell.setCellStyle(createBoldStyle(workbook));

            Cell totalValueCell = totalRow.createCell(6);
            totalValueCell.setCellValue(tongDoanhThu);
            totalValueCell.setCellStyle(currencyStyle);

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper methods for creating styles
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private static CellStyle createNormalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = createNormalStyle(workbook);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0 \"đ\""));
        return style;
    }

    private static CellStyle createBoldStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }

    private static void createRow(Sheet sheet, int rowNum, String value, CellStyle style) {
        Row row = sheet.createRow(rowNum);
        Cell cell = row.createCell(0);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private static void createInfoRow(Sheet sheet, int rowNum, String label, String value,
                                      CellStyle labelStyle, CellStyle valueStyle) {
        Row row = sheet.createRow(rowNum);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(labelStyle);

        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(valueStyle);
    }

    private static void createSummaryRow(Sheet sheet, int rowNum, String label, double value,
                                         CellStyle labelStyle, CellStyle valueStyle) {
        Row row = sheet.createRow(rowNum);
        Cell labelCell = row.createCell(4);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(labelStyle);

        Cell valueCell = row.createCell(5);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(valueStyle);
    }
}