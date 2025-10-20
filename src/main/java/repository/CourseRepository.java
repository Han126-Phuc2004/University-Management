package repository;

import entity.Course;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class CourseRepository {

    private Connection connection;

    public CourseRepository() {
        this.connection = DBConnection.getConnection();
    }

    public boolean addCourse(Course course) {
        String sql = "INSERT INTO course (course_id, course_name, description, credits, lecturer_id, department_id, semester_id, max_students, enrolled_students) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getDescription());
            pstmt.setInt(4, course.getCredits());
            // lecturer_id
            if (course.getLecturerId() != null) {
                pstmt.setString(5, String.valueOf(course.getLecturerId()));
            } else {
                pstmt.setNull(5, java.sql.Types.VARCHAR);
            }
            // department_id
            if (course.getDepartmentId() != null) {
                pstmt.setString(6, String.valueOf(course.getDepartmentId()));
            } else {
                pstmt.setNull(6, java.sql.Types.VARCHAR);
            }
            // semester_id
            if (course.getSemesterId() != null) {
                pstmt.setString(7, String.valueOf(course.getSemesterId()));
            } else {
                pstmt.setNull(7, java.sql.Types.VARCHAR);
            }
            pstmt.setInt(8, course.getMaxStudents());
            pstmt.setInt(9, course.getEnrolledStudents());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm khóa học: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCourse(Course course) {
        String sql = "UPDATE course SET course_name = ?, description = ?, credits = ?, lecturer_id = ?, department_id = ?, semester_id = ?, " +
                "max_students = ?, enrolled_students = ? WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getDescription());
            pstmt.setInt(3, course.getCredits());
            // lecturer_id
            if (course.getLecturerId() != null) {
                pstmt.setString(4, String.valueOf(course.getLecturerId()));
            } else {
                pstmt.setNull(4, java.sql.Types.VARCHAR);
            }
            // department_id
            if (course.getDepartmentId() != null) {
                pstmt.setString(5, String.valueOf(course.getDepartmentId()));
            } else {
                pstmt.setNull(5, java.sql.Types.VARCHAR);
            }
            // semester_id
            if (course.getSemesterId() != null) {
                pstmt.setString(6, String.valueOf(course.getSemesterId()));
            } else {
                pstmt.setNull(6, java.sql.Types.VARCHAR);
            }
            pstmt.setInt(7, course.getMaxStudents());
            pstmt.setInt(8, course.getEnrolledStudents());
            pstmt.setString(9, course.getCourseId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật khóa học: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(String courseId) {
        String sql = "DELETE FROM course WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa khóa học: " + e.getMessage());
            return false;
        }
    }

    public Course findById(String courseId) {
        // Retry mechanism với 3 lần thử
        for (int attempt = 1; attempt <= 3; attempt++) {
            String sql = "SELECT * FROM course WHERE course_id = ?";
            try (var connection = DBConnection.getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {
                if (connection == null) {
                    if (attempt < 3) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        continue;
                    }
                    return null;
                }
                
                pstmt.setString(1, courseId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return extractCourseFromResultSet(rs);
                }
            } catch (SQLException e) {
                if (attempt < 3) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                System.err.println("Lỗi khi tìm khóa học theo ID (attempt " + attempt + "): " + e.getMessage());
            }
        }
        return null;
    }


    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả khóa học: " + e.getMessage());
        }
        return courses;
    }


    public List<Course> findByName(String name) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE course_name LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khóa học theo tên: " + e.getMessage());
        }
        return courses;
    }


    public List<Course> findByLecturer(String lecturerId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE lecturer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // lecturer_id trong DB là NVARCHAR, bind dưới dạng chuỗi
            pstmt.setString(1, lecturerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
System.err.println("Lỗi khi tìm khóa học theo giảng viên: " + e.getMessage());
        }
        return courses;
    }


    /**
     * Thêm thông tin đăng ký vào bảng enrollment
     * Tạo sinh viên mới cho mỗi simulation
     */
    public boolean updateEnrollment(String courseId, String studentName) {
        // Retry mechanism với 3 lần thử
        for (int attempt = 1; attempt <= 3; attempt++) {
            try (var connection = DBConnection.getConnection()) {
                if (connection == null) {
                    if (attempt < 3) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        continue;
                    }
                    return false;
                }
                
                // Tạo sinh viên mới cho simulation
                int studentId = createStudentForSimulation(connection, studentName);
                if (studentId == -1) {
                    if (attempt < 3) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        continue;
                    }
                    return false;
                }
                
                // Kiểm tra xem đã có enrollment cho student này và course này chưa
                String checkSql = "SELECT COUNT(*) FROM enrollment WHERE student_id = ? AND course_id = ?";
                try (PreparedStatement checkPstmt = connection.prepareStatement(checkSql)) {
                    checkPstmt.setInt(1, studentId);
                    checkPstmt.setString(2, courseId);
                    try (var rs = checkPstmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            // Đã có enrollment cho student này và course này
                            return true;
                        }
                    }
                }
                
                // Thêm enrollment mới
                String sql = "INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, studentId);
                    pstmt.setString(2, courseId);
                    return pstmt.executeUpdate() > 0;
                }
            } catch (SQLException e) {
                if (attempt < 3) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                System.err.println("Lỗi khi cập nhật enrollment (attempt " + attempt + "): " + e.getMessage());
                return false;
            }
        }
        return false;
    }
    
    /**
     * Tạo sinh viên mới cho simulation
     */
    private int createStudentForSimulation(Connection connection, String studentName) {
        try {
            // Tạo email unique cho simulation
            String email = studentName.toLowerCase().replace(" ", "") + "_simulation@student.edu.vn";
            
            // Kiểm tra xem sinh viên đã tồn tại chưa
            String checkSql = "SELECT student_id FROM student WHERE email = ?";
            try (PreparedStatement checkPstmt = connection.prepareStatement(checkSql)) {
                checkPstmt.setString(1, email);
                try (var rs = checkPstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("student_id");
                    }
                }
            }
            
            // Tạo sinh viên mới
            String insertSql = "INSERT INTO student (full_name, date_of_birth, gender, phone, email, department_id, enrollment_date, gpa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, studentName);
                pstmt.setDate(2, java.sql.Date.valueOf("2000-01-01")); // Ngày sinh mặc định
                pstmt.setString(3, "Male"); // Giới tính mặc định
                pstmt.setString(4, "0123456789"); // Số điện thoại mặc định
                pstmt.setString(5, email);
                pstmt.setString(6, "SE"); // Khoa mặc định
                pstmt.setDate(7, java.sql.Date.valueOf("2024-09-01")); // Ngày nhập học mặc định
                pstmt.setBigDecimal(8, java.math.BigDecimal.valueOf(3.5)); // GPA mặc định
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    try (var generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo sinh viên mới: " + e.getMessage());
        }
        return -1;
    }

    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        return new Course(
                rs.getString("course_id"),
                rs.getString("course_name"),
                rs.getString("description"),
                rs.getInt("credits"),
                rs.getString("lecturer_id"),
                rs.getString("department_id"),
                rs.getString("semester_id"),
                rs.getInt("max_students"),
                rs.getInt("enrolled_students")
        );
    }

    public static void main(String[] args) {
        CourseRepository repo = new CourseRepository();

        // Test addCourse
        System.out.println("Testing addCourse:");
        Course newCourse = new Course("C001", "Test Course", "This is a test description", 3, "L001", "SE", "S001", 50, 0);
        boolean added = repo.addCourse(newCourse);
        System.out.println("Add successful: " + added);

        // Test findAll
        System.out.println("\nTesting findAll:");
        List<Course> allCourses = repo.findAll();
        for (Course course : allCourses) {
            System.out.println(course);
        }

        // Test findById
        System.out.println("\nTesting findById:");
        Course foundCourse = repo.findById("C001");
        if (foundCourse != null) {
            System.out.println(foundCourse);
        } else {
            System.out.println("No course found with ID 1");
        }

        if (foundCourse != null) {
            System.out.println("\nTesting updateCourse:");
            foundCourse.setCourseName("Updated Test Course");
            boolean updated = repo.updateCourse(foundCourse);
            System.out.println("Update successful: " + updated);
            Course updatedCourse = repo.findById("C001");
            System.out.println(updatedCourse);
        }

        // Test findByName
        System.out.println("\nTesting findByName:");
        List<Course> coursesByName = repo.findByName("Test");
        for (Course course : coursesByName) {
            System.out.println(course);
        }

        // Test findByLecturer
        System.out.println("\nTesting findByLecturer:");
        List<Course> coursesByLecturer = repo.findByLecturer("L001");
        for (Course course : coursesByLecturer) {
            System.out.println(course);
        }

        // Test deleteCourse (thay 1 bằng ID thực tế, cẩn thận vì xóa thật)
        System.out.println("\nTesting deleteCourse:");
        boolean deleted = repo.deleteCourse("C001");
        System.out.println("Delete successful: " + deleted);
    }
}