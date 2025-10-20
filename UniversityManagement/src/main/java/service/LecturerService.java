package service;

import entity.Lecturer;
import repository.LecturerRepository;
import util.InputValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Service cho nghiệp vụ giảng viên
 */
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final Scanner scanner;

    public LecturerService() {
        this.lecturerRepository = new LecturerRepository();
        this.scanner = new Scanner(System.in);
    }

    public void addLecturer() {
        System.out.println("\nTHÊM GIẢNG VIÊN MỚI");

        String lecturerId = getValidInput("Nhập mã giảng viên: ", InputValidator::isValidId);
        if (lecturerRepository.existsById(lecturerId)) {
            System.out.println("Mã giảng viên đã tồn tại!");
            return;
        }

        String fullName = getValidInput("Nhập họ tên: ", InputValidator::isValidName);
        LocalDate dateOfBirth = getValidDateInput("Nhập ngày sinh (dd/MM/yyyy): ");
        String gender = getValidInput("Nhập giới tính (Male/Female/Other): ", InputValidator::isValidGender);
        String phone = getValidInput("Nhập số điện thoại: ", InputValidator::isValidPhone);
        String email = getValidInput("Nhập email: ", InputValidator::isValidEmail);
        String departmentId = getValidInput("Nhập mã khoa (SE/AI/IB): ", InputValidator::isValidId);
        String degree = getValidInput("Nhập bằng cấp: ", s -> !InputValidator.isEmpty(s));
        System.out.print("Nhập chuyên ngành: ");
        String specialization = scanner.nextLine().trim();

        if (!lecturerRepository.departmentExists(departmentId)) {
            System.out.println("Mã khoa không tồn tại!");
            return;
        }

        if (lecturerRepository.existsByEmail(email)) {
            System.out.println("Email đã được sử dụng!");
            return;
        }

        Lecturer l = new Lecturer(lecturerId, fullName, dateOfBirth, gender, phone, email, departmentId, degree, specialization);
        boolean ok = lecturerRepository.addLecturer(l);
        System.out.println(ok ? "Thêm giảng viên thành công!" : "Lỗi khi thêm giảng viên!");
    }

    public void updateLecturer() {
        System.out.println("\nCẬP NHẬT THÔNG TIN GIẢNG VIÊN");
        
        System.out.print("Nhập ID giảng viên cần cập nhật: ");
        String lecturerId = scanner.nextLine().trim();
        
        if (!InputValidator.isValidId(lecturerId)) {
            System.out.println("ID không hợp lệ!");
            return;
        }
        
        Lecturer lecturer = lecturerRepository.findById(lecturerId);
        if (lecturer == null) {
            System.out.println("Không tìm thấy giảng viên với ID " + lecturerId);
            return;
        }
        
        System.out.println("\nThông tin hiện tại:");
        displayLecturerInfo(lecturer);
        System.out.println("\nNhập thông tin mới (Enter để giữ nguyên):");
        
        System.out.print("Họ tên [" + lecturer.getFullName() + "]: ");
        String fullName = scanner.nextLine().trim();
        if (!fullName.isEmpty() && InputValidator.isValidName(fullName)) {
            lecturer.setFullName(fullName);
        }
        
        System.out.print("Ngày sinh [" + lecturer.getDateOfBirth() + "] (dd/MM/yyyy): ");
        String dobStr = scanner.nextLine().trim();
        if (!dobStr.isEmpty() && InputValidator.isValidDate(dobStr)) {
            lecturer.setDateOfBirth(InputValidator.parseDate(dobStr));
        }
        
        System.out.print("Giới tính [" + lecturer.getGender() + "] (Male/Female/Other): ");
        String gender = scanner.nextLine().trim();
        if (!gender.isEmpty() && InputValidator.isValidGender(gender)) {
            lecturer.setGender(gender);
        }
        
        System.out.print("Số điện thoại [" + lecturer.getPhone() + "]: ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty() && InputValidator.isValidPhone(phone)) {
            lecturer.setPhone(phone);
        }
        
        System.out.print("Email [" + lecturer.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty() && InputValidator.isValidEmail(email)) {
            lecturer.setEmail(email);
        }
        
        System.out.print("Mã khoa [" + lecturer.getDepartmentId() + "]: ");
        String deptId = scanner.nextLine().trim();
        if (!deptId.isEmpty() && InputValidator.isValidId(deptId)) {
            if (!lecturerRepository.departmentExists(deptId)) {
                System.out.println("Mã khoa không tồn tại! Giữ nguyên mã khoa cũ.");
            } else {
                lecturer.setDepartmentId(deptId);
            }
        }
        
        System.out.print("Bằng cấp [" + lecturer.getDegree() + "]: ");
        String degree = scanner.nextLine().trim();
        if (!degree.isEmpty()) {
            lecturer.setDegree(degree);
        }
        
        System.out.print("Chuyên ngành [" + lecturer.getSpecialization() + "]: ");
        String spec = scanner.nextLine().trim();
        if (!spec.isEmpty()) {
            lecturer.setSpecialization(spec);
        }
        
        if (lecturerRepository.updateLecturer(lecturer)) {
            System.out.println("Cập nhật thông tin giảng viên thành công!");
        } else {
            System.out.println("Lỗi khi cập nhật thông tin giảng viên!");
        }
    }
    
    public void deleteLecturer() {
        System.out.println("\n XÓA GIẢNG VIÊN");
        
        System.out.print("Nhập ID giảng viên cần xóa: ");
        String lecturerId = scanner.nextLine().trim();
        
        if (!InputValidator.isValidId(lecturerId)) {
            System.out.println("ID không hợp lệ!");
            return;
        }
        
        Lecturer lecturer = lecturerRepository.findById(lecturerId);
        if (lecturer == null) {
            System.out.println("Không tìm thấy giảng viên với ID " + lecturerId);
            return;
        }
        
        System.out.println("\nThông tin giảng viên sẽ bị xóa:");
        displayLecturerInfo(lecturer);
        
        System.out.print("\nBạn có chắc chắn muốn xóa? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            if (lecturerRepository.deleteLecturer(lecturerId)) {
                System.out.println("Xóa giảng viên thành công!");
            } else {
                System.out.println("Lỗi khi xóa giảng viên!");
            }
        } else {
            System.out.println("Hủy thao tác xóa.");
        }
    }
    
    public void viewLecturer() {
        System.out.println("\nXEM THÔNG TIN GIẢNG VIÊN");
        
        System.out.print("Nhập ID giảng viên: ");
        String lecturerId = scanner.nextLine().trim();
        
        if (!InputValidator.isValidId(lecturerId)) {
            System.out.println("ID không hợp lệ!");
            return;
        }
        
        Lecturer lecturer = lecturerRepository.findById(lecturerId);
        if (lecturer == null) {
            System.out.println("Không tìm thấy giảng viên với ID " + lecturerId);
            return;
        }
        
        System.out.println("\n" + "=".repeat(50));
        displayLecturerInfo(lecturer);
        System.out.println("=".repeat(50));
    }
    
    public void listAllLecturers() {
        System.out.println("\nDANH SÁCH TẤT CẢ GIẢNG VIÊN");
        
        List<Lecturer> lecturers = lecturerRepository.findAll();
        if (lecturers.isEmpty()) {
            System.out.println("Không có giảng viên nào trong hệ thống.");
            return;
        }
        
        System.out.println("\nTổng số: " + lecturers.size() + " giảng viên\n");
        System.out.printf("%-10s %-25s %-12s %-8s %-15s %-30s %-15s%n",
                "ID", "Họ tên", "Ngày sinh", "Giới tính", "Điện thoại", "Email", "Bằng cấp");
        System.out.println("-".repeat(120));
        
        for (Lecturer l : lecturers) {
            System.out.printf("%-10s %-25s %-12s %-8s %-15s %-30s %-15s%n",
                    l.getLecturerId(),
                    truncate(l.getFullName(), 25),
                    l.getDateOfBirth(),
                    l.getGender(),
                    l.getPhone(),
                    truncate(l.getEmail(), 30),
                    truncate(l.getDegree(), 15));
        }
        System.out.println("-".repeat(120));
    }
    
    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }
    
    private void displayLecturerInfo(Lecturer l) {
        System.out.println("ID: " + l.getLecturerId());
        System.out.println("Họ tên: " + l.getFullName());
        System.out.println("Ngày sinh: " + l.getDateOfBirth());
        System.out.println("Giới tính: " + l.getGender());
        System.out.println("Số điện thoại: " + l.getPhone());
        System.out.println("Email: " + l.getEmail());
        System.out.println("Mã khoa: " + l.getDepartmentId());
        System.out.println("Bằng cấp: " + l.getDegree());
        System.out.println("Chuyên ngành: " + l.getSpecialization());
    }

    private String getValidInput(String prompt, java.util.function.Function<String, Boolean> validator) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (validator.apply(input)) return input;
            System.out.println("Dữ liệu không hợp lệ! Vui lòng nhập lại.");
        }
    }

    private LocalDate getValidDateInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (InputValidator.isValidDate(input)) return InputValidator.parseDate(input);
            System.out.println("Định dạng ngày không hợp lệ! Vui lòng nhập dd/MM/yyyy.");
        }
    }
}

