package repository;

import entity.Course;
import util.DBConnection;
import repository.ReadCourseFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {

    private Connection connection;

    public CourseRepository() {
        this.connection = DBConnection.getConnection();
    }

    public boolean addCourse(Course c) {
        String sql = "INSERT INTO course (course_id, course_name, description, credits, "
                + "lecturer_id, department_id, semester_id, max_students, enrolled_students) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setNString(1, String.valueOf(c.getCourseId()));
            ps.setNString(2, c.getCourseName());
            ps.setNString(3, c.getDescription());
            ps.setInt(4, c.getCredits());
            if (c.getLecturerId() != null) {
                ps.setNString(5, String.valueOf(c.getLecturerId()));
            } else {
                ps.setNull(5, Types.NVARCHAR);
            }
            if (c.getDepartmentId() != null) {
                ps.setNString(6, String.valueOf(c.getDepartmentId()));
            } else {
                ps.setNull(6, Types.NVARCHAR);
            }
            if (c.getSemesterId() != null) {
                ps.setNString(7, String.valueOf(c.getSemesterId()));
            } else {
                ps.setNull(7, Types.NVARCHAR);
            }
            ps.setInt(8, c.getMaxStudents());
            ps.setInt(9, c.getEnrolledStudents());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("addCourse error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCourse(Course course) {
        String sql = "UPDATE course SET course_name = ?, description = ?, credits = ?, lecturer_id = ?, department_id = ?, semester_id = ?, "
                + "max_students = ?, enrolled_students = ? WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getDescription());
            pstmt.setInt(3, course.getCredits());
            if (course.getLecturerId() != null) {
                pstmt.setInt(4, course.getLecturerId());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            if (course.getDepartmentId() != null) {
                pstmt.setInt(5, course.getDepartmentId());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            if (course.getSemesterId() != null) {
                pstmt.setInt(6, course.getSemesterId());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
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

    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM course WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa khóa học: " + e.getMessage());
            return false;
        }
    }

    public Course findById(int courseId) {
        String sql = "SELECT * FROM course WHERE course_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractCourseFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khóa học theo ID: " + e.getMessage());
        }
        return null;
    }

    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course";
        try (PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
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

    public List<Course> findByLecturer(int lecturerId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE lecturer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, lecturerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khóa học theo giảng viên: " + e.getMessage());
        }
        return courses;
    }

    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        return new Course(
                rs.getString("course_id"),
                rs.getString("course_name"),
                rs.getString("description"),
                rs.getInt("credits"),
                rs.getObject("lecturer_id") != null ? rs.getInt("lecturer_id") : null,
                rs.getObject("department_id") != null ? rs.getInt("department_id") : null,
                rs.getObject("semester_id") != null ? rs.getInt("semester_id") : null,
                rs.getInt("max_students"),
                rs.getInt("enrolled_students")
        );
    }

    public void saveCourseFromCSVToBD(String filePath) {
        List<Course> courses = ReadCourseFile.loadCoursesFromCSV(filePath);
        int successCount = 0;
        for (Course course : courses) {
            if (addCourse(course)) {
                successCount++;
            }
        }
        System.out.println("Đã lưu thành công " + successCount + "/" + courses.size() + " courses từ file CSV vào database.");
    }

    public static void main(String[] args) {
        CourseRepository repo = new CourseRepository();

        // Test addCourse
        System.out.println("Testing addCourse:");
        Course newCourse = new Course("Test Course", "This is a test description", 3, 1, 1, 1, 50, 0);
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
        Course foundCourse = repo.findById(2);
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
            Course updatedCourse = repo.findById(1);
            System.out.println(updatedCourse);
        }

        // Test findByName
        System.out.println("\nTesting findByName:");
        List<Course> coursesByName = repo.findByName("Test");
        for (Course course : coursesByName) {
            System.out.println(course);
        }

        // Test findByLecturer (thay 1 bằng lecturer_id thực tế)
        System.out.println("\nTesting findByLecturer:");
        List<Course> coursesByLecturer = repo.findByLecturer(2);
        for (Course course : coursesByLecturer) {
            System.out.println(course);
        }

        // Test deleteCourse (thay 1 bằng ID thực tế, cẩn thận vì xóa thật)
        System.out.println("\nTesting deleteCourse:");
        boolean deleted = repo.deleteCourse(15); // Thay 1 bằng ID thực, comment out nếu không muốn xóa
        System.out.println("Delete successful: " + deleted);

    }
}
