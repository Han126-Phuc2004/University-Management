package entity;

import java.time.LocalDate;

public class Student {

    private String studentId;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String email;
    private String departmentId;
    private LocalDate enrollmentDate;
    private double gpa;

    public Student() {
        // test pussh 
    }

    public Student(String studentId, String fullName, LocalDate dob,
            String gender, String phone, String email, String departmentId,
            LocalDate enrollmentDate, double gpa) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.dateOfBirth = dob;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.departmentId = departmentId;
        this.enrollmentDate = enrollmentDate;
        this.gpa = gpa;
    }

    public Student(String fullName, LocalDate dateOfBirth, String gender,
            String phone, String email, String departmentId,
            LocalDate enrollmentDate, double gpa) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.departmentId = departmentId;
        this.enrollmentDate = enrollmentDate;
        this.gpa = gpa;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public double getGpa() {
        return gpa;
    }

    @Override
    public String toString() {
        return "Student{"
                + "studentId=" + studentId
                + ", fullName='" + fullName + '\''
                + ", dateOfBirth=" + dateOfBirth
                + ", gender='" + gender + '\''
                + ", phone='" + phone + '\''
                + ", email='" + email + '\''
                + ", departmentId=" + departmentId
                + ", enrollmentDate=" + enrollmentDate
                + ", gpa=" + gpa
                + '}';
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

}
