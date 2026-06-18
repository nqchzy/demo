package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import dao.*;
import model.*;
import utils.ExcelExporter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Comparator;

public class InvoiceManagementPanel extends JPanel {
    private JTextField txtMaHD, txtMaNV, txtTenNV, txtMaKH, txtTenKH, txtSDT;
    private JTextField txtTongTien, txtGiamGia, txtThanhTien;
    private JTextArea txtGhiChu;

    private JTable tableHoaDon;
    private DefaultTableModel modelHoaDon;
    private JTable tableChiTiet;
    private DefaultTableModel modelChiTiet;

    private JButton btnTaoMoi, btnXoa, btnTim, btnRefresh, btnThongKe, btnSapXep;
    private JButton btnXuatExcel, btnXuatTatCa;
    private JTextField txtTimKiem;
    private JComboBox<String> cboLocThang;

    private HoaDonDAO hoaDonDAO;
    private NhanVienDAO nhanVienDAO;
    private KhachHangDAO khachHangDAO;
    private SanPhamDAO sanPhamDAO;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private List<HoaDon> currentList;

    public InvoiceManagementPanel() {
        hoaDonDAO = new HoaDonDAO();
        nhanVienDAO = new NhanVienDAO();
        khachHangDAO = new KhachHangDAO();
        sanPhamDAO = new SanPhamDAO();

        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadData();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(25, 30, 20, 30));

        JLabel lblTitle = new JLabel("Quản lý Hóa đơn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(30, 41, 59));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(248, 250, 252));

        btnXuatTatCa = createHeaderButton("Xuất tất cả", new Color(139, 92, 246));
        btnXuatTatCa.addActionListener(e -> xuatTatCaHoaDon());

        btnThongKe = createHeaderButton("Thống kê", new Color(59, 130, 246));
        btnThongKe.addActionListener(e -> xemThongKe());

        btnTaoMoi = createHeaderButton("Tạo hóa đơn", new Color(16, 185, 129));
        btnTaoMoi.addActionListener(e -> taoHoaDonMoi());

        rightPanel.add(btnXuatTatCa);
        rightPanel.add(btnThongKe);
        rightPanel.add(btnTaoMoi);

        panel.add(lblTitle, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createMainContent() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(0, 30, 30, 30));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createLeftPanel());
        splitPane.setRightComponent(createRightPanel());
        splitPane.setDividerLocation(750);
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(null);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(new Color(248, 250, 252));

