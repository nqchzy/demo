package gui;

import dao.SanPhamDAO;
import model.SanPham;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Comparator;

public class ProductManagementPanel extends JPanel {

    private JTextField txtMaSP, txtTenSP, txtGiaNhap, txtGiaBan, txtSoLuong, txtNgaySanXuat;
    private JComboBox<String> cboLoaiSP, cboSize;
    private JTextArea txtMoTa;
    private JTable table;
    private DefaultTableModel tableModel;

    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnTim, btnSapXep;
    private JTextField txtTimKiem;
    private JComboBox<String> cboLocLoai;

    private SanPhamDAO sanPhamDAO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private List<SanPham> currentList;

    public ProductManagementPanel() {
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

        JLabel lblTitle = new JLabel("📦 Quản lý Sản phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(30, 41, 59));

        panel.add(lblTitle, BorderLayout.WEST);
        return panel;
    }

    private JPanel createMainContent() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(0, 30, 30, 30));

        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.add(createFormPanel());

        panel.add(formContainer, BorderLayout.NORTH);
        panel.add(createTablePanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblFormTitle = new JLabel("Thông tin sản phẩm");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblFormTitle.setForeground(new Color(30, 41, 59));
        lblFormTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 5, 8);

        txtMaSP = createTextField();
        txtMaSP.setEnabled(false);
        txtMaSP.setBackground(new Color(241, 245, 249));

        txtTenSP = createTextField();
        txtGiaNhap = createTextField();
        txtGiaBan = createTextField();
        txtSoLuong = createTextField();
        txtNgaySanXuat = createTextField();
        txtNgaySanXuat.setToolTipText("Định dạng: dd/MM/yyyy");

        String[] loaiSP = {"Áo Thun", "Quần Short", "Áo Khoác", "Quần Jean"};
        cboLoaiSP = new JComboBox<>(loaiSP);
        styleComboBox(cboLoaiSP);

        String[] sizes = {"S", "M", "L", "XL", "XXL"};
        cboSize = new JComboBox<>(sizes);
        styleComboBox(cboSize);

