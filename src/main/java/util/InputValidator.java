package util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utility class để validate input từ user
 * Cung cấp các phương thức kiểm tra dữ liệu đầu vào
 */
public class InputValidator {
    
    // Regex patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10,11}$"
    );
    
    private static final Pattern ID_PATTERN = Pattern.compile(
        "^[A-Za-z0-9]{2,20}$"
    );
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Kiểm tra email có hợp lệ không
     * @param email email cần kiểm tra
     * @return true nếu email hợp lệ
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Kiểm tra số điện thoại có hợp lệ không
     * @param phone số điện thoại cần kiểm tra
     * @return true nếu số điện thoại hợp lệ
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Loại bỏ khoảng trắng và ký tự đặc biệt
        String cleanPhone = phone.replaceAll("[\\s-()]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    /**
     * Kiểm tra ID có hợp lệ không
     * @param id ID cần kiểm tra
     * @return true nếu ID hợp lệ
     */
    public static boolean isValidId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return ID_PATTERN.matcher(id.trim()).matches();
    }
    
    /**
     * Kiểm tra ngày có hợp lệ không
     * @param dateString chuỗi ngày theo format dd/MM/yyyy
     * @return true nếu ngày hợp lệ
     */
    public static boolean isValidDate(String dateString) {

        if (dateString == null || dateString.trim().isEmpty()) {
            return false;
        }
        try {
          LocalDate dob = LocalDate.parse(dateString.trim(), DATE_FORMATTER);
            LocalDate today = LocalDate.now();
            Period age = Period.between(dob,today);
            if(age.getYears()<18 || dob.isAfter(today))
            {
                return false ;
            }
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Chuyển đổi chuỗi ngày thành LocalDate
     * @param dateString chuỗi ngày theo format dd/MM/yyyy
     * @return LocalDate object hoặc null nếu không hợp lệ
     */
    public static LocalDate parseDate(String dateString) {
        if (!isValidDate(dateString)) {
            return null;
        }
        
        try {
            return LocalDate.parse(dateString.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Kiểm tra tên có hợp lệ không (chỉ chứa chữ cái và khoảng trắng)
     * @param name tên cần kiểm tra
     * @return true nếu tên hợp lệ
     */
    public static boolean isValidName(String name) {
        if (name == null) return false;

        String trimmedName = name.trim()
                .replaceAll("\\s+", " ");

        if (trimmedName.length() < 2 || trimmedName.length() > 100) {
            return false;
        }

        // Cho phép mỗi từ: Chữ hoa + (chữ thường/dấu)* - chấp nhận chữ đơn như "A", "Y"
        String regex = "^([\\p{Lu}][\\p{Ll}\\p{M}]*)(\\s[\\p{Lu}][\\p{Ll}\\p{M}]*)*$";

        return trimmedName.matches(regex);
    }

    
    /**
     * Kiểm tra GPA có hợp lệ không (0.0 - 4.0)
     * @param gpa GPA cần kiểm tra
     * @return true nếu GPA hợp lệ
     */
    public static boolean isValidGPA(double gpa) {
        return gpa >= 0.0 && gpa <= 4.0;
    }
    
    /**
     * Kiểm tra điểm số có hợp lệ không (0.0 - 10.0)
     * @param score điểm số cần kiểm tra
     * @return true nếu điểm số hợp lệ
     */
    public static boolean isValidScore(double score) {
        return score >= 0.0 && score <= 10.0;
    }
    
    /**
     * Kiểm tra số tín chỉ có hợp lệ không (1-6)
     * @param credits số tín chỉ cần kiểm tra
     * @return true nếu số tín chỉ hợp lệ
     */
    public static boolean isValidCredits(int credits) {
        return credits >= 1 && credits <= 6;
    }
    
    /**
     * Kiểm tra số lượng sinh viên tối đa có hợp lệ không (> 0)
     * @param maxStudents số lượng sinh viên tối đa
     * @return true nếu hợp lệ
     */
    public static boolean isValidMaxStudents(int maxStudents) {
        return maxStudents > 0;
    }
    
    /**
     * Kiểm tra giới tính có hợp lệ không
     * @param gender giới tính cần kiểm tra
     * @return true nếu giới tính hợp lệ
     */
    public static boolean isValidGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            return false;
        }
        
        String trimmedGender = gender.trim().toLowerCase();
        return trimmedGender.equals("male") || 
               trimmedGender.equals("female") || 
               trimmedGender.equals("other");
    }
    
    /**
     * Kiểm tra trạng thái sinh viên có hợp lệ không
     * @param status trạng thái cần kiểm tra
     * @return true nếu trạng thái hợp lệ
     */
    public static boolean isValidStudentStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }
        
        String trimmedStatus = status.trim().toLowerCase();
        return trimmedStatus.equals("active") || 
               trimmedStatus.equals("suspended") || 
               trimmedStatus.equals("graduated") || 
               trimmedStatus.equals("dropped");
    }
    
    /**
     * Kiểm tra trạng thái giảng viên có hợp lệ không
     * @param status trạng thái cần kiểm tra
     * @return true nếu trạng thái hợp lệ
     */
    public static boolean isValidLecturerStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }
        
        String trimmedStatus = status.trim().toLowerCase();
        return trimmedStatus.equals("active") || 
               trimmedStatus.equals("on_leave") || 
               trimmedStatus.equals("retired");
    }
    
    /**
     * Kiểm tra trạng thái khóa học có hợp lệ không
     * @param status trạng thái cần kiểm tra
     * @return true nếu trạng thái hợp lệ
     */
    public static boolean isValidCourseStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }
        
        String trimmedStatus = status.trim().toLowerCase();
        return trimmedStatus.equals("open") || 
               trimmedStatus.equals("closed") || 
               trimmedStatus.equals("cancelled");
    }
    
    /**
     * Kiểm tra trạng thái đăng ký có hợp lệ không
     * @param status trạng thái cần kiểm tra
     * @return true nếu trạng thái hợp lệ
     */
    public static boolean isValidEnrollmentStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }
        
        String trimmedStatus = status.trim().toLowerCase();
        return trimmedStatus.equals("enrolled") || 
               trimmedStatus.equals("completed") || 
               trimmedStatus.equals("dropped") || 
               trimmedStatus.equals("failed");
    }
    
    /**
     * Làm sạch và chuẩn hóa chuỗi input
     * @param input chuỗi cần làm sạch
     * @return chuỗi đã được làm sạch
     */
    public static String cleanInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim();
    }
    
    /**
     * Kiểm tra chuỗi có rỗng hoặc null không
     * @param input chuỗi cần kiểm tra
     * @return true nếu chuỗi rỗng hoặc null
     */
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }
}
