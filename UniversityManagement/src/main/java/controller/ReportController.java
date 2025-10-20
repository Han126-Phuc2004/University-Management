package controller;

import repository.StudentRepository;
import entity.Student;

import java.util.List;
import java.util.Scanner;

public class ReportController {
    private StudentRepository studentRepository;
    private Scanner scanner;

    public ReportController() {
        this.studentRepository = new StudentRepository();
        this.scanner = new Scanner(System.in);
    }

    public void showReportMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("      BÁO CÁO VÀ THỐNG KÊ");
            System.out.println("=".repeat(40));
            System.out.println("1. Thống kê tổng quan");
            System.out.println("0. Quay lại menu chính");
            System.out.println("=".repeat(40));
            System.out.print("Vui lòng chọn chức năng (0-1): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        showGeneralStatistics();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 0-1.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên!");
            }
        }
    }

    private void showGeneralStatistics() {
        System.out.println("\n=== THỐNG KÊ TỔNG QUAN ===");
        List<Student> students = studentRepository.findAll();
        System.out.println("Tổng số sinh viên: " + students.size());
    }
}
