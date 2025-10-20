@echo off
echo ========================================
echo    HỆ THỐNG QUẢN LÝ ĐẠI HỌC
echo ========================================
echo.

REM Thiết lập encoding UTF-8 cho console Windows
chcp 65001

REM Thiết lập biến môi trường Java
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8

echo Đang khởi động ứng dụng...
echo.

REM Chạy ứng dụng với Maven
mvn -Dexec.mainClass=main.java.Main exec:java

echo.
echo ========================================
echo Ứng dụng đã kết thúc. Nhấn phím bất kỳ để thoát...
pause > nul
