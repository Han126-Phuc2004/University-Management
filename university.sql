-- ===============================
-- Tạo DATABASE
-- ===============================
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'university_management')
BEGIN
    CREATE DATABASE university_management
    COLLATE Vietnamese_CI_AS;
END
GO

USE university_management;
GO

-- ===============================
-- BẢNG DEPARTMENT (Khoa/Bộ môn)
-- ===============================
CREATE TABLE department (
    department_id NVARCHAR(20) PRIMARY KEY,
    department_name NVARCHAR(200) NOT NULL UNIQUE,
    description NVARCHAR(MAX),
    head_lecturer_id NVARCHAR(20) NULL  
);
GO

INSERT INTO department (department_id, department_name)
VALUES 
('SE', N'Khoa Phần mềm'),
('AI', N'Khoa Trí tuệ Nhân tạo'),
('IB', N'Khoa Kinh doanh Quốc tế');
GO

CREATE INDEX idx_department_name ON department(department_name);
GO

-- ===============================
-- BẢNG SEMESTER (Học kỳ)
-- ===============================
CREATE TABLE semester (
    semester_id NVARCHAR(20) PRIMARY KEY,  
    semester_name NVARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    CONSTRAINT chk_semester_dates CHECK (end_date > start_date)
);

CREATE INDEX idx_semester_dates ON semester(start_date, end_date);
GO

-- ===============================
-- BẢNG STUDENT (Sinh viên)
-- ===============================
CREATE TABLE student (
    student_id NVARCHAR(20) PRIMARY KEY,
    full_name NVARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender NVARCHAR(10) NOT NULL CHECK (gender IN ('Male', 'Female', 'Other')),
    phone VARCHAR(15),
    email VARCHAR(100) NOT NULL UNIQUE,
    department_id NVARCHAR(20),  
    enrollment_date DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    gpa DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_student_department FOREIGN KEY (department_id)
        REFERENCES department(department_id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT chk_gpa CHECK (gpa >= 0.00 AND gpa <= 4.00),
    CONSTRAINT chk_student_dob CHECK (date_of_birth < CAST(GETDATE() AS DATE))
);

CREATE INDEX idx_student_name ON student(full_name);
GO

-- ===============================
-- BẢNG LECTURER (Giảng viên)
-- ===============================
CREATE TABLE lecturer (
    lecturer_id NVARCHAR(20) PRIMARY KEY,
    full_name NVARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender NVARCHAR(10) NOT NULL CHECK (gender IN ('Male', 'Female', 'Other')),
    phone VARCHAR(15),
    email VARCHAR(100) NOT NULL UNIQUE,
    department_id NVARCHAR(20),
    degree NVARCHAR(50) NOT NULL,
    specialization NVARCHAR(100),
    CONSTRAINT fk_lecturer_department FOREIGN KEY (department_id)
        REFERENCES department(department_id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT chk_lecturer_dob CHECK (date_of_birth < CAST(GETDATE() AS DATE))
);

CREATE INDEX idx_lecturer_name ON lecturer(full_name);
GO

-- ===============================
-- FOREIGN KEY head_lecturer_id
-- ===============================
ALTER TABLE department 
ADD CONSTRAINT fk_department_head 
FOREIGN KEY (head_lecturer_id)
    REFERENCES lecturer(lecturer_id)
    ON UPDATE NO ACTION 
    ON DELETE SET NULL;
GO

-- ===============================
-- BẢNG COURSE (Khóa học)
-- ===============================
CREATE TABLE course (
    course_id NVARCHAR(20) PRIMARY KEY,  
    course_name NVARCHAR(200) NOT NULL,
    description NVARCHAR(MAX),
    credits INT NOT NULL,
    lecturer_id NVARCHAR(20) NULL, 
    department_id NVARCHAR(20), 
    semester_id NVARCHAR(20),    
    max_students INT NOT NULL DEFAULT 50,
    enrolled_students INT DEFAULT 0,
    CONSTRAINT fk_course_lecturer FOREIGN KEY (lecturer_id)
        REFERENCES lecturer(lecturer_id)
        ON UPDATE NO ACTION
        ON DELETE SET NULL,
    CONSTRAINT fk_course_department FOREIGN KEY (department_id)
        REFERENCES department(department_id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_course_semester FOREIGN KEY (semester_id)
        REFERENCES semester(semester_id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT chk_credits CHECK (credits > 0 AND credits <= 6),
    CONSTRAINT chk_max_students CHECK (max_students > 0),
    CONSTRAINT chk_enrolled_students CHECK (enrolled_students >= 0 AND enrolled_students <= max_students)
);

CREATE INDEX idx_course_name ON course(course_name);
GO

-- ===============================
-- BẢNG ENROLLMENT (Đăng ký học phần)
-- ===============================
CREATE TABLE enrollment (
    enrollment_id NVARCHAR(20) PRIMARY KEY,  
    student_id NVARCHAR(20) NOT NULL,        
    course_id NVARCHAR(20) NOT NULL,       
    enrollment_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    status NVARCHAR(20) DEFAULT 'enrolled' CHECK (status IN ('enrolled', 'completed', 'dropped', 'failed')),
    midterm_score DECIMAL(4,2),
    final_score DECIMAL(4,2),
    total_score DECIMAL(4,2),
    grade VARCHAR(2),
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id)
        REFERENCES student(student_id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id)
        REFERENCES course(course_id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT unique_enrollment UNIQUE (student_id, course_id),
    CONSTRAINT chk_midterm_score CHECK (midterm_score IS NULL OR (midterm_score >= 0 AND midterm_score <= 10)),
    CONSTRAINT chk_final_score CHECK (final_score IS NULL OR (final_score >= 0 AND final_score <= 10)),
    CONSTRAINT chk_total_score CHECK (total_score IS NULL OR (total_score >= 0 AND total_score <= 10))
);

CREATE INDEX idx_enrollment_course ON enrollment(course_id);
CREATE INDEX idx_enrollment_date ON enrollment(enrollment_date);
GO
