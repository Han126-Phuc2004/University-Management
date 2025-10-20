//import entity.Course;
//import repository.CourseRepository;
//import util.DBConnection;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//
//public class CourseCrudTest {
//    public static void main(String[] args) {
//        System.out.println("=== COURSE CRUD TEST (real DB) ===");
//
//        // Ensure dependencies: department + semester
//        ensureDepartmentExists("SE", "Khoa Phần mềm");
//        ensureSemesterExists("SEMTEST", "Học kỳ Test");
//
//        CourseRepository repo = new CourseRepository();
//        boolean keepData = true; // giữ dữ liệu sau khi test
//
//        // Danh sách khóa học mẫu
//        String[] ids = {"LAB211", "DBI202", "CSD201", "PRJ301", "PRO192"};
//        String[] names = {
//            "LAB - OOP",
//            "Cơ sở dữ liệu",
//            "Cấu trúc dữ liệu & Giải thuật",
//            "Dự án Java Web",
//            "Lập trình hướng đối tượng"
//        };
//        String[] descs = {
//            "Thực hành OOP nâng cao",
//            "Thiết kế và truy vấn SQL",
//            "Cấu trúc dữ liệu cơ bản",
//            "Xây dựng ứng dụng web với Java",
//            "Khái niệm OOP và Java"
//        };
//        int[] credits = {3, 3, 4, 3, 3};
//        int[] maxStudents = {40, 45, 35, 40, 50};
//        String[] rooms = {"A101", "B201", "A202", "Lab01", "A103"};
//        String[] schedules = {"T2-4 08:00-10:00", "T3-5 10:00-12:00", "T2-4 14:00-16:00", "T6 13:00-17:00", "T3-5 08:00-10:00"};
//
//        for (int i = 0; i < ids.length; i++) {
//            String courseId = ids[i];
//            Course c = new Course();
//            c.setCourseId(courseId);
//            c.setCourseName(names[i]);
//            c.setDescription(descs[i]);
//            c.setCredits(credits[i]);
//            c.setLecturerId(null); // optional
//            c.setDepartmentId("SE");
//            c.setSemesterId("SEMTEST");
//            c.setMaxStudents(maxStudents[i]);
//            c.setEnrolledStudents(0);
//            c.setRoom(rooms[i]);
//            c.setSchedule(schedules[i]);
//            c.setStatus("open");
//
//            System.out.println("\n-- INSERT (" + courseId + ") --");
//            boolean inserted = repo.addCourse(c);
//            System.out.println("inserted=" + inserted);
//
//            System.out.println("-- DISPLAY (findById) --");
//            System.out.println(repo.findById(courseId));
//
//            System.out.println("-- UPDATE (credits, room) --");
//            c.setCredits(Math.min(6, c.getCredits() + 1));
//            c.setRoom(c.getRoom() + "-UPD");
//            boolean updated = repo.updateCourse(c);
//            System.out.println("updated=" + updated);
//            System.out.println(repo.findById(courseId));
//
//            if (!keepData) {
//                System.out.println("-- DELETE --");
//                boolean deleted = repo.deleteCourse(courseId);
//                System.out.println("deleted=" + deleted);
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
//}
//
//
