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
        System.out.println("8. Mô phỏng đăng ký đa luồng");
        System.out.println("0. Thoát chương trình");
        System.out.println("=".repeat(50));
        System.out.print("Vui lòng chọn chức năng (0-8): ");
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
                case 8:
                    handleSimulationMenu();
                    break;
                case 0:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 0-8.");
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
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        ĐỌC DỮ LIỆU TỪ FILE CSV");
        System.out.println("=".repeat(50));
        
        try {
            // Import sinh viên
            System.out.println("Đang đọc dữ liệu sinh viên...");
            List<entity.Student> students = repository.ReadStudentFile.loadStudents();
            System.out.println("Đã đọc " + students.size() + " sinh viên");
            
            // Import giảng viên
            System.out.println("Đang đọc dữ liệu giảng viên...");
            List<entity.Lecturer> lecturers = repository.ReadLectureFile.loadLecturers();
            System.out.println("Đã đọc " + lecturers.size() + " giảng viên");
            
            // Import khóa học
            System.out.println("Đang đọc dữ liệu khóa học...");
            String coursePath = System.getProperty("user.dir") + "/data/courses.csv";
            List<String[]> courseData = util.FileUtils.readCSV(coursePath);
            List<entity.Course> courses = repository.ReadCourseFile.parseCourses(courseData);
            System.out.println("Đã đọc " + courses.size() + " khóa học");
            
            // Hiển thị thống kê
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           THỐNG KÊ DỮ LIỆU");
            System.out.println("=".repeat(50));
            System.out.println("Tổng số sinh viên: " + students.size());
            System.out.println("Tổng số giảng viên: " + lecturers.size());
            System.out.println("Tổng số khóa học: " + courses.size());
            
            // Hiển thị mẫu dữ liệu
            if (!students.isEmpty()) {
                System.out.println("\nMẫu dữ liệu sinh viên:");
                students.stream().limit(3).forEach(s -> 
                    System.out.println("  • " + s.getFullName() + " (ID: " + s.getStudentId() + ", GPA: " + s.getGpa() + ")")
                );
            }
            
            if (!lecturers.isEmpty()) {
                System.out.println("\nMẫu dữ liệu giảng viên:");
                lecturers.stream().limit(3).forEach(l -> 
                    System.out.println("  • " + l.getFullName() + " (" + l.getDegree() + ")")
                );
            }
            
            if (!courses.isEmpty()) {
                System.out.println("\nMẫu dữ liệu khóa học:");
                courses.stream().limit(3).forEach(c -> 
                    System.out.println("  • " + c.getCourseName() + " (ID: " + c.getCourseId() + ", " + c.getCredits() + " tín chỉ)")
                );
            }
            
            System.out.println("\nHoàn thành đọc dữ liệu từ CSV!");
            System.out.println("Lưu ý: Dữ liệu đã được đọc vào bộ nhớ. Để lưu vào database, hãy sử dụng các chức năng thêm dữ liệu thủ công.");
            
        } catch (Exception e) {
            System.err.println("Lỗi khi đọc dữ liệu CSV: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\nNhấn Enter để quay lại menu chính...");
        try {
            scanner.nextLine();
        } catch (Exception e) {
            // Ignore input errors
        }
    }

    /**
     * Xử lý menu mô phỏng đăng ký đa luồng
     */
    private void handleSimulationMenu() {
        System.out.println("\n=== MÔ PHỎNG ĐĂNG KÝ ĐA LUỒNG ===");
        
        // Hiển thị danh sách khóa học có sẵn
        System.out.println("Danh sách khóa học có sẵn:");
        courseService.listAllCourses();
        
        System.out.print("\nNhập mã khóa học để mô phỏng: ");
        String courseId = scanner.nextLine();
        
        if (courseId.trim().isEmpty()) {
            System.out.println("Mã khóa học không được để trống!");
            return;
        }
        
        // Kiểm tra khóa học có tồn tại không
        entity.Course course = courseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học với mã: " + courseId);
            return;
        }
        
        System.out.print("Nhập số lượng sinh viên mô phỏng (mặc định 5): ");
        String numStudentsStr = scanner.nextLine();
        int numStudents = 5;
        if (!numStudentsStr.trim().isEmpty()) {
            try {
                numStudents = Integer.parseInt(numStudentsStr);
                if (numStudents <= 0) {
                    System.out.println("Số lượng sinh viên phải lớn hơn 0!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Số lượng không hợp lệ, sử dụng mặc định 5.");
            }
        }
        
        // Chạy mô phỏng
        runSimulation(courseId, numStudents);
    }
    
    /**
     * Chạy mô phỏng đăng ký đa luồng
     */
    private void runSimulation(String courseId, int numStudents) {
        System.out.println("\n=== BẮT ĐẦU MÔ PHỎNG ===");
        System.out.println("Khóa học: " + courseService.getCourseById(courseId).getCourseName());
        System.out.println("Mã khóa học: " + courseId);
        System.out.println("Số sinh viên mô phỏng: " + numStudents);
        System.out.println("------------------------------------");
        
        java.util.List<Thread> threads = new java.util.ArrayList<>();
        java.util.List<String> results = new java.util.ArrayList<>();
        
        for (int i = 0; i < numStudents; i++) {
            String studentName = "SinhVien_" + (i + 1);
            Thread thread = new Thread(() -> {
                System.out.printf("Người dùng '%s' đang cố gắng đăng ký môn học.%n", studentName);
                String result = courseService.registerStudent(courseId, studentName);
                synchronized (results) {
                    results.add(studentName + ": " + result);
                }
            });
            threads.add(thread);
            thread.start();
        }
        
        // Đợi tất cả threads hoàn thành
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Lỗi khi đợi thread: " + e.getMessage());
            }
        }
        
        // Hiển thị kết quả
        System.out.println("\n------------------------------------");
        System.out.println("KẾT QUẢ MÔ PHỎNG:");
        for (String result : results) {
            System.out.println("  " + result);
        }
        
        // Hiển thị trạng thái cuối cùng - sử dụng CourseService để lấy dữ liệu
        try {
            entity.Course finalCourse = courseService.getCourseById(courseId);
            if (finalCourse != null) {
                System.out.println("\nTrạng thái cuối cùng:");
                System.out.printf("  Số sinh viên đã đăng ký: %d/%d%n", 
                    finalCourse.getEnrolledStudents(), finalCourse.getMaxStudents());
                System.out.printf("  Danh sách sinh viên: %s%n", finalCourse.getRegisteredStudents());
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy trạng thái cuối cùng: " + e.getMessage());
        }
        
        System.out.println("\nNhấn Enter để quay lại menu chính...");
        scanner.nextLine();
    }
}
