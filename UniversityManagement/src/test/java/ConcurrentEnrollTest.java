//import repository.EnrollmentRepository;
//import repository.StudentRepository;
//import repository.CourseRepository;
//import entity.Student;
//import util.DBConnection;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//public class ConcurrentEnrollTest {
//    public static void main(String[] args) throws Exception {
//        System.out.println("=== CONCURRENT ENROLL TEST ===");
//
//        // Course target: WED201 với sức chứa 10, reset enrolled và xóa đăng ký cũ
//        final String courseId = "WED201";
//        ensureCourseCapacity(courseId, 10);
//
//        // Seed 12 sinh viên tạm thời
//        List<String> studentIds = seedTempStudents(12);
//
//        EnrollmentRepository enrollRepo = new EnrollmentRepository();
//
//        // Chuẩn bị concurrent
//        int n = studentIds.size();
//        ExecutorService pool = Executors.newFixedThreadPool(10);
//        CountDownLatch start = new CountDownLatch(1);
//
//        List<Future<Boolean>> results = new ArrayList<>();
//        for (String sid : studentIds) {
//            results.add(pool.submit(() -> {
//                start.await();
//                return enrollRepo.enrollWithCapacity(sid, courseId);
//            }));
//        }
//
//        // Nổ đồng thời
//        start.countDown();
//        pool.shutdown();
//        while (!pool.isTerminated()) {
//            Thread.sleep(50);
//        }
//
//        int success = 0, fail = 0;
//        for (Future<Boolean> f : results) {
//            if (f.get()) success++; else fail++;
//        }
//
//        System.out.println("success=" + success + ", fail=" + fail + " (capacity=10, course=" + courseId + ")");
//        System.out.println("=== DONE ===");
//    }
//
//    private static void ensureCourseCapacity(String courseId, int capacity) {
//        String resetSql = "UPDATE course SET max_students=?, enrolled_students=0 WHERE course_id=?";
//        String deleteEnrollSql = "DELETE FROM enrollment WHERE course_id=?";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement reset = conn.prepareStatement(resetSql);
//             PreparedStatement del = conn.prepareStatement(deleteEnrollSql)) {
//            reset.setInt(1, capacity);
//            reset.setString(2, courseId);
//            reset.executeUpdate();
//            del.setString(1, courseId);
//            del.executeUpdate();
//        } catch (SQLException e) {
//            System.err.println("Seed course capacity error: " + e.getMessage());
//        }
//    }
//
//    private static List<String> seedTempStudents(int n) {
//        StudentRepository repo = new StudentRepository();
//        List<String> ids = new ArrayList<>();
//        for (int i = 1; i <= n; i++) {
//            String id = ("DECT" + String.format("%03d", i));
//            ids.add(id);
//            if (repo.findById(id) != null) continue;
//            Student st = new Student(
//                id,
//                "SV Test " + i,
//                LocalDate.now().minusYears(19),
//                "Male",
//                "0908" + String.format("%06d", i),
//                ("sv.ct" + i + "@fpt.edu.vn").toLowerCase(),
//                "Địa chỉ CT",
//                "SE",
//                LocalDate.now(),
//                "active",
//                3.0
//            );
//            repo.addStudent(st);
//        }
//        return ids;
//    }
//}
//
//
