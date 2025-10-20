//package service;
//
//import entity.Enrollment;
//import entity.Student;
//import entity.Course;
//import repository.EnrollmentRepository;
//import repository.StudentRepository;
//import repository.CourseRepository;
//import util.InputValidator;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Scanner;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * Service class cho Enrollment
// * Xử lý business logic liên quan đến đăng ký môn học
// * Hỗ trợ xử lý đồng thời nhiều người dùng đăng ký
// */
//public class EnrollmentService {
//    private EnrollmentRepository enrollmentRepository;
//    private StudentRepository studentRepository;
//    private CourseRepository courseRepository;
//    private Scanner scanner;
//
//    // Lock để xử lý đồng thời
//    private final ReentrantLock enrollmentLock = new ReentrantLock();
//
//    public EnrollmentService() {
//        this.enrollmentRepository = new EnrollmentRepository();
//        this.studentRepository = new StudentRepository();
//        this.courseRepository = new CourseRepository();
//        this.scanner = new Scanner(System.in);
//    }
//
//    /**
//     * Đăng ký môn học cho sinh viên
//     * Xử lý đồng thời nhiều người dùng
//     */
//    public void enrollStudent() {
//        System.out.println("\n=== ĐĂNG KÝ MÔN HỌC ===");
//
//        String studentId = getValidInput("Nhập mã sinh viên: ", InputValidator::isValidId);
//        if (studentId == null) return;
//
//        // Kiểm tra sinh viên có tồn tại không
//        Student student = studentRepository.findById(studentId);
//        if (student == null) {
//            System.out.println("Không tìm thấy sinh viên với mã " + studentId);
//            return;
//        }
//
//        // Kiểm tra trạng thái sinh viên
//        if (!"active".equalsIgnoreCase(student.getStatus())) {
//            System.out.println("Sinh viên không ở trạng thái active, không thể đăng ký môn học!");
//            return;
//        }
//
//        String courseId = getValidInput("Nhập mã khóa học: ", InputValidator::isValidId);
//        if (courseId == null) return;
//
//        // Kiểm tra khóa học có tồn tại không
//        Course course = courseRepository.findById(courseId);
//        if (course == null) {
//            System.out.println("Không tìm thấy khóa học với mã " + courseId);
//            return;
//        }
//
//        // Kiểm tra trạng thái khóa học
//        if (!"open".equalsIgnoreCase(course.getStatus())) {
//            System.out.println("Khóa học không ở trạng thái mở, không thể đăng ký!");
//            return;
//        }
//
//        // Sử dụng lock để xử lý đồng thời
//        enrollmentLock.lock();
//        try {
//            // Kiểm tra sinh viên đã đăng ký khóa học chưa
//            if (enrollmentRepository.isEnrolled(studentId, courseId)) {
//                System.out.println("Sinh viên đã đăng ký khóa học này rồi!");
//                return;
//            }
//
//            // Kiểm tra số lượng sinh viên đã đăng ký
//            int currentEnrolled = enrollmentRepository.countEnrolledStudents(courseId);
//            if (currentEnrolled >= course.getMaxStudents()) {
//                System.out.println("Khóa học đã đầy! Số lượng hiện tại: " + currentEnrolled + "/" + course.getMaxStudents());
//                return;
//            }
//
//            // Thực hiện đăng ký
//            if (enrollmentRepository.enrollStudent(studentId, courseId)) {
//                // Cập nhật số lượng sinh viên đã đăng ký
//                courseRepository.updateEnrolledStudents(courseId, currentEnrolled + 1);
//                System.out.println("Đăng ký môn học thành công!");
//                System.out.println("Sinh viên: " + student.getFullName());
//                System.out.println("Khóa học: " + course.getCourseName());
//            } else {
//                System.out.println("Lỗi khi đăng ký môn học!");
//            }
//        } finally {
//            enrollmentLock.unlock();
//        }
//    }
//
//    /**
//     * Hủy đăng ký môn học
//     */
//    public void dropEnrollment() {
//        System.out.println("\n=== HỦY ĐĂNG KÝ MÔN HỌC ===");
//
//        String studentId = getValidInput("Nhập mã sinh viên: ", InputValidator::isValidId);
//        if (studentId == null) return;
//
//        String courseId = getValidInput("Nhập mã khóa học: ", InputValidator::isValidId);
//        if (courseId == null) return;
//
//        // Kiểm tra đăng ký có tồn tại không
//        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(studentId, courseId);
//        if (enrollment == null) {
//            System.out.println("Không tìm thấy đăng ký này!");
//            return;
//        }
//
//        // Kiểm tra trạng thái đăng ký
//        if (!"enrolled".equalsIgnoreCase(enrollment.getStatus())) {
//            System.out.println("Đăng ký không ở trạng thái enrolled, không thể hủy!");
//            return;
//        }
//
//        System.out.println("Thông tin đăng ký:");
//        System.out.println("Sinh viên: " + studentId);
//        System.out.println("Khóa học: " + courseId);
//        System.out.println("Ngày đăng ký: " + enrollment.getEnrollmentDate());
//
//        System.out.print("Bạn có chắc chắn muốn hủy đăng ký? (y/n): ");
//        String confirm = scanner.nextLine();
//
//        if (confirm.toLowerCase().equals("y") || confirm.toLowerCase().equals("yes")) {
//            if (enrollmentRepository.dropEnrollment(studentId, courseId)) {
//                // Cập nhật số lượng sinh viên đã đăng ký
//                Course course = courseRepository.findById(courseId);
//                if (course != null) {
//                    int currentEnrolled = enrollmentRepository.countEnrolledStudents(courseId);
//                    courseRepository.updateEnrolledStudents(courseId, currentEnrolled);
//                }
//                System.out.println("Hủy đăng ký thành công!");
//            } else {
//                System.out.println("Lỗi khi hủy đăng ký!");
//            }
//        } else {
//            System.out.println("Hủy thao tác.");
//        }
//    }
//
//    /**
//     * Cập nhật điểm số
//     */
//    public void updateScores() {
//        System.out.println("\n=== CẬP NHẬT ĐIỂM SỐ ===");
//
//        String studentId = getValidInput("Nhập mã sinh viên: ", InputValidator::isValidId);
//        if (studentId == null) return;
//
//        String courseId = getValidInput("Nhập mã khóa học: ", InputValidator::isValidId);
//        if (courseId == null) return;
//
//        // Tìm đăng ký
//        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(studentId, courseId);
//        if (enrollment == null) {
//            System.out.println("Không tìm thấy đăng ký này!");
//            return;
//        }
//
//        System.out.println("Thông tin đăng ký:");
//        System.out.println("Sinh viên: " + studentId);
//        System.out.println("Khóa học: " + courseId);
//        System.out.println("Trạng thái: " + enrollment.getStatus());
//
//        // Nhập điểm giữa kỳ
//        Double midtermScore = getValidScoreInput("Nhập điểm giữa kỳ (0-10): ");
//        if (midtermScore == null) return;
//
//        // Nhập điểm cuối kỳ
//        Double finalScore = getValidScoreInput("Nhập điểm cuối kỳ (0-10): ");
//        if (finalScore == null) return;
//
//        // Tính điểm tổng kết
//        Double totalScore = (midtermScore * 0.3) + (finalScore * 0.7);
//
//        // Tính điểm chữ
//        String grade = calculateGrade(totalScore);
//
//        System.out.println("Điểm tổng kết: " + totalScore);
//        System.out.println("Điểm chữ: " + grade);
//
//        System.out.print("Xác nhận cập nhật điểm? (y/n): ");
//        String confirm = scanner.nextLine();
//
//        if (confirm.toLowerCase().equals("y") || confirm.toLowerCase().equals("yes")) {
//            if (enrollmentRepository.updateScores(enrollment.getEnrollmentId(), midtermScore, finalScore, totalScore, grade)) {
//                System.out.println("Cập nhật điểm số thành công!");
//            } else {
//                System.out.println("Lỗi khi cập nhật điểm số!");
//            }
//        } else {
//            System.out.println("Hủy thao tác.");
//        }
//    }
//
//    /**
//     * Xem danh sách đăng ký của sinh viên
//     */
//    public void viewStudentEnrollments() {
//        System.out.println("\n=== DANH SÁCH ĐĂNG KÝ CỦA SINH VIÊN ===");
//
//        String studentId = getValidInput("Nhập mã sinh viên: ", InputValidator::isValidId);
//        if (studentId == null) return;
//
//        List<Enrollment> enrollments = enrollmentRepository.findByStudent(studentId);
//        if (enrollments.isEmpty()) {
//            System.out.println("Sinh viên chưa đăng ký môn học nào!");
//        } else {
//            System.out.println("\nDanh sách đăng ký của sinh viên " + studentId + ":");
//            System.out.printf("%-15s %-15s %-20s %-10s %-10s %-10s %-10s %-5s%n",
//                            "Mã đăng ký", "Mã khóa học", "Ngày đăng ký", "Trạng thái", "Giữa kỳ", "Cuối kỳ", "Tổng kết", "Điểm");
//            System.out.println("-".repeat(100));
//
//            for (Enrollment enrollment : enrollments) {
//                System.out.printf("%-15s %-15s %-20s %-10s %-10s %-10s %-10s %-5s%n",
//                                enrollment.getEnrollmentId(),
//                                enrollment.getCourseId(),
//                                enrollment.getEnrollmentDate(),
//                                enrollment.getStatus(),
//                                enrollment.getMidtermScore() != null ? enrollment.getMidtermScore() : "N/A",
//                                enrollment.getFinalScore() != null ? enrollment.getFinalScore() : "N/A",
//                                enrollment.getTotalScore() != null ? enrollment.getTotalScore() : "N/A",
//                                enrollment.getGrade() != null ? enrollment.getGrade() : "N/A");
//            }
//        }
//    }
//
//    /**
//     * Xem danh sách sinh viên trong khóa học
//     */
//    public void viewCourseStudents() {
//        System.out.println("\n=== DANH SÁCH SINH VIÊN TRONG KHÓA HỌC ===");
//
//        String courseId = getValidInput("Nhập mã khóa học: ", InputValidator::isValidId);
//        if (courseId == null) return;
//
//        List<Enrollment> enrollments = enrollmentRepository.findByCourse(courseId);
//        if (enrollments.isEmpty()) {
//            System.out.println("Khóa học chưa có sinh viên nào đăng ký!");
//        } else {
//            System.out.println("\nDanh sách sinh viên trong khóa học " + courseId + ":");
//            System.out.printf("%-15s %-15s %-20s %-10s %-10s %-10s %-10s %-5s%n",
//                            "Mã đăng ký", "Mã sinh viên", "Ngày đăng ký", "Trạng thái", "Giữa kỳ", "Cuối kỳ", "Tổng kết", "Điểm");
//            System.out.println("-".repeat(100));
//
//            for (Enrollment enrollment : enrollments) {
//                System.out.printf("%-15s %-15s %-20s %-10s %-10s %-10s %-10s %-5s%n",
//                                enrollment.getEnrollmentId(),
//                                enrollment.getStudentId(),
//                                enrollment.getEnrollmentDate(),
//                                enrollment.getStatus(),
//                                enrollment.getMidtermScore() != null ? enrollment.getMidtermScore() : "N/A",
//                                enrollment.getFinalScore() != null ? enrollment.getFinalScore() : "N/A",
//                                enrollment.getTotalScore() != null ? enrollment.getTotalScore() : "N/A",
//                                enrollment.getGrade() != null ? enrollment.getGrade() : "N/A");
//            }
//        }
//    }
//
//    /**
//     * Xem tất cả đăng ký
//     */
//    public void viewAllEnrollments() {
//        System.out.println("\n=== DANH SÁCH TẤT CẢ ĐĂNG KÝ ===");
//
//        List<Enrollment> enrollments = enrollmentRepository.findAll();
//        if (enrollments.isEmpty()) {
//            System.out.println("Không có đăng ký nào trong hệ thống!");
//        } else {
//            System.out.printf("%-15s %-15s %-15s %-20s %-10s %-10s %-10s %-10s %-5s%n",
//                            "Mã đăng ký", "Mã sinh viên", "Mã khóa học", "Ngày đăng ký", "Trạng thái", "Giữa kỳ", "Cuối kỳ", "Tổng kết", "Điểm");
//            System.out.println("-".repeat(120));
//
//            for (Enrollment enrollment : enrollments) {
//                System.out.printf("%-15s %-15s %-15s %-20s %-10s %-10s %-10s %-10s %-5s%n",
//                                enrollment.getEnrollmentId(),
//                                enrollment.getStudentId(),
//                                enrollment.getCourseId(),
//                                enrollment.getEnrollmentDate(),
//                                enrollment.getStatus(),
//                                enrollment.getMidtermScore() != null ? enrollment.getMidtermScore() : "N/A",
//                                enrollment.getFinalScore() != null ? enrollment.getFinalScore() : "N/A",
//                                enrollment.getTotalScore() != null ? enrollment.getTotalScore() : "N/A",
//                                enrollment.getGrade() != null ? enrollment.getGrade() : "N/A");
//            }
//        }
//    }
//
//    /**
//     * Tính điểm chữ
//     */
//    private String calculateGrade(Double totalScore) {
//        if (totalScore >= 9.0) return "A+";
//        if (totalScore >= 8.5) return "A";
//        if (totalScore >= 8.0) return "B+";
//        if (totalScore >= 7.0) return "B";
//        if (totalScore >= 6.5) return "C+";
//        if (totalScore >= 5.5) return "C";
//        if (totalScore >= 5.0) return "D+";
//        if (totalScore >= 4.0) return "D";
//        return "F";
//    }
//
//    /**
//     * Lấy input hợp lệ từ user
//     */
//    private String getValidInput(String prompt, java.util.function.Function<String, Boolean> validator) {
//        while (true) {
//            System.out.print(prompt);
//            String input = scanner.nextLine();
//
//            if (InputValidator.isEmpty(input)) {
//                System.out.println("Dữ liệu không được để trống!");
//                continue;
//            }
//
//            if (validator.apply(input)) {
//                return input;
//            } else {
//                System.out.println("Dữ liệu không hợp lệ! Vui lòng nhập lại.");
//            }
//        }
//    }
//
//    /**
//     * Lấy điểm số hợp lệ từ user
//     */
//    private Double getValidScoreInput(String prompt) {
//        while (true) {
//            System.out.print(prompt);
//            String input = scanner.nextLine();
//
//            if (InputValidator.isEmpty(input)) {
//                System.out.println("Điểm số không được để trống!");
//                continue;
//            }
//
//            try {
//                Double score = Double.parseDouble(input);
//                if (InputValidator.isValidScore(score)) {
//                    return score;
//                } else {
//                    System.out.println("Điểm số phải từ 0 đến 10!");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Điểm số phải là số thực!");
//            }
//        }
//    }
//}
