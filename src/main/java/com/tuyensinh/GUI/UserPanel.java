package com.tuyensinh.GUI;

import com.tuyensinh.BUS.UserBUS;
import com.tuyensinh.DTO.UserDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UserPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private UserBUS userBUS = new UserBUS();

    private JButton btnAdd, btnDelete, btnRefresh;

    public UserPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 249, 250)); // Nền xám sáng hiện đại
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Tiêu đề trang
        JLabel lblTitle = new JLabel("QUẢN LÝ NGƯỜI DÙNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        // Nội dung chính
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        RoundedPanel mainPanel = new RoundedPanel(20, Color.WHITE);
        mainPanel.setLayout(new BorderLayout(10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // ===== TOOLBAR =====
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setOpaque(false);

        JLabel lblListTitle = new JLabel("Danh Sách Tài Khoản");
        lblListTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblListTitle.setForeground(new Color(80, 80, 80));

        // Action Panel (Bên phải)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        btnAdd = createFlatButton("Thêm mới", new Color(46, 204, 113), new Color(39, 174, 96));
        btnDelete = createFlatButton("Xóa", new Color(231, 76, 60), new Color(192, 57, 43));
        btnRefresh = createFlatButton("Làm Mới", new Color(240, 240, 240), new Color(220, 220, 220));
        btnRefresh.setForeground(new Color(80, 80, 80));

        actionPanel.add(btnAdd);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);

        toolBar.add(lblListTitle, BorderLayout.WEST);
        toolBar.add(actionPanel, BorderLayout.EAST);
        mainPanel.add(toolBar, BorderLayout.NORTH);

        // ===== BẢNG DỮ LIỆU =====
        String[] cols = {"ID", "Tên đăng nhập", "Quyền hạn"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Chống sửa trực tiếp
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setSelectionBackground(new Color(220, 235, 252));
        table.setSelectionForeground(new Color(30, 30, 30));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(240, 240, 240));

        // Tùy chỉnh Header
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(new Color(80, 80, 80));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        table.getColumnModel().getColumn(0).setMaxWidth(80); // Cột ID nhỏ lại

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== SỰ KIỆN =====
        btnAdd.addActionListener(e -> openAddDialog());
        btnDelete.addActionListener(e -> deleteUser());
        btnRefresh.addActionListener(e -> loadData());

        return mainPanel;
    }

    // ================= CUSTOM NÚT PHẲNG =================
    private JButton createFlatButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(110, 38));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { btn.setBackground(hoverColor); }
            public void mouseExited(MouseEvent evt) { btn.setBackground(bgColor); }
        });
        return btn;
    }

    // ================= LOAD DATA =================
    private void loadData() {
        model.setRowCount(0);
        List<UserDTO> list = userBUS.getAll();

        if (list != null) {
            for (UserDTO u : list) {
                model.addRow(new Object[]{
                        u.getId(),
                        u.getUsername(),
                        u.getRole()
                });
            }
        }
    }

    // ================= FORM THÊM USER MỚI =================
    private void openAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Tài Khoản Mới", true);
        dialog.setSize(450, 380);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());

        // Header Form
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        JLabel lblFormTitle = new JLabel("THÊM TÀI KHOẢN");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblFormTitle.setForeground(new Color(50, 50, 50));
        headerPanel.add(lblFormTitle, BorderLayout.WEST);
        dialog.add(headerPanel, BorderLayout.NORTH);

        // Body Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 30, 20, 30));

        JTextField txtUsername = createStyledTextField();
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JComboBox<UserDTO.Role> cboRole = new JComboBox<>(UserDTO.Role.values());
        cboRole.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cboRole.setBackground(Color.WHITE);
        cboRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        cboRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(createLabeledComponent("Tên đăng nhập:"));
        formPanel.add(txtUsername);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createLabeledComponent("Mật khẩu:"));
        formPanel.add(txtPassword);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createLabeledComponent("Quyền hạn:"));
        formPanel.add(cboRole);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Footer Form
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 30, 20, 30));

        JButton btnSave = createFlatButton("Tạo Tài Khoản", new Color(46, 204, 113), new Color(39, 174, 96));
        btnSave.setPreferredSize(new Dimension(140, 40));
        JButton btnCancel = createFlatButton("Hủy bỏ", new Color(230, 230, 230), new Color(210, 210, 210));
        btnCancel.setForeground(new Color(80, 80, 80));

        btnSave.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            UserDTO.Role role = (UserDTO.Role) cboRole.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ Tên đăng nhập và Mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserDTO user = new UserDTO();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);

            if (userBUS.insert(user)) {
                JOptionPane.showMessageDialog(dialog, "Thêm tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thêm thất bại. Có thể tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    
    // Hàm phụ trợ tạo Label cho Form
    private JLabel createLabeledComponent(String labelText) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(100, 100, 100));
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // Hàm phụ trợ tạo Textfield cho Form
    private JTextField createStyledTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return txt;
    }

    // ================= DELETE USER =================
    private void deleteUser() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tài khoản này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            int id = (int) model.getValueAt(row, 0);

            if (userBUS.delete(id)) {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ================= CLASS CUSTOM BO GÓC =================
    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Đổ bóng mờ nhẹ
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Nền trắng
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            // Viền nhạt
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }
}