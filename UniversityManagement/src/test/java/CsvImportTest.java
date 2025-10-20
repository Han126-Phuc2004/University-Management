//import util.FileUtils;
//import util.DBConnection;
//import util.InputValidator;
//import repository.StudentRepository;
//import repository.LecturerRepository;
//import repository.CourseRepository;
//import entity.Student;
//import entity.Lecturer;
//import entity.Course;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public class CsvImportTest {
//    public static void main(String[] args) {
//        System.out.println("=== CSV IMPORT TEST ===");
//
//        // Configurable CSV paths (edit if needed)
//        String studentsCsv = "data/students.csv";
//        String lecturersCsv = "data/lecturers.csv";
//        String coursesCsv = "data/courses.csv";
//
//        // Ensure DB exists
//        DBConnection.createDatabaseIfNotExists();
//
//        importStudents(studentsCsv);
//        importLecturers(lecturersCsv);
//        importCourses(coursesCsv);
//
//        System.out.println("=== DONE ===");
//    }
//
//    private static void importStudents(String csvPath) {
//        if (!FileUtils.fileExists(csvPath)) {
//            System.out.println("[Students] File not found: " + csvPath);
//            return;
//        }
//        List<String[]> rows = FileUtils.readCSV(csvPath);
//        if (rows.isEmpty()) {
//            System.out.println("[Students] Empty file: " + csvPath);
//            return;
//        }
//
//        StudentRepository repo = new StudentRepository();
//        int inserted = 0; int skipped = 0;
//
//        int startIdx = rows.get(0)[0].equalsIgnoreCase("student_id") ? 1 : 0;
//        for (int i = startIdx; i < rows.size(); i++) {
//            String[] r = rows.get(i);
//            // Expected columns: student_id,full_name,date_of_birth,gender,phone,email,address,department_id,enrollment_date,status,gpa
//            if (r.length < 11) { skipped++; continue; }
//            String id = r[0].trim();
//            String name = r[1].trim();
//            LocalDate dob = InputValidator.parseDate(r[2].trim());
//            String gender = r[3].trim();
//            String phone = r[4].trim();
//            String email = r[5].trim();
//            String address = r[6].trim();
//            String depId = r[7].trim();
//            LocalDate enrollDate = InputValidator.parseDate(r[8].trim());
//            String status = r[9].trim();
//            double gpa = 0.0;
//            try { gpa = Double.parseDouble(r[10].trim()); } catch (Exception ignored) {}
//
//            if (dob == null) dob = LocalDate.now().minusYears(18);
//            if (enrollDate == null) enrollDate = LocalDate.now();
//
//            Student st = new Student(id, name, dob, gender, phone, email, address, depId, enrollDate, status, gpa);
//            boolean ok = repo.addStudent(st);
//            if (ok) inserted++; else skipped++;
//        }
//        System.out.println("[Students] inserted=" + inserted + ", skipped=" + skipped);
//    }
//
//    private static void importLecturers(String csvPath) {
//        if (!FileUtils.fileExists(csvPath)) {
//            System.out.println("[Lecturers] File not found: " + csvPath);
//            return;
//        }
//        List<String[]> rows = FileUtils.readCSV(csvPath);
//        if (rows.isEmpty()) {
//            System.out.println("[Lecturers] Empty file: " + csvPath);
//            return;
//        }
//
//        LecturerRepository repo = new LecturerRepository();
//        int inserted = 0; int skipped = 0;
//
//        int startIdx = rows.get(0)[0].equalsIgnoreCase("lecturer_id") ? 1 : 0;
//        for (int i = startIdx; i < rows.size(); i++) {
//            String[] r = rows.get(i);
//            // Expected: lecturer_id,full_name,date_of_birth,gender,phone,email,address,department_id,degree,specialization,hire_date,status
//            if (r.length < 12) { skipped++; continue; }
//            Lecturer lec = new Lecturer();
//            lec.setLecturerId(r[0].trim());
//            lec.setFullName(r[1].trim());
//            LocalDate dob = InputValidator.parseDate(r[2].trim());
//            lec.setDateOfBirth(dob != null ? dob : LocalDate.now().minusYears(30));
//            lec.setGender(r[3].trim());
//            lec.setPhone(r[4].trim());
//            lec.setEmail(r[5].trim());
//            lec.setAddress(r[6].trim());
//            lec.setDepartmentId(r[7].trim());
//            lec.setDegree(r[8].trim());
//            lec.setSpecialization(r[9].trim());
//            LocalDate hire = InputValidator.parseDate(r[10].trim());
//            lec.setHireDate(hire != null ? hire : LocalDate.now());
//            lec.setStatus(r[11].trim());
//
//            boolean ok = repo.addLecturer(lec);
//            if (ok) inserted++; else skipped++;
//        }
//        System.out.println("[Lecturers] inserted=" + inserted + ", skipped=" + skipped);
//    }
//
//    private static void importCourses(String csvPath) {
//        if (!FileUtils.fileExists(csvPath)) {
//            System.out.println("[Courses] File not found: " + csvPath);
//            return;
//        }
//        List<String[]> rows = FileUtils.readCSV(csvPath);
//        if (rows.isEmpty()) {
//            System.out.println("[Courses] Empty file: " + csvPath);
//            return;
//        }
//
//        CourseRepository repo = new CourseRepository();
//        int inserted = 0; int skipped = 0;
//
//        int startIdx = rows.get(0)[0].equalsIgnoreCase("course_id") ? 1 : 0;
//        for (int i = startIdx; i < rows.size(); i++) {
//            String[] r = rows.get(i);
//            // Expected: course_id,course_name,description,credits,lecturer_id,department_id,semester_id,max_students,enrolled_students,room,schedule,status
//            if (r.length < 12) { skipped++; continue; }
//            Course c = new Course();
//            c.setCourseId(r[0].trim());
//            c.setCourseName(r[1].trim());
//            c.setDescription(r[2].trim());
//            try { c.setCredits(Integer.parseInt(r[3].trim())); } catch (Exception e) { c.setCredits(3); }
//            c.setLecturerId(emptyToNull(r[4]));
//            c.setDepartmentId(r[5].trim());
//            c.setSemesterId(r[6].trim());
//            try { c.setMaxStudents(Integer.parseInt(r[7].trim())); } catch (Exception e) { c.setMaxStudents(50); }
//            try { c.setEnrolledStudents(Integer.parseInt(r[8].trim())); } catch (Exception e) { c.setEnrolledStudents(0); }
//            c.setRoom(r[9].trim());
//            c.setSchedule(r[10].trim());
//            c.setStatus(r[11].trim());
//
//            boolean ok = repo.addCourse(c);
//            if (ok) inserted++; else skipped++;
//        }
//        System.out.println("[Courses] inserted=" + inserted + ", skipped=" + skipped);
//    }
//
//    private static String emptyToNull(String s) {
//        if (s == null) return null;
//        String t = s.trim();
//        return t.isEmpty() ? null : t;
//    }
//}
//
//
