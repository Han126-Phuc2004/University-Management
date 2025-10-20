//import repository.EnrollmentRepository;
//import repository.StudentRepository;
//import repository.CourseRepository;
//import entity.Student;
//import entity.Enrollment;
//import util.DBConnection;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//
//public class EnrollmentRegisterTest {
//    public static void main(String[] args) {
//        System.out.println("=== ENROLLMENT TEST===");
//
//        // Ensure dependencies
//        ensureDepartmentExists("SE", "Khoa Phần mềm");
//        ensureSemesterExists("SEMTEST", "Học kỳ Test");
//        ensureCourseExists("LAB211", "LAB - OOP", 3);
//        ensureCourseExists("DBI202", "Cơ sở dữ liệu", 3);
//        ensureCourseExists("CSD201", "Cấu trúc dữ liệu & Giải thuật", 4);
//
//        StudentRepository studentRepo = new StudentRepository();
//        CourseRepository courseRepo = new CourseRepository();
//        EnrollmentRepository enrollRepo = new EnrollmentRepository();
//
//        // Fixed target students that already exist in table student
//        String[] studentIds = {"DE180101", "DE180102", "DE180103", "DE180104", "DE180105"};
//        String[] courseIds = {"LAB211", "DBI202", "CSD201"};
//
//        // Validate presence of students
//        for (String sid : studentIds) {
//            Student s = studentRepo.findById(sid);
//            if (s == null) {
//                System.out.println("Student not found in DB, please insert first: " + sid);
//                return;
//            }
//        }
//
//        // Clear existing pairs to avoid unique constraint, then enroll
//        System.out.println("\n-- ENROLL 5 STUDENTS INTO 3 COURSES --");
//        for (String sid : studentIds) {
//            for (String cid : courseIds) {
//                clearEnrollmentPair(sid, cid);
//                boolean ok = enrollRepo.enrollStudent(sid, cid);
//                System.out.println("enroll " + sid + " -> " + cid + " = " + ok);
//            }
//        }
//
//        // Show counts per course
//        System.out.println("\n-- ENROLLED COUNTS --");
//        for (String cid : courseIds) {
//            System.out.println(cid + ": " + enrollRepo.countEnrolledStudents(cid));
//        }
//
//        // List chi tiết sau khi đăng ký
//        System.out.println("\n-- ENROLLMENT LIST BY COURSE --");
//        for (String cid : courseIds) {
//            System.out.println("Course: " + cid);
//            for (Enrollment e : enrollRepo.findByCourse(cid)) {
//                System.out.println(e);
//            }
//        }
//
//        System.out.println("\n=== DONE ===");
//    }
//
//    private static void ensureDepartmentExists(String id, String name) {
//        String checkSql = "SELECT 1 FROM department WHERE department_id=?";
//        String insertSql = "INSERT INTO department (department_id, department_name) VALUES (?, ?)";
//        Connection conn = DBConnection.getConnection();
//        try (PreparedStatement check = conn.prepareStatement(checkSql)) {
//            check.setString(1, id);
//            try (ResultSet rs = check.executeQuery()) {
//                if (!rs.next()) {
//                    try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
//                        ins.setString(1, id);
//                        ins.setString(2, name);
//                        ins.executeUpdate();
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Seed department error: " + e.getMessage());
//        }
//    }
//
//    private static void ensureSemesterExists(String id, String name) {
//        String checkSql = "SELECT 1 FROM semester WHERE semester_id=?";
//        String insertSql = "INSERT INTO semester (semester_id, semester_name, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
//        Connection conn = DBConnection.getConnection();
//        try (PreparedStatement check = conn.prepareStatement(checkSql)) {
//            check.setString(1, id);
//            try (ResultSet rs = check.executeQuery()) {
//                if (!rs.next()) {
//                    try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
//                        ins.setString(1, id);
//                        ins.setString(2, name);
//                        ins.setDate(3, java.sql.Date.valueOf(LocalDate.now().minusMonths(1)));
//                        ins.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusMonths(3)));
//                        ins.setString(5, "ongoing");
//                        ins.executeUpdate();
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Seed semester error: " + e.getMessage());
//        }
//    }
//
//    private static void ensureCourseExists(String courseId, String courseName, int credits) {
//        String checkSql = "SELECT 1 FROM course WHERE course_id=?";
//        String insertSql = "INSERT INTO course (course_id, course_name, description, credits, lecturer_id, department_id, semester_id, max_students, enrolled_students, room, schedule, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        Connection conn = DBConnection.getConnection();
//        try (PreparedStatement check = conn.prepareStatement(checkSql)) {
//            check.setString(1, courseId);
//            try (ResultSet rs = check.executeQuery()) {
//                if (!rs.next()) {
//                    try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
//                        ins.setString(1, courseId);
//                        ins.setString(2, courseName);
//                        ins.setString(3, courseName);
//                        ins.setInt(4, credits);
//                        ins.setString(5, null);
//                        ins.setString(6, "SE");
//                        ins.setString(7, "SEMTEST");
//                        ins.setInt(8, 50);
//                        ins.setInt(9, 0);
//                        ins.setString(10, "A101");
//                        ins.setString(11, "T2-4 08:00-10:00");
//                        ins.setString(12, "open");
//                        ins.executeUpdate();
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Seed course error: " + e.getMessage());
//        }
//    }
//
//    private static void clearEnrollmentPair(String studentId, String courseId) {
//        String sql = "DELETE FROM enrollment WHERE student_id=? AND course_id=?";
//        Connection conn = DBConnection.getConnection();
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, studentId);
//            stmt.setString(2, courseId);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            System.err.println("Clear enrollment pair error: " + e.getMessage());
//        }
//    }
//}
//
//
