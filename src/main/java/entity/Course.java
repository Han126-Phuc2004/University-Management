package entity;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class Course  {
    private String courseId;
    private String courseName;
    private String description;
    private int credits;
    private String lecturerId; // Changed from Integer
    private String departmentId; // Changed from Integer
    private String semesterId; // Changed from Integer
    private int maxStudents;
    private int enrolledStudents;
    private List<String> registeredStudents = new CopyOnWriteArrayList<>();

    public Course(String courseId, String courseName, String description, int credits, Integer lecturerId,
                  Integer departmentId, Integer semesterId, int maxStudents, int enrolledStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.lecturerId = lecturerId != null ? String.valueOf(lecturerId) : null;
        this.departmentId = departmentId != null ? String.valueOf(departmentId) : null;
        this.semesterId = semesterId != null ? String.valueOf(semesterId) : null;
        this.maxStudents = maxStudents;
        this.enrolledStudents = enrolledStudents;
        this.registeredStudents = new CopyOnWriteArrayList<>();
    }
    
    public Course(String courseName, String description, int credits, Integer lecturerId,
                  Integer departmentId, Integer semesterId, int maxStudents, int enrolledStudents) {
        this.courseId = "TEMP"; // Tạm thời cho constructor không có courseId
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.lecturerId = lecturerId != null ? String.valueOf(lecturerId) : null;
        this.departmentId = departmentId != null ? String.valueOf(departmentId) : null;
        this.semesterId = semesterId != null ? String.valueOf(semesterId) : null;
        this.maxStudents = maxStudents;
        this.enrolledStudents = enrolledStudents;
        this.registeredStudents = new CopyOnWriteArrayList<>();
    }

    // Constructors nhận trực tiếp String IDs
    public Course(String courseId, String courseName, String description, int credits,
                  String lecturerId, String departmentId, String semesterId,
                  int maxStudents, int enrolledStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.lecturerId = lecturerId;
        this.departmentId = departmentId;
        this.semesterId = semesterId;
        this.maxStudents = maxStudents;
        this.enrolledStudents = enrolledStudents;
        this.registeredStudents = new CopyOnWriteArrayList<>();
    }

    public Course(String courseName, String description, int credits,
                  String lecturerId, String departmentId, String semesterId,
                  int maxStudents, int enrolledStudents) {
        this("TEMP", courseName, description, credits, lecturerId, departmentId, semesterId, maxStudents, enrolledStudents);
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public int getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(int enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    // Phương thức đăng ký với synchronized
    public synchronized String registerStudent(String studentName) {
        if (enrolledStudents < maxStudents) {
            registeredStudents.add(studentName);
            enrolledStudents++;
            System.out.printf("  Đăng ký thành công: Sinh viên '%s' đã đăng ký môn '%s'.%n", studentName, courseName);
            return "SUCCESS";
        } else {
            System.err.printf("  Đăng ký thất bại: Môn '%s' đã đủ số lượng sinh viên tối đa.%n", courseName);
            return "FAIL";
        }
    }

    // Getters và setters (giữ nguyên các getter hiện có)
    public List<String> getRegisteredStudents() {
        return registeredStudents;
    }

    public void setRegisteredStudents(List<String> registeredStudents) {
        this.registeredStudents = registeredStudents;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                ", credits=" + credits +
                ", lecturerId='" + lecturerId + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", semesterId='" + semesterId + '\'' +
                ", maxStudents=" + maxStudents +
                ", enrolledStudents=" + enrolledStudents +
                '}';
    }
}