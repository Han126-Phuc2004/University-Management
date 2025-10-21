package repository;

import util.FileUtils;
import entity.Course;
import repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

public class ReadCourseFile {

    /**
     * CSV rows -> List<Course> (dùng các cột đúng thứ tự file bạn đưa)
     */
    public static List<Course> parseCourses(List<String[]> csvData) {
        List<Course> courses = new ArrayList<>();

        // Nếu dòng đầu là header thì bỏ qua
        int start = 0;
        if (!csvData.isEmpty() && csvData.get(0).length > 0
                && "course_id".equalsIgnoreCase(csvData.get(0)[0])) {
            start = 1;
        }

        for (int i = start; i < csvData.size(); i++) {
            String[] cols = csvData.get(i);
            // course_id,course_name,description,credits,lecturer_id,department_id,semester_id,max_students,enrolled_students,room,schedule,status
            if (cols.length < 9) {
                continue; // tối thiểu tới enrolled_students
            }
            String courseId = safe(cols, 0);               // VD: MAD101
            String courseName = safe(cols, 1);               // Tên khoá
            String description = safe(cols, 2);               // Mô tả
            int credits = parseInt(safe(cols, 3), 0);  // Số tín chỉ
            String lecturerId = safe(cols, 4); // Giữ nguyên lecturer_id dạng string
            String departmentId = safe(cols, 5);                
            // semester_id trong CSV là chuỗi kiểu "SEMTEST" -> entity của bạn là String, nên để null
            String semesterId = null;
            int maxStudents = parseInt(safe(cols, 7), 50);
            int enrolledStudents = parseInt(safe(cols, 8), 0);

            Course c = new Course(
                    courseId,
                    courseName,
                    description,
                    credits,
                    lecturerId,
                    departmentId,
                    semesterId,
                    maxStudents,
                    enrolledStudents
            );
            courses.add(c);
        }
        return courses;
    }

    /**
     * Đọc CSV → List<Course> (có kiểm tra tồn tại file)
     */
    public static List<Course> loadCoursesFromCSV(String filePath) {
        List<Course> courses = new ArrayList<>();
        if (!FileUtils.fileExists(filePath)) {
            System.err.println("File CSV không tồn tại: " + filePath);
            return courses;
        }
        List<String[]> rows = FileUtils.readCSV(filePath);
        return parseCourses(rows);
    }

    /**
     * In danh sách Course (theo entity)
     */
    public static void printCourses(List<Course> courses) {
        System.out.println("=== COURSE (ENTITY VIEW) ===");
        System.out.println("Total courses loaded: " + courses.size());
        for (Course c : courses) {
            System.out.printf("%s | %s | credits=%d | deptId=%s | lecturerId=%s | max=%d | enrolled=%d%n",
                    c.getCourseId(),
                    c.getCourseName(),
                    c.getCredits(),
                    c.getDepartmentId(),
                    c.getLecturerId(),
                    c.getMaxStudents(),
                    c.getEnrolledStudents()
            );
        }
    }

    /**
     * 👉 In NGUYÊN DỮ LIỆU CSV: hiển thị hết các cột (kể cả
     * room/schedule/status)
     */
    public static void printCsvFull(String filePath) {
        if (!FileUtils.fileExists(filePath)) {
            System.err.println("File CSV không tồn tại: " + filePath);
            return;
        }
        List<String[]> rows = FileUtils.readCSV(filePath);
        if (rows == null || rows.isEmpty()) {
            System.out.println("CSV trống.");
            return;
        }

        System.out.println("=== RAW CSV VIEW (ALL COLUMNS) ===");
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            // Ghép mọi cột bằng " | " để nhìn rõ
            System.out.println(String.join(" | ", row));
        }
    }

    private static String safe(String[] arr, int idx) {
        return (idx >= 0 && idx < arr.length && arr[idx] != null) ? arr[idx].trim() : "";
    }

    private static int parseInt(String s, int def) {
        try {
            return (s == null || s.isBlank()) ? def : Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }

    public static void main(String[] args) {
        String filePath = System.getProperty("user.dir") + "/data/courses.csv";

        List<Course> courses = loadCoursesFromCSV(filePath);
         new CourseRepository().saveCourseFromCSVToBD(filePath);
        printCourses(courses);
    }
}
