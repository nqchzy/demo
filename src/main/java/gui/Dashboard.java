package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Map;
import dao.ThongKeDAO;

public class Dashboard extends JFrame {

    private JPanel mainContentPanel;
    private JPanel sidebar;

    private JButton btnHome, btnProducts, btnEmployees, btnReports, btnCustomers, btnSuppliers, btnLogout;

    private ThongKeDAO thongKeDAO;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    public Dashboard() {
        thongKeDAO = new ThongKeDAO();
        initializeFrame();
        setupLayout();
    }

    private void initializeFrame() {
        setTitle("Hệ thống Quản lý Cửa hàng - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
    }

    private void setupLayout() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(248, 250, 252));
        setContentPane(contentPane);

        sidebar = createSidebar();
        contentPane.add(sidebar, BorderLayout.WEST);

        mainContentPanel = createDashboardPanel();
        contentPane.add(mainContentPanel, BorderLayout.CENTER);
    }

    // ================= SIDEBAR =================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(30, 41, 59));
        sidebar.setPreferredSize(new Dimension(250, 0));

        sidebar.add(createLogoPanel(), BorderLayout.NORTH);
        sidebar.add(createMenuPanel(), BorderLayout.CENTER);

        return sidebar;
    }

    private JPanel createLogoPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 41, 59));
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblLogo = new JLabel(
                "<html><div style='text-align:center'>" +
                        "<span style='font-size:24px'>👔</span><br/>" +
                        "<span style='font-size:16px; font-weight:bold'>Cửa hàng Thời trang</span>" +
                        "</div></html>"
        );
        lblLogo.setForeground(Color.WHITE);
        panel.add(lblLogo);

        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 1, 0, 8));
        panel.setBackground(new Color(30, 41, 59));
        panel.setBorder(new EmptyBorder(10, 15, 20, 15));

        btnHome = createMenuButton("Trang chủ");
        btnProducts = createMenuButton("Sản phẩm");
        btnEmployees = createMenuButton("Nhân viên");
        btnReports = createMenuButton("Hóa đơn");
        btnCustomers = createMenuButton("Khách hàng");
        btnSuppliers = createMenuButton("Nhà cung cấp");
        btnLogout = createMenuButton("Đăng xuất");

        btnHome.setBackground(new Color(51, 65, 85));

        btnHome.addActionListener(e -> showDashboard());
        btnProducts.addActionListener(e -> showProducts());
        btnEmployees.addActionListener(e -> showEmployees());
        btnReports.addActionListener(e -> showReports());
        btnCustomers.addActionListener(e -> showCustomers());
        btnSuppliers.addActionListener(e -> showSuppliers());
        btnLogout.addActionListener(e -> handleLogout());

        panel.add(btnHome);
        panel.add(btnProducts);
        panel.add(btnEmployees);
        panel.add(btnReports);
        panel.add(btnCustomers);
        panel.add(btnSuppliers);
        panel.add(btnLogout);

        return panel;
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 41, 59));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(12, 15, 12, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(new Color(51, 65, 85))) {
                    btn.setBackground(new Color(51, 65, 85));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActiveButton(btn)) {
                    btn.setBackground(new Color(30, 41, 59));
                }
            }
        });

        return btn;
    }

    private boolean isActiveButton(JButton btn) {
        return btn.getBackground().equals(new Color(51, 65, 85));
    }

    private void resetAllButtons() {
        Color defaultColor = new Color(30, 41, 59);
        btnHome.setBackground(defaultColor);
        btnProducts.setBackground(defaultColor);
        btnEmployees.setBackground(defaultColor);
        btnReports.setBackground(defaultColor);
        btnCustomers.setBackground(defaultColor);
        btnSuppliers.setBackground(defaultColor);
        btnLogout.setBackground(defaultColor);
    }

    private void switchPanel(JPanel newPanel, JButton activeButton) {
        getContentPane().remove(mainContentPanel);
        mainContentPanel = newPanel;
        getContentPane().add(mainContentPanel, BorderLayout.CENTER);

        resetAllButtons();
        if (activeButton != null) {
            activeButton.setBackground(new Color(51, 65, 85));
        }

        revalidate();
        repaint();
    }

    private void showDashboard() {
        switchPanel(createDashboardPanel(), btnHome);
        setTitle("Hệ thống Quản lý Cửa hàng - Dashboard");
    }

    private void showProducts() {
        switchPanel(new ProductManagementPanel(), btnProducts);
        setTitle("Hệ thống Quản lý Cửa hàng - Sản phẩm");
    }

    private void showEmployees() {
        switchPanel(new EmployeeManagementPanel(), btnEmployees);
        setTitle("Hệ thống Quản lý Cửa hàng - Nhân viên");
    }

    private void showReports() {
        switchPanel(new InvoiceManagementPanel(), btnReports);
        setTitle("Hệ thống Quản lý Cửa hàng - Hóa đơn");
    }

    private void showCustomers() {
        switchPanel(new CustomerManagementPanel(), btnCustomers);
        setTitle("Hệ thống Quản lý Cửa hàng - Khách hàng");
    }

    private void showSuppliers() {
        switchPanel(new SupplierManagementPanel(), btnSuppliers);
        setTitle("Hệ thống Quản lý Cửa hàng - Nhà cung cấp");
    }


    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));

        panel.add(createHeader(), BorderLayout.NORTH);
        panel.add(createChartArea(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(25, 30, 20, 30));

        JLabel lblTitle = new JLabel("Bảng điều khiển");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(30, 41, 59));

        JLabel lblSubtitle = new JLabel("Tổng quan hoạt động của bạn");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(100, 116, 139));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(248, 250, 252));

        textPanel.add(lblTitle);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(lblSubtitle);

        panel.add(textPanel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createChartArea() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(248, 250, 252));
        container.setBorder(new EmptyBorder(0, 30, 30, 30));

        JPanel chartCard = new JPanel(new BorderLayout());
        chartCard.setBackground(Color.WHITE);
        chartCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("Doanh thu theo tháng - Năm " + java.time.LocalDate.now().getYear());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 41, 59));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        chartCard.add(lblTitle, BorderLayout.NORTH);
        chartCard.add(new RevenueChartPanel(), BorderLayout.CENTER);

        container.add(chartCard);
        return container;
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có muốn đăng xuất?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new Login().setVisible(true);
        }
    }


    class RevenueChartPanel extends JPanel {
        private Map<Integer, Double> data;
        private double maxValue;
        private final String[] monthNames = {
                "T1", "T2", "T3", "T4", "T5", "T6",
                "T7", "T8", "T9", "T10", "T11", "T12"
        };

        public RevenueChartPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(0, 350));
            loadData();
        }

        private void loadData() {
            data = thongKeDAO.getDoanhThuTheoThang();
            maxValue = data.values().stream().mapToDouble(Double::doubleValue).max().orElse(1_000_000);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int padding = 60;

            g2.setColor(new Color(226, 232, 240));
            g2.drawLine(padding, padding, padding, h - padding);
            g2.drawLine(padding, h - padding, w - padding, h - padding);

            int barWidth = (w - 2 * padding) / 12 - 10;

            for (int i = 1; i <= 12; i++) {
                double value = data.getOrDefault(i, 0.0);
                int barHeight = (int) ((value / maxValue) * (h - 2 * padding));
                int x = padding + (i - 1) * (barWidth + 10) + 10;
                int y = h - padding - barHeight;

                g2.setPaint(new GradientPaint(
                        x, y, new Color(59, 130, 246),
                        x, h - padding, new Color(147, 197, 253)
                ));
                g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);

                g2.setColor(new Color(100, 116, 139));
                g2.drawString(monthNames[i - 1], x + 8, h - padding + 20);
            }
        }
    }
}
