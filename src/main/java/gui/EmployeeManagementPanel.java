package gui;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import dao.NhanVienDAO;
import model.NhanVien;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Comparator;

public class EmployeeManagementPanel extends JPanel {

    private JTextField txtMaNV, txtTenNV, txtSDT, txtEmail, txtDiaChi, txtNgayVaoLam, txtTenDangNhap;
    private JComboBox<String> cboChucVu;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnTim, btnSapXep;
    private JTextField txtTimKiem;
    private NhanVienDAO nhanVienDAO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private JComboBox<String> cboLocChucVu;
    private List<NhanVien> currentList; // Lưu danh sách hiện tại

    public EmployeeManagementPanel() {
        nhanVienDAO = new NhanVienDAO();
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

        JLabel lblTitle = new JLabel("Quản lý Nhân viên");
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

        JLabel lblFormTitle = new JLabel("Thông tin nhân viên");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblFormTitle.setForeground(new Color(30, 41, 59));
        lblFormTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 5, 8);

        txtMaNV = createTextField();
        txtMaNV.setEnabled(false);
        txtMaNV.setBackground(new Color(241, 245, 249));

        txtTenNV = createTextField();
        txtSDT = createTextField();
        txtEmail = createTextField();
        txtDiaChi = createTextField();
        txtNgayVaoLam = createTextField();
        txtNgayVaoLam.setToolTipText("Định dạng: dd/MM/yyyy");

        txtTenDangNhap = createTextField();
        txtTenDangNhap.setEnabled(false);
        txtTenDangNhap.setBackground(new Color(241, 245, 249));

