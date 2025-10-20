package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
public class FileUtils {

    /* =====================================================
     * ============== ĐỌC / GHI CSV =========================
     * ===================================================== */

    /**
     * Đọc file CSV thành danh sách các dòng (bỏ header, bỏ dòng trống)
     * Hỗ trợ dữ liệu trong dấu "..." chứa dấu phẩy.
     */
    public static List<String[]> readCSV(String filePath) {
        List<String[]> records = new ArrayList<>();

        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                System.err.println("File không tồn tại: " + filePath);
                return records;
            }

            try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                String header = br.readLine(); // bỏ dòng header
                if (header == null) return records;

                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] cols = line.split(",");

                    // Bỏ dòng toàn rỗng
                    boolean allBlank = true;
                    for (String v : cols) {
                        if (v != null && !v.isBlank()) { allBlank = false; break; }
                    }
                    if (!allBlank) records.add(cols);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi đọc file CSV: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }

    /* =====================================================
     * ============== FILE HELPER =========================
     * ===================================================== */

    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Hàm tiện ích: rút số nguyên từ chuỗi như "GV006" → 6
     */
    public static int extractDigitsToInt(String s) {
        if (s == null) return 0;
        String digits = s.replaceAll("\\D+", "");
        if (digits.isEmpty()) return 0;
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
