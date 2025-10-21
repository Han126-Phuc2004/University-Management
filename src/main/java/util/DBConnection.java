package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class để quản lý kết nối database
 * Tạo connection mới cho mỗi request để an toàn khi đa luồng
 */
public class DBConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=university_management;encrypt=false;trustServerCertificate=true";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "sa123";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy SQL Driver: " + e.getMessage());
        }
    }

    /**
     * Lấy connection mới từ database
     *
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối database: " + e.getMessage());
            return null;
        }
    }

    /**
     * Đóng connection (không cần thiết vì mỗi connection được tự động đóng với try-with-resources)
     */
    public static void closeConnection() {
        // Không cần thiết vì mỗi connection được tự động đóng với try-with-resources
        // System.out.println("Connection được tự động đóng với try-with-resources!");
    }

}
