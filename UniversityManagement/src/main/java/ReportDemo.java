import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDemo {
    public static void main(String[] args) {
        System.out.println("=== SIMPLE REPORTS ===");

        // Ví dụ: danh sách sinh viên trong 1 khóa học
        printStudentsInCourse("WED201");

        // Ví dụ: danh sách khóa học của 1 sinh viên
        printCoursesOfStudent("DE180101");

        // Ví dụ: tổng số sinh viên đang enrolled theo từng khóa học
        printEnrollmentCounts();

        System.out.println("=== DONE ===");
    }

    /**
     * Danh sách sinh viên trong 1 khóa học (id, tên, email, phone, ngày đăng ký, trạng thái)
     */
    public static void printStudentsInCourse(String courseId) {
        String sql = "SELECT e.student_id, s.full_name, s.email, s.phone, e.enrollment_date, e.status " +
                     "FROM enrollment e JOIN student s ON s.student_id = e.student_id " +
                     "WHERE e.course_id = ? ORDER BY e.enrollment_date";
        System.out.println("\n-- Students in course: " + courseId + " --");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.printf("%-12s %-28s %-28s %-12s %-20s %-10s%n",
                        "student_id", "full_name", "email", "phone", "enrollment_date", "status");
                while (rs.next()) {
                    System.out.printf("%-12s %-28s %-28s %-12s %-20s %-10s%n",
                            rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getTimestamp(5), rs.getString(6));
                }
            }
        } catch (SQLException e) {
            System.err.println("Report error (students in course): " + e.getMessage());
        }
    }

    /**
     * Danh sách khóa học của 1 sinh viên (id, tên môn, trạng thái)
     */
    public static void printCoursesOfStudent(String studentId) {
        String sql = "SELECT e.course_id, c.course_name, e.status, e.enrollment_date " +
                     "FROM enrollment e JOIN course c ON c.course_id = e.course_id " +
                     "WHERE e.student_id = ? ORDER BY e.enrollment_date DESC";
        System.out.println("\n-- Courses of student: " + studentId + " --");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.printf("%-10s %-40s %-10s %-20s%n", "course_id", "course_name", "status", "enroll_date");
                while (rs.next()) {
                    System.out.printf("%-10s %-40s %-10s %-20s%n",
                            rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4));
                }
            }
        } catch (SQLException e) {
            System.err.println("Report error (courses of student): " + e.getMessage());
        }
    }

    /**
     * Tổng số sinh viên đang enrolled theo từng khóa học
     */
    public static void printEnrollmentCounts() {
        String sql = "SELECT c.course_id, c.course_name, COUNT(*) AS enrolled_count " +
                     "FROM course c LEFT JOIN enrollment e ON c.course_id = e.course_id AND e.status='enrolled' " +
                     "GROUP BY c.course_id, c.course_name ORDER BY c.course_id";
        System.out.println("\n-- Enrollment counts by course --");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.printf("%-10s %-40s %-8s%n", "course_id", "course_name", "enrolled");
            while (rs.next()) {
                System.out.printf("%-10s %-40s %-8d%n", rs.getString(1), rs.getString(2), rs.getInt(3));
            }
        } catch (SQLException e) {
            System.err.println("Report error (counts): " + e.getMessage());
        }
    }
}


