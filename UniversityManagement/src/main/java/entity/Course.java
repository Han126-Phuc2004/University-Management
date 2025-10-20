package entity;
public class Course  {
    private String courseId;
    private String courseName;
    private String description;
    private int credits;
    private Integer lecturerId; 
    private Integer departmentId;
    private Integer semesterId;
    private int maxStudents;
    private int enrolledStudents;

    public Course(String courseId, String courseName, String description, int credits, Integer lecturerId,
                  Integer departmentId, Integer semesterId, int maxStudents, int enrolledStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.lecturerId = lecturerId;
        this.departmentId = departmentId;
        this.semesterId = semesterId;
        this.maxStudents = maxStudents;
        this.enrolledStudents = enrolledStudents;
    }
    
    public Course(String courseName, String description, int credits, Integer lecturerId,
                  Integer departmentId, Integer semesterId, int maxStudents, int enrolledStudents) {
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.lecturerId = lecturerId;
        this.departmentId = departmentId;
        this.semesterId = semesterId;
        this.maxStudents = maxStudents;
        this.enrolledStudents = enrolledStudents;
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

    public Integer getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Integer lecturerId) {
        this.lecturerId = lecturerId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
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

    @Override
    public String toString() {
        return "Course{" +
"courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                ", credits=" + credits +
                ", lecturerId=" + lecturerId +
                ", departmentId=" + departmentId +
                ", semesterId=" + semesterId +
                ", maxStudents=" + maxStudents +
                ", enrolledStudents=" + enrolledStudents +
                '}';
    }
}