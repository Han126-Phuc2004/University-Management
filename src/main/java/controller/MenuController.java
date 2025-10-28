package controller;

import service.*;
import util.DBConnection;
import util.FileUtils;
import repository.StudentRepository;
import repository.LecturerRepository;
import repository.CourseRepository;
/*import controller.ReportController;*/

import java.util.Scanner;

/**
 * Controller class để quản lý menu chính của ứng dụng
 * Điều khiển luồng chương trình và tương tác với user
 */
public class MenuController {
    private StudentService studentService;
    private LecturerService lecturerService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    private ReportGenerator report;
    private Scanner scanner;
    private boolean isRunning;

    public MenuController() {
        this.studentService = new StudentService();
        this.lecturerService = new LecturerService();
        this.courseService = new CourseService();
        this.enrollmentService = new EnrollmentService();
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        this.report = new ReportGenerator();
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
        System.out.println("7. Mô phỏng đăng ký đa luồng");
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
               case 4:
                   handleEnrollmentMenu();
                   break;
                case 5:
                    handleReportMenu();
                    break;
                case 6:
                    handleImportData();
                    break;
                case 7:
                    handleSimulationMenu();
                    break;
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

    private void handleReportMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("      HỆ THỐNG BÁO CÁO SINH VIÊN");
            System.out.println("=".repeat(40));
            System.out.println("1. Báo cáo phân bố điểm GPA");
            System.out.println("2. Báo cáo trạng thái đăng ký (Theo học kỳ)");
            System.out.println("3. Báo cáo trạng thái đăng ký (Theo khóa học)");
            System.out.println("0. Thoát");
            System.out.println("=".repeat(40));
            System.out.print("Vui lòng chọn chức năng (0-3): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        report.generateGpaReport();
                        break;
                    case 2:
                        report.generateEnrollmentStatusReport("semester");
                        break;
                    case 3:
                        report.generateEnrollmentStatusReport("course");
                        break;
                    case 0:
                        System.out.println("Thoát chương trình...");
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 0-3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên!");
            }
        }
    }
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

    /*
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

    /**
     * Xử lý import dữ liệu từ CSV
     */
    private void handleImportData() {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("      IMPORT DỮ LIỆU TỪ CSV");
            System.out.println("=".repeat(40));
            System.out.println("1. Import sinh viên (students.csv)");
            System.out.println("2. Import giảng viên (lecturers.csv)");
            System.out.println("3. Import khóa học (courses.csv)");
            System.out.println("4. Import tất cả (Students, Lecturers, Courses)");
            System.out.println("0. Quay lại menu chính");
            System.out.println("=".repeat(40));
            System.out.print("Vui lòng chọn chức năng (0-4): ");

            String choiceStr = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên!");
                continue;
            }

            // Xác định đường dẫn mặc định tới thư mục data
            String baseDir = System.getProperty("user.dir");
            String studentCsv = baseDir + "/data/students.csv";
            String lecturerCsv = baseDir + "/data/lecturers.csv";
            String courseCsv = baseDir + "/data/courses.csv";

            switch (choice) {
                case 1: {
                    System.out.println("\n=== IMPORT STUDENTS ===");
                    if (!FileUtils.fileExists(studentCsv)) {
                        System.out.println("Không tìm thấy file: " + studentCsv);
                        break;
                    }
                    new StudentRepository().saveStudentFromCSVToBD(studentCsv);
                    break;
                }
                case 2: {
                    System.out.println("\n=== IMPORT LECTURERS ===");
                    if (!FileUtils.fileExists(lecturerCsv)) {
                        System.out.println("Không tìm thấy file: " + lecturerCsv);
                        break;
                    }
                    new LecturerRepository().saveLecturerFromCSVToBD(lecturerCsv);
                    break;
                }
                case 3: {
                    System.out.println("\n=== IMPORT COURSES ===");
                    if (!FileUtils.fileExists(courseCsv)) {
                        System.out.println("Không tìm thấy file: " + courseCsv);
                        break;
                    }
                    new CourseRepository().saveCourseFromCSVToBD(courseCsv);
                    break;
                }
                case 4: {
                    System.out.println("\n=== IMPORT ALL (Students, Lecturers, Courses) ===");
                    boolean hasAny = false;
                    if (FileUtils.fileExists(studentCsv)) {
                        new StudentRepository().saveStudentFromCSVToBD(studentCsv);
                        hasAny = true;
                    } else {
                        System.out.println("Không tìm thấy file: " + studentCsv);
                    }
                    if (FileUtils.fileExists(lecturerCsv)) {
                        new LecturerRepository().saveLecturerFromCSVToBD(lecturerCsv);
                        hasAny = true;
                    } else {
                        System.out.println("Không tìm thấy file: " + lecturerCsv);
                    }
                    if (FileUtils.fileExists(courseCsv)) {
                        new CourseRepository().saveCourseFromCSVToBD(courseCsv);
                        hasAny = true;
                    } else {
                        System.out.println("Không tìm thấy file: " + courseCsv);
                    }
                    if (!hasAny) {
                        System.out.println("Không có file CSV nào để import trong thư mục data.");
                    }
                    break;
                }
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 0-4.");
            }
        }
    }

    /**
     * Xử lý menu quản lý đăng ký môn học
     */
    
    private void handleEnrollmentMenu() {
        while (true) {
            System.out.println("\n=== ENROLLMENT MANAGEMENT ===");
            System.out.println("1. Register for a course");
            System.out.println("2. Cancel course enrollment");
            System.out.println("3. View student's enrolled courses");
            System.out.println("4. View course enrollments");
            System.out.println("5. View all enrollments");
            System.out.println("6. Update enrollment scores");
            System.out.println("7. Update enrollment status");
            System.out.println("0. Back to main menu");
            System.out.print("Please choose an option (0-7): ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) break;

            switch (choice) {
                case "1":
                    System.out.print("Enter Student ID: ");
                    String studentId = scanner.nextLine();
                    courseService.listAllCourses(); 
                    System.out.print("Enter Course ID: ");
                    String courseId = scanner.nextLine();
                    enrollmentService.registerCourse(studentId, courseId);
                    break;
                case "2":
                    System.out.print("Enter Student ID: ");
                    studentId = scanner.nextLine();
                    System.out.print("Enter Course ID: ");
                    courseId = scanner.nextLine();
                    enrollmentService.cancelEnrollment(studentId, courseId);
                    break;
                case "3":
                    System.out.print("Enter Student ID: ");
                    studentId = scanner.nextLine();
                    enrollmentService.viewEnrollments(studentId);
                    break;
                case "4":
                    System.out.print("Enter Course ID: ");
                    courseId = scanner.nextLine();
                    enrollmentService.viewCourseEnrollments(courseId);
                    break;
                case "5":
                    enrollmentService.viewAllEnrollments();
                    break;
                case "6":
                    handleUpdateScores();
                    break;
                case "7":
                    handleUpdateStatus();
                    break;
                default:
                    System.out.println("Invalid option! Please choose between 0-7.");
            }
        }
    }

    private void handleUpdateScores() {
        System.out.print("Enter Enrollment ID: ");
        String enrollmentId = scanner.nextLine();
        
        System.out.print("Enter Midterm Score (0-10): ");
        String midtermInput = scanner.nextLine();
        System.out.print("Enter Final Score (0-10): ");
        String finalInput = scanner.nextLine();
        
        try {
java.math.BigDecimal midtermScore = new java.math.BigDecimal(midtermInput);
            java.math.BigDecimal finalScore = new java.math.BigDecimal(finalInput);
            enrollmentService.updateScores(enrollmentId, midtermScore, finalScore);
        } catch (NumberFormatException e) {
            System.out.println("Invalid score format! Please enter valid numbers.");
        }
    }

    private void handleUpdateStatus() {
        System.out.print("Enter Enrollment ID: ");
        String enrollmentId = scanner.nextLine();
        
        System.out.println("Available statuses: enrolled, completed, dropped, failed");
        System.out.print("Enter new status: ");
        String status = scanner.nextLine();
        
        enrollmentService.updateStatus(enrollmentId, status);
    }


    /**
     * Xử lý export dữ liệu ra CSV
     */
    private void handleExportData() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("      EXPORT DỮ LIỆU RA CSV");
        System.out.println("=".repeat(40));
        System.out.println("1. Export sinh viên");
        System.out.println("2. Export giảng viên");
        System.out.println("3. Export khóa học");
        System.out.println("4. Export tất cả");
        System.out.println("0. Quay lại menu chính");
        System.out.println("=".repeat(40));
        System.out.print("Vui lòng chọn chức năng (0-4): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            String baseDir = System.getProperty("user.dir") + "/data/export/";
            
            switch (choice) {
                case 1:
                    System.out.println("Export sinh viên - Đang phát triển...");
                    break;
                case 2:
                    System.out.println("Export giảng viên - Đang phát triển...");
                    break;
                case 3:
                    System.out.println("Export khóa học - Đang phát triển...");
                    break;
                case 4:
                    System.out.println("Export tất cả - Đang phát triển...");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số nguyên!");
        }
        
        System.out.println("\nNhấn Enter để quay lại menu chính...");
        scanner.nextLine();
    }

    /**
     * Xử lý menu mô phỏng đăng ký đa luồng
     */
    private void handleSimulationMenu() {
        System.out.println("\n=== MÔ PHỎNG ĐĂNG KÝ ĐA LUỒNG ===");

        // Kiểm tra xem có khóa học nào để mô phỏng không
        java.util.List<entity.Course> availableCourses = courseService.getAllCourses();
        if (availableCourses.isEmpty()) {
            System.out.println("Chưa có khóa học nào trong hệ thống để mô phỏng. Vui lòng thêm khóa học trước.");
            System.out.println("\nNhấn Enter để quay lại menu chính...");
            scanner.nextLine();
            return;
        }

        System.out.println("Danh sách khóa học có sẵn:");
        courseService.listAllCourses(); // Hiển thị danh sách khóa học

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

        // Kiểm tra khóa học đã đầy chưa
        if (courseService.isCourseFull(courseId)) {
            System.out.println("KHÓA HỌC ĐÃ ĐẦY!");
            System.out.println("Khóa học '" + course.getCourseName() + "' đã đầy (" + 
                             course.getEnrolledStudents() + "/" + course.getMaxStudents() + " sinh viên).");
            System.out.println("Không thể mô phỏng đăng ký hay đăng ký thêm, vui lòng chọn khóa khác.");
            System.out.println("\nNhấn Enter để quay lại menu chính...");
            scanner.nextLine();
            return;
        }

        // Hiển thị thông tin khóa học
        System.out.println("✅ Khóa học: " + course.getCourseName());
        System.out.println("📊 Số sinh viên hiện tại: " + course.getEnrolledStudents() + "/" + course.getMaxStudents());
        System.out.println("🎯 Số chỗ trống còn lại: " + (course.getMaxStudents() - course.getEnrolledStudents()));

        System.out.print("\nNhập số lượng sinh viên mô phỏng (mặc định 5): ");
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
                System.out.printf("Người dùng '%s' đăng ký %s%n", studentName,
                        "SUCCESS".equals(result) ? "thành công" : "thất bại");
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
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy trạng thái cuối cùng: " + e.getMessage());
        }

        System.out.println("\nNhấn Enter để quay lại menu chính...");
        scanner.nextLine();
    }
}