        txtMoTa = new JTextArea(2, 20);
        txtMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMoTa.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        scrollMoTa.setBorder(new LineBorder(new Color(203, 213, 225), 1, true));
        scrollMoTa.setPreferredSize(new Dimension(0, 50));

        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.gridwidth = 1;
        formFields.add(createLabel("Mã sản phẩm"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        formFields.add(txtMaSP, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formFields.add(createLabel("Tên sản phẩm"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridwidth = 2;
        formFields.add(txtTenSP, gbc);

        gbc.gridx = 5; gbc.weightx = 0; gbc.gridwidth = 1;
        formFields.add(createLabel("Loại SP"), gbc);
        gbc.gridx = 6; gbc.weightx = 0.2;
        formFields.add(cboLoaiSP, gbc);

        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        formFields.add(createLabel("Giá nhập"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        formFields.add(txtGiaNhap, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formFields.add(createLabel("Giá bán"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.2;
        formFields.add(txtGiaBan, gbc);

        gbc.gridx = 4; gbc.weightx = 0;
        formFields.add(createLabel("Size"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.15;
        formFields.add(cboSize, gbc);

        gbc.gridx = 6; gbc.weightx = 0;
        formFields.add(createLabel("Số lượng"), gbc);
        gbc.gridx = 7; gbc.weightx = 0.15;
        formFields.add(txtSoLuong, gbc);

        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.gridwidth = 1;
        formFields.add(createLabel("Ngày SX"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        formFields.add(txtNgaySanXuat, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formFields.add(createLabel("Mô tả"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; gbc.gridwidth = 5;
        formFields.add(scrollMoTa, gbc);

        JPanel buttonPanel = createButtonPanel();

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(Color.WHITE);
        content.add(formFields, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);

        container.add(lblFormTitle, BorderLayout.NORTH);
        container.add(content, BorderLayout.CENTER);

        return container;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(Color.WHITE);

        btnThem = createButton("Thêm", new Color(16, 185, 129));
        btnSua = createButton("Sửa", new Color(59, 130, 246));
        btnXoa = createButton("Xóa", new Color(239, 68, 68));
        btnLuu = createButton("Lưu", new Color(16, 185, 129));
        btnHuy = createButton("Hủy", new Color(100, 116, 139));

        btnLuu.setEnabled(false);
        btnHuy.setEnabled(false);

        btnThem.addActionListener(e -> handleThem());
        btnSua.addActionListener(e -> handleSua());
        btnXoa.addActionListener(e -> handleXoa());
        btnLuu.addActionListener(e -> handleLuu());
        btnHuy.addActionListener(e -> handleHuy());

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLuu);
        panel.add(btnHuy);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblTableTitle = new JLabel("Danh sách sản phẩm");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTableTitle.setForeground(new Color(30, 41, 59));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(Color.WHITE);

        String[] loaiFilter = {"Tất cả", "Áo Thun", "Quần Short", "Áo Khoác", "Quần Jean"};
        cboLocLoai = new JComboBox<>(loaiFilter);
        styleComboBox(cboLocLoai);
        cboLocLoai.setPreferredSize(new Dimension(150, 32));

        txtTimKiem = createTextField();
        txtTimKiem.setPreferredSize(new Dimension(220, 32));
        txtTimKiem.setToolTipText("Tìm theo tên sản phẩm");

        btnTim = createButton("Tìm", new Color(59, 130, 246));
        btnTim.addActionListener(e -> handleTimKiem());

        // NÚT SẮP XẾP
        btnSapXep = createButton("⇅ Sắp xếp", new Color(139, 92, 246));
        btnSapXep.setPreferredSize(new Dimension(110, 32));
        btnSapXep.addActionListener(e -> showSortMenu());

        searchPanel.add(cboLocLoai);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTim);
        searchPanel.add(btnSapXep);

        headerPanel.add(lblTableTitle, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        String[] columns = {
                "Mã SP", "Tên sản phẩm", "Loại SP",
                "Giá Nhập", "Giá Bán", "Size",
                "Ngày SX", "Số Lượng", "Mô tả"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        styleTable(table);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    fillFormFromTable(row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(226, 232, 240), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        container.add(headerPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);

        return container;
    }

    // MENU SẮP XẾP
    private void showSortMenu() {
        JPopupMenu sortMenu = new JPopupMenu();
        sortMenu.setBorder(new LineBorder(new Color(226, 232, 240), 1));

        String[] sortOptions = {
                "↑ Mã SP (A-Z)",
                "↓ Mã SP (Z-A)",
                "↑ Tên (A-Z)",
                "↓ Tên (Z-A)",
                "↑ Giá bán (Thấp → Cao)",
                "↓ Giá bán (Cao → Thấp)",
                "↑ Số lượng (Ít → Nhiều)",
                "↓ Số lượng (Nhiều → Ít)",
                "↑ Ngày SX (Cũ → Mới)",
                "↓ Ngày SX (Mới → Cũ)"
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

        List<SanPham> sortedList = new java.util.ArrayList<>(currentList);

        switch (option) {
            case "↑ Mã SP (A-Z)":
                sortedList.sort(Comparator.comparing(SanPham::getMaSP));
                break;
            case "↓ Mã SP (Z-A)":
                sortedList.sort(Comparator.comparing(SanPham::getMaSP).reversed());
                break;
            case "↑ Tên (A-Z)":
                sortedList.sort(Comparator.comparing(SanPham::getTenSP));
                break;
            case "↓ Tên (Z-A)":
                sortedList.sort(Comparator.comparing(SanPham::getTenSP).reversed());
                break;
            case "↑ Giá bán (Thấp → Cao)":
                sortedList.sort(Comparator.comparing(SanPham::getGiaBan));
                break;
            case "↓ Giá bán (Cao → Thấp)":
                sortedList.sort(Comparator.comparing(SanPham::getGiaBan).reversed());
                break;
            case "↑ Số lượng (Ít → Nhiều)":
                sortedList.sort(Comparator.comparing(SanPham::getSoluong));
                break;
            case "↓ Số lượng (Nhiều → Ít)":
                sortedList.sort(Comparator.comparing(SanPham::getSoluong).reversed());
                break;
            case "↑ Ngày SX (Cũ → Mới)":
                sortedList.sort(Comparator.comparing(
                        sp -> sp.getNgaySanXuat() != null ? sp.getNgaySanXuat() : LocalDate.MIN
                ));
                break;
            case "↓ Ngày SX (Mới → Cũ)":
                sortedList.sort(Comparator.comparing(
                        sp -> sp.getNgaySanXuat() != null ? sp.getNgaySanXuat() : LocalDate.MIN,
                        Comparator.reverseOrder()
                ));
                break;
        }

        displayProducts(sortedList);
    }

    private void displayProducts(List<SanPham> list) {
        currentList = list;
        tableModel.setRowCount(0);
        for (SanPham sp : list) {
            tableModel.addRow(new Object[]{
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getMaLoai(),
                    String.format("%,.0f đ", sp.getGiaNhap()),
                    String.format("%,.0f đ", sp.getGiaBan()),
                    sp.getSize(),
                    sp.getNgaySanXuat() != null ? sp.getNgaySanXuat().format(formatter) : "",
                    sp.getSoluong(),
                    sp.getMoTa()
            });
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(51, 65, 85));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(0, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(4, 10, 4, 10)
        ));
        return field;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(0, 32));
        combo.setBackground(Color.WHITE);
        combo.setBorder(new LineBorder(new Color(203, 213, 225), 1, true));
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(85, 32));
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

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(new Color(30, 41, 59));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(51, 65, 85));
        header.setPreferredSize(new Dimension(0, 45));
        header.setBorder(new MatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(50);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);
        table.getColumnModel().getColumn(7).setPreferredWidth(70);
        table.getColumnModel().getColumn(8).setPreferredWidth(150);
    }

    private void handleThem() {
        enableFormEditing(true);
        clearForm();
        btnLuu.setEnabled(true);
        btnHuy.setEnabled(true);
        btnThem.setEnabled(false);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        txtMaSP.setText("");
        txtMaSP.setEnabled(true);
    }

    private void handleSua() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
            return;
        }
        enableFormEditing(true);
        btnLuu.setEnabled(true);
        btnHuy.setEnabled(true);
        btnThem.setEnabled(false);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
    }

    private void handleXoa() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa sản phẩm này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String maSP = txtMaSP.getText();
            if (sanPhamDAO.xoaSanPham(maSP)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    private void handleLuu() {
        if (!validateForm()) {
            return;
        }

        try {
            SanPham sp = new SanPham(
                    txtMaSP.getText(),
                    txtTenSP.getText(),
                    cboLoaiSP.getSelectedItem().toString(),
                    Double.parseDouble(txtGiaNhap.getText()),
                    Double.parseDouble(txtGiaBan.getText()),
                    cboSize.getSelectedItem().toString(),
                    Integer.parseInt(txtSoLuong.getText()),
                    LocalDate.parse(txtNgaySanXuat.getText(), formatter),
                    txtMoTa.getText()
            );

            boolean success;
            if (table.getSelectedRow() >= 0) {
                success = sanPhamDAO.capNhatSanPham(sp);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                }
            } else {
                success = sanPhamDAO.themSanPham(sp);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Thêm mới thành công!");
                }
            }

            if (success) {
                loadData();
                handleHuy();
            } else {
                JOptionPane.showMessageDialog(this, "Lưu thất bại!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleHuy() {
        enableFormEditing(false);
        clearForm();
        table.clearSelection();
        btnLuu.setEnabled(false);
        btnHuy.setEnabled(false);
        btnThem.setEnabled(true);
        btnSua.setEnabled(true);
        btnXoa.setEnabled(true);
    }

    private void handleTimKiem() {
        String keyword = txtTimKiem.getText().trim();
        String loai = cboLocLoai.getSelectedItem().toString();

        List<SanPham> list;

        if (loai.equals("Tất cả")) {
            list = keyword.isEmpty()
                    ? sanPhamDAO.getAllSanPham()
                    : sanPhamDAO.timSanPhamTheoTen(keyword);
        } else {
            list = sanPhamDAO.getAllSanPham();
            list.removeIf(sp -> !sp.getMaLoai().equals(loai));

            if (!keyword.isEmpty()) {
                list.removeIf(sp -> !sp.getTenSP().toLowerCase().contains(keyword.toLowerCase()));
            }
        }

        displayProducts(list);
    }

    private void fillFormFromTable(int row) {
        txtMaSP.setText(table.getValueAt(row, 0).toString());
        txtTenSP.setText(table.getValueAt(row, 1).toString());
        cboLoaiSP.setSelectedItem(table.getValueAt(row, 2));

        String giaNhap = table.getValueAt(row, 3).toString().replaceAll("[^0-9]", "");
        txtGiaNhap.setText(giaNhap);

        String giaBan = table.getValueAt(row, 4).toString().replaceAll("[^0-9]", "");
        txtGiaBan.setText(giaBan);

        cboSize.setSelectedItem(table.getValueAt(row, 5));
        txtNgaySanXuat.setText(table.getValueAt(row, 6).toString());
        txtSoLuong.setText(table.getValueAt(row, 7).toString());
        txtMoTa.setText(table.getValueAt(row, 8).toString());
    }

    private void clearForm() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        cboLoaiSP.setSelectedIndex(0);
        txtGiaNhap.setText("");
        txtGiaBan.setText("");
        cboSize.setSelectedIndex(0);
        txtNgaySanXuat.setText("");
        txtSoLuong.setText("");
        txtMoTa.setText("");
    }

    private void enableFormEditing(boolean enabled) {
        txtTenSP.setEnabled(enabled);
        cboLoaiSP.setEnabled(enabled);
        txtGiaNhap.setEnabled(enabled);
        txtGiaBan.setEnabled(enabled);
        cboSize.setEnabled(enabled);
        txtNgaySanXuat.setEnabled(enabled);
        txtSoLuong.setEnabled(enabled);
        txtMoTa.setEnabled(enabled);
    }

    private boolean validateForm() {
        if (txtTenSP.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm!");
            return false;
        }

        try {
            Double.parseDouble(txtGiaNhap.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá nhập phải là số!");
            return false;
        }

        try {
            Double.parseDouble(txtGiaBan.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá bán phải là số!");
            return false;
        }

        try {
            Integer.parseInt(txtSoLuong.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
            return false;
        }

        try {
            LocalDate.parse(txtNgaySanXuat.getText(), formatter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày sản xuất không đúng định dạng (dd/MM/yyyy)!");
            return false;
        }

        return true;
    }

    private void loadData() {
        List<SanPham> list = sanPhamDAO.getAllSanPham();
        displayProducts(list);
    }
}