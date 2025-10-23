package service;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
public class ReportGenerator {
    private static void printTableHeader(String[] headers, int[] columnWidths) {
        StringBuilder headerLine = new StringBuilder();
        StringBuilder separatorLine = new StringBuilder();
        for (int i = 0; i < headers.length; i++) {
            headerLine.append(String.format("%-" + columnWidths[i] + "s | ", headers[i]));
            separatorLine.append("-".repeat(columnWidths[i])).append("-+-");
        }
        System.out.println(separatorLine.toString());
        System.out.println(headerLine.toString());
        System.out.println(separatorLine.toString());
    }

    private static void printTableRow(String[] row, int[] columnWidths) {
        StringBuilder rowLine = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            rowLine.append(String.format("%-" + columnWidths[i] + "s | ", row[i] != null ? row[i] : ""));
        }
        System.out.println(rowLine.toString());
    }

    public void generateGpaReport() {
        String query = """
            SELECT 
                CASE 
                    WHEN gpa < 2.0 THEN '0 - 2.0'
                    WHEN gpa BETWEEN 2.0 AND 2.5 THEN '2.0 - 2.5'
                    WHEN gpa BETWEEN 2.5 AND 3.0 THEN '2.5 - 3.0'
                    ELSE '3.0 - 4.0'
                END AS gpa_range,
                COUNT(*) AS student_count
            FROM student
            GROUP BY 
                CASE 
                    WHEN gpa < 2.0 THEN '0 - 2.0'
                    WHEN gpa BETWEEN 2.0 AND 2.5 THEN '2.0 - 2.5'
                    WHEN gpa BETWEEN 2.5 AND 3.0 THEN '2.5 - 3.0'
                    ELSE '3.0 - 4.0'
                END;
        """;
        String csvDir = "data";
        String csvFile = csvDir + "/gpa_distribution_report.csv";

        try {
            Files.createDirectories(Paths.get(csvDir));

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n=== GPA Distribution Report ===");
                String[] headers = {"GPA Range", "Number of Students"};
                int[] columnWidths = {15, 20};
                printTableHeader(headers, columnWidths);
                try (FileWriter writer = new FileWriter(csvFile)) {
                    writer.write("GPA Range,Number of Students\n");
                    while (rs.next()) {
                        String gpaRange = rs.getString("gpa_range");
                        int studentCount = rs.getInt("student_count");
                        String[] row = {gpaRange, String.valueOf(studentCount)};
                        printTableRow(row, columnWidths);
                        writer.write(String.format("\"%s\",%d\n", gpaRange, studentCount));
                    }

                    System.out.println("-".repeat(columnWidths[0] + columnWidths[1] + 3));
                    System.out.println("Report has been exported to " + csvFile);
                } catch (IOException e) {
                    System.err.println("Error exporting to CSV: " + e.getMessage());
                }

            } catch (SQLException e) {
                System.err.println("Error generating GPA report: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error creating directory 'data': " + e.getMessage());
        }
    }
    public void generateEnrollmentStatusReport(String by) {
        String query;
        String[] headers;
        int[] columnWidths;

        if ("semester".equalsIgnoreCase(by)) {
            query = """
                SELECT 
                    s.semester_name,
                    e.status,
                    COUNT(*) AS count
                FROM enrollment e
                JOIN course c ON e.course_id = c.course_id
                JOIN semester s ON c.semester_id = s.semester_id
                GROUP BY s.semester_name, e.status;
            """;
            headers = new String[]{"Semester", "Status", "Count"};
            columnWidths = new int[]{20, 15, 10};
        } else {
            query = """
                SELECT 
                    c.course_name,
                    e.status,
                    COUNT(*) AS count
                FROM enrollment e
                JOIN course c ON e.course_id = c.course_id
                GROUP BY c.course_name, e.status;
            """;
            headers = new String[]{"Course", "Status", "Count"};
            columnWidths = new int[]{30, 15, 10};
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n=== Enrollment Status Report (By " + by + ") ===");
            printTableHeader(headers, columnWidths);

            while (rs.next()) {
                String[] row = {
                        rs.getString(1), // semester_name or course_name
                        rs.getString("status"),
                        String.valueOf(rs.getInt("count"))
                };
                printTableRow(row, columnWidths);
            }

            System.out.println("-".repeat(columnWidths[0] + columnWidths[1] + columnWidths[2] + 5));
        } catch (SQLException e) {
            System.err.println("Error generating enrollment status report: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        ReportGenerator reportGen = new ReportGenerator();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Student Report System ===");
            System.out.println("1. GPA Distribution Report");
            System.out.println("2. Enrollment Status Report (By Semester)");
            System.out.println("3. Enrollment Status Report (By Course)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                reportGen.generateGpaReport();
            } else if (choice.equals("2")) {
                reportGen.generateEnrollmentStatusReport("semester");
            } else if (choice.equals("3")) {
                reportGen.generateEnrollmentStatusReport("course");
            } else if (choice.equals("4")) {
                System.out.println("Exiting program...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}