        panel.add(createInfoPanel(), BorderLayout.NORTH);
        panel.add(createInvoiceTablePanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("Thông tin hóa đơn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 41, 59));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 5, 4, 5);

        txtMaHD = createSmallTextField(); txtMaHD.setEditable(false);
        txtMaNV = createSmallTextField(); txtMaNV.setEditable(false);
        txtTenNV = createSmallTextField(); txtTenNV.setEditable(false);
        txtMaKH = createSmallTextField(); txtMaKH.setEditable(false);
        txtTenKH = createSmallTextField(); txtTenKH.setEditable(false);
        txtSDT = createSmallTextField(); txtSDT.setEditable(false);
        txtTongTien = createSmallTextField(); txtTongTien.setEditable(false);
        txtGiamGia = createSmallTextField(); txtGiamGia.setEditable(false);
        txtThanhTien = createSmallTextField(); txtThanhTien.setEditable(false);

        txtThanhTien.setFont(new Font("Segoe UI", Font.BOLD, 13));

        txtGhiChu = new JTextArea(2, 20);
        txtGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        txtGhiChu.setEditable(false);

        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        formFields.add(createSmallLabel("Mã HD"), gbc);
        gbc.gridx = 1; formFields.add(txtMaHD, gbc);

        gbc.gridx = 2; formFields.add(createSmallLabel("Mã NV"), gbc);
        gbc.gridx = 3; formFields.add(txtMaNV, gbc);

        gbc.gridx = 4; formFields.add(createSmallLabel("Tên NV"), gbc);
        gbc.gridx = 5; formFields.add(txtTenNV, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        formFields.add(createSmallLabel("Mã KH"), gbc);
        gbc.gridx = 1; formFields.add(txtMaKH, gbc);

        gbc.gridx = 2; formFields.add(createSmallLabel("Tên KH"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 3;
        formFields.add(txtTenKH, gbc);
        gbc.gridwidth = 1;

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        formFields.add(createSmallLabel("Tổng tiền"), gbc);
        gbc.gridx = 1; formFields.add(txtTongTien, gbc);

        gbc.gridx = 2; formFields.add(createSmallLabel("Giảm giá"), gbc);
        gbc.gridx = 3; formFields.add(txtGiamGia, gbc);

        gbc.gridx = 4; formFields.add(createSmallLabel("Thanh toán"), gbc);
        gbc.gridx = 5; formFields.add(txtThanhTien, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 3;
        formFields.add(createSmallLabel("Ghi chú"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 5;
        formFields.add(scrollGhiChu, gbc);

        container.add(lblTitle, BorderLayout.NORTH);
        container.add(formFields, BorderLayout.CENTER);
        return container;
    }

    private JPanel createInvoiceTablePanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("Danh sách hóa đơn");

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 41, 59));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setBackground(Color.WHITE);

        String[] thangFilter = {
                "Tất cả",
                "Tháng này",
                "Tháng trước",
                "3 tháng gần đây"
        };

        cboLocThang = new JComboBox<>(thangFilter);
        styleComboBox(cboLocThang);
        cboLocThang.setPreferredSize(new Dimension(150, 28));
        cboLocThang.addActionListener(e -> handleTimKiem());

        txtTimKiem = createSmallTextField();
        txtTimKiem.setPreferredSize(new Dimension(180, 28));
        txtTimKiem.setToolTipText("Tìm theo mã hóa đơn");

        btnTim = createSmallButton(" Tìm", new Color(59, 130, 246));

        btnSapXep = createSmallButton("⇅ Sắp xếp", new Color(139, 92, 246));
        btnSapXep.setPreferredSize(new Dimension(100, 28));
        btnSapXep.addActionListener(e -> showSortMenu());

        btnRefresh = createSmallButton("↻", new Color(100, 116, 139));
        btnRefresh.setPreferredSize(new Dimension(32, 28));
        btnRefresh.setToolTipText("Làm mới");

        btnRefresh.addActionListener(e -> {
            txtTimKiem.setText("");
            cboLocThang.setSelectedIndex(0);
            loadData();
        });

        searchPanel.add(cboLocThang);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTim);
        searchPanel.add(btnSapXep);
        searchPanel.add(btnRefresh);

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        String[] columns = {
                "Mã HD",
                "Ngày lập",
                "Khách hàng",
                "Tổng tiền",
                "Thanh toán"
        };

        modelHoaDon = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableHoaDon = new JTable(modelHoaDon);
        styleSmallTable(tableHoaDon);

        tableHoaDon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableHoaDon.getSelectedRow();
                if (row >= 0) {
                    fillFormFromTable(row);
                    loadChiTietHoaDon();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableHoaDon);
        scrollPane.setBorder(new LineBorder(new Color(226, 232, 240), 1));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnXuatExcel = createSmallButton("Xuất Excel", new Color(139, 92, 246));
        btnXuatExcel.setPreferredSize(new Dimension(110, 28));
        btnXuatExcel.addActionListener(e -> xuatHoaDonRaExcel());

        btnXoa = createSmallButton(" Xóa", new Color(239, 68, 68));
        btnXoa.addActionListener(e -> handleXoa());

        buttonPanel.add(btnXuatExcel);
        buttonPanel.add(btnXoa);

        container.add(headerPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        return container;
    }

    private void showSortMenu() {
        JPopupMenu sortMenu = new JPopupMenu();
        sortMenu.setBorder(new LineBorder(new Color(226, 232, 240), 1));

        String[] sortOptions = {
                "↑ Mã HD (A-Z)",
                "↓ Mã HD (Z-A)",
                "↑ Ngày lập (Cũ → Mới)",
                "↓ Ngày lập (Mới → Cũ)",
                "↑ Tổng tiền (Thấp → Cao)",
                "↓ Tổng tiền (Cao → Thấp)",
                "↑ Thanh toán (Thấp → Cao)",
                "↓ Thanh toán (Cao → Thấp)"
        };


        for (String option : sortOptions) {
            JMenuItem item = new JMenuItem(option);
            item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            item.addActionListener(e -> handleSort(option));
            sortMenu.add(item);
        }

        sortMenu.show(btnSapXep, 0, btnSapXep.getHeight());
    }

    private void handleSort(String option) {
        if (currentList == null || currentList.isEmpty()) {
            return;
        }

        List<HoaDon> sortedList = new java.util.ArrayList<>(currentList);

        switch (option) {
            case "↑ Mã HD (A-Z)":
                sortedList.sort(Comparator.comparing(HoaDon::getMaHD));
                break;

            case "↓ Mã HD (Z-A)":
                sortedList.sort(Comparator.comparing(HoaDon::getMaHD).reversed());
                break;

            case "↑ Ngày lập (Cũ → Mới)":
                sortedList.sort(Comparator.comparing(
                        hd -> hd.getNgayLap() != null ? hd.getNgayLap() : LocalDateTime.MIN
                ));
                break;

            case "↓ Ngày lập (Mới → Cũ)":
                sortedList.sort(Comparator.comparing(
                        hd -> hd.getNgayLap() != null ? hd.getNgayLap() : LocalDateTime.MIN,
                        Comparator.reverseOrder()
                ));
                break;

            case "↑ Tổng tiền (Thấp → Cao)":
                sortedList.sort(Comparator.comparing(HoaDon::getTongTien));
                break;

            case "↓ Tổng tiền (Cao → Thấp)":
                sortedList.sort(Comparator.comparing(HoaDon::getTongTien).reversed());
                break;

            case "↑ Thanh toán (Thấp → Cao)":
                sortedList.sort(Comparator.comparing(HoaDon::getThanhTien));
                break;

            case "↓ Thanh toán (Cao → Thấp)":
                sortedList.sort(Comparator.comparing(HoaDon::getThanhTien).reversed());
                break;
        }


        displayHoaDon(sortedList);
    }

    private JPanel createRightPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("Chi tiết hóa đơn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 41, 59));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        String[] columns = {
                "Mã SP",
                "Tên sản phẩm",
                "SL",
                "Đơn giá",
                "Thành tiền"
        };

        modelChiTiet = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChiTiet = new JTable(modelChiTiet);
        styleSmallTable(tableChiTiet);

        JScrollPane scrollPane = new JScrollPane(tableChiTiet);
        scrollPane.setBorder(new LineBorder(new Color(226, 232, 240), 1));

        container.add(lblTitle, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    private void xuatHoaDonRaExcel() {
        int row = tableHoaDon.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xuất!");
            return;
        }

        String maHD = txtMaHD.getText().trim();
        if (maHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có hóa đơn được chọn!");
            return;
        }

        HoaDon hoaDon = hoaDonDAO.timHoaDonTheoMa(maHD);
        if (hoaDon == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin hóa đơn!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu hóa đơn");
        fileChooser.setSelectedFile(new File("HoaDon_" + maHD + ".xlsx"));
        FileNameExtensionFilter filter =
                new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);


        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            if (ExcelExporter.exportSingleInvoice(hoaDon, filePath)) {
                int result = JOptionPane.showConfirmDialog(
                        this,
                        "Xuất hóa đơn thành công!\nBạn có muốn mở file?",
                        "Thành công",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (result == JOptionPane.YES_OPTION) {
                    try {
                        Desktop.getDesktop().open(new File(filePath));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Không thể mở file: " + ex.getMessage()
                        );
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Xuất hóa đơn thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        }
    }

    private void xuatTatCaHoaDon() {
        if (currentList == null || currentList.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không có hóa đơn nào để xuất!"
            );
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Xuất danh sách hóa đơn");

        fileChooser.setSelectedFile(new File("DanhSachHoaDon.xlsx"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            if (ExcelExporter.exportMultipleInvoices(currentList, filePath)) {
                int result = JOptionPane.showConfirmDialog(
                        this,
                        "Xuất " + currentList.size() + " hóa đơn thành công!\nBạn có muốn mở file?",
                        "Thành công",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (result == JOptionPane.YES_OPTION) {
                    try {
                        Desktop.getDesktop().open(new File(filePath));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Không thể mở file: " + ex.getMessage()
                        );
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Xuất danh sách thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        }
        }


    private JLabel createSmallLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(51, 65, 85));
        return label;
    }

    private JTextField createSmallTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setPreferredSize(new Dimension(0, 28));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(3, 8, 3, 8)
        ));
        return field;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setPreferredSize(new Dimension(0, 28));
        combo.setBackground(Color.WHITE);
        combo.setBorder(new LineBorder(new Color(203, 213, 225), 1, true));
    }

    private JButton createHeaderButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(150, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(bgColor.darker());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private JButton createSmallButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(text.length() > 5 ? 100 : 70, 28));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(bgColor.darker());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private void styleSmallTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(new Color(30, 41, 59));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(51, 65, 85));
        header.setPreferredSize(new Dimension(0, 38));
    }

    private void taoHoaDonMoi() {
        CreateInvoicePanel dialog = new CreateInvoicePanel((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        loadData();
    }

    private void xemThongKe() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tuNgay = null;
        LocalDateTime denNgay = now;
        String khoangThoiGian = "";

        String selected = cboLocThang.getSelectedItem().toString();
            switch (selected) {
                case "Tháng này":
                    tuNgay = now.withDayOfMonth(1)
                            .withHour(0).withMinute(0).withSecond(0);
                    khoangThoiGian = "tháng này";
                    break;

                case "Tháng trước":
                    tuNgay = now.minusMonths(1)
                            .withDayOfMonth(1)
                            .withHour(0).withMinute(0).withSecond(0);
                    denNgay = now.withDayOfMonth(1)
                            .minusDays(1)
                            .withHour(23).withMinute(59).withSecond(59);
                    khoangThoiGian = "tháng trước";
                    break;

                case "3 tháng gần đây":
                    tuNgay = now.minusMonths(3)
                            .withHour(0).withMinute(0).withSecond(0);
                    khoangThoiGian = "3 tháng gần đây";
                    break;

                default:
                    tuNgay = LocalDateTime.of(2000, 1, 1, 0, 0);
                    khoangThoiGian = "toàn bộ thời gian";
                    break;
            }


            double tongDoanhThu = hoaDonDAO.getTongDoanhThuTheoNgay(tuNgay, denNgay);
        List<HoaDon> danhSach = hoaDonDAO.getHoaDonTheoNgay(tuNgay, denNgay);
        int soHoaDon = danhSach.size();
        String message = String.format(
                "📊 THỐNG KÊ HÓA ĐƠN\n\n" +
                        "⏰ Khoảng thời gian: %s\n" +
                        "🧾 Số hóa đơn: %d\n" +
                        "💰 Tổng doanh thu: %,.0f đ\n" +
                        "📈 Trung bình/hóa đơn: %,.0f đ",
                khoangThoiGian,
                soHoaDon,
                tongDoanhThu,
                soHoaDon > 0 ? tongDoanhThu / soHoaDon : 0
        );

        JOptionPane.showMessageDialog(
                this,
                message,
                "Thống kê doanh thu",
                JOptionPane.INFORMATION_MESSAGE
        );

    }

    private void handleXoa() {
        int row = tableHoaDon.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn hóa đơn cần xóa!"
            );
            return;
        }


        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa hóa đơn này",
                "xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String maHD = txtMaHD.getText();
            if (hoaDonDAO.xoaHoaDon(maHD)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }

        }
    }

    private void handleTimKiem() {
        String keyword = txtTimKiem.getText().trim();
        String thangFilter = cboLocThang.getSelectedItem().toString();

        List<HoaDon> list;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tuNgay = null;
        LocalDateTime denNgay = now;

        switch (thangFilter) {
            case "Tháng này":
                tuNgay = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                break;
            case "Tháng trước":
                tuNgay = now.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                denNgay = now.withDayOfMonth(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);
                break;
            case "3 tháng gần đây":
                tuNgay = now.minusMonths(3).withHour(0).withMinute(0).withSecond(0);
                break;
        }

        if (tuNgay != null) {
            list = hoaDonDAO.getHoaDonTheoNgay(tuNgay, denNgay);
        } else if (!keyword.isEmpty()) {
            HoaDon hd = hoaDonDAO.timHoaDonTheoMa(keyword);
            list = new java.util.ArrayList<>();
            if (hd != null) {
                list.add(hd);
            }
        } else {
            list = hoaDonDAO.getAllHoaDon();
        }

        displayHoaDon(list);
    }

    private void displayHoaDon(List<HoaDon> list) {
        currentList = list;
        modelHoaDon.setRowCount(0);
        for (HoaDon hd : list) {
            String tenKH = "";
            KhachHang kh = khachHangDAO.timKhachHangTheoMa(hd.getMaKH());
            if (kh != null) {
                tenKH = kh.getTenKH();
            }

            Object[] row = {
                    hd.getMaHD(),
                    hd.getNgayLap() != null ? hd.getNgayLap().format(formatter) : "",
                    tenKH,
                    String.format("%,.0f đ", hd.getTongTien()),
                    String.format("%,.0f đ", hd.getThanhTien())
            };

            modelHoaDon.addRow(row);
        }
    }

    private void fillFormFromTable(int row) {
        try {
            String maHD = tableHoaDon.getValueAt(row, 0).toString();
            HoaDon hd = hoaDonDAO.timHoaDonTheoMa(maHD);

            if (hd != null) {
                txtMaHD.setText(hd.getMaHD());
                txtMaNV.setText(hd.getMaNV());
                txtMaKH.setText(hd.getMaKH());

                NhanVien nv = nhanVienDAO.timNhanVienTheoMa(hd.getMaNV());
                if (nv != null) {
                    txtTenNV.setText(nv.getTenNV());
                } else {
                    txtTenNV.setText("");
                }

                KhachHang kh = khachHangDAO.timKhachHangTheoMa(hd.getMaKH());
                if (kh != null) {
                    txtTenKH.setText(kh.getTenKH());
                    txtSDT.setText(kh.getSdt());
                } else {
                    txtTenKH.setText("");
                    txtSDT.setText("");
                }

                txtTongTien.setText(String.format("%,.0f Ä‘", hd.getTongTien()));
                txtGiamGia.setText(String.format("%,.0f Ä‘", hd.getGiamGia()));
                txtThanhTien.setText(String.format("%,.0f Ä‘", hd.getThanhTien()));
                txtGhiChu.setText(hd.getGhiChu() != null ? hd.getGhiChu() : "");
            } else {
                JOptionPane.showMessageDialog(this, "không tìm thấy thông tin hóa đơn!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi load thông tin " + e.getMessage());
        }
    }
    private void loadChiTietHoaDon() {
        String maHD = txtMaHD.getText().trim();
        if (maHD.isEmpty()) return;

        try {
            modelChiTiet.setRowCount(0);
            List<ChiTietHoaDon> list = hoaDonDAO.getChiTietHoaDon(maHD);

            for (ChiTietHoaDon ct : list) {
                String tenSP = hoaDonDAO.getTenSanPham(ct.getMaSP());

                Object[] row = {
                        ct.getMaSP(),
                        tenSP,
                        ct.getSoLuong(),
                        String.format("%,.0f đ", ct.getDonGia()),
                        String.format("%,.0f đ", ct.getThanhTien())
                };
                modelChiTiet.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi load chi tiết: " + e.getMessage());
        }
    }
    private void loadData() {
        List<HoaDon> list = hoaDonDAO.getAllHoaDon();
        displayHoaDon(list);
        modelChiTiet.setRowCount(0);
        clearForm();
    }
    private void clearForm() {
        txtMaHD.setText("");
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSDT.setText("");
        txtTongTien.setText("");
        txtGiamGia.setText("");
        txtThanhTien.setText("");
        txtGhiChu.setText("");
    }



}