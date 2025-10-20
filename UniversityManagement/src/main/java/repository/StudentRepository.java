package repository;

import entity.Lecturer;
import entity.Student;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    private Connection connection;

    public StudentRepository() {
        this.connection = DBConnection.getConnection();
    }

    public List<String> getDepartment() {
        List<String> departments = new ArrayList<>();
        String sql = "SELECT department_name FROM department";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                departments.add(rs.getString("department_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public String getdepartmentidbyname(String name) {
        String sql = "SELECT department_id FROM department WHERE department_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("department_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // or throw exception
    }

    public String getDepartmentNameById(String id) {
        if (id == null) {
            return null;
        }
        String sql = "SELECT department_name FROM department WHERE department_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("department_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addStudent(Student student) {
    String sql = "INSERT INTO student (student_id, full_name, date_of_birth, gender, phone, email, department_id, enrollment_date, gpa) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, student.getStudentId());                     // student_id
        pstmt.setString(2, student.getFullName());                      // full_name
        pstmt.setDate(3, Date.valueOf(student.getDateOfBirth()));       // date_of_birth
        pstmt.setString(4, student.getGender());                        // gender
        pstmt.setString(5, student.getPhone());                         // phone
        pstmt.setString(6, student.getEmail());                         // email
        pstmt.setString(7, student.getDepartmentId());                  // department_id
        pstmt.setDate(8, Date.valueOf(student.getEnrollmentDate()));    // enrollment_date
        pstmt.setDouble(9, student.getGpa());                          // gpa
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    public Student findById(int id) {
        String sql = "SELECT * FROM student WHERE student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToStudent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE student SET full_name = ?, date_of_birth = ?, gender = ?, phone = ?, email = ?, "
                + "department_id = ?, gpa = ? WHERE student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, student.getFullName());
            pstmt.setDate(2, Date.valueOf(student.getDateOfBirth()));
            pstmt.setString(3, student.getGender());
            pstmt.setString(4, student.getPhone());
            pstmt.setString(5, student.getEmail());
            if (student.getDepartmentId() != null) {
                pstmt.setString(6, student.getDepartmentId());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            pstmt.setDouble(7, student.getGpa());
            pstmt.setString(8, student.getStudentId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM student WHERE student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(mapToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public List<Student> findByName(String name) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE full_name LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapToStudent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    private Student mapToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getString("student_id"));
        student.setFullName(rs.getString("full_name"));
        student.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        student.setGender(rs.getString("gender"));
        student.setPhone(rs.getString("phone"));
        student.setEmail(rs.getString("email"));
        student.setDepartmentId("department_id");

        student.setEnrollmentDate(rs.getDate("enrollment_date").toLocalDate());
        student.setGpa(rs.getDouble("gpa"));
        return student;
    }

    public void saveStudentFromCSVToBD(String filePath) {
        List<Student> students = ReadStudentFile.loadStudents(filePath);
        int successCount = 0;
        for (Student student : students) {
            if (addStudent(student)) {
                successCount++;

            }
        }
        System.out.println("Đã lưu thành công " + successCount + "/" + students.size() + " student từ file CSV vào database.");

    }
}
