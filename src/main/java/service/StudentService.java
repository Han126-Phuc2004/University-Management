package service;

import entity.Student;
import repository.StudentRepository;
import util.InputValidator;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Scanner;

/**
 * Service class for Student
 * Handles business logic related to students
 */
public class StudentService {
    private StudentRepository studentRepository;
    private Scanner scanner;

    public StudentService() {
        this.studentRepository = new StudentRepository();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        StudentService service = new StudentService();
        service.updateStudent();
    }

    /**
     * Get valid department ID
     */
    public String getDepartmentId() {
        while (true) {
            System.out.println("List of departments:");
            List<String> departments = studentRepository.getDepartment();
            for (String dept : departments) {
                System.out.println(dept);
            }
            System.out.print("Enter department name: ");
            String deptName = scanner.nextLine();
            if (departments.contains(deptName)) {
                return studentRepository.getDepartmentIdByName(deptName);
            } else {
                System.out.println("Invalid department name, please try again.");
            }
        }
    }

    /**
     * Add a new student
     */
    public void addStudent() {
        System.out.println("\n=== ADD NEW STUDENT ===");

        String studentId = getValidIdInput("Enter student ID: ", false);
        if (studentId == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        String fullName = getValidInput("Enter full name: ", InputValidator::isValidName, false);
        if (fullName == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        LocalDate dateOfBirth = getValidDateInput("Enter date of birth (dd/MM/yyyy): ", false);
        if (dateOfBirth == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        String gender = getValidInput("Enter gender (Male/Female/Other): ", InputValidator::isValidGender, false);
        if (gender == null) {
            System.out.println("Operation cancelled.");
            return;
        }
        gender = Character.toUpperCase(gender.toLowerCase().charAt(0)) + gender.toLowerCase().substring(1);

        String phone = getValidInput("Enter phone number: ", InputValidator::isValidPhone, false);
        if (phone == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        String email = getValidInput("Enter email: ", InputValidator::isValidEmail, false);
        if (email == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        String departmentId = getDepartmentId();

        LocalDate enrollmentDate = LocalDate.now();

        double gpa = getValidGPA("Enter GPA (0.0-4.0): ");
        if (gpa == -1) {
            System.out.println("Operation cancelled.");
            return;
        }

        Student student = new Student(studentId, fullName, dateOfBirth, gender, phone, email,
                departmentId, enrollmentDate, gpa);

        if (studentRepository.addStudent(student)) {
            System.out.println("Student added successfully!");
        } else {
            System.out.println("Error adding student!");
        }
    }

    /**
     * Update student information
     */
    public void updateStudent() {
        System.out.println("\n=== UPDATE STUDENT INFORMATION ===");

        String studentId = getValidIdInput("Enter student ID to update: ", true);
        if (studentId == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        Student student = studentRepository.findById(studentId);
        if (student == null) {
            System.out.println("No student found with ID " + studentId);
            return;
        }

        System.out.println("Current information:");
        System.out.println(student);

        System.out.println("\nEnter new information (Enter to keep unchanged):");

        updateFullName(student);
        updateDateOfBirth(student);
        updateGender(student);
        updatePhone(student);
        updateEmail(student);
        updateDepartment(student);
        updateGPA(student);

        if (studentRepository.updateStudent(student)) {
            System.out.println("Student information updated successfully!");
        } else {
            System.out.println("Error updating student information!");
        }
    }

    /**
     * Get valid ID input from user
     */
    private String getValidIdInput(String prompt, boolean allowNull) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (allowNull && InputValidator.isEmpty(input)) {
                return null;
            }

            if (InputValidator.isValidId(input)) {
                return input;
            } else {
                System.out.println("ID must be 3-20 characters and contain only letters or numbers!");
            }
        }
    }

    /**
     * Get valid input with custom validator
     */
    private String getValidInput(String prompt, java.util.function.Function<String, Boolean> validator, boolean allowNull) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (allowNull && InputValidator.isEmpty(input)) {
                return null;
            }

            if (InputValidator.isEmpty(input)) {
                System.out.println("Input cannot be empty!");
                continue;
            }

            if (validator.apply(input)) {
                return input;
            } else {
                System.out.println("Invalid input! Please try again.");
            }
        }
    }

    /**
     * Get valid date input
     */
    private LocalDate getValidDateInput(String prompt, boolean allowNull) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (allowNull && InputValidator.isEmpty(input)) {
                return null;
            }

            if (InputValidator.isEmpty(input)) {
                System.out.println("Date cannot be empty!");
                continue;
            }

            if (InputValidator.isValidDate(input)) {
                LocalDate date = InputValidator.parseDate(input);
                LocalDate today = LocalDate.now();
                Period age = Period.between(date, today);
                if (age.getYears() >= 18 && date.isBefore(today)) {
                    return date;
                } else {
                    System.out.println("Date must ensure age >= 18 and be before today!");
                }
            } else {
                System.out.println("Invalid date format! Please use dd/MM/yyyy.");
            }
        }
    }

