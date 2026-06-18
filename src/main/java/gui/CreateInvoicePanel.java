package gui;

import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

public class CreateInvoicePanel extends JDialog {

    private JTextField txtMaHD, txtMaNV, txtTenNV;
    private JTextField txtMaKH, txtTenKH, txtSDT;
    private JTextField txtMaSP, txtTenSP, txtDonGia, txtSoLuong;
    private JTextField txtTongTien, txtGiamGia, txtThanhTien;
    private JTextArea txtGhiChu;

    private JTextField txtNgayLap;
    private JComboBox<String> cboTrangThai, cboHinhThucTT;

    private JTable tableCT;
    private DefaultTableModel modelCT;

    private JButton btnThemSP, btnLuu, btnHuy;
    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    private NhanVienDAO nhanVienDAO = new NhanVienDAO();

    public CreateInvoicePanel(Frame owner) {
        super(owner, "Tạo hóa đơn mới", true);
        setSize(900, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        taoMaHoaDonMoi();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel row1 = new JPanel(new GridLayout(1, 6, 5, 5));
        txtMaHD = createField(false);
        txtMaNV = createField(false);
        txtTenNV = createField(false);

        row1.add(new JLabel("Mã HĐ"));
        row1.add(txtMaHD);
        row1.add(new JLabel("Mã NV"));
        row1.add(txtMaNV);
        row1.add(new JLabel("Tên NV"));
        row1.add(txtTenNV);

        JPanel row2 = new JPanel(new GridLayout(1, 6, 5, 5));
        txtMaKH = createField(true);
        txtTenKH = createField(false);
        txtSDT = createField(false);

        row2.add(new JLabel("Mã KH"));
        row2.add(txtMaKH);
        row2.add(new JLabel("Tên KH"));
        row2.add(txtTenKH);
        row2.add(new JLabel("SĐT"));
        row2.add(txtSDT);

        txtMaKH.addActionListener(e -> loadKhachHang());

        JPanel row3 = new JPanel(new GridLayout(1, 4, 5, 5));
        txtNgayLap = createField(false);
        txtNgayLap.setText(LocalDateTime.now().toString());

        cboTrangThai = new JComboBox<>(new String[]{
                "Chưa thanh toán", "Đã thanh toán"
        });

        row3.add(new JLabel("Ngày lập"));
        row3.add(txtNgayLap);
        row3.add(new JLabel("Trạng thái"));
        row3.add(cboTrangThai);

        JPanel row4 = new JPanel(new GridLayout(1, 8, 5, 5));
        txtMaSP = createField(true);
        txtTenSP = createField(false);
        txtDonGia = createField(false);
        txtSoLuong = createField(true);

        cboHinhThucTT = new JComboBox<>(new String[]{
                "Tiền mặt", "Chuyển khoản", "QR"
        });

        row4.add(new JLabel("Mã SP"));
        row4.add(txtMaSP);
        row4.add(new JLabel("Tên SP"));
        row4.add(txtTenSP);
        row4.add(new JLabel("Đơn giá"));
        row4.add(txtDonGia);
        row4.add(new JLabel("SL"));
        row4.add(txtSoLuong);

        panel.add(row1);
        panel.add(row2);
        panel.add(row3);
        panel.add(row4);

        txtMaSP.addActionListener(e -> loadSanPham());

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        modelCT = new DefaultTableModel(
                new String[]{"Mã SP", "Tên SP", "SL", "Đơn giá", "Thành tiền"}, 0
        );
        tableCT = new JTable(modelCT);
        tableCT.setRowHeight(30);

        panel.add(new JScrollPane(tableCT), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2, 4, 5, 5));
        bottom.setBorder(new EmptyBorder(10, 0, 0, 0));

        txtTongTien = createField(false);
        txtGiamGia = createField(true);
        txtThanhTien = createField(false);
        txtGhiChu = new JTextArea(2, 20);

        txtGiamGia.addActionListener(e -> tinhThanhTien());

        bottom.add(new JLabel("Tổng tiền"));
        bottom.add(txtTongTien);
        bottom.add(new JLabel("Giảm giá"));
        bottom.add(txtGiamGia);
        bottom.add(new JLabel("Thanh toán"));
        bottom.add(txtThanhTien);
        bottom.add(new JLabel("Ghi chú"));
        bottom.add(new JScrollPane(txtGhiChu));

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnLuu = new JButton("Lưu hóa đơn");
        btnHuy = new JButton("Hủy");

        btnLuu.addActionListener(e -> luuHoaDon());
        btnHuy.addActionListener(e -> dispose());

        panel.add(btnHuy);
        panel.add(btnLuu);
        return panel;
    }

    private void taoMaHoaDonMoi() {
        txtMaHD.setText("HD" + System.currentTimeMillis());
        txtMaNV.setText("NV01");
        txtTenNV.setText("Nhân viên demo");
    }

    private void loadKhachHang() {
        KhachHang kh = khachHangDAO.timKhachHangTheoMa(txtMaKH.getText());
        if (kh != null) {
            txtTenKH.setText(kh.getTenKH());
            txtSDT.setText(kh.getSdt());
        }
    }

    private void loadSanPham() {
        SanPham sp = sanPhamDAO.timSanPhamTheoMa(txtMaSP.getText());
        if (sp != null) {
            txtTenSP.setText(sp.getTenSP());
            txtDonGia.setText(String.valueOf(sp.getGiaBan()));
        }
    }

    private void themSanPham() {
        int sl = Integer.parseInt(txtSoLuong.getText());
        double gia = Double.parseDouble(txtDonGia.getText());
        double thanhTien = sl * gia;

        modelCT.addRow(new Object[]{
                txtMaSP.getText(),
                txtTenSP.getText(),
                sl,
                gia,
                thanhTien
        });

        tinhTongTien();
    }

    private void tinhTongTien() {
        double tong = 0;
        for (int i = 0; i < modelCT.getRowCount(); i++) {
            tong += (double) modelCT.getValueAt(i, 4);
        }
        txtTongTien.setText(String.valueOf(tong));
        tinhThanhTien();
    }

    private void tinhThanhTien() {
        double tong = Double.parseDouble(txtTongTien.getText());
        double giam = txtGiamGia.getText().isEmpty() ? 0 : Double.parseDouble(txtGiamGia.getText());
        txtThanhTien.setText(String.valueOf(tong - giam));
    }

    private void luuHoaDon() {
        HoaDon hd = new HoaDon(
                txtMaHD.getText(),
                txtMaNV.getText(),
                txtMaKH.getText(),
                LocalDateTime.now(),
                Double.parseDouble(txtTongTien.getText()),
                Double.parseDouble(txtGiamGia.getText()),
                Double.parseDouble(txtThanhTien.getText()),
                txtGhiChu.getText()
        );

        if (hoaDonDAO.themHoaDon(hd)) {
            for (int i = 0; i < modelCT.getRowCount(); i++) {
                ChiTietHoaDon ct = new ChiTietHoaDon(
                        txtMaHD.getText(),
                        modelCT.getValueAt(i, 0).toString(),
                        (int) modelCT.getValueAt(i, 2),
                        (double) modelCT.getValueAt(i, 3)
                );
                hoaDonDAO.themChiTietHoaDon(ct);
            }
            JOptionPane.showMessageDialog(this, "Lưu hóa đơn thành công!");
            dispose();
        }
    }

    private JTextField createField(boolean editable) {
        JTextField f = new JTextField();
        f.setEditable(editable);
        return f;
    }
}
