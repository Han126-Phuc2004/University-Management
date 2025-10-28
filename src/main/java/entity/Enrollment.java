package entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entity class representing an enrollment.
 * Maps to the enrollment table in the database.
 */
public class Enrollment {
    private String enrollmentId;
    private String studentId;
    private String courseId;
    private Timestamp enrollmentDate;
    private String status;
    private BigDecimal midtermScore;
    private BigDecimal finalScore;
    private BigDecimal totalScore;
    private String grade;

    // Constructor for creating new enrollment
    public Enrollment(String studentId, String courseId) {
        if (studentId == null || courseId == null) {
            throw new IllegalArgumentException("Student ID and Course ID cannot be null");
        }
        this.studentId = studentId.trim();
        this.courseId = courseId.trim();
        this.enrollmentDate = new Timestamp(System.currentTimeMillis());
        this.status = "enrolled";
    }

    // Full constructor for loading from database
    public Enrollment(String enrollmentId, String studentId, String courseId, 
                     Timestamp enrollmentDate, String status, BigDecimal midtermScore, 
                     BigDecimal finalScore, BigDecimal totalScore, String grade) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
        this.midtermScore = midtermScore;
        this.finalScore = finalScore;
        this.totalScore = totalScore;
        this.grade = grade;
    }

    public Enrollment(String enrollmentId, String studentId, String courseId, String status, String grade) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = status;
        this.grade = grade;
    }

    // Getters
    public String getEnrollmentId() {
        return enrollmentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public Timestamp getEnrollmentDate() {
        return enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getMidtermScore() {
        return midtermScore;
    }

    public BigDecimal getFinalScore() {
        return finalScore;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public String getGrade() {
        return grade;
    }

    // Setters
    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setEnrollmentDate(Timestamp enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public void setStatus(String status) {
        if (status != null && !status.matches("enrolled|completed|dropped|failed")) {
            throw new IllegalArgumentException("Invalid status. Must be: enrolled, completed, dropped, or failed");
        }
        this.status = status;
    }

    public void setMidtermScore(BigDecimal midtermScore) {
        if (midtermScore != null && (midtermScore.compareTo(BigDecimal.ZERO) < 0 || midtermScore.compareTo(BigDecimal.TEN) > 0)) {
            throw new IllegalArgumentException("Midterm score must be between 0 and 10");
        }
        this.midtermScore = midtermScore;
    }

    public void setFinalScore(BigDecimal finalScore) {
        if (finalScore != null && (finalScore.compareTo(BigDecimal.ZERO) < 0 || finalScore.compareTo(BigDecimal.TEN) > 0)) {
            throw new IllegalArgumentException("Final score must be between 0 and 10");
        }
        this.finalScore = finalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        if (totalScore != null && (totalScore.compareTo(BigDecimal.ZERO) < 0 || totalScore.compareTo(BigDecimal.TEN) > 0)) {
            throw new IllegalArgumentException("Total score must be between 0 and 10");
        }
        this.totalScore = totalScore;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    // Helper methods
    public void calculateTotalScore() {
        if (midtermScore != null && finalScore != null) {
            this.totalScore = midtermScore.multiply(new BigDecimal("0.4"))
                                        .add(finalScore.multiply(new BigDecimal("0.6")));
        }
    }

    public void calculateGrade() {
        if (totalScore != null) {
            if (totalScore.compareTo(new BigDecimal("8.5")) >= 0) {
                this.grade = "A";
            } else if (totalScore.compareTo(new BigDecimal("7.0")) >= 0) {
                this.grade = "B";
            } else if (totalScore.compareTo(new BigDecimal("5.5")) >= 0) {
                this.grade = "C";
            } else if (totalScore.compareTo(new BigDecimal("4.0")) >= 0) {
                this.grade = "D";
            } else {
                this.grade = "F";
            }
        }
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId='" + enrollmentId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                ", status='" + status + '\'' +
                ", midtermScore=" + midtermScore +
                ", finalScore=" + finalScore +
                ", totalScore=" + totalScore +
                ", grade='" + grade + '\'' +
                '}';
    }
}