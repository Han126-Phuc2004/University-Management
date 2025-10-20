//import entity.Lecturer;
//import repository.LecturerRepository;
//import util.DBConnection;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//
//public class LecturerCrudTest {
//    public static void main(String[] args) {
//        System.out.println("=== LECTURER CRUD TEST (real DB) ===");
//
//        // Ensure dependency: department exists
//        ensureDepartmentExists("SE", "Khoa Phần mềm");
//
//        LecturerRepository repo = new LecturerRepository();
//        boolean keepData = true; // giữ dữ liệu sau khi test
//
//        // Dữ liệu chuẩn: 5 giảng viên
//        String[] ids = {"GV180101", "GV180102", "GV180103", "GV180104", "GV180105"};
//        String[] names = {"Nguyễn Văn Giáo", "Trần Thị Học", "Lê Văn Khoa", "Phạm Thị Lý", "Hoàng Văn Minh"};
//        String[] genders = {"Male", "Female", "Male", "Female", "Male"};
//        String[] emails = {
//            ("gv.giao." + System.currentTimeMillis() + "@fpt.edu.vn").toLowerCase(),
//            ("gv.hoc." + (System.currentTimeMillis()+1) + "@fpt.edu.vn").toLowerCase(),
//            ("gv.khoa." + (System.currentTimeMillis()+2) + "@fpt.edu.vn").toLowerCase(),
//            ("gv.ly." + (System.currentTimeMillis()+3) + "@fpt.edu.vn").toLowerCase(),
//            ("gv.minh." + (System.currentTimeMillis()+4) + "@fpt.edu.vn").toLowerCase()
//        };
//        String[] phones = {"0988000001", "0988000002", "0988000003", "0988000004", "0988000005"};
//        String[] addresses = {"P.101, Tòa A", "P.102, Tòa A", "P.201, Tòa B", "P.202, Tòa B", "P.301, Tòa C"};
//        String[] degrees = {"PhD", "PhD", "PhD", "Master", "PhD"};
//        String[] specs = {"Computer Science", "Information Systems", "Software Engineering", "Data Science", "Artificial Intelligence"};
//        String[] statuses = {"active", "active", "active", "active", "active"};
//
//        for (int i = 0; i < 5; i++) {
//            String lecturerId = ids[i];
//            Lecturer lec = new Lecturer();
//            lec.setLecturerId(lecturerId);
//            lec.setFullName(names[i]);
//            lec.setDateOfBirth(LocalDate.now().minusYears(40 + i).withMonth(5).withDayOfMonth(20));
//            lec.setGender(genders[i]);
//            lec.setPhone(phones[i]);
//            lec.setEmail(emails[i]);
//            lec.setAddress(addresses[i]);
//            lec.setDepartmentId("SE");
//            lec.setDegree(degrees[i]);
//            lec.setSpecialization(specs[i]);
//            lec.setHireDate(LocalDate.now().minusYears(5 - Math.min(i, 4)));
//            lec.setStatus(statuses[i]);
//
//            System.out.println("\n-- INSERT (" + lecturerId + ") --");
//            boolean inserted = repo.addLecturer(lec);
//            System.out.println("inserted=" + inserted);
//
//            System.out.println("-- DISPLAY (findById) --");
//            System.out.println(repo.findById(lecturerId));
//
//            System.out.println("-- UPDATE (phone, status) --");
//            lec.setPhone("0977999" + String.format("%03d", i + 1));
//            lec.setStatus("active");
//            boolean updated = repo.updateLecturer(lec);
//            System.out.println("updated=" + updated);
//            System.out.println(repo.findById(lecturerId));
//
//            if (!keepData) {
//                System.out.println("-- DELETE --");
//                boolean deleted = repo.deleteLecturer(lecturerId);
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
//        Connection conn = DBConnection.getConnection(); // không đóng connection chia sẻ
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