        String[] chucVu = {"Quản lý", "Nhân viên bán hàng", "Nhân viên kho", "Thu ngân"};
        cboChucVu = new JComboBox<>(chucVu);
        styleComboBox(cboChucVu);

        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.gridwidth = 1;
        formFields.add(createLabel("Mã nhân viên"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        formFields.add(txtMaNV, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formFields.add(createLabel("Tên nhân viên"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridwidth = 1;
        formFields.add(txtTenNV, gbc);

        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        formFields.add(createLabel("Chức vụ"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        formFields.add(cboChucVu, gbc);

        gbc.weighty = 1;
        gbc.gridx = 2; gbc.weightx = 0;
        formFields.add(createLabel("Số điện thoại"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.2;
        formFields.add(txtSDT, gbc);

        gbc.weighty = 1;
        gbc.gridx = 4; gbc.weightx = 0;
        formFields.add(createLabel("Email"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.2;
        formFields.add(txtEmail, gbc);

        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.gridwidth = 1;
        formFields.add(createLabel("Địa chỉ"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6; gbc.gridwidth = 3;
        formFields.add(txtDiaChi, gbc);

        gbc.weighty = 1;
        gbc.gridx = 4; gbc.weightx = 0; gbc.gridwidth = 1;
        formFields.add(createLabel("Ngày vào làm"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.2;
        formFields.add(txtNgayVaoLam, gbc);

        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.gridwidth = 1;
        formFields.add(createLabel("Tên đăng nhập"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        formFields.add(txtTenDangNhap, gbc);

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

        JLabel lblTableTitle = new JLabel("Danh sách nhân viên");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTableTitle.setForeground(new Color(30, 41, 59));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(Color.WHITE);

        String[] chucVu = {"Tất cả", "Quản lý", "Nhân viên bán hàng", "Nhân viên kho", "Thu ngân"};
        cboLocChucVu = new JComboBox<>(chucVu);
        styleComboBox(cboLocChucVu);
        cboLocChucVu.setPreferredSize(new Dimension(180, 32));

        txtTimKiem = createTextField();
        txtTimKiem.setPreferredSize(new Dimension(220, 32));
        txtTimKiem.setToolTipText("Tìm theo tên nhân viên");

        btnTim = createButton("Tìm", new Color(59, 130, 246));
        btnTim.addActionListener(e -> handleTimKiem());

        // NÚT SẮP XẾP
        btnSapXep = createButton("⇅ Sắp xếp", new Color(139, 92, 246));
        btnSapXep.setPreferredSize(new Dimension(110, 32));
        btnSapXep.addActionListener(e -> showSortMenu());

        searchPanel.add(cboLocChucVu);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTim);
        searchPanel.add(btnSapXep);

        headerPanel.add(lblTableTitle, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        String[] columns = {
                "Mã NV", "Tên nhân viên", "Tên đăng nhập", "Chức vụ",
                "Số điện thoại", "Email", "Địa chỉ", "Ngày vào làm"
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
    private void showSortMenu() {
        JPopupMenu sortMenu = new JPopupMenu();
        sortMenu.setBorder(new LineBorder(new Color(226, 232, 240), 1));

        String[] sortOptions = {
                "↑ Mã NV (A-Z)",
                "↓ Mã NV (Z-A)",
                "↑ Tên (A-Z)",
                "↓ Tên (Z-A)",
                "↑ Chức vụ (A-Z)",
                "↓ Chức vụ (Z-A)",
                "↑ Ngày vào làm (Cũ → Mới)",
                "↓ Ngày vào làm (Mới → Cũ)"
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

        List<NhanVien> sortedList = new java.util.ArrayList<>(currentList);

        switch (option) {
            case "↑ Mã NV (A-Z)":
                sortedList.sort(Comparator.comparing(NhanVien::getMaNV));
                break;
            case "↓ Mã NV (Z-A)":
                sortedList.sort(Comparator.comparing(NhanVien::getMaNV).reversed());
                break;
            case "↑ Tên (A-Z)":
                sortedList.sort(Comparator.comparing(NhanVien::getTenNV));
                break;
            case "↓ Tên (Z-A)":
                sortedList.sort(Comparator.comparing(NhanVien::getTenNV).reversed());
                break;
            case "↑ Chức vụ (A-Z)":
                sortedList.sort(Comparator.comparing(NhanVien::getChucVu));
                break;
            case "↓ Chức vụ (Z-A)":
                sortedList.sort(Comparator.comparing(NhanVien::getChucVu).reversed());
                break;
            case "↑ Ngày vào làm (Cũ → Mới)":
                sortedList.sort(Comparator.comparing(
                        nv -> nv.getNgayVaoLam() != null ? nv.getNgayVaoLam() : LocalDate.MIN
                ));
                break;
            case "↓ Ngày vào làm (Mới → Cũ)":
                sortedList.sort(Comparator.comparing(
                        nv -> nv.getNgayVaoLam() != null ? nv.getNgayVaoLam() : LocalDate.MIN,
                        Comparator.reverseOrder()
                ));
                break;
        }

        displayEmployees(sortedList);
    }

    private void displayEmployees(List<NhanVien> list) {
        currentList = list;
        tableModel.setRowCount(0);
        for (NhanVien nv : list) {
            tableModel.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getTenDangNhap(),
                    nv.getChucVu(),
                    nv.getSdt(),
                    nv.getEmail(),
                    nv.getDiaChi(),
                    nv.getNgayVaoLam() != null
                            ? nv.getNgayVaoLam().format(formatter)
                            : ""
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
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        table.getColumnModel().getColumn(6).setPreferredWidth(200);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);
    }

    private void handleThem() {
        enableFormEditing(true);
        clearForm();
        btnLuu.setEnabled(true);
        btnHuy.setEnabled(true);
        btnThem.setEnabled(false);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        txtMaNV.setText("");
        txtMaNV.setEnabled(true);
        txtTenDangNhap.setText("");
    }

    private void handleSua() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa nhân viên này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String maNV = txtMaNV.getText();
            if (nhanVienDAO.xoaNhanVien(maNV)) {
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
            NhanVien nv = new NhanVien(
                    txtMaNV.getText(),
                    txtTenNV.getText(),
                    txtSDT.getText(),
                    txtEmail.getText(),
                    txtDiaChi.getText(),
                    cboChucVu.getSelectedItem().toString(),
                    LocalDate.parse(txtNgayVaoLam.getText(), formatter),
                    txtTenDangNhap.getText()
            );

            boolean success;
            if (table.getSelectedRow() >= 0) {
                success = nhanVienDAO.capNhatNhanVien(nv);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                }
            } else {
                success = nhanVienDAO.themNhanVien(nv);
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
        String chucVu = cboLocChucVu.getSelectedItem().toString();

        List<NhanVien> list;

        if (chucVu.equals("Tất cả")) {
            list = keyword.isEmpty()
                    ? nhanVienDAO.getAllNhanVien()
                    : nhanVienDAO.timNhanVienTheoTen(keyword);
        } else {
            list = nhanVienDAO.getNhanVienTheoChucVu(chucVu);
        }

        displayEmployees(list);
    }

    private void fillFormFromTable(int row) {
        txtMaNV.setText(table.getValueAt(row, 0).toString());
        txtTenNV.setText(table.getValueAt(row, 1).toString());
        txtTenDangNhap.setText(table.getValueAt(row, 2).toString());
        cboChucVu.setSelectedItem(table.getValueAt(row, 3));
        txtSDT.setText(table.getValueAt(row, 4).toString());
        txtEmail.setText(table.getValueAt(row, 5).toString());
        txtDiaChi.setText(table.getValueAt(row, 6).toString());
        txtNgayVaoLam.setText(table.getValueAt(row, 7).toString());
    }

    private void clearForm() {
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtTenDangNhap.setText("");
        cboChucVu.setSelectedIndex(0);
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtNgayVaoLam.setText("");
    }

    private void enableFormEditing(boolean enabled) {
        txtTenNV.setEnabled(enabled);
        cboChucVu.setEnabled(enabled);
        txtSDT.setEnabled(enabled);
        txtEmail.setEnabled(enabled);
        txtDiaChi.setEnabled(enabled);
        txtNgayVaoLam.setEnabled(enabled);
    }

    private boolean validateForm() {
        if (txtTenNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhân viên!");
            return false;
        }

        if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!");
            return false;
        }

        try {
            LocalDate.parse(txtNgayVaoLam.getText(), formatter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày vào làm không đúng định dạng (dd/MM/yyyy)!");
            return false;
        }

        return true;
    }

    private void loadData() {
        List<NhanVien> list = nhanVienDAO.getAllNhanVien();
        displayEmployees(list);
    }
}