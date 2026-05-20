# Hệ Thống Quản Lý Tuyển Sinh

Ứng dụng quản lý tuyển sinh toàn diện cho các cơ sở giáo dục, giúp quản lý thí sinh, điểm thi, nguyên vọng, và xét tuyển.

## 📋 Mô Tả Dự Án

Hệ thống quản lý tuyển sinh này được phát triển bằng Java Swing, cung cấp giao diện người dùng thân thiện để quản lý:
- Thí sinh (Test takers/Applicants)
- Điểm thi (Exam scores)
- Điểm cộng (Bonus points)
- Nguyên vọng (Preferences/Choices)
- Ngành (Majors/Fields)
- Phổ hợp (Combinations)
- Quy đổi điểm (Score conversion)
- Xét tuyển (Admission selection)
- Quản lý người dùng (User management)

## 🛠️ Công Nghệ Sử Dụng

- **Ngôn ngữ:** Java 17
- **Giao diện:** Java Swing + FlatLaf (Modern UI theme)
- **Cơ sở dữ liệu:** MySQL
- **ORM:** Hibernate
- **Build Tool:** Maven
- **Xử lý Excel:** Apache POI
- **Logging:** Log4j 2

## 📦 Cấu Trúc Dự Án

```
src/main/java/com/tuyensinh/
├── BUS/                          # Business Logic Layer
│   ├── ThiSinhBUS.java
│   ├── NguyenVongBUS.java
│   ├── DiemThiBUS.java
│   ├── DiemCongBUS.java
│   ├── NganhBUS.java
│   ├── ToHopBUS.java
│   ├── XetTuyenBUS.java
│   ├── QuyDoiBUS.java
│   ├── UserBUS.java
│   └── Import*.java              # Import utilities
├── DAO/                          # Data Access Layer
│   ├── ThiSinhDAO.java
│   ├── NguyenVongDAO.java
│   ├── DiemThiDAO.java
│   └── ...
├── DTO/                          # Data Transfer Objects
│   ├── ThiSinhDTO.java
│   ├── NguyenVongDTO.java
│   └── ...
├── GUI/                          # User Interface Layer
│   ├── LoginForm.java            # Login screen
│   ├── MainFrame.java            # Main application window
│   ├── Dashboard.java            # Dashboard
│   ├── *Panel.java               # Module panels
│   ├── *Form.java                # Data entry forms
│   ├── ImportExcelFrm.java       # Import dialog
│   └── UITheme.java              # Theme configuration
├── config/                       # Configuration
│   ├── DB.java                   # Database configuration
│   └── HibernateUtil.java        # Hibernate utilities
└── main/
    └── Main.java                 # Application entry point

src/main/resources/
└── hibernate.cfg.xml             # Hibernate configuration
```

## ✨ Tính Năng Chính

### 1. Quản Lý Thí Sinh
- Thêm, sửa, xóa, xem thí sinh
- Tìm kiếm và lọc thí sinh
- Import thí sinh từ Excel

### 2. Quản Lý Điểm
- Quản lý điểm thi
- Quản lý điểm cộng
- Quản lý quy đổi điểm

### 3. Quản Lý Nguyên Vọng
- Quản lý nguyên vọng của thí sinh
- Quản lý ngành học
- Quản lý tổ hợp môn thi

### 4. Xét Tuyển
- Xét tuyển thí sinh
- Tính toán điểm xét tuyển
- Quản lý kết quả xét tuyển

### 5. Nhập/Xuất Dữ Liệu
- Import dữ liệu từ file Excel
- Xử lý bảng dữ liệu DGNL
- Xử lý bảng dữ liệu điểm

### 6. Quản Lý Người Dùng
- Xác thực người dùng (Login)
- Quản lý tài khoản người dùng

## 🚀 Hướng Dẫn Cài Đặt

### Yêu Cầu
- Java Development Kit (JDK) 17 trở lên
- Maven 3.6+
- MySQL 5.7 trở lên

### Bước Cài Đặt

1. **Clone dự án:**
   ```bash
   git clone <repository-url>
   cd Admissions-software
   ```

2. **Cấu hình cơ sở dữ liệu:**
   - Sửa file `src/main/resources/hibernate.cfg.xml`
   - Thiết lập URL, username, password cho MySQL

3. **Cài đặt dependencies:**
   ```bash
   mvn clean install
   ```

4. **Chạy ứng dụng:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.tuyensinh.main.Main"
   ```
   
   Hoặc:
   ```bash
   mvn compile
   java -cp target/classes:~/.m2/repository/.../* com.tuyensinh.main.Main
   ```

## 📝 Cấu Hình

### Hibernate Configuration
File `src/main/resources/hibernate.cfg.xml` chứa cấu hình kết nối cơ sở dữ liệu:
- Database URL
- Username/Password
- Dialect
- Connection pool settings

### Database Configuration
File `src/main/java/com/tuyensinh/config/DB.java` chứa các hằng số cấu hình cơ sở dữ liệu.

## 🔐 Bảo Mật

- Sử dụng hệ thống login để xác thực người dùng
- Mật khẩu nên được mã hóa trong cơ sở dữ liệu
- Các thao tác nhạy cảm được kiểm soát quyền hạn

## 📊 Mô Hình Dữ Liệu

Ứng dụng sử dụng kiến trúc 3-layer:
- **Presentation Layer (GUI):** Giao diện người dùng Swing
- **Business Logic Layer (BUS):** Xử lý logic kinh doanh
- **Data Access Layer (DAO):** Truy cập cơ sở dữ liệu thông qua Hibernate

## 🐛 Xử Lý Lỗi & Logging

- Sử dụng Log4j 2 để ghi log
- Hỗ trợ logging các lỗi và thông tin hoạt động
- File log có thể được cấu hình trong `log4j2.xml`

## 🤝 Đóng Góp

1. Fork dự án
2. Tạo branch cho feature mới (`git checkout -b feature/AmazingFeature`)
3. Commit thay đổi (`git commit -m 'Add some AmazingFeature'`)
4. Push lên branch (`git push origin feature/AmazingFeature`)
5. Mở Pull Request

## 📄 Giấy Phép

Dự án này được phép sử dụng cho các mục đích giáo dục.

## 📧 Liên Hệ

Nếu có câu hỏi hoặc góp ý, vui lòng liên hệ qua email hoặc tạo Issue trên repository.

## 🎯 Phiên Bản

- **Phiên bản hiện tại:** 1.0
- **Trạng thái:** Đang phát triển

---

**Được phát triển bằng ❤️ cho hệ thống quản lý tuyển sinh**
