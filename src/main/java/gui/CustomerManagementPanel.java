package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import dao.KhachHangDAO;
import model.KhachHang;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Comparator;

public class CustomerManagementPanel extends JPanel {

    private JTextField txtMaKH, txtTenKH, txtSDT, txtEmail, txtDiaChi, txtDiem;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnTim, btnSapXep;
    private JTextField txtTimKiem;

    private KhachHangDAO khachHangDAO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private List<KhachHang> currentList;

    public CustomerManagementPanel() {
        khachHangDAO = new KhachHangDAO();
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadData();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(25, 30, 20, 30));
        panel.setBackground(new Color(248, 250, 252));

        JLabel title = new JLabel("👤 Quản lý Khách hàng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 41, 59));

        panel.add(title, BorderLayout.WEST);
        return panel;
    }

    private JPanel createMainContent() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBorder(new EmptyBorder(0, 30, 30, 30));
        panel.setBackground(new Color(248, 250, 252));

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

        JLabel lblTitle = new JLabel("Thông tin khách hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(30, 41, 59));
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        txtMaKH = createTextField();
        txtMaKH.setEnabled(false);
        txtMaKH.setBackground(new Color(241, 245, 249));

        txtTenKH = createTextField();
        txtSDT = createTextField();
        txtEmail = createTextField();
        txtDiaChi = createTextField();
        txtDiem = createTextField();

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 8, 5, 8);

        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.gridwidth = 1;
        form.add(createLabel("Mã KH"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        form.add(txtMaKH, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        form.add(createLabel("Tên KH"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridwidth = 1;
        form.add(txtTenKH, gbc);

        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        form.add(createLabel("SĐT"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        form.add(txtSDT, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        form.add(createLabel("Email"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.2;
        form.add(txtEmail, gbc);

        gbc.gridx = 4; gbc.weightx = 0;
        form.add(createLabel("Điểm"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.2;
        form.add(txtDiem, gbc);

        gbc.weighty = 1;
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.gridwidth = 1;
        form.add(createLabel("Địa chỉ"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6; gbc.gridwidth = 5;
        form.add(txtDiaChi, gbc);

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

        JLabel lblTableTitle = new JLabel("Danh sách khách hàng");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTableTitle.setForeground(new Color(30, 41, 59));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(Color.WHITE);

        txtTimKiem = createTextField();
        txtTimKiem.setPreferredSize(new Dimension(220, 32));
        txtTimKiem.setToolTipText("Tìm theo tên khách hàng");

        btnTim = createButton("Tìm", new Color(59, 130, 246));
        btnTim.addActionListener(e -> handleTimKiem());

        // NÚT SẮP XẾP
        btnSapXep = createButton("⇅ Sắp xếp", new Color(139, 92, 246));
        btnSapXep.setPreferredSize(new Dimension(110, 32));
        btnSapXep.addActionListener(e -> showSortMenu());

        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTim);
        searchPanel.add(btnSapXep);

        headerPanel.add(lblTableTitle, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        String[] columns = {"Mã KH", "Tên KH", "SĐT", "Email", "Địa chỉ", "Điểm"};

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
                "↑ Mã KH (A-Z)",
                "↓ Mã KH (Z-A)",
                "↑ Tên (A-Z)",
                "↓ Tên (Z-A)",
                "↑ Điểm (Thấp → Cao)",
                "↓ Điểm (Cao → Thấp)"
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

        List<KhachHang> sortedList = new java.util.ArrayList<>(currentList);

        switch (option) {
            case "↑ Mã KH (A-Z)":
                sortedList.sort(Comparator.comparing(KhachHang::getMaKH));
                break;
            case "↓ Mã KH (Z-A)":
                sortedList.sort(Comparator.comparing(KhachHang::getMaKH).reversed());
                break;
            case "↑ Tên (A-Z)":
                sortedList.sort(Comparator.comparing(KhachHang::getTenKH));
                break;
            case "↓ Tên (Z-A)":
                sortedList.sort(Comparator.comparing(KhachHang::getTenKH).reversed());
                break;
            case "↑ Điểm (Thấp → Cao)":
                sortedList.sort(Comparator.comparing(KhachHang::getDiemTichLuy));
                break;
            case "↓ Điểm (Cao → Thấp)":
                sortedList.sort(Comparator.comparing(KhachHang::getDiemTichLuy).reversed());
                break;
        }

        displayCustomers(sortedList);
    }

    private void displayCustomers(List<KhachHang> list) {
        currentList = list;
        tableModel.setRowCount(0);
        for (KhachHang kh : list) {
            tableModel.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getTenKH(),
                    kh.getSdt(),
                    kh.getEmail(),
                    kh.getDiaChi(),
                    kh.getDiemTichLuy()
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

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(60);
    }

    private void handleThem() {
        enableFormEditing(true);
        clearForm();
        btnLuu.setEnabled(true);
        btnHuy.setEnabled(true);
        btnThem.setEnabled(false);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        txtMaKH.setText("");
        txtMaKH.setEnabled(true);
    }

    private void handleSua() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa khách hàng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String maKH = txtMaKH.getText();
            if (khachHangDAO.xoaKhachHang(maKH)) {
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
            int diem = 0;
            if (!txtDiem.getText().trim().isEmpty()) {
                diem = Integer.parseInt(txtDiem.getText());
            }

            KhachHang kh = new KhachHang(
                    txtMaKH.getText(),
                    txtTenKH.getText(),
                    txtSDT.getText(),
                    txtEmail.getText(),
                    txtDiaChi.getText()
            );

            kh.setDiemTichLuy(diem);

            boolean success;
            if (table.getSelectedRow() >= 0) {
                success = khachHangDAO.capNhatKhachHang(kh);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                }
            } else {
                success = khachHangDAO.themKhachHang(kh);
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

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số!");
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

        List<KhachHang> list = keyword.isEmpty()
                ? khachHangDAO.getAllKhachHang()
                : khachHangDAO.timKhachHangTheoTen(keyword);

        displayCustomers(list);
    }

    private void fillFormFromTable(int row) {
        txtMaKH.setText(table.getValueAt(row, 0).toString());
        txtTenKH.setText(table.getValueAt(row, 1).toString());
        txtSDT.setText(table.getValueAt(row, 2).toString());
        txtEmail.setText(table.getValueAt(row, 3).toString());
        txtDiaChi.setText(table.getValueAt(row, 4).toString());
        txtDiem.setText(table.getValueAt(row, 5).toString());
    }

    private void clearForm() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtDiem.setText("");
    }

    private void enableFormEditing(boolean enabled) {
        txtTenKH.setEnabled(enabled);
        txtSDT.setEnabled(enabled);
        txtEmail.setEnabled(enabled);
        txtDiaChi.setEnabled(enabled);
        txtDiem.setEnabled(enabled);
    }

    private boolean validateForm() {
        if (txtTenKH.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!");
            return false;
        }

        if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!");
            return false;
        }

        return true;
    }

    private void loadData() {
        List<KhachHang> list = khachHangDAO.getAllKhachHang();
        displayCustomers(list);
    }
}