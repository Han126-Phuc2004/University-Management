//import entity.Student;
//import repository.StudentRepository;
//import util.DBConnection;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//
//public class StudentCrudTest {
//    public static void main(String[] args) {
//        System.out.println("=== STUDENT CRUD TEST (real DB) ===");
//
//        // Ensure dependency: department exists
//        ensureDepartmentExists("SE", "Khoa Phần mềm");
//
//        StudentRepository repo = new StudentRepository();
//        boolean keepData = true; // giữ dữ liệu sau khi test
//
//        // Dữ liệu chuẩn: tên tiếng Việt, MSSV DE180 + 3 số, email @FPT.EDU.VN, địa chỉ chuẩn
//        String[] ids = {"DE180101", "DE180102", "DE180103", "DE180104", "DE180105"};
//        String[] names = {"Trần Văn An", "Nguyễn Thị Bình", "Lê Văn Cường", "Phạm Thị Dung", "Hoàng Anh Tuấn"};
//        String[] genders = {"Male", "Female", "Male", "Female", "Male"};
//        String[] emails = {"ATV101@FPT.EDU.VN", "NTB102@FPT.EDU.VN", "LVC103@FPT.EDU.VN", "PTD104@FPT.EDU.VN", "HAT105@FPT.EDU.VN"};
//        String[] phones = {"0901111101", "0901111102", "0901111103", "0901111104", "0901111105"};
//        String[] addresses = {"Số 1, Tầng 3, Tòa A", "Số 2, Tầng 4, Tòa B", "Số 3, Tầng 5, Tòa C", "Số 4, Tầng 6, Tòa D", "Số 5, Tầng 7, Tòa E"};
//        double[] gpas = {3.2, 3.4, 3.1, 3.6, 3.3};
//
//        for (int i = 0; i < 5; i++) {
//            String studentId = ids[i];
//            Student st = new Student(
//                studentId,
//                names[i],
//                LocalDate.now().minusYears(19 + i).withMonth(1).withDayOfMonth(15),
//                genders[i],
//                phones[i],
//                emails[i],
//                addresses[i],
//                "SE",
//                LocalDate.now(),
//                "active",
//                gpas[i]
//            );
//
//            System.out.println("\n-- INSERT (" + studentId + ") --");
//            boolean inserted = repo.addStudent(st);
//            System.out.println("inserted=" + inserted);
//
//            System.out.println("-- DISPLAY (findById) --");
//            System.out.println(repo.findById(studentId));
//
//            System.out.println("-- UPDATE (phone, gpa) --");
//            st.setPhone("0912999" + String.format("%03d", i + 1));
//            st.setGpa(Math.min(4.0, st.getGpa() + 0.2));
//            boolean updated = repo.updateStudent(st);
//            System.out.println("updated=" + updated);
//            System.out.println(repo.findById(studentId));
//
//            if (!keepData) {
//                System.out.println("-- DELETE --");
//                boolean deleted = repo.deleteStudent(studentId);
//                System.out.println("deleted=" + deleted);
//            }
//        }
//
//        // Hiển thị một phần danh sách để xác nhận dữ liệu còn trong DB
//        System.out.println("\n-- LIST ALL (first 20) --");
//        repo.findAll().stream().limit(20).forEach(System.out::println);
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
//}
//
//
