package repository;

import entity.Lecturer;
import util.FileUtils;
import repository.LecturerRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReadLectureFile {

    // dd/MM/yyyy theo dữ liệu bạn cung cấp (01/09/2012, ...)
    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static String mapDepartment(String depCode) {
        if (depCode == null) {
            return null;
        }
        switch (depCode.trim().toUpperCase()) {
            case "SE":
                return "SE";
            case "AI":
                return "AI";
            case "IB":
                return "IB";
            case "IT":
                return "IT";
            default:
                return null;
        }
    }

    private static LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s.trim(), DMY);
        } catch (Exception e) {
            return null; // hoặc LocalDate.now()
        }
    }

    /**
     * Chuyển từng dòng CSV -> Lecturer
     */
    public static List<Lecturer> parseLecturers(List<String[]> csvData) {
        List<Lecturer> list = new ArrayList<>();
        boolean skipHeader = true; // Bỏ qua dòng header đầu tiên

        for (String[] cols : csvData) {
            if (skipHeader) {
                skipHeader = false;
                continue;
            }
            if (cols.length < 9) {
                continue;
            }

            String lecturerId = cols[0];
            String fullName = cols[1];
            String dobStr = cols[2];
            String gender = cols[3];
            String phone = cols[4];
            String email = cols[5];
            String depStr = cols[6];
            String degree = cols[7];
            String specialization = cols[8];
            String departmentId = mapDepartment(depStr);
            LocalDate dob = parseDate(dobStr);

            Lecturer lec = new Lecturer(
                    lecturerId,
                    fullName,
                    dob,
                    gender,
                    phone,
                    email,
                    departmentId,
                    degree,
                    specialization
            );
            list.add(lec);
        }
        return list;
    }

    public static List<Lecturer> loadLecturersFromCSV(String filePath) {
        List<Lecturer> result = new ArrayList<>();
        if (!FileUtils.fileExists(filePath)) {
            System.err.println("File CSV không tồn tại: " + filePath);
            return result;
        }
        List<String[]> rows = FileUtils.readCSV(filePath);
        return parseLecturers(rows);
    }
    
    public static void printLecturers(List<Lecturer> lecturers) {
        System.out.println("=== LECTURER DATA ===");
        System.out.println("Total lecturers: " + lecturers.size());
        lecturers.stream().limit(50).forEach(l ->
                System.out.println(
                        l.getLecturerId() + " | " + l.getFullName()
                        + " | dob=" + l.getDateOfBirth()
                        + " | gender=" + l.getGender()
                        + " | phone=" + l.getPhone()
                        + " | email=" + l.getEmail()
                        + " | deptId=" + l.getDepartmentId()
                        + " | degree=" + l.getDegree()
                        + " | specialization=" + l.getSpecialization()
                )
        );
    }

    public static void main(String[] args) {
        String filePath = System.getProperty("user.dir") + "/data/lecturers.csv";
        List<Lecturer> lecturers = loadLecturersFromCSV(filePath);
        new LecturerRepository().saveLecturerFromCSVToBD(filePath);
        printLecturers(lecturers);
    }
}