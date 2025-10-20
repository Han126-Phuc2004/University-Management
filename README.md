# Hệ thống Quản lý Đại học (University Management System)

## Mô tả
Ứng dụng console (CLI) để quản lý các đối tượng cốt lõi của một trường đại học, bao gồm Sinh viên (Student), Giảng viên (Lecturer), và Khóa học (Course).

## Tính năng chính
- ✅ Quản lý thông tin sinh viên, giảng viên, khóa học (Thêm, sửa, xóa, hiển thị)
- ✅ Chức năng đăng ký môn học cho sinh viên
- ✅ Đọc dữ liệu từ tệp tin CSV
- ✅ Kết nối và lưu dữ liệu vào cơ sở dữ liệu MySQL
- ✅ Xử lý đồng thời nhiều người dùng đăng ký môn học
- ✅ Tạo các báo cáo đơn giản



## Yêu cầu hệ thống

### Menu chính:
1. **Quản lý Sinh viên** - Thêm, sửa, xóa, xem thông tin sinh viên
2. **Quản lý Giảng viên** - Thêm, sửa, xóa, xem thông tin giảng viên  
3. **Quản lý Khóa học** - Thêm, sửa, xóa, xem thông tin khóa học
4. **Quản lý Đăng ký môn học** - Đăng ký, hủy đăng ký, cập nhật điểm
5. **Báo cáo và Thống kê** - Xem các báo cáo tổng hợp
6. **Đọc dữ liệu từ file CSV** - Import dữ liệu từ CSV
7. **Lưu dữ liệu ra file CSV** - Export dữ liệu ra CSV

### Tính năng đặc biệt:
- **Xử lý đồng thời**: Hỗ trợ nhiều người dùng đăng ký môn học cùng lúc
- **Validation**: Kiểm tra dữ liệu đầu vào nghiêm ngặt
- **Báo cáo**: Tạo các báo cáo thống kê chi tiết
- **CSV Support**: Đọc/ghi dữ liệu từ file CSV

## Cấu trúc Database

### Các bảng chính:
- `student` - Thông tin sinh viên
- `lecturer` - Thông tin giảng viên  
- `course` - Thông tin khóa học
- `enrollment` - Đăng ký môn học
- `department` - Khoa/Bộ môn
- `semester` - Học kỳ

### Mối quan hệ:
- Student → Department (N:1)
- Lecturer → Department (N:1)
- Course → Lecturer (N:1)
- Course → Department (N:1)
- Course → Semester (N:1)
- Enrollment → Student (N:1)
- Enrollment → Course (N:1)

