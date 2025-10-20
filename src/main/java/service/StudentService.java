package service;

import entity.Student;
import repository.StudentRepository;
import util.InputValidator;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Scanner;

/**
 * Service class cho Student
 * Xử lý business logic liên quan đến sinh viên
 */
public class StudentService {
    private StudentRepository studentRepository;
    private Scanner scanner;

//=== THÊM SINH VIÊN MỚI ===
    public StudentService() {
        this.studentRepository = new StudentRepository();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Lấy ID khoa hợp lệ
     */
    public String getDepartmentId() {
        while (true) {
            System.out.println("Danh sách các khoa:");
            List<String> departments = studentRepository.getDepartment();
            for (String dept : departments) {
                System.out.println(dept);
            }
            System.out.print("Nhập tên khoa: ");
            String deptName = scanner.nextLine();
            if (departments.contains(deptName)) {
                return studentRepository.getDepartmentIdByName(deptName);
            } else {
                System.out.println("Tên khoa không hợp lệ, vui lòng nhập lại.");
            }
        }
    }

    public void addStudent() {
        System.out.println("\n=== THÊM SINH VIÊN MỚI ===");

        String fullName = getValidInput("Nhập họ tên: ", InputValidator::isValidName);

        LocalDate dateOfBirth = getValidDateInput("Nhập ngày sinh (dd/MM/yyyy): ");

        String gender = getValidInput("Nhập giới tính (Male/Female/Other): ", InputValidator::isValidGender);

        String phone = getValidInput("Nhập số điện thoại: ", InputValidator::isValidPhone);

        String email = getValidInput("Nhập email: ", InputValidator::isValidEmail);

        String departmentId = getDepartmentId();

        LocalDate enrollmentDate = LocalDate.now();

        double gpa = getValidGPA();

        String studentId = getValidIdInput("Nhập mã sinh viên: ");
        Student student = new Student(studentId, fullName, dateOfBirth, gender, phone, email,
                departmentId, enrollmentDate, gpa);

        if (studentRepository.addStudent(student)) {
            System.out.println("Thêm sinh viên thành công!");
        } else {
            System.out.println("Lỗi khi thêm sinh viên!");
        }
    }

    private double getValidGPA() {
        while (true) {
            System.out.print("Nhập GPA (0.0-4.0): ");
            String gpaStr = scanner.nextLine();
            if (gpaStr.isEmpty()) {
                return 0.0;
            }
            try {
                double gpa = Double.parseDouble(gpaStr);
                if (InputValidator.isValidGPA(gpa)) {
                    return gpa;
                } else {
                    System.out.println("GPA phải nằm trong khoảng 0.0 đến 4.0. Vui lòng nhập lại!");
                }
            } catch (NumberFormatException e) {
                System.out.println("GPA không hợp lệ. Vui lòng nhập lại!");
            }
        }
    }

    public void updateStudent() {
        System.out.println("\n=== CẬP NHẬT THÔNG TIN SINH VIÊN ===");

        String studentId = getValidIdInput("Nhập mã sinh viên cần cập nhật (Enter để hủy): ");
        if (studentId == null || studentId.isEmpty()) return;

        Student student = studentRepository.findById(studentId);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên với mã " + studentId);
            return;
        }

        System.out.println("Thông tin hiện tại:");
        System.out.println(student);

        System.out.println("\nNhập thông tin mới (Enter để giữ nguyên):");

        updateFullName(student);

        updateDateOfBirth(student);

        updateGender(student);

        updatePhone(student);

        updateEmail(student);

        updateDepartment(student);

        updateGPA(student);

        if (studentRepository.updateStudent(student)) {
            System.out.println("Cập nhật thông tin sinh viên thành công!");
        } else {
            System.out.println("Lỗi khi cập nhật thông tin sinh viên!");
        }
    }

    private String getValidIdInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String id = scanner.nextLine();
            if (!InputValidator.isEmpty(id)) return id;
            System.out.println("Mã không được để trống!");
        }
    }

    private void updateFullName(Student student) {
        System.out.print("Họ tên [" + student.getFullName() + "]: ");
        String fullName = scanner.nextLine();
        if (!InputValidator.isEmpty(fullName) && InputValidator.isValidName(fullName)) {
            student.setFullName(fullName);
        } else if (!InputValidator.isEmpty(fullName)) {
            System.out.println("Họ tên không hợp lệ, giữ nguyên.");
        }
    }

    private void updateDateOfBirth(Student student) {
        System.out.print("Ngày sinh [" + student.getDateOfBirth() + "] (dd/MM/yyyy): ");
        String dateStr = scanner.nextLine();
        if (!InputValidator.isEmpty(dateStr)) {
            if (InputValidator.isValidDate(dateStr)) {
                LocalDate dateOfBirth = InputValidator.parseDate(dateStr);
                LocalDate today = LocalDate.now();
                Period age = Period.between(dateOfBirth, today);
                if (age.getYears() >= 18 && dateOfBirth.isBefore(today)) {
                    student.setDateOfBirth(dateOfBirth);
                } else {
                    System.out.println("Ngày sinh phải đảm bảo tuổi >=18 và trước ngày hiện tại! Giữ nguyên.");
                }
            } else {
                System.out.println("Vui lòng nhập ngày sinh hợp lệ. Giữ nguyên.");
            }
        }
    }

    private void updateGender(Student student) {
        System.out.print("Giới tính [" + student.getGender() + "] (Male/Female/Other): ");
        String gender = scanner.nextLine();
        if (!InputValidator.isEmpty(gender) && InputValidator.isValidGender(gender)) {
            String lower = gender.toLowerCase();
            gender = Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
            student.setGender(gender);
        } else if (!InputValidator.isEmpty(gender)) {
            System.out.println("Giới tính không hợp lệ, giữ nguyên.");
        }
    }

    private void updatePhone(Student student) {
        System.out.print("Số điện thoại [" + student.getPhone() + "]: ");
        String phone = scanner.nextLine();
        if (!InputValidator.isEmpty(phone) && InputValidator.isValidPhone(phone)) {
            student.setPhone(phone);
        } else if (!InputValidator.isEmpty(phone)) {
            System.out.println("Số điện thoại không hợp lệ, giữ nguyên.");
        }
    }

    private void updateEmail(Student student) {
        System.out.print("Email [" + student.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!InputValidator.isEmpty(email) && InputValidator.isValidEmail(email)) {
            student.setEmail(email);
        } else if (!InputValidator.isEmpty(email)) {
            System.out.println("Email không hợp lệ, giữ nguyên.");
        }
    }

    private void updateDepartment(Student student) {
        String currentDept = studentRepository.getDepartmentNameById(student.getDepartmentId());
        System.out.print("Tên khoa [" + (currentDept != null ? currentDept : "Chưa có") + "]: ");
        String deptName = scanner.nextLine();
        if (!InputValidator.isEmpty(deptName)) {
            List<String> departments = studentRepository.getDepartment();
            if (departments.contains(deptName)) {
                String deptId = studentRepository.getDepartmentIdByName(deptName);
                student.setDepartmentId(deptId);
            } else {
                System.out.println("Tên khoa không hợp lệ, giữ nguyên.");
            }
        }
    }

    private void updateGPA(Student student) {
        System.out.print("GPA [" + student.getGpa() + "] (0.0-4.0): ");
        String gpaStr = scanner.nextLine();
        if (!InputValidator.isEmpty(gpaStr)) {
            try {
                double gpa = Double.parseDouble(gpaStr);
                if (InputValidator.isValidGPA(gpa)) {
                    student.setGpa(gpa);
                } else {
                    System.out.println("GPA phải nằm trong khoảng 0.0 đến 4.0, giữ nguyên.");
                }
            } catch (NumberFormatException e) {
                System.out.println("GPA không hợp lệ, giữ nguyên giá trị cũ.");
            }
        }
    }

    public void deleteStudent() {
        System.out.println("\n=== XÓA SINH VIÊN ===");

        String studentId = getValidIdInput("Nhập mã sinh viên cần xóa (Enter để hủy): ");
        if (InputValidator.isEmpty(studentId)) return;

        Student student = studentRepository.findById(studentId);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên với mã " + studentId);
            return;
        }

        System.out.println("Thông tin sinh viên sẽ bị xóa:");
        System.out.println(student);

        System.out.print("Bạn có chắc chắn muốn xóa? (y/n): ");
        String confirm = scanner.nextLine().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            if (studentRepository.deleteStudent(studentId)) {
                System.out.println("Xóa sinh viên thành công!");
            } else {
                System.out.println("Lỗi khi xóa sinh viên!");
            }
        } else {
            System.out.println("Hủy thao tác xóa.");
        }
    }

    public void viewStudent() {
        System.out.println("\n=== XEM THÔNG TIN SINH VIÊN ===");

        String studentId = getValidIdInput("Nhập mã sinh viên cần xem (Enter để hủy): ");
        if (InputValidator.isEmpty(studentId)) return;

        Student student = studentRepository.findById(studentId);
        if (student != null) {
            System.out.println("\nThông tin sinh viên:");
            System.out.println(student);
        } else {
            System.out.println("Không tìm thấy sinh viên với mã " + studentId);
        }
    }

    public void listAllStudents() {
        System.out.println("\n=== DANH SÁCH TẤT CẢ SINH VIÊN ===");

        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            System.out.println("Không có sinh viên nào trong hệ thống.");
        } else {
            System.out.printf("%-10s %-30s %-15s %-10s %-15s %-30s %-10s %-15s %-5s%n",
                    "Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Điện thoại", "Email", "Mã khoa", "Ngày nhập học", "GPA");
            System.out.println("-".repeat(150));

            for (Student student : students) {
            System.out.printf("%-10s %-30s %-15s %-10s %-15s %-30s %-10s %-15s %-5.2f%n",
                        student.getStudentId(),
                        student.getFullName(),
                        student.getDateOfBirth(),
                        student.getGender(),
                        student.getPhone(),
                        student.getEmail(),
                        student.getDepartmentId(),
                        student.getEnrollmentDate(),
                        student.getGpa());
            }
        }
    }

    public void searchStudentsByName() {
        System.out.println("\n=== TÌM KIẾM SINH VIÊN THEO TÊN ===");

        System.out.print("Nhập tên sinh viên cần tìm: ");
        String name = scanner.nextLine();

        if (InputValidator.isEmpty(name)) {
            System.out.println("Tên không được để trống!");
            return;
        }

        List<Student> students = studentRepository.findByName(name);
        if (students.isEmpty()) {
            System.out.println("Không tìm thấy sinh viên nào với tên: " + name);
        } else {
            System.out.println("\nKết quả tìm kiếm:");
            System.out.printf("%-10s %-30s %-15s %-10s %-15s %-30s %-10s %-15s %-5s%n",
                    "Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Điện thoại", "Email", "Mã khoa", "Ngày nhập học", "GPA");
            System.out.println("-".repeat(150));

            for (Student student : students) {
            System.out.printf("%-10s %-30s %-15s %-10s %-15s %-30s %-10s %-15s %-5.2f%n",
                        student.getStudentId(),
                        student.getFullName(),
                        student.getDateOfBirth(),
                        student.getGender(),
                        student.getPhone(),
                        student.getEmail(),
                        student.getDepartmentId(),
                        student.getEnrollmentDate(),
                        student.getGpa());
            }
        }
    }

    private String getValidInput(String prompt, java.util.function.Function<String, Boolean> validator) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Dữ liệu không được để trống!");
                continue;
            }
            if (validator.apply(input)) {
                return input;
            } else {
                System.out.println("Dữ liệu không hợp lệ! Vui lòng nhập lại.");
            }
        }
    }

    private LocalDate getValidDateInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Ngày không được để trống!");
                continue;
            }
            if (InputValidator.isValidDate(input)) {
                LocalDate date = InputValidator.parseDate(input);
                LocalDate today = LocalDate.now();
                Period age = Period.between(date, today);
                if (age.getYears() >= 18 && date.isBefore(today)) {
                    return date;
                } else {
                    System.out.println("Tuổi phải >=18 và ngày sinh trước ngày hiện tại! Vui lòng nhập lại.");
                }
            } else {
                System.out.println("Định dạng ngày không hợp lệ! Vui lòng nhập theo format dd/MM/yyyy.");
            }
        }
    }

    public static void main(String[] args) {
        StudentService service = new StudentService();
        service.addStudent();
    }
}