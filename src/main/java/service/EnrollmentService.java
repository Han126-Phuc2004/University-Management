package service;

import entity.Course;
import entity.Enrollment;
import repository.CourseRepository;
import repository.EnrollmentRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * Service class for managing student enrollments.
 */
public class EnrollmentService {
    private EnrollmentRepository enrollmentRepository;

    public EnrollmentService() {
        this.enrollmentRepository = new EnrollmentRepository();
    }

    /**
     * Register a student for a course
     */
    public void registerCourse(String studentId, String courseId) {
        if (studentId == null || courseId == null || studentId.trim().isEmpty() || courseId.trim().isEmpty()) {
            System.out.println("Student ID and Course ID cannot be empty!");
            return;
        }

        // Check if the enrollment already exists
        if (enrollmentRepository.findByStudentAndCourse(studentId, courseId) != null) {
            System.out.println("Student is already enrolled in this course!");
            return;
        }

        Enrollment enrollment = new Enrollment(studentId, courseId);
        if (enrollmentRepository.addEnrollment(enrollment)) {
            System.out.println("Course enrollment successful!");
        } else {
            System.out.println("Failed to enroll in course. Check course capacity or database error.");
        }
    }

    /**
     * Cancel a student's enrollment in a course
     */
    public void cancelEnrollment(String studentId, String courseId) {
        if (studentId == null || courseId == null || studentId.trim().isEmpty() || courseId.trim().isEmpty()) {
            System.out.println("Student ID and Course ID cannot be empty!");
            return;
        }

        if (enrollmentRepository.deleteEnrollment(studentId, courseId)) {
            System.out.println("Course enrollment canceled successfully!");
        } else {
            System.out.println("Failed to cancel enrollment. Check if the enrollment exists.");
        }
    }

    /**
     * View all enrollments for a student
     */
    public void viewEnrollments(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("Student ID cannot be empty!");
            return;
        }

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        if (enrollments == null || enrollments.isEmpty()) {
            System.out.println("No enrollments found for student ID: " + studentId);
        } else {
            System.out.println("\n=== ENROLLMENTS FOR STUDENT ID: " + studentId + " ===");
            System.out.printf("%-15s %-15s %-15s %-10s %-8s %-8s %-8s %-5s%n",
                    "Course ID", "Enrollment Date", "Status", "Midterm", "Final", "Total", "Grade", "");
            System.out.println("-".repeat(90));
            for (Enrollment enrollment : enrollments) {
                System.out.printf("%-15s %-15s %-15s %-10s %-8s %-8s %-8s %-5s%n",
                        enrollment.getCourseId(),
                        enrollment.getEnrollmentDate(),
                        enrollment.getStatus(),
                        enrollment.getMidtermScore() != null ? enrollment.getMidtermScore() : "N/A",
                        enrollment.getFinalScore() != null ? enrollment.getFinalScore() : "N/A",
                        enrollment.getTotalScore() != null ? enrollment.getTotalScore() : "N/A",
                        enrollment.getGrade() != null ? enrollment.getGrade() : "N/A",
                        "");
            }
        }
    }

    /**
     * View all enrollments for a course
     */
    public void viewCourseEnrollments(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            System.out.println("Course ID cannot be empty!");
            return;
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        if (enrollments == null || enrollments.isEmpty()) {
            System.out.println("No enrollments found for course ID: " + courseId);
        } else {
            System.out.println("\n=== ENROLLMENTS FOR COURSE ID: " + courseId + " ===");
            System.out.printf("%-15s %-15s %-15s %-10s %-8s %-8s %-8s%n",
                    "Student ID", "Enrollment Date", "Status", "Midterm", "Final", "Total", "Grade");
            System.out.println("-".repeat(90));
            for (Enrollment enrollment : enrollments) {
                System.out.printf("%-15s %-15s %-15s %-10s %-8s %-8s %-8s%n",
                        enrollment.getStudentId(),
                        enrollment.getEnrollmentDate(),
                        enrollment.getStatus(),
                        enrollment.getMidtermScore() != null ? enrollment.getMidtermScore() : "N/A",
                        enrollment.getFinalScore() != null ? enrollment.getFinalScore() : "N/A",
                        enrollment.getTotalScore() != null ? enrollment.getTotalScore() : "N/A",
                        enrollment.getGrade() != null ? enrollment.getGrade() : "N/A");
            }
        }
    }

    /**
     * Update enrollment scores
     */
    public void updateScores(String enrollmentId, BigDecimal midtermScore, BigDecimal finalScore) {
        if (enrollmentId == null || enrollmentId.trim().isEmpty()) {
            System.out.println("Enrollment ID cannot be empty!");
            return;
        }

        if (midtermScore == null || finalScore == null) {
            System.out.println("Both midterm and final scores are required!");
            return;
        }

        if (midtermScore.compareTo(BigDecimal.ZERO) < 0 || midtermScore.compareTo(BigDecimal.TEN) > 0 ||
                finalScore.compareTo(BigDecimal.ZERO) < 0 || finalScore.compareTo(BigDecimal.TEN) > 0) {
            System.out.println("Scores must be between 0 and 10!");
            return;
        }

        if (enrollmentRepository.updateScores(enrollmentId, midtermScore, finalScore)) {
            System.out.println("Scores updated successfully!");
        } else {
            System.out.println("Failed to update scores. Check if enrollment exists.");
        }
    }

    private static String safe(String[] arr, int idx) {
        return (idx >= 0 && idx < arr.length && arr[idx] != null) ? arr[idx].trim() : "";
    }

    public ArrayList<Enrollment> readEnrollment(List<String[]> csvData) {
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        int start = 0;
        if (!csvData.isEmpty() && csvData.get(0).length > 0
                && "enrollment_id".equalsIgnoreCase(csvData.get(0)[0])) {
            start = 1;
        }
        for (int i = start; i < csvData.size(); i++) {
            String[] cols = csvData.get(i);
            if (cols.length < 9) {
                continue;
            }
            String enrollmentId = safe(cols, 0);
            String studentId = safe(cols, 1);
            String courseId = safe(cols, 2);
            String status = safe(cols, 3);
            String grade = safe(cols, 4);

            Enrollment e = new Enrollment(
                    enrollmentId,
                    studentId,
                    courseId,
                    status,
                    grade
            );
            enrollments.add(e);
        }
        return  enrollments;

    }


    /**
     * Update enrollment status
     */
    public void updateStatus(String enrollmentId, String status) {
        if (enrollmentId == null || enrollmentId.trim().isEmpty()) {
            System.out.println("Enrollment ID cannot be empty!");
            return;
        }

        if (status == null || status.trim().isEmpty()) {
            System.out.println("Status cannot be empty!");
            return;
        }

        if (!status.matches("enrolled|completed|dropped|failed")) {
            System.out.println("Invalid status. Must be: enrolled, completed, dropped, or failed");
            return;
        }

        if (enrollmentRepository.updateStatus(enrollmentId, status)) {
            System.out.println("Status updated successfully!");
        } else {
            System.out.println("Failed to update status. Check if enrollment exists.");
        }
    }

    /**
     * View all enrollments
     */
    public void viewAllEnrollments() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        if (enrollments == null || enrollments.isEmpty()) {
            System.out.println("No enrollments found.");
        } else {
            System.out.println("\n=== ALL ENROLLMENTS ===");
            System.out.printf("%-15s %-15s %-15s %-15s %-10s %-8s %-8s %-8s %-5s%n", 
                "Enrollment ID", "Student ID", "Course ID", "Enrollment Date", "Status", "Midterm", "Final", "Total", "Grade");
            System.out.println("-".repeat(120));
            for (Enrollment enrollment : enrollments) {
                System.out.printf("%-15s %-15s %-15s %-15s %-10s %-8s %-8s %-8s %-5s%n", 
                    enrollment.getEnrollmentId(),
                    enrollment.getStudentId(), 
                    enrollment.getCourseId(),
                    enrollment.getEnrollmentDate(),
                    enrollment.getStatus(),
                    enrollment.getMidtermScore() != null ? enrollment.getMidtermScore() : "N/A",
                    enrollment.getFinalScore() != null ? enrollment.getFinalScore() : "N/A",
                    enrollment.getTotalScore() != null ? enrollment.getTotalScore() : "N/A",
                    enrollment.getGrade() != null ? enrollment.getGrade() : "N/A");
            }
        }
    }

    /**
     * Get enrollment by ID
     */
    public Enrollment getEnrollmentById(String enrollmentId) {
        if (enrollmentId == null || enrollmentId.trim().isEmpty()) {
            System.out.println("Enrollment ID cannot be empty!");
            return null;
        }

        List<Enrollment> allEnrollments = enrollmentRepository.findAll();
        if (allEnrollments != null) {
            for (Enrollment enrollment : allEnrollments) {
                if (enrollment.getEnrollmentId().equals(enrollmentId)) {
                    return enrollment;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {

        String filePath = System.getProperty("user.dir") + "/data/Enroll.csv";
        List<String[]> csvData =new ArrayList<>();


    }
}