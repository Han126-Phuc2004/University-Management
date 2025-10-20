package repository;

import entity.Lecturer;
import util.DBConnection;
import repository.ReadLectureFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository cho bảng lecturer
 */
public class LecturerRepository {
    private final Connection connection;

    public LecturerRepository() {
        this.connection = DBConnection.getConnection();
    }

    /**
     * Thêm giảng viên (kiểm tra trùng ID/email, tồn tại khoa)
     */
    public boolean addLecturer(Lecturer lecturer) {
        if (lecturer == null) return false;
        if (existsById(lecturer.getLecturerId())) return false;
        if (existsByEmail(lecturer.getEmail())) return false;
        if (!departmentExists(lecturer.getDepartmentId())) return false;

        final String sql = "INSERT INTO lecturer (lecturer_id, full_name, date_of_birth, gender, phone, email, department_id, degree, specialization) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lecturer.getLecturerId());
            stmt.setString(2, lecturer.getFullName());
            stmt.setDate(3, Date.valueOf(lecturer.getDateOfBirth()));
            stmt.setString(4, lecturer.getGender());
            stmt.setString(5, lecturer.getPhone());
            stmt.setString(6, lecturer.getEmail());
            stmt.setString(7, lecturer.getDepartmentId());
            stmt.setString(8, lecturer.getDegree());
            stmt.setString(9, lecturer.getSpecialization());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm giảng viên: " + e.getMessage());
            return false;
        }
    }

    public boolean existsById(String lecturerId) {
        if (lecturerId == null) return false;
        final String sql = "SELECT 1 FROM lecturer WHERE lecturer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lecturerId);
            try (ResultSet rs = stmt.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { return false; }
    }

    public boolean existsByEmail(String email) {
        if (email == null) return false;
        final String sql = "SELECT 1 FROM lecturer WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { return false; }
    }

    public boolean departmentExists(String departmentId) {
        if (departmentId == null) return false;
        final String sql = "SELECT 1 FROM department WHERE department_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departmentId);
            try (ResultSet rs = stmt.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { return false; }
    }

    public Lecturer findById(String lecturerId) {
        final String sql = "SELECT lecturer_id, full_name, date_of_birth, gender, phone, email, department_id, degree, specialization FROM lecturer WHERE lecturer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lecturerId);
            try (ResultSet rs = stmt.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { System.err.println("Lỗi khi tìm giảng viên: " + e.getMessage()); }
        return null;
    }

    public boolean updateLecturer(Lecturer lecturer) {
        if (lecturer == null || lecturer.getLecturerId() == null) return false;
        if (!departmentExists(lecturer.getDepartmentId())) return false;

        final String sql = "UPDATE lecturer SET full_name = ?, date_of_birth = ?, gender = ?, phone = ?, email = ?, department_id = ?, degree = ?, specialization = ? WHERE lecturer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lecturer.getFullName());
            stmt.setDate(2, Date.valueOf(lecturer.getDateOfBirth()));
            stmt.setString(3, lecturer.getGender());
            stmt.setString(4, lecturer.getPhone());
            stmt.setString(5, lecturer.getEmail());
            stmt.setString(6, lecturer.getDepartmentId());
            stmt.setString(7, lecturer.getDegree());
            stmt.setString(8, lecturer.getSpecialization());
            stmt.setString(9, lecturer.getLecturerId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật giảng viên: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteLecturer(String lecturerId) {
        if (lecturerId == null) return false;
        
        final String sql = "DELETE FROM lecturer WHERE lecturer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lecturerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa giảng viên: " + e.getMessage());
            return false;
        }
    }

    public List<Lecturer> findAll() {
        final String sql = "SELECT lecturer_id, full_name, date_of_birth, gender, phone, email, department_id, degree, specialization FROM lecturer ORDER BY lecturer_id";
        List<Lecturer> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { list.add(map(rs)); }
        } catch (SQLException e) { System.err.println("Lỗi khi lấy danh sách giảng viên: " + e.getMessage()); }
        return list;
    }

    private Lecturer map(ResultSet rs) throws SQLException {
        Lecturer l = new Lecturer();
        l.setLecturerId(rs.getString("lecturer_id"));
        l.setFullName(rs.getString("full_name"));
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) l.setDateOfBirth(dob.toLocalDate());
        l.setGender(rs.getString("gender"));
        l.setPhone(rs.getString("phone"));
        l.setEmail(rs.getString("email"));
        l.setDepartmentId(rs.getString("department_id"));
        l.setDegree(rs.getString("degree"));
        l.setSpecialization(rs.getString("specialization"));
        return l;
    }


    public void saveLecturerFromCSVToBD(String filePath) {
        List<Lecturer> lecturers = ReadLectureFile.loadLecturersFromCSV(filePath);
        int successCount = 0;
        int skippedCount = 0;

        for (Lecturer lecturer : lecturers) {
            // Nếu trùng ID → bỏ qua
            if (existsById(lecturer.getLecturerId())) {
                System.out.println("Bỏ qua: Lecturer ID " + lecturer.getLecturerId() + " đã tồn tại trong DB.");
                skippedCount++;
                continue;
            }

            // Thêm mới nếu chưa tồn tại
            if (addLecturer(lecturer)) {
                successCount++;
            }
        }

        System.out.println("Import hoàn tất!");
        System.out.println(" - Thêm mới: " + successCount + " giảng viên");
        System.out.println(" - Bỏ qua (đã tồn tại): " + skippedCount + " giảng viên");
        System.out.println(" - Tổng số dòng trong CSV: " + lecturers.size());
    }
}
