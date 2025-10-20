//package repository;
//
//import entity.Enrollment;
//import util.DBConnection;
//
//import java.sql.*;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Repository class cho Enrollment entity
// * Xử lý các thao tác CRUD với database
// */
//public class EnrollmentRepository {
//    private Connection connection;
//
//    public EnrollmentRepository() {
//        this.connection = DBConnection.getConnection();
//    }
//
//    /**
//     * Đăng ký môn học có kiểm soát sức chứa trong một transaction.
//     * Trả về true nếu giữ chỗ và insert thành công, false nếu lớp đã đầy hoặc lỗi trùng.
//     */
//    public boolean enrollWithCapacity(String studentId, String courseId) {
//        boolean previousAutoCommit;
//        try {
//            previousAutoCommit = connection.getAutoCommit();
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi đọc autoCommit: " + e.getMessage());
//            return false;
//        }
//
//        try {
//            connection.setAutoCommit(false);
//
//            // Bước 1: giữ chỗ nếu còn sức chứa
//            String holdSeatSql = "UPDATE course SET enrolled_students = enrolled_students + 1 " +
//                                 "WHERE course_id = ? AND enrolled_students < max_students";
//            try (PreparedStatement holdStmt = connection.prepareStatement(holdSeatSql)) {
//                holdStmt.setString(1, courseId);
//                int affected = holdStmt.executeUpdate();
//                if (affected == 0) {
//                    connection.rollback();
//                    return false; // lớp đầy
//                }
//            }
//
//            // Bước 2: insert enrollment
//            String insertSql = "INSERT INTO enrollment (student_id, course_id, enrollment_date, status) VALUES (?, ?, ?, 'enrolled')";
//            try (PreparedStatement ins = connection.prepareStatement(insertSql)) {
//                ins.setString(1, studentId);
//                ins.setString(2, courseId);
//                ins.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
//                ins.executeUpdate();
//            } catch (SQLException insertEx) {
//                // nếu lỗi (ví dụ đã đăng ký), trả lại chỗ rồi rollback
//                try (PreparedStatement restore = connection.prepareStatement(
//                        "UPDATE course SET enrolled_students = enrolled_students - 1 WHERE course_id = ?")) {
//                    restore.setString(1, courseId);
//                    restore.executeUpdate();
//                } catch (SQLException ignored) {}
//                connection.rollback();
//                return false;
//            }
//
//            connection.commit();
//            return true;
//        } catch (SQLException e) {
//            try { connection.rollback(); } catch (SQLException ignored) {}
//            System.err.println("Lỗi enrollWithCapacity: " + e.getMessage());
//            return false;
//        } finally {
//            try { connection.setAutoCommit(previousAutoCommit); } catch (SQLException ignored) {}
//        }
//    }
//
//    /**
//     * Đăng ký môn học cho sinh viên
//     */
//    public boolean enrollStudent(String studentId, String courseId) {
//        String sql = "INSERT INTO enrollment (student_id, course_id, enrollment_date, status) VALUES (?, ?, ?, ?)";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, studentId);
//            stmt.setString(2, courseId);
//            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
//            stmt.setString(4, "enrolled");
//
//            return stmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi đăng ký môn học: " + e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Hủy đăng ký môn học
//     */
//    public boolean dropEnrollment(String studentId, String courseId) {
//        String sql = "UPDATE enrollment SET status='dropped', updated_at=CURRENT_TIMESTAMP WHERE student_id=? AND course_id=?";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, studentId);
//            stmt.setString(2, courseId);
//            return stmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi hủy đăng ký môn học: " + e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Cập nhật điểm số
//     */
//    public boolean updateScores(long enrollmentId, Double midtermScore, Double finalScore, Double totalScore, String grade) {
//        String sql = "UPDATE enrollment SET midterm_score=?, final_score=?, total_score=?, grade=?, " +
//                     "status='completed', updated_at=CURRENT_TIMESTAMP WHERE enrollment_id=?";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setDouble(1, midtermScore);
//            stmt.setDouble(2, finalScore);
//            stmt.setDouble(3, totalScore);
//            stmt.setString(4, grade);
//            stmt.setLong(5, enrollmentId);
//
//            return stmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi cập nhật điểm số: " + e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Tìm đăng ký theo ID
//     */
//    public Enrollment findById(long enrollmentId) {
//        String sql = "SELECT * FROM enrollment WHERE enrollment_id=?";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setLong(1, enrollmentId);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                return mapResultSetToEnrollment(rs);
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi tìm đăng ký: " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * Tìm đăng ký theo sinh viên và khóa học
//     */
//    public Enrollment findByStudentAndCourse(String studentId, String courseId) {
//        String sql = "SELECT * FROM enrollment WHERE student_id=? AND course_id=?";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, studentId);
//            stmt.setString(2, courseId);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                return mapResultSetToEnrollment(rs);
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi tìm đăng ký theo sinh viên và khóa học: " + e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * Lấy danh sách đăng ký của sinh viên
//     */
//    public List<Enrollment> findByStudent(String studentId) {
//        List<Enrollment> enrollments = new ArrayList<>();
//        String sql = "SELECT * FROM enrollment WHERE student_id=? ORDER BY enrollment_date DESC";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, studentId);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                enrollments.add(mapResultSetToEnrollment(rs));
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi lấy danh sách đăng ký của sinh viên: " + e.getMessage());
//        }
//        return enrollments;
//    }
//
//    /**
//     * Lấy danh sách sinh viên trong khóa học
//     */
//    public List<Enrollment> findByCourse(String courseId) {
//        List<Enrollment> enrollments = new ArrayList<>();
//        String sql = "SELECT * FROM enrollment WHERE course_id=? AND status='enrolled' ORDER BY enrollment_date";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, courseId);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                enrollments.add(mapResultSetToEnrollment(rs));
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi lấy danh sách sinh viên trong khóa học: " + e.getMessage());
//        }
//        return enrollments;
//    }
//
//    /**
//     * Lấy danh sách tất cả đăng ký
//     */
//    public List<Enrollment> findAll() {
//        List<Enrollment> enrollments = new ArrayList<>();
//        String sql = "SELECT * FROM enrollment ORDER BY enrollment_date DESC";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                enrollments.add(mapResultSetToEnrollment(rs));
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi lấy danh sách đăng ký: " + e.getMessage());
//        }
//        return enrollments;
//    }
//
//    /**
//     * Kiểm tra sinh viên đã đăng ký khóa học chưa
//     */
//    public boolean isEnrolled(String studentId, String courseId) {
//        String sql = "SELECT COUNT(*) FROM enrollment WHERE student_id=? AND course_id=? AND status='enrolled'";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, studentId);
//            stmt.setString(2, courseId);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                return rs.getInt(1) > 0;
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi kiểm tra đăng ký: " + e.getMessage());
//        }
//        return false;
//    }
//
//    /**
//     * Đếm số lượng sinh viên đã đăng ký khóa học
//     */
//    public int countEnrolledStudents(String courseId) {
//        String sql = "SELECT COUNT(*) FROM enrollment WHERE course_id=? AND status='enrolled'";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, courseId);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                return rs.getInt(1);
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi đếm số lượng sinh viên đăng ký: " + e.getMessage());
//        }
//        return 0;
//    }
//
//    /**
//     * Chuyển đổi ResultSet thành Enrollment object
//     */
//    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
//        Enrollment enrollment = new Enrollment();
//        enrollment.setEnrollmentId(rs.getLong("enrollment_id"));
//        enrollment.setStudentId(rs.getString("student_id"));
//        enrollment.setCourseId(rs.getString("course_id"));
//
//        Timestamp enrollmentDate = rs.getTimestamp("enrollment_date");
//        if (enrollmentDate != null) {
//            enrollment.setEnrollmentDate(enrollmentDate.toLocalDateTime());
//        }
//
//        enrollment.setStatus(rs.getString("status"));
//
//        Double midtermScore = rs.getDouble("midterm_score");
//        if (!rs.wasNull()) {
//            enrollment.setMidtermScore(midtermScore);
//        }
//
//        Double finalScore = rs.getDouble("final_score");
//        if (!rs.wasNull()) {
//            enrollment.setFinalScore(finalScore);
//        }
//
//        Double totalScore = rs.getDouble("total_score");
//        if (!rs.wasNull()) {
//            enrollment.setTotalScore(totalScore);
//        }
//
//        enrollment.setGrade(rs.getString("grade"));
//
//        Timestamp createdAt = rs.getTimestamp("created_at");
//        if (createdAt != null) {
//            enrollment.setCreatedAt(createdAt.toLocalDateTime());
//        }
//
//        Timestamp updatedAt = rs.getTimestamp("updated_at");
//        if (updatedAt != null) {
//            enrollment.setUpdatedAt(updatedAt.toLocalDateTime());
//        }
//
//        return enrollment;
//    }
//}