    /**
     * Get valid GPA input
     */
    private double getValidGPA(String prompt) {
        while (true) {
            System.out.print(prompt);
            String gpaStr = scanner.nextLine();

            if (InputValidator.isEmpty(gpaStr)) {
                System.out.println("GPA cannot be empty!");
                continue;
            }

            try {
                double gpa = Double.parseDouble(gpaStr);
                if (InputValidator.isValidGPA(gpa)) {
                    return gpa;
                } else {
                    System.out.println("GPA must be between 0.0 and 4.0!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid GPA format! Please enter a number.");
            }
        }
    }

    /**
     * Update full name
     */
    private void updateFullName(Student student) {
        System.out.print("Full name [" + student.getFullName() + "]: ");
        String fullName = scanner.nextLine();
        if (!InputValidator.isEmpty(fullName)) {
            if (InputValidator.isValidName(fullName)) {
                student.setFullName(fullName);
            } else {
                String newFullName = getValidInput("Invalid name. Re-enter full name (Enter to keep unchanged): ",
                        InputValidator::isValidName, true);
                if (newFullName != null) {
                    student.setFullName(newFullName);
                }
            }
        }
    }

    /**
     * Update date of birth
     */
    private void updateDateOfBirth(Student student) {
        System.out.print("Date of birth [" + student.getDateOfBirth() + "] (dd/MM/yyyy): ");
        String dateStr = scanner.nextLine();
        if (!InputValidator.isEmpty(dateStr)) {
            if (InputValidator.isValidDate(dateStr)) {
                LocalDate dateOfBirth = InputValidator.parseDate(dateStr);
                LocalDate today = LocalDate.now();
                Period age = Period.between(dateOfBirth, today);
                if (age.getYears() >= 18 && dateOfBirth.isBefore(today)) {
                    student.setDateOfBirth(dateOfBirth);
                } else {
                    LocalDate newDate = getValidDateInput("Invalid date. Re-enter date of birth (dd/MM/yyyy, Enter to keep unchanged): ", true);
                    if (newDate != null) {
                        student.setDateOfBirth(newDate);
                    }
                }
            } else {
                LocalDate newDate = getValidDateInput("Invalid date format. Re-enter date of birth (dd/MM/yyyy, Enter to keep unchanged): ", true);
                if (newDate != null) {
                    student.setDateOfBirth(newDate);
                }
            }
        }
    }

    /**
     * Update gender
     */
    private void updateGender(Student student) {
        System.out.print("Gender [" + student.getGender() + "] (Male/Female/Other): ");
        String gender = scanner.nextLine();
        if (!InputValidator.isEmpty(gender)) {
            if (InputValidator.isValidGender(gender)) {
                String lower = gender.toLowerCase();
                gender = Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
                student.setGender(gender);
            } else {
                String newGender = getValidInput("Invalid gender. Re-enter gender (Male/Female/Other, Enter to keep unchanged): ",
                        InputValidator::isValidGender, true);
                if (newGender != null) {
                    String lower = newGender.toLowerCase();
                    student.setGender(Character.toUpperCase(lower.charAt(0)) + lower.substring(1));
                }
            }
        }
    }

    /**
     * Update phone number
     */
    private void updatePhone(Student student) {
        System.out.print("Phone number [" + student.getPhone() + "]: ");
        String phone = scanner.nextLine();
        if (!InputValidator.isEmpty(phone)) {
            if (InputValidator.isValidPhone(phone)) {
                student.setPhone(phone);
            } else {
                String newPhone = getValidInput("Invalid phone number. Re-enter phone number (Enter to keep unchanged): ",
                        InputValidator::isValidPhone, true);
                if (newPhone != null) {
                    student.setPhone(newPhone);
                }
            }
        }
    }

    /**
     * Update email
     */
    private void updateEmail(Student student) {
        System.out.print("Email [" + student.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!InputValidator.isEmpty(email)) {
            if (InputValidator.isValidEmail(email)) {
                student.setEmail(email);
            } else {
                String newEmail = getValidInput("Invalid email. Re-enter email (Enter to keep unchanged): ",
                        InputValidator::isValidEmail, true);
                if (newEmail != null) {
                    student.setEmail(newEmail);
                }
            }
        }
    }

    /**
     * Update department
     */
    private void updateDepartment(Student student) {
        String currentDept = studentRepository.getDepartmentNameById(student.getDepartmentId());
        System.out.print("Department [" + (currentDept != null ? currentDept : "N/A") + "]: ");
        String deptName = scanner.nextLine();
        if (!InputValidator.isEmpty(deptName)) {
            List<String> departments = studentRepository.getDepartment();
            if (departments.contains(deptName)) {
                String deptId = studentRepository.getDepartmentIdByName(deptName);
                student.setDepartmentId(deptId);
            } else {
                System.out.println("Invalid department name. Re-enter department name:");
                String newDeptName = getValidDepartmentName();
                if (newDeptName != null) {
                    String deptId = studentRepository.getDepartmentIdByName(newDeptName);
                    student.setDepartmentId(deptId);
                }
            }
        }
    }

    /**
     * Get valid department name
     */
    private String getValidDepartmentName() {
        while (true) {
            System.out.println("List of departments:");
            List<String> departments = studentRepository.getDepartment();
            for (String dept : departments) {
                System.out.println(dept);
            }
            System.out.print("Re-enter department name (Enter to keep unchanged): ");
            String deptName = scanner.nextLine();
            if (InputValidator.isEmpty(deptName)) {
                return null;
            }
            if (departments.contains(deptName)) {
                return deptName;
            } else {
                System.out.println("Invalid department name, please try again.");
            }
        }
    }

    /**
     * Update GPA
     */
    private void updateGPA(Student student) {
        System.out.print("GPA [" + student.getGpa() + "] (0.0-4.0): ");
        String gpaStr = scanner.nextLine();
        if (!InputValidator.isEmpty(gpaStr)) {
            try {
                double gpa = Double.parseDouble(gpaStr);
                if (InputValidator.isValidGPA(gpa)) {
                    student.setGpa(gpa);
                } else {
                    double newGpa = getValidGPA("Invalid GPA. Re-enter GPA (0.0-4.0, Enter to keep unchanged): ");
                    if (newGpa != -1) {
                        student.setGpa(newGpa);
                    }
                }
            } catch (NumberFormatException e) {
                double newGpa = getValidGPA("Invalid GPA format. Re-enter GPA (0.0-4.0, Enter to keep unchanged): ");
                if (newGpa != -1) {
                    student.setGpa(newGpa);
                }
            }
        }
    }

    /**
     * Delete a student
     */
    public void deleteStudent() {
        System.out.println("\n=== DELETE STUDENT ===");

        String studentId = getValidIdInput("Enter student ID to delete: ", true);
        if (studentId == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        Student student = studentRepository.findById(studentId);
        if (student == null) {
            System.out.println("No student found with ID " + studentId);
            return;
        }

        System.out.println("Student information to be deleted:");
        System.out.println(student);

        System.out.print("Are you sure you want to delete? (y/n): ");
        String confirm = scanner.nextLine().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            if (studentRepository.deleteStudent(studentId)) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Error deleting student!");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    /**
     * View student information
     */
    public void viewStudent() {
        System.out.println("\n=== VIEW STUDENT INFORMATION ===");

        String studentId = getValidIdInput("Enter student ID to view: ", true);
        if (studentId == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        Student student = studentRepository.findById(studentId);
        if (student != null) {
            System.out.println("\nStudent information:");
            System.out.println(student);
        } else {
            System.out.println("No student found with ID " + studentId);
        }
    }

    /**
     * List all students
     */
    public void listAllStudents() {
        System.out.println("\n=== LIST ALL STUDENTS ===");

        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            System.out.println("No students found in the system.");
        } else {
            System.out.printf("%-10s %-30s %-15s %-10s %-15s %-30s %-10s %-15s %-5s%n",
                    "Student ID", "Full Name", "Date of Birth", "Gender", "Phone", "Email", "Dept ID", "Enrollment Date", "GPA");
            System.out.println("-".repeat(150));

            for (Student student : students) {
                System.out.printf("%-10s %-30s %-15s %-10s %-15s %-30s %-10s %-15s %-5.2f%n",
                        student.getStudentId(),
                        student.getFullName(),
                        student.getDateOfBirth(),
                        student.getGender(),
                        student.getPhone(),
                        student.getEmail(),
                        student.getDepartmentId() != null ? student.getDepartmentId() : "N/A",
                        student.getEnrollmentDate(),
                        student.getGpa());
            }
        }
    }

    /**
     * Search students by name
     */
    public void searchStudentsByName() {
        System.out.println("\n=== SEARCH STUDENTS BY NAME ===");

        String name = getValidInput("Enter student name to search: ", InputValidator::isValidName, false);
        if (name == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        List<Student> students = studentRepository.findByName(name);
        if (students.isEmpty()) {
            System.out.println("No students found with name: " + name);
        } else {
            System.out.println("\nSearch results:");
            System.out.printf("%-10s %-30s %-15s %-10s %-15s %-30s %-10s %-15s %-5s%n",
                    "Student ID", "Full Name", "Date of Birth", "Gender", "Phone", "Email", "Dept ID", "Enrollment Date", "GPA");
            System.out.println("-".repeat(150));

            for (Student student : students) {
                System.out.printf("%-10s %-30s %-15s %-10s %-15s %-30s %-10s %-15s %-5.2f%n",
                        student.getStudentId(),
                        student.getFullName(),
                        student.getDateOfBirth(),
                        student.getGender(),
                        student.getPhone(),
                        student.getEmail(),
                        student.getDepartmentId() != null ? student.getDepartmentId() : "N/A",
                        student.getEnrollmentDate(),
                        student.getGpa());
            }
        }
    }
}