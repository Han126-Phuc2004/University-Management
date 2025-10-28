@echo off
echo Dang chay ung dung University Management System...
cd /d "%~dp0"

REM Thiết lập mã hóa UTF-8 cho console và biến môi trường Java
chcp 65001
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8

REM Copy dependencies nếu chưa có
if not exist "target\dependency" (
    echo Dang tai dependencies...
    mvn dependency:copy-dependencies -q
)

REM Chạy ứng dụng với classpath đầy đủ
echo Dang khoi dong ung dung...
mvn clean compile exec:java -Dexec.mainClass=main.java.Main

REM Đợi người dùng nhấn phím
pause