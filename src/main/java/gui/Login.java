package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import dao.AccountDAO;
import dao.ConnectDB;
import dao.NhanVienDAO;
import model.Account;
import model.NhanVien;
import java.time.LocalDate;

public class Login extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JCheckBox chkShowPassword;


    private AccountDAO accountDAO;
    private NhanVienDAO nhanVienDAO;

    public Login() {
        accountDAO = new AccountDAO();
        nhanVienDAO = new NhanVienDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Đăng nhập - Hệ thống Quản lý Cửa hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(30, 41, 59);
                Color color2 = new Color(51, 65, 85);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        JPanel loginCard = createLoginCard();
        mainPanel.add(loginCard);

        setContentPane(mainPanel);
    }

    private JPanel createLoginCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(40, 40, 40, 40)
        ));
        card.setPreferredSize(new Dimension(500, 500));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);

        JLabel lblLogo = new JLabel("👔");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("Đăng nhập");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(30, 41, 59));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Cửa hàng Thời Trang");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(100, 116, 139));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblLogo);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(lblSubtitle);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblUsername = new JLabel("Tên đăng nhập");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsername.setForeground(new Color(51, 65, 85));
        lblUsername.setPreferredSize(new Dimension(120, 30));
        formPanel.add(lblUsername, gbc);

        // Username Field
        gbc.gridx = 1;
        gbc.weightx = 1;
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(250, 40));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblPassword = new JLabel("Mật khẩu");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPassword.setForeground(new Color(51, 65, 85));
        lblPassword.setPreferredSize(new Dimension(120, 30));
        formPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(250, 40));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        txtPassword.addActionListener(e -> handleLogin());
        formPanel.add(txtPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 5, 8, 5);
        chkShowPassword = new JCheckBox("Hiển thị mật khẩu");
        chkShowPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkShowPassword.setForeground(new Color(100, 116, 139));
        chkShowPassword.setBackground(Color.WHITE);
        chkShowPassword.setFocusPainted(false);
        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });
        formPanel.add(chkShowPassword, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        buttonPanel.setBackground(Color.WHITE);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(59, 130, 246));
        btnLogin.setPreferredSize(new Dimension(380, 42));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(37, 99, 235));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(59, 130, 246));
            }
        });

        btnLogin.addActionListener(e -> handleLogin());
        buttonPanel.add(btnLogin);

        // Register Link Panel
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblRegisterText = new JLabel("Chưa có tài khoản? ");
        lblRegisterText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRegisterText.setForeground(new Color(100, 116, 139));

        JLabel lblRegisterLink = new JLabel("Đăng ký ngay");
        lblRegisterLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRegisterLink.setForeground(new Color(59, 130, 246));
        lblRegisterLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegisterLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showRegisterDialog();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblRegisterLink.setForeground(new Color(37, 99, 235));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblRegisterLink.setForeground(new Color(59, 130, 246));
            }
        });

        registerPanel.add(lblRegisterText);
        registerPanel.add(lblRegisterLink);


        card.add(headerPanel, BorderLayout.NORTH);
        card.add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(registerPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showRegisterDialog() {
        JDialog registerDialog = new JDialog(this, "Đăng ký tài khoản", true);
        registerDialog.setSize(500, 550);
        registerDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));


        JLabel lblTitle = new JLabel("Đăng ký nhân viên mới");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(30, 41, 59));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(0, 0, 25, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        JTextField txtRegMaNV = createRegisterTextField();
        JTextField txtRegTenNV = createRegisterTextField();
        JTextField txtRegSDT = createRegisterTextField();
        JTextField txtRegEmail = createRegisterTextField();
        JTextField txtRegDiaChi = createRegisterTextField();
        JPasswordField txtRegPassword = createRegisterPasswordField();
        JPasswordField txtRegConfirmPassword = createRegisterPasswordField();
        txtRegMaNV.setEnabled(true);
        txtRegMaNV.setText("");

        addRegisterField(formPanel, gbc, 0, "Mã nhân viên:", txtRegMaNV);
        addRegisterField(formPanel, gbc, 1, "Tên nhân viên:", txtRegTenNV);
        addRegisterField(formPanel, gbc, 2, "Số điện thoại:", txtRegSDT);
        addRegisterField(formPanel, gbc, 3, "Email:", txtRegEmail);
        addRegisterField(formPanel, gbc, 4, "Địa chỉ:", txtRegDiaChi);
        addRegisterField(formPanel, gbc, 5, "Mật khẩu:", txtRegPassword);
        addRegisterField(formPanel, gbc, 6, "Xác nhận mật khẩu:", txtRegConfirmPassword);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnRegister = createRegisterButton("Đăng ký", new Color(16, 185, 129));
        JButton btnCancel = createRegisterButton("Hủy", new Color(100, 116, 139));

        btnRegister.addActionListener(e -> {
            String maNV = txtRegMaNV.getText().trim();
            String tenNV = txtRegTenNV.getText().trim();
            String sdt = txtRegSDT.getText().trim();
            String email = txtRegEmail.getText().trim();
            String diaChi = txtRegDiaChi.getText().trim();
            String password = new String(txtRegPassword.getPassword());
            String confirmPassword = new String(txtRegConfirmPassword.getPassword());

            if (maNV.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog,
                        "Vui lòng nhập mã nhân viên!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (tenNV.isEmpty() || sdt.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog,
                        "Vui lòng điền đầy đủ thông tin bắt buộc!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(registerDialog,
                        "Mật khẩu xác nhận không khớp!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(registerDialog,
                        "Mật khẩu phải có ít nhất 6 ký tự!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = accountDAO.register(maNV, tenNV, sdt, email, diaChi, password);

            if (success) {
                JOptionPane.showMessageDialog(registerDialog,
                        "Đăng ký thành công!\n" +
                                "Tên đăng nhập: " + maNV + "\n" +
                                "Mật khẩu: " + password + "\n\n" +
                                "Vui lòng đăng nhập với thông tin trên.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                txtUsername.setText(maNV);
                txtPassword.setText("");
                txtPassword.requestFocus();

                registerDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(registerDialog,
                        "Đăng ký thất bại!\nMã nhân viên có thể đã tồn tại.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> registerDialog.dispose());

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);

        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        registerDialog.add(mainPanel);
        registerDialog.setVisible(true);
    }

    private void addRegisterField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(51, 65, 85));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JTextField createRegisterTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(250, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private JPasswordField createRegisterPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(250, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private JButton createRegisterButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập tên đăng nhập!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập mật khẩu!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Đang đăng nhập...");

        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account account = accountDAO.login(username, password);

                if (account != null) {
                    JOptionPane.showMessageDialog(Login.this,
                            "Đăng nhập thành công!\nChào mừng " + account.getFullName() +
                                    "\nVai trò: " + account.getRole(),
                            "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);

                    dispose();
                    EventQueue.invokeLater(() -> {
                        Dashboard dashboard = new Dashboard();
                        dashboard.setVisible(true);
                    });
                } else {
                    JOptionPane.showMessageDialog(Login.this,
                            "Tên đăng nhập hoặc mật khẩu không đúng!",
                            "Lỗi đăng nhập",
                            JOptionPane.ERROR_MESSAGE);
                    txtPassword.setText("");
                    txtPassword.requestFocus();
                }

                btnLogin.setEnabled(true);
                btnLogin.setText("Đăng nhập");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            try {
                new ConnectDB();
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Không thể kết nối database!\n" + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}