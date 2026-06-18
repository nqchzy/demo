package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import dao.NhaCungCapDAO;
import model.NhaCungCap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Comparator;

public class SupplierManagementPanel extends JPanel {

    private JTextField txtMaNCC, txtTenNCC, txtSDT, txtEmail, txtDiaChi, txtNguoiLienHe, txtNgayHopTac;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnTim, btnSapXep;
    private JTextField txtTimKiem;

    private NhaCungCapDAO nhaCungCapDAO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private List<NhaCungCap> currentList;

    public SupplierManagementPanel() {
        nhaCungCapDAO = new NhaCungCapDAO();
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

        JLabel lblTitle = new JLabel("🏭 Quản lý Nhà cung cấp");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(30, 41, 59));

        panel.add(lblTitle, BorderLayout.WEST);
        return panel;
    }

    private JPanel createMainContent() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(0, 30, 30, 30));

        panel.add(createFormPanel(), BorderLayout.NORTH);
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

        JLabel lblTitle = new JLabel("Thông tin nhà cung cấp");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(30, 41, 59));
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        txtMaNCC = createTextField();
        txtMaNCC.setEnabled(false);
        txtMaNCC.setBackground(new Color(241, 245, 249));

        txtTenNCC = createTextField();
        txtSDT = createTextField();
        txtEmail = createTextField();
        txtDiaChi = createTextField();
        txtNguoiLienHe = createTextField();
        txtNgayHopTac = createTextField();
        txtNgayHopTac.setToolTipText("Định dạng: dd/MM/yyyy");

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 5, 8);

        // Row 0
        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.gridwidth = 1;
        form.add(createLabel("Mã NCC"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        form.add(txtMaNCC, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        form.add(createLabel("Tên nhà cung cấp"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridwidth = 3;
        form.add(txtTenNCC, gbc);

        // Row 1
        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        form.add(createLabel("Số điện thoại"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        form.add(txtSDT, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        form.add(createLabel("Email"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.2;
        form.add(txtEmail, gbc);

        gbc.gridx = 4; gbc.weightx = 0;
        form.add(createLabel("Người liên hệ"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.2;
        form.add(txtNguoiLienHe, gbc);

        // Row 2
        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.gridwidth = 1;
        form.add(createLabel("Địa chỉ"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6; gbc.gridwidth = 3;
        form.add(txtDiaChi, gbc);

        gbc.gridx = 4; gbc.weightx = 0; gbc.gridwidth = 1;
        form.add(createLabel("Ngày hợp tác"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.2;
        form.add(txtNgayHopTac, gbc);

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(Color.WHITE);
        content.add(form, BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        container.add(lblTitle, BorderLayout.NORTH);
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

        JLabel lblTableTitle = new JLabel("Danh sách nhà cung cấp");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTableTitle.setForeground(new Color(30, 41, 59));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(Color.WHITE);

        txtTimKiem = createTextField();
        txtTimKiem.setPreferredSize(new Dimension(220, 32));
        txtTimKiem.setToolTipText("Tìm theo tên nhà cung cấp");

        btnTim = createButton("Tìm", new Color(59, 130, 246));
        btnTim.addActionListener(e -> handleTimKiem());

        btnSapXep = createButton("⇅ Sắp xếp", new Color(139, 92, 246));
        btnSapXep.setPreferredSize(new Dimension(110, 32));
        btnSapXep.addActionListener(e -> showSortMenu());

        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTim);
        searchPanel.add(btnSapXep);

        headerPanel.add(lblTableTitle, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        String[] columns = {"Mã NCC", "Tên nhà cung cấp", "Số điện thoại",
                "Email", "Người liên hệ", "Địa chỉ", "Ngày hợp tác"};

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
                "↑ Mã NCC (A-Z)",
                "↓ Mã NCC (Z-A)",
                "↑ Tên (A-Z)",
                "↓ Tên (Z-A)",
                "↑ Ngày hợp tác (Cũ → Mới)",
                "↓ Ngày hợp tác (Mới → Cũ)"
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

        List<NhaCungCap> sortedList = new java.util.ArrayList<>(currentList);

        switch (option) {
            case "↑ Mã NCC (A-Z)":
                sortedList.sort(Comparator.comparing(NhaCungCap::getMaNCC));
                break;
            case "↓ Mã NCC (Z-A)":
                sortedList.sort(Comparator.comparing(NhaCungCap::getMaNCC).reversed());
                break;
            case "↑ Tên (A-Z)":
                sortedList.sort(Comparator.comparing(NhaCungCap::getTenNCC));
                break;
            case "↓ Tên (Z-A)":
                sortedList.sort(Comparator.comparing(NhaCungCap::getTenNCC).reversed());
                break;
            case "↑ Ngày hợp tác (Cũ → Mới)":
                sortedList.sort(Comparator.comparing(
                        ncc -> ncc.getNgayHopTac() != null ? ncc.getNgayHopTac() : LocalDate.MIN
                ));
                break;
            case "↓ Ngày hợp tác (Mới → Cũ)":
                sortedList.sort(Comparator.comparing(
                        ncc -> ncc.getNgayHopTac() != null ? ncc.getNgayHopTac() : LocalDate.MIN,
                        Comparator.reverseOrder()
                ));
                break;
        }

        displaySuppliers(sortedList);
    }

    private void displaySuppliers(List<NhaCungCap> list) {
        currentList = list;
        tableModel.setRowCount(0);
        for (NhaCungCap ncc : list) {
            tableModel.addRow(new Object[]{
                    ncc.getMaNCC(),
                    ncc.getTenNCC(),
                    ncc.getSdt(),
                    ncc.getEmail(),
                    ncc.getNguoiLienHe(),
                    ncc.getDiaChi(),
                    ncc.getNgayHopTac() != null ? ncc.getNgayHopTac().format(formatter) : ""
            });
        }
    }

    private JTextField createTextField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(0, 32));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(4, 10, 4, 10)
        ));
        return f;
    }

    private JLabel createLabel(String text) {
        JLabel lb = new JLabel(text);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lb.setForeground(new Color(51, 65, 85));
        return lb;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(85, 32));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(color.darker());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
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
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(200);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
    }

    private void handleThem() {
        enableFormEditing(true);
        clearForm();
        btnLuu.setEnabled(true);
        btnHuy.setEnabled(true);
        btnThem.setEnabled(false);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        txtMaNCC.setText("");
        txtMaNCC.setEnabled(true);
    }

    private void handleSua() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần sửa!");
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa nhà cung cấp này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String maNCC = txtMaNCC.getText();
            if (nhaCungCapDAO.xoaNhaCungCap(maNCC)) {
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
            NhaCungCap ncc = new NhaCungCap(
                    txtMaNCC.getText(),
                    txtTenNCC.getText(),
                    txtSDT.getText(),
                    txtEmail.getText(),
                    txtDiaChi.getText(),
                    txtNguoiLienHe.getText(),
                    LocalDate.parse(txtNgayHopTac.getText(), formatter)
            );

            boolean success;
            if (table.getSelectedRow() >= 0) {
                success = nhaCungCapDAO.capNhatNhaCungCap(ncc);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                }
            } else {
                success = nhaCungCapDAO.themNhaCungCap(ncc);
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

        List<NhaCungCap> list = keyword.isEmpty()
                ? nhaCungCapDAO.getAllNhaCungCap()
                : nhaCungCapDAO.timNhaCungCapTheoTen(keyword);

        displaySuppliers(list);
    }

    private void fillFormFromTable(int row) {
        txtMaNCC.setText(table.getValueAt(row, 0).toString());
        txtTenNCC.setText(table.getValueAt(row, 1).toString());
        txtSDT.setText(table.getValueAt(row, 2).toString());
        txtEmail.setText(table.getValueAt(row, 3).toString());
        txtNguoiLienHe.setText(table.getValueAt(row, 4).toString());
        txtDiaChi.setText(table.getValueAt(row, 5).toString());
        txtNgayHopTac.setText(table.getValueAt(row, 6).toString());
    }

    private void clearForm() {
        txtMaNCC.setText("");
        txtTenNCC.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtNguoiLienHe.setText("");
        txtDiaChi.setText("");
        txtNgayHopTac.setText("");
    }

    private void enableFormEditing(boolean enabled) {
        txtTenNCC.setEnabled(enabled);
        txtSDT.setEnabled(enabled);
        txtEmail.setEnabled(enabled);
        txtNguoiLienHe.setEnabled(enabled);
        txtDiaChi.setEnabled(enabled);
        txtNgayHopTac.setEnabled(enabled);
    }

    private boolean validateForm() {
        if (txtTenNCC.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhà cung cấp!");
            return false;
        }

        if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!");
            return false;
        }

        try {
            LocalDate.parse(txtNgayHopTac.getText(), formatter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày hợp tác không đúng định dạng (dd/MM/yyyy)!");
            return false;
        }

        return true;
    }

    private void loadData() {
        List<NhaCungCap> list = nhaCungCapDAO.getAllNhaCungCap();
        displaySuppliers(list);
    }
}