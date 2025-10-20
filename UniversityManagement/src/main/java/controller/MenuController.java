package controller;

import service.StudentService;
import service.LecturerService;
import service.CourseService;
//import service.EnrollmentService;
import util.DBConnection;

import java.util.List;

import java.util.Scanner;

/**
 * Controller class để quản lý menu chính của ứng dụng
 * Điều khiển luồng chương trình và tương tác với user
 */
public class MenuController {
    private StudentService studentService;
    private LecturerService lecturerService;
    private CourseService courseService;
    //    private EnrollmentService enrollmentService;
    private Scanner scanner;
    private boolean isRunning;

    public MenuController() {
        this.studentService = new StudentService();
        this.lecturerService = new LecturerService();
        this.courseService = new CourseService();
//        this.enrollmentService = new EnrollmentService();
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    /**
     * Khởi chạy ứng dụng
     */
    public void start() {
        System.out.println("=== HỆ THỐNG QUẢN LÝ ĐẠI HỌC ===");
        System.out.println("Chào mừng bạn đến với hệ thống quản lý đại học!");
        System.out.println("Kết nối database thành công!");

        while (isRunning) {
            showMainMenu();
            handleMainMenuChoice();
        }

        // Đóng kết nối database khi thoát
        DBConnection.closeConnection();
        System.out.println("Cảm ơn bạn đã sử dụng hệ thống!");
    }

    /**
     * Hiển thị menu chính
     */
    private void showMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           MENU CHÍNH");
        System.out.println("=".repeat(50));
        System.out.println("1. Quản lý Sinh viên");
        System.out.println("2. Quản lý Giảng viên");
        System.out.println("3. Quản lý Khóa học");
        System.out.println("4. Quản lý Đăng ký môn học");
        System.out.println("5. Báo cáo và Thống kê");
        System.out.println("6. Đọc dữ liệu từ file CSV");
        System.out.println("7. Lưu dữ liệu ra file CSV");
        System.out.println("0. Thoát chương trình");
        System.out.println("=".repeat(50));
        System.out.print("Vui lòng chọn chức năng (0-7): ");
    }

    /**
     * Xử lý lựa chọn từ menu chính
     */
    private void handleMainMenuChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    handleStudentMenu();
                    break;
                case 2:
                    handleLecturerMenu();
                    break;
                case 3:
                    handleCourseMenu();
                    break;
//                case 4:
//                    handleEnrollmentMenu();
//                    break;
//                case 5:
//                    handleReportMenu();
//                    break;
                case 6:
                    handleImportData();
                    break;
//                case 7:
//                    handleExportData();
//                    break;
                case 0:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 0-7.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số nguyên!");
        }
    }

    /**
     * Xử lý menu quản lý sinh viên
     */
    private void handleStudentMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("      QUẢN LÝ SINH VIÊN");
            System.out.println("=".repeat(40));
            System.out.println("1. Thêm sinh viên mới");
            System.out.println("2. Cập nhật thông tin sinh viên");
            System.out.println("3. Xóa sinh viên");
            System.out.println("4. Xem thông tin sinh viên");
            System.out.println("5. Danh sách tất cả sinh viên");
            System.out.println("6. Tìm kiếm sinh viên theo tên");
            System.out.println("0. Quay lại menu chính");
            System.out.println("=".repeat(40));
            System.out.print("Vui lòng chọn chức năng (0-6): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        studentService.addStudent();
                        break;
                    case 2:
                        studentService.updateStudent();
                        break;
                    case 3:
                        studentService.deleteStudent();
                        break;
                    case 4:
                        studentService.viewStudent();
                        break;
                    case 5:
                        studentService.listAllStudents();
                        break;
                    case 6:
                        studentService.searchStudentsByName();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 0-6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên!");
            }
        }
    }

    /**
     * Xử lý menu quản lý giảng viên
     */
    private void handleLecturerMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("      QUẢN LÝ GIẢNG VIÊN");
            System.out.println("=".repeat(40));
            System.out.println("1. Thêm giảng viên mới");
            System.out.println("2. Cập nhật thông tin giảng viên");
            System.out.println("3. Xóa giảng viên");
            System.out.println("4. Xem thông tin giảng viên");
            System.out.println("5. Danh sách tất cả giảng viên");
            System.out.println("0. Quay lại menu chính");
            System.out.println("=".repeat(40));
            System.out.print("Vui lòng chọn chức năng (0-5): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        lecturerService.addLecturer();
                        break;
                    case 2:
                        lecturerService.updateLecturer();
                        break;
                    case 3:
                        lecturerService.deleteLecturer();
                        break;
                    case 4:
                        lecturerService.viewLecturer();
                        break;
                    case 5:
                        lecturerService.listAllLecturers();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 0-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên!");
            }
        }
    }

    /**
     * Xử lý menu quản lý khóa học
     */
    private void handleCourseMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("      QUẢN LÝ KHÓA HỌC");
            System.out.println("=".repeat(40));
            System.out.println("1. Thêm khóa học mới");
            System.out.println("2. Cập nhật thông tin khóa học");
            System.out.println("3. Xóa khóa học");
            System.out.println("4. Xem thông tin khóa học");
            System.out.println("5. Danh sách tất cả khóa học");
            System.out.println("6. Tìm kiếm khóa học theo tên");
            System.out.println("7. Tìm kiếm khóa học theo giảng viên");
            System.out.println("0. Quay lại menu chính");
            System.out.println("=".repeat(40));
            System.out.print("Vui lòng chọn chức năng (0-7): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        courseService.addCourse();
                        break;
                    case 2:
                        courseService.updateCourse();
                        break;
                    case 3:
                        courseService.deleteCourse();
                        break;
                    case 4:
                        courseService.viewCourse();
                        break;
                    case 5:
                        courseService.listAllCourses();
                        break;
                    case 6:
                        courseService.searchCoursesByName();
                        break;
                    case 7:
                        courseService.searchCoursesByLecturer();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 0-7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên!");
            }
        }
    }

    //
//    /**
//     * Xử lý menu quản lý đăng ký môn học
//     */
//    private void handleEnrollmentMenu() {
//        while (true) {
//            System.out.println("\n" + "=".repeat(40));
//            System.out.println("   QUẢN LÝ ĐĂNG KÝ MÔN HỌC");
//            System.out.println("=".repeat(40));
//            System.out.println("1. Đăng ký môn học cho sinh viên");
//            System.out.println("2. Hủy đăng ký môn học");
//            System.out.println("3. Cập nhật điểm số");
//            System.out.println("4. Xem danh sách đăng ký của sinh viên");
//            System.out.println("5. Xem danh sách sinh viên trong khóa học");
//            System.out.println("6. Xem tất cả đăng ký");
//            System.out.println("0. Quay lại menu chính");
//            System.out.println("=".repeat(40));
//            System.out.print("Vui lòng chọn chức năng (0-6): ");
//
//            try {
//                int choice = Integer.parseInt(scanner.nextLine());
//
//                switch (choice) {
//                    case 1:
//                        enrollmentService.enrollStudent();
//                        break;
//                    case 2:
//                        enrollmentService.dropEnrollment();
//                        break;
//                    case 3:
//                        enrollmentService.updateScores();
//                        break;
//                    case 4:
//                        enrollmentService.viewStudentEnrollments();
//                        break;
//                    case 5:
//                        enrollmentService.viewCourseStudents();
//                        break;
//                    case 6:
//                        enrollmentService.viewAllEnrollments();
//                        break;
//                    case 0:
//                        return;
//                    default:
//                        System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 0-6.");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Vui lòng nhập số nguyên!");
//            }
//        }
//    }
//
//    /**
//     * Xử lý menu báo cáo
//     */
    private void handleReportMenu() {
        ReportController reportController = new ReportController();
        reportController.showReportMenu();
    }

    /**
     * Xử lý import dữ liệu từ CSV
     */
    private void handleImportData() {
        System.out.println("Chức năng này sẽ được triển khai trong phiên bản tiếp theo.");
        System.out.println("Hiện tại bạn có thể sử dụng các chức năng thêm dữ liệu thủ công.");
        System.out.println("(Dữ liệu chỉ mới đọc vào bộ nhớ, chưa lưu vào hệ thống. Bạn cần bổ sung logic lưu vào repository nếu muốn sử dụng tiếp.)");
    }
}
