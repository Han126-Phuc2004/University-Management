package entity;

import java.time.LocalDate;

public class Enroll {
    private int enrollId;
    private int studentId;
    private int courseId;
    private LocalDate enrollDate;
    private String status; // e.g. "Enrolled", "Completed", "Dropped"
    private Double grade;  // optional

    public Enroll() {}

    public Enroll(int enrollId, int studentId, int courseId, LocalDate enrollDate, String status, Double grade) {
        this.enrollId = enrollId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollDate = enrollDate;
        this.status = status;
        this.grade = grade;
    }

    public int getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(int enrollId) {
        this.enrollId = enrollId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public LocalDate getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(LocalDate enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
