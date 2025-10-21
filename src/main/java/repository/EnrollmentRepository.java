package repository;

import entity.Enrollment;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing enrollment data in the database.
 */
public class EnrollmentRepository {
    private Connection connection;

    public EnrollmentRepository() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Check if a student ID exists in the student table
     */
    private boolean isValidStudentId(String studentId) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT COUNT(*) FROM student WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId.trim().toUpperCase());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("SQL Error in isValidStudentId: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if a course ID exists in the course table
     */
    private boolean isValidCourseId(String courseId) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT COUNT(*) FROM course WHERE course_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId.trim().toUpperCase());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("SQL Error in isValidCourseId: " + e.getMessage());
            return false;
        }
    }

    /**
     * Add a new enrollment to the database
     */
    public boolean addEnrollment(Enrollment enrollment) {
        Connection conn = DBConnection.getConnection();
        if (!isValidStudentId(enrollment.getStudentId())) {
            System.out.println("Invalid Student ID: " + enrollment.getStudentId() + " does not exist.");
            return false;
        }
        if (!isValidCourseId(enrollment.getCourseId())) {
            System.out.println("Invalid Course ID: " + enrollment.getCourseId() + " does not exist.");
            return false;
        }

        // Check if enrollment already exists
        if (findByStudentAndCourse(enrollment.getStudentId(), enrollment.getCourseId()) != null) {
            System.out.println("Student is already enrolled in this course.");
            return false;
        }

        String insertSql = "INSERT INTO enrollment (enrollment_id, student_id, course_id, enrollment_date, status) VALUES (?, ?, ?, ?, ?)";
        String updateSql = "UPDATE course SET enrolled_students = enrolled_students + 1 WHERE course_id = ?";
        try (PreparedStatement pstmtInsert = conn.prepareStatement(insertSql);
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {

            // Generate enrollment ID
            String enrollmentId = "ENR" + System.currentTimeMillis();
            
            // Thực hiện chèn bản ghi
            pstmtInsert.setString(1, enrollmentId);
            pstmtInsert.setString(2, enrollment.getStudentId());
            pstmtInsert.setString(3, enrollment.getCourseId());
            pstmtInsert.setTimestamp(4, enrollment.getEnrollmentDate());
            pstmtInsert.setString(5, enrollment.getStatus());
            
            int rowsAffected = pstmtInsert.executeUpdate();
            if (rowsAffected > 0) {
                // Cập nhật cột enrolled_students
                pstmtUpdate.setString(1, enrollment.getCourseId());
                pstmtUpdate.executeUpdate();
                enrollment.setEnrollmentId(enrollmentId);
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("SQL Error in addEnrollment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete an enrollment from the database
     */
    public boolean deleteEnrollment(String studentId, String courseId) {
        Connection conn = DBConnection.getConnection();
        String deleteSql = "DELETE FROM enrollment WHERE student_id = ? AND course_id = ?";
        String updateSql = "UPDATE course SET enrolled_students = enrolled_students - 1 WHERE course_id = ? AND enrolled_students > 0";
        try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql);
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {

            // Thực hiện xóa bản ghi
            pstmtDelete.setString(1, studentId);
            pstmtDelete.setString(2, courseId);
            int rowsAffected = pstmtDelete.executeUpdate();
            if (rowsAffected > 0) {
                // Cập nhật cột enrolled_students
                pstmtUpdate.setString(1, courseId);
                pstmtUpdate.executeUpdate();
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("SQL Error in deleteEnrollment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find an enrollment by student ID and course ID
     */
    public Enrollment findByStudentAndCourse(String studentId, String courseId) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEnrollment(rs);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("SQL Error in findByStudentAndCourse: " + e.getMessage());
            return null;
        }
    }

    /**
     * Find all enrollments for a student
     */
    public List<Enrollment> findByStudentId(String studentId) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM enrollment WHERE student_id = ?";
        List<Enrollment> enrollments = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
            return enrollments.isEmpty() ? null : enrollments;
        } catch (SQLException e) {
            System.out.println("SQL Error in findByStudentId: " + e.getMessage());
            return null;
        }
    }

    /**
     * Find all enrollments for a course
     */
    public List<Enrollment> findByCourseId(String courseId) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM enrollment WHERE course_id = ?";
        List<Enrollment> enrollments = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
            return enrollments.isEmpty() ? null : enrollments;
        } catch (SQLException e) {
            System.out.println("SQL Error in findByCourseId: " + e.getMessage());
            return null;
        }
    }

    /**
     * Update enrollment scores
     */
    public boolean updateScores(String enrollmentId, BigDecimal midtermScore, BigDecimal finalScore) {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE enrollment SET midterm_score = ?, final_score = ?, total_score = ? WHERE enrollment_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, midtermScore);
            pstmt.setBigDecimal(2, finalScore);
            
            // Calculate total score (40% midterm + 60% final)
            BigDecimal totalScore = midtermScore.multiply(new BigDecimal("0.4"))
                                             .add(finalScore.multiply(new BigDecimal("0.6")));
            pstmt.setBigDecimal(3, totalScore);
            pstmt.setString(4, enrollmentId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error in updateScores: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update enrollment status
     */
    public boolean updateStatus(String enrollmentId, String status) {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE enrollment SET status = ? WHERE enrollment_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, enrollmentId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error in updateStatus: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all enrollments
     */
    public List<Enrollment> findAll() {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM enrollment ORDER BY enrollment_date DESC";
        List<Enrollment> enrollments = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
            return enrollments.isEmpty() ? null : enrollments;
        } catch (SQLException e) {
            System.out.println("SQL Error in findAll: " + e.getMessage());
            return null;
        }
    }

    /**
     * Helper method to map ResultSet to Enrollment object
     */
    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        String enrollmentId = rs.getString("enrollment_id");
        String studentId = rs.getString("student_id");
        String courseId = rs.getString("course_id");
        Timestamp enrollmentDate = rs.getTimestamp("enrollment_date");
        String status = rs.getString("status");
        BigDecimal midtermScore = rs.getBigDecimal("midterm_score");
        BigDecimal finalScore = rs.getBigDecimal("final_score");
        BigDecimal totalScore = rs.getBigDecimal("total_score");
        String grade = rs.getString("grade");
        
        return new Enrollment(enrollmentId, studentId, courseId, enrollmentDate, 
                           status, midtermScore, finalScore, totalScore, grade);
    }
}