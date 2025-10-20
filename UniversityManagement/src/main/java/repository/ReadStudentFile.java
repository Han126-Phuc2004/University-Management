package repository;

import entity.Student;
import util.FileUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReadStudentFile {

    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static int mapDepartment(String depCode) {
        if (depCode == null) {
            return 0;
        }
        switch (depCode.trim().toUpperCase()) {
            case "SE":
                return 1;
            case "AI":
                return 2;
            case "BA":
                return 3;
            case "IT":
                return 4;
            default:
                return 0;
        }
    }

    private static LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s.trim(), DMY);
        } catch (Exception e) {
            return null;
        }
    }

    private static double parseDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Chuyển từng dòng CSV -> Student
     */
    public static List<Student> parseStudents(List<String[]> csvData) {
        List<Student> list = new ArrayList<>();

        for (String[] cols : csvData) {
            // Header (11 cột):
            // student_id,full_name,date_of_birth,gender,phone,email,address,
            // department_id,enrollment_date,status,gpa
            if (cols.length < 11) {
                continue;
            }

            String stuCode = cols[0]; // "DE180106"
            String fullName = cols[1];
            String dobStr = cols[2];
            String gender = cols[3];
            String phone = cols[4];
            String email = cols[5];
            String depStr = cols[7];
            String enrollStr = cols[8];
            String gpaStr = cols[10];

            int studentId = FileUtils.extractDigitsToInt(stuCode); // "DE180106" -> 180106
            int departmentId = mapDepartment(depStr);
            LocalDate dob = parseDate(dobStr);
            LocalDate enroll = parseDate(enrollStr);
            double gpa = parseDouble(gpaStr);

            Student st = new Student(
                    stuCode,
                    fullName,
                    dob,
                    gender,
                    phone,
                    email,
                    departmentId,
                    enroll,
                    gpa
            );
            list.add(st);
        }
        return list;
    }

    public static List<Student> loadStudents(String filePath) {
        List<Student> result = new ArrayList<>();
        if (!FileUtils.fileExists(filePath)) {
            System.err.println("File CSV không tồn tại: " + filePath);
            return result; // trả về list rỗng, không null
        }
        List<String[]> rows = FileUtils.readCSV(filePath);
        return parseStudents(rows);
    }

    public static void printStudents(List<Student> students) {
        System.out.println("=== STUDENT DATA ===");
        System.out.println("Total students: " + students.size());
        students.stream().limit(50).forEach(s
                -> System.out.println(
                        s.getStudentId() + " | " + s.getFullName()
                        + " | deptId=" + s.getDepartmentId()
                        + " | gpa=" + s.getGpa()
                        + " | enrollment=" + s.getEnrollmentDate()
                )
        );
    }

    public static void main(String[] args) {
        String filePath = System.getProperty("user.dir") + "/src/main/java/data/students.csv";
        List<Student> students = loadStudents(filePath);
        printStudents(students);
    }
}
