package com.tuyensinh.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.tuyensinh.BUS.UserBUS;
import com.tuyensinh.DTO.UserDTO;

public class UserPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private UserBUS userBUS = new UserBUS();

    private JButton btnAdd, btnDelete, btnRefresh, btnEdit, btnChangePass, btnToggleStatus;
    
    // Tìm kiếm
    private JTextField txtSearch;
    
    // Phân trang
    private int currentPage = 1;
    private int itemsPerPage = 25;
    private int totalUsers = 0;
    private JLabel lblPageInfo;
    private JButton btnPrevPage, btnNextPage;

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

        // BÊN TRÁI: Tiêu đề + Ô tìm kiếm
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);

        JLabel lblListTitle = new JLabel("Danh Sách Tài Khoản");
        lblListTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblListTitle.setForeground(new Color(80, 80, 80));

        // Ô tìm kiếm có chữ mờ
        txtSearch = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(150, 150, 150));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString("Tìm kiếm tên đăng nhập...", 12, y);
                    g2.dispose();
                }
            }
        };
        txtSearch.setPreferredSize(new Dimension(250, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        
        // Sự kiện gõ tìm kiếm Real-time
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { doSearch(); }
            public void removeUpdate(DocumentEvent e) { doSearch(); }
            public void changedUpdate(DocumentEvent e) { doSearch(); }
        });

        leftPanel.add(lblListTitle);
        leftPanel.add(txtSearch);

        // BÊN PHẢI: Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionPanel.setOpaque(false);

        btnAdd = createFlatButton("Thêm mới", new Color(46, 204, 113), new Color(39, 174, 96));
        btnDelete = createFlatButton("Xóa", new Color(231, 76, 60), new Color(192, 57, 43));
        btnEdit = createFlatButton("Sửa", new Color(52, 152, 219), new Color(41, 128, 185));
        btnChangePass = createFlatButton("Đổi mật khẩu", new Color(155, 89, 182), new Color(142, 68, 173));
        btnToggleStatus = createFlatButton("Bật/Tắt", new Color(241, 196, 15), new Color(230, 126, 34));
        btnRefresh = createFlatButton("Làm Mới", new Color(240, 240, 240), new Color(220, 220, 220));
        btnRefresh.setForeground(new Color(80, 80, 80));

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnChangePass);
        actionPanel.add(btnToggleStatus);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);

        toolBar.add(leftPanel, BorderLayout.WEST);
        toolBar.add(actionPanel, BorderLayout.EAST);
        mainPanel.add(toolBar, BorderLayout.NORTH);

        // ===== BẢNG DỮ LIỆU =====
        String[] cols = {"ID", "Tên đăng nhập", "Quyền hạn", "Trạng thái"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(new Color(80, 80, 80));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        table.getColumnModel().getColumn(0).setMaxWidth(80);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== PHÂN TRANG =====
        JPanel paginationPanel = new JPanel(new BorderLayout());
        paginationPanel.setOpaque(false);
        paginationPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        lblPageInfo = new JLabel("Trang 1");
        lblPageInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPageInfo.setForeground(new Color(100, 100, 100));

        JPanel btnPaginationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPaginationPanel.setOpaque(false);

        btnPrevPage = createFlatButton("< Trước", new Color(200, 200, 200), new Color(180, 180, 180));
        btnPrevPage.setForeground(new Color(80, 80, 80));
        btnNextPage = createFlatButton("Tiếp >", new Color(200, 200, 200), new Color(180, 180, 180));
        btnNextPage.setForeground(new Color(80, 80, 80));

        btnPrevPage.addActionListener(e -> previousPage());
        btnNextPage.addActionListener(e -> nextPage());

        btnPaginationPanel.add(btnPrevPage);
        btnPaginationPanel.add(lblPageInfo);
        btnPaginationPanel.add(btnNextPage);

        paginationPanel.add(btnPaginationPanel, BorderLayout.WEST);
        mainPanel.add(paginationPanel, BorderLayout.SOUTH);

        // ===== SỰ KIỆN =====
        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> openEditDialog());
        btnChangePass.addActionListener(e -> openChangePasswordDialog());
        btnToggleStatus.addActionListener(e -> toggleUserStatus());
        btnDelete.addActionListener(e -> deleteUser());
        
        btnRefresh.addActionListener(e -> { 
            txtSearch.setText(""); 
            loadData(); 
        });

        return mainPanel;
    }
    
    // ================= XỬ LÝ TÌM KIẾM =================
    private void doSearch() {
        String keyword = txtSearch.getText().trim();
        
        if (keyword.isEmpty()) {
            loadPage(1);
            return;
        }

        List<UserDTO> resultList = userBUS.search(keyword);
        model.setRowCount(0);
        int resultCount = 0;

        if (resultList != null) {
            for (UserDTO u : resultList) {
                String status = (u.getStatus() != null && u.getStatus()) ? "✓ Bật" : "✗ Tắt";
                model.addRow(new Object[]{
                        u.getId(),
                        u.getUsername(),
                        u.getRole(),
                        status
                });
                resultCount++;
            }
        }

        lblPageInfo.setText("Kết quả tìm kiếm: " + resultCount + " tài khoản");
        btnPrevPage.setEnabled(false);
        btnNextPage.setEnabled(false);
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
            public void mouseEntered(MouseEvent evt) { if(btn.isEnabled()) btn.setBackground(hoverColor); }
            public void mouseExited(MouseEvent evt) { if(btn.isEnabled()) btn.setBackground(bgColor); }
        });
        return btn;
    }

    // ================= LOAD DATA =================
    private void loadData() {
        loadPage(1);
    }

    private void loadPage(int pageNumber) {
        model.setRowCount(0);
        totalUsers = (int) userBUS.countUsers();
        
        if (totalUsers == 0) {
            lblPageInfo.setText("Không có dữ liệu");
            btnPrevPage.setEnabled(false);
            btnNextPage.setEnabled(false);
            return;
        }

        int totalPages = (int) Math.ceil((double) totalUsers / itemsPerPage);

        if (pageNumber < 1) pageNumber = 1;
        if (pageNumber > totalPages) pageNumber = totalPages;
        currentPage = pageNumber;

        List<UserDTO> users = userBUS.getPage(pageNumber, itemsPerPage);

        if (users != null && !users.isEmpty()) {
            for (UserDTO u : users) {
                String status = (u.getStatus() != null && u.getStatus()) ? "✓ Bật" : "✗ Tắt";
                model.addRow(new Object[]{
                        u.getId(),
                        u.getUsername(),
                        u.getRole(),
                        status
                });
            }
        }

        lblPageInfo.setText(String.format("Trang %d / %d (Tổng: %d)", currentPage, totalPages, totalUsers));
        btnPrevPage.setEnabled(currentPage > 1);
        btnNextPage.setEnabled(currentPage < totalPages);
    }

    private void previousPage() {
        if (currentPage > 1) loadPage(currentPage - 1);
    }

    private void nextPage() {
        int totalPages = (int) Math.ceil((double) totalUsers / itemsPerPage);
        if (currentPage < totalPages) loadPage(currentPage + 1);
    }

    // ================= FORM THÊM USER MỚI =================
    private void openAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Tài Khoản Mới", true);
        dialog.setSize(450, 480); // Đã tăng chiều cao lên 480px để rộng rãi hơn
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
        cboRole.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cboRole.setBackground(Color.WHITE);
        cboRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        cboRole.setAlignmentX(Component.LEFT_ALIGNMENT);
        cboRole.setCursor(new Cursor(Cursor.HAND_CURSOR));

        formPanel.add(createLabeledComponent("Tên đăng nhập:"));
        formPanel.add(txtUsername);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createLabeledComponent("Mật khẩu:"));
        formPanel.add(txtPassword);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createLabeledComponent("Chọn Quyền hạn (ADMIN / USER):"));
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
            user.setStatus(true);

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

    // ================= FORM SỬA USER =================
    private void openEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String usernameTable = (String) model.getValueAt(row, 1);
        UserDTO user = userBUS.getUser(usernameTable);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Thông Tin Tài Khoản", true);
        dialog.setSize(450, 520); // Đã tăng chiều cao lên 520px để bao gồm cả Checkbox
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());

        // Header Form
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        JLabel lblFormTitle = new JLabel("CHỈNH SỬA TÀI KHOẢN");
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
        txtUsername.setText(user.getUsername());
        txtUsername.setEditable(false);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JComboBox<UserDTO.Role> cboRole = new JComboBox<>(UserDTO.Role.values());
        cboRole.setSelectedItem(user.getRole());
        cboRole.setFont(new Font("Segoe UI", Font.BOLD, 15));
        cboRole.setBackground(Color.WHITE);
        cboRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        cboRole.setAlignmentX(Component.LEFT_ALIGNMENT);
        cboRole.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JCheckBox cbStatus = new JCheckBox("Bật kích hoạt");
        cbStatus.setSelected(user.getStatus() != null && user.getStatus());
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbStatus.setBackground(Color.WHITE);
        cbStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(createLabeledComponent("Tên đăng nhập:"));
        formPanel.add(txtUsername);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createLabeledComponent("Mật khẩu mới (để trống nếu không đổi):"));
        formPanel.add(txtPassword);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createLabeledComponent("Chọn Quyền hạn (ADMIN / USER):"));
        formPanel.add(cboRole);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(cbStatus);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Footer Form
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 30, 20, 30));

        JButton btnSave = createFlatButton("Lưu", new Color(46, 204, 113), new Color(39, 174, 96));
        btnSave.setPreferredSize(new Dimension(120, 40));
        JButton btnCancel = createFlatButton("Hủy bỏ", new Color(230, 230, 230), new Color(210, 210, 210));
        btnCancel.setForeground(new Color(80, 80, 80));

        btnSave.addActionListener(e -> {
            String newPassword = new String(txtPassword.getPassword()).trim();
            UserDTO.Role newRole = (UserDTO.Role) cboRole.getSelectedItem();
            boolean newStatus = cbStatus.isSelected();

            user.setRole(newRole);
            user.setStatus(newStatus);
            
            if (!newPassword.isEmpty()) {
                user.setPassword(newPassword);
            }

            if (userBUS.update(user)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ================= FORM ĐỔI MẬT KHẨU =================
    private void openChangePasswordDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String username = (String) model.getValueAt(row, 1);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Đổi Mật Khẩu", true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        JLabel lblFormTitle = new JLabel("ĐỔI MẬT KHẨU");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblFormTitle.setForeground(new Color(50, 50, 50));
        headerPanel.add(lblFormTitle, BorderLayout.WEST);
        dialog.add(headerPanel, BorderLayout.NORTH);

        // Body
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 30, 20, 30));

        JLabel lblUsername = new JLabel("Tài khoản: " + username);
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setForeground(new Color(100, 100, 100));
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField txtNewPass = new JPasswordField();
        txtNewPass.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNewPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        txtNewPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txtNewPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField txtConfirm = new JPasswordField();
        txtConfirm.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtConfirm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        txtConfirm.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txtConfirm.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(lblUsername);
        formPanel.add(Box.createVerticalStrut(20));
        
        formPanel.add(createLabeledComponent("Mật khẩu mới:"));
        formPanel.add(txtNewPass);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createLabeledComponent("Xác nhận mật khẩu:"));
        formPanel.add(txtConfirm);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Footer
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(new EmptyBorder(0, 30, 20, 30));

        JButton btnUpdate = createFlatButton("Đổi mật khẩu", new Color(155, 89, 182), new Color(142, 68, 173));
        btnUpdate.setPreferredSize(new Dimension(140, 40));
        JButton btnCancel = createFlatButton("Hủy bỏ", new Color(230, 230, 230), new Color(210, 210, 210));
        btnCancel.setForeground(new Color(80, 80, 80));

        btnUpdate.addActionListener(e -> {
            String newPass = new String(txtNewPass.getPassword()).trim();
            String confirm = new String(txtConfirm.getPassword()).trim();

            if (newPass.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPass.equals(confirm)) {
                JOptionPane.showMessageDialog(dialog, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userBUS.changePassword(id, newPass)) {
                JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnUpdate);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ================= BẬT/TẮT TRẠNG THÁI =================
    private void toggleUserStatus() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String username = (String) model.getValueAt(row, 1);
        String currentStatus = (String) model.getValueAt(row, 3);
        
        String newStatus = currentStatus.contains("Bật") ? "Tắt" : "Bật";
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Đổi trạng thái tài khoản '" + username + "' thành '" + newStatus + "'?", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (userBUS.toggleStatus(id)) {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
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
            
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            
            g2.setColor(new Color(225, 225, 225));
            g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }
}