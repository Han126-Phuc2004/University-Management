package service;

import entity.Course;
import repository.CourseRepository;
import util.DBConnection;
import util.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * Service class cho Course
 * Xử lý business logic liên quan đến khóa học
 */
public class CourseService {
    private CourseRepository courseRepository;
    private Scanner scanner;

    public CourseService() {
        this.courseRepository = new CourseRepository();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Thêm khóa học mới
     */
    public void addCourse() {
        System.out.println("\n=== THÊM KHÓA HỌC MỚI ===");
        // Nhập course_id (khóa chính dạng chuỗi)
        System.out.print("Nhập mã khóa học (course_id): ");
        String courseId = scanner.nextLine();
        if (InputValidator.isEmpty(courseId)) {
            System.out.println("Mã khóa học không được để trống!");
            return;
        }

        System.out.print("Nhập tên khóa học: ");
        String courseName = scanner.nextLine();
        if (InputValidator.isEmpty(courseName)) {
            System.out.println("Tên khóa học không được để trống!");
            return;
        }

        System.out.print("Nhập mô tả khóa học: ");
        String description = scanner.nextLine();

        int credits = getValidCreditsInput("Nhập số tín chỉ (1-6): ");
        if (credits == -1) return;

        System.out.print("Nhập mã giảng viên (nếu có, Enter để bỏ qua): ");
        String lecturerId = scanner.nextLine();
        if (InputValidator.isEmpty(lecturerId)) lecturerId = null;

        System.out.print("Nhập mã khoa (nếu có, Enter để bỏ qua): ");
        String departmentId = scanner.nextLine();
        if (InputValidator.isEmpty(departmentId)) departmentId = null;

        System.out.print("Nhập mã học kỳ (nếu có, Enter để bỏ qua): ");
        String semesterId = scanner.nextLine();
        if (InputValidator.isEmpty(semesterId)) semesterId = null;

        int maxStudents = getValidMaxStudentsInput("Nhập số lượng sinh viên tối đa: ");
        if (maxStudents == -1) return;

        // enrolled_students mặc định 0
        Course course = new Course(courseId, courseName, description, credits, lecturerId,
                departmentId, semesterId, maxStudents, 0);

        if (courseRepository.addCourse(course)) {
            System.out.println("Thêm khóa học thành công!");
        } else {
            System.out.println("Lỗi khi thêm khóa học!");
        }
    }

    /**
     * Cập nhật thông tin khóa học
     */
    public void updateCourse() {
        System.out.println("\n=== CẬP NHẬT THÔNG TIN KHÓA HỌC ===");

        System.out.print("Nhập mã khóa học cần cập nhật: ");
        String courseId = scanner.nextLine();
        if (InputValidator.isEmpty(courseId)) return;

        Course course = courseRepository.findById(courseId);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học với mã " + courseId);
            return;
        }

        System.out.println("Thông tin hiện tại:");
        System.out.println(course);

        System.out.println("\nNhập thông tin mới (Enter để giữ nguyên):");

        System.out.print("Tên khóa học [" + course.getCourseName() + "]: ");
        String courseName = scanner.nextLine();
        if (!InputValidator.isEmpty(courseName)) {
            course.setCourseName(courseName);
}

        System.out.print("Mô tả [" + course.getDescription() + "]: ");
        String description = scanner.nextLine();
        if (!InputValidator.isEmpty(description)) {
            course.setDescription(description);
        }

        System.out.print("Số tín chỉ [" + course.getCredits() + "] (1-6): ");
        String creditsStr = scanner.nextLine();
        if (!InputValidator.isEmpty(creditsStr)) {
            try {
                int credits = Integer.parseInt(creditsStr);
                if (credits >= 1 && credits <= 6) { // Phù hợp chk_credits
                    course.setCredits(credits);
                }
            } catch (NumberFormatException e) {
                System.out.println("Số tín chỉ không hợp lệ, giữ nguyên giá trị cũ.");
            }
        }

        System.out.print("Mã giảng viên [" + course.getLecturerId() + "]: ");
        String lecturerIdStr = scanner.nextLine();
        if (!InputValidator.isEmpty(lecturerIdStr)) {
            course.setLecturerId(lecturerIdStr);
        }

        System.out.print("Mã khoa [" + course.getDepartmentId() + "]: ");
        String departmentIdStr = scanner.nextLine();
        if (!InputValidator.isEmpty(departmentIdStr)) {
            course.setDepartmentId(departmentIdStr);
        }

        System.out.print("Mã học kỳ [" + course.getSemesterId() + "]: ");
        String semesterIdStr = scanner.nextLine();
        if (!InputValidator.isEmpty(semesterIdStr)) {
            course.setSemesterId(semesterIdStr);
        }

        System.out.print("Số lượng sinh viên tối đa [" + course.getMaxStudents() + "]: ");
        String maxStudentsStr = scanner.nextLine();
        if (!InputValidator.isEmpty(maxStudentsStr)) {
            try {
                int maxStudents = Integer.parseInt(maxStudentsStr);
                if (maxStudents > 0) { // Phù hợp chk_max_students
                    course.setMaxStudents(maxStudents);
                }
            } catch (NumberFormatException e) {
                System.out.println("Số lượng sinh viên tối đa không hợp lệ, giữ nguyên giá trị cũ.");
            }
        }

        if (courseRepository.updateCourse(course)) {
            System.out.println("Cập nhật thông tin khóa học thành công!");
        } else {
System.out.println("Lỗi khi cập nhật thông tin khóa học!");
        }
    }

    /**
     * Xóa khóa học
     */
    public void deleteCourse() {
        System.out.println("\n=== XÓA KHÓA HỌC ===");

        System.out.print("Nhập mã khóa học cần xóa: ");
        String courseId = scanner.nextLine();
        if (InputValidator.isEmpty(courseId)) return;

        Course course = courseRepository.findById(courseId);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học với mã " + courseId);
            return;
        }

        System.out.println("Thông tin khóa học sẽ bị xóa:");
        System.out.println(course);

        System.out.print("Bạn có chắc chắn muốn xóa? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.toLowerCase().equals("y") || confirm.toLowerCase().equals("yes")) {
            if (courseRepository.deleteCourse(courseId)) {
                System.out.println("Xóa khóa học thành công!");
            } else {
                System.out.println("Lỗi khi xóa khóa học!");
            }
        } else {
            System.out.println("Hủy thao tác xóa.");
        }
    }

    /**
     * Hiển thị thông tin khóa học
     */
    public void viewCourse() {
        System.out.println("\n=== XEM THÔNG TIN KHÓA HỌC ===");

        System.out.print("Nhập mã khóa học: ");
        String courseId = scanner.nextLine();
        if (InputValidator.isEmpty(courseId)) return;

        Course course = courseRepository.findById(courseId);
        if (course != null) {
            System.out.println("\nThông tin khóa học:");
            System.out.println(course);
        } else {
            System.out.println("Không tìm thấy khóa học với mã " + courseId);
        }
    }

    /**
     * Hiển thị danh sách tất cả khóa học
     */
    public void listAllCourses() {
        System.out.println("\n=== DANH SÁCH TẤT CẢ KHÓA HỌC ===");

        List<Course> courses = courseRepository.findAll();
        if (courses.isEmpty()) {
            System.out.println("Không có khóa học nào trong hệ thống.");
        } else {
            System.out.printf("%-10s %-30s %-8s %-15s %-15s %-15s %-10s %-10s%n",
                    "Mã KH", "Tên khóa học", "Tín chỉ", "Giảng viên", "Khoa", "Học kỳ", "Tối đa", "Đã đăng ký");
            System.out.println("-".repeat(120));

            for (Course course : courses) {
                System.out.printf("%-10s %-30s %-8s %-15s %-15s %-15s %-10s %-10s%n",
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getCredits(),
                        course.getLecturerId() != null ? course.getLecturerId() : "N/A",
                        course.getDepartmentId() != null ? course.getDepartmentId() : "N/A",
                        course.getSemesterId() != null ? course.getSemesterId() : "N/A",
course.getMaxStudents(),
                        course.getEnrolledStudents());
            }
        }
    }

    /**
     * Tìm kiếm khóa học theo tên
     */
    public void searchCoursesByName() {
        System.out.println("\n=== TÌM KIẾM KHÓA HỌC THEO TÊN ===");

        System.out.print("Nhập tên khóa học cần tìm: ");
        String name = scanner.nextLine();

        if (InputValidator.isEmpty(name)) {
            System.out.println("Tên không được để trống!");
            return;
        }

        List<Course> courses = courseRepository.findByName(name);
        if (courses.isEmpty()) {
            System.out.println("Không tìm thấy khóa học nào với tên: " + name);
        } else {
            System.out.println("\nKết quả tìm kiếm:");
            System.out.printf("%-10s %-30s %-8s %-15s %-15s %-15s %-10s %-10s%n",
                    "Mã KH", "Tên khóa học", "Tín chỉ", "Giảng viên", "Khoa", "Học kỳ", "Tối đa", "Đã đăng ký");
            System.out.println("-".repeat(120));

            for (Course course : courses) {
                System.out.printf("%-10s %-30s %-8s %-15s %-15s %-15s %-10s %-10s%n",
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getCredits(),
                        course.getLecturerId() != null ? course.getLecturerId() : "N/A",
                        course.getDepartmentId() != null ? course.getDepartmentId() : "N/A",
                        course.getSemesterId() != null ? course.getSemesterId() : "N/A",
                        course.getMaxStudents(),
                        course.getEnrolledStudents());
            }
        }
    }

    /**
     * Tìm kiếm khóa học theo giảng viên
     */
    public void searchCoursesByLecturer() {
        System.out.println("\n=== TÌM KIẾM KHÓA HỌC THEO GIẢNG VIÊN ===");

        System.out.print("Nhập mã giảng viên: ");
        String lecturerId = scanner.nextLine();
        if (InputValidator.isEmpty(lecturerId)) return;

        List<Course> courses = courseRepository.findByLecturer(lecturerId);
        if (courses.isEmpty()) {
            System.out.println("Không tìm thấy khóa học nào của giảng viên " + lecturerId);
        } else {
            System.out.println("\nDanh sách khóa học của giảng viên " + lecturerId + ":");
            System.out.printf("%-10s %-30s %-8s %-15s %-15s %-10s %-10s%n",
                    "Mã KH", "Tên khóa học", "Tín chỉ", "Khoa", "Học kỳ", "Tối đa", "Đã đăng ký");
            System.out.println("-".repeat(110));

            for (Course course : courses) {
                System.out.printf("%-10s %-30s %-8s %-15s %-15s %-10s %-10s%n",
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getCredits(),
course.getDepartmentId() != null ? course.getDepartmentId() : "N/A",
                        course.getSemesterId() != null ? course.getSemesterId() : "N/A",
                        course.getMaxStudents(),
                        course.getEnrolledStudents());
            }
        }
    }

    /**
     * Đăng ký sinh viên vào khóa học
     */
    public String registerStudent(String courseId, String studentName) {
        // Retry mechanism với 3 lần thử
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                // Lấy course từ database để có dữ liệu mới nhất
                Course course = courseRepository.findById(courseId);
                if (course == null) {
                    if (attempt < 3) {
                        Thread.sleep(100); // Đợi 100ms trước khi thử lại
                        continue;
                    }
                    return "FAIL: Course not found";
                }

                // Kiểm tra và cập nhật trong một transaction
                String checkAndUpdateSql = "UPDATE course SET enrolled_students = enrolled_students + 1 WHERE course_id = ? AND enrolled_students < max_students";
                
                try (var connection = DBConnection.getConnection();
                     var pstmt = connection.prepareStatement(checkAndUpdateSql)) {
                    if (connection == null) {
                        if (attempt < 3) {
                            Thread.sleep(100);
                            continue;
                        }
                        return "FAIL: Cannot connect to database";
                    }
                    
                    pstmt.setString(1, courseId);
                    int rowsUpdated = pstmt.executeUpdate();
                    
                    if (rowsUpdated > 0) {
                        // Cập nhật thành công, lưu thông tin đăng ký
                        courseRepository.updateEnrollment(courseId, studentName);
                        System.out.printf("  Đăng ký thành công: Sinh viên '%s' đã đăng ký môn '%s'.%n", studentName, course.getCourseName());
                        return "SUCCESS";
                    } else {
                        // Không cập nhật được (đã đủ sinh viên)
                        System.err.printf("  Đăng ký thất bại: Môn '%s' đã đủ số lượng sinh viên tối đa.%n", course.getCourseName());
                        return "FAIL";
                    }
                }
            } catch (Exception e) {
                if (attempt < 3) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                System.err.println("Lỗi khi đăng ký (attempt " + attempt + "): " + e.getMessage());
                return "FAIL";
            }
        }
        return "FAIL";
    }

    /**
     * Lấy số nguyên hợp lệ từ user
     * @param prompt Prompt
     * @param allowNull Cho phép null (Enter để bỏ qua)
     * @return Integer hoặc null
     */
    private Integer getValidIntegerInput(String prompt, boolean allowNull) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (allowNull && InputValidator.isEmpty(input)) {
                return null;
            }

            if (InputValidator.isEmpty(input)) {
                System.out.println("Dữ liệu không được để trống!");
                continue;
            }

            try {
                int value = Integer.parseInt(input);
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Dữ liệu phải là số nguyên! Vui lòng nhập lại.");
            }
        }
    }

    /**
     * Lấy số tín chỉ hợp lệ từ user
     */
    private int getValidCreditsInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (InputValidator.isEmpty(input)) {
                System.out.println("Số tín chỉ không được để trống!");
                continue;
            }

            try {
                int credits = Integer.parseInt(input);
                if (credits >= 1 && credits <= 6) { // Phù hợp chk_credits
                    return credits;
                } else {
                    System.out.println("Số tín chỉ phải từ 1 đến 6!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Số tín chỉ phải là số nguyên!");
            }
        }
    }

    /**
     * Lấy số lượng sinh viên tối đa hợp lệ từ user
     */
    private int getValidMaxStudentsInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (InputValidator.isEmpty(input)) {
                System.out.println("Số lượng sinh viên tối đa không được để trống!");
                continue;
            }

            try {
                int maxStudents = Integer.parseInt(input);
                if (maxStudents > 0) { // Phù hợp chk_max_students
                    return maxStudents;
                } else {
                    System.out.println("Số lượng sinh viên tối đa phải lớn hơn 0!");
                }
            } catch (NumberFormatException e) {
System.out.println("Số lượng sinh viên tối đa phải là số nguyên!");
            }
        }
    }

    /**
     * Main method để test các chức năng
     * Chạy từng phương thức để kiểm tra
     */
    public static void main(String[] args) {
        CourseService service = new CourseService();

        // Test addCourse
        System.out.println("Testing addCourse:");
        service.addCourse();

        // Test listAllCourses
        System.out.println("Testing listAllCourses:");
        service.listAllCourses();

        // Test viewCourse (thay 1 bằng ID thực tế sau khi add)
        System.out.println("Testing viewCourse:");
        service.viewCourse();

        // Test updateCourse (thay ID thực tế)
        System.out.println("Testing updateCourse:");
        service.updateCourse();

        // Test searchCoursesByName
        System.out.println("Testing searchCoursesByName:");
        service.searchCoursesByName();

        // Test searchCoursesByLecturer (thay ID thực tế)
        System.out.println("Testing searchCoursesByLecturer:");
        service.searchCoursesByLecturer();

        // Test deleteCourse (thay ID thực tế)
        System.out.println("Testing deleteCourse:");
        service.deleteCourse();

        // Đóng kết nối sau test
        DBConnection.closeConnection();
    }
    
    /**
     * Lấy khóa học theo ID
     */
    public entity.Course getCourseById(String courseId) {
        return courseRepository.findById(courseId);
    }
}