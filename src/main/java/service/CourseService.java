package service;

import entity.Course;
import repository.CourseRepository;
import util.DBConnection;
import util.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * Service class for Course
 * Handles business logic related to courses
 */
public class CourseService {
    private CourseRepository courseRepository;
    private Scanner scanner;

    public CourseService() {
        this.courseRepository = new CourseRepository();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Main method to test functionalities
     */
    public static void main(String[] args) {
        CourseService service = new CourseService();

        // Test addCourse
        System.out.println("Testing addCourse:");
        service.addCourse();

        // Test listAllCourses
        System.out.println("Testing listAllCourses:");
        service.listAllCourses();

        // Test viewCourse
        System.out.println("Testing viewCourse:");
        service.viewCourse();

        // Test updateCourse
        System.out.println("Testing updateCourse:");
        service.updateCourse();

        // Test searchCoursesByName
        System.out.println("Testing searchCoursesByName:");
        service.searchCoursesByName();

        // Test searchCoursesByLecturer
        System.out.println("Testing searchCoursesByLecturer:");
        service.searchCoursesByLecturer();

        // Test deleteCourse
        System.out.println("Testing deleteCourse:");
        service.deleteCourse();

        // Close database connection after testing
        DBConnection.closeConnection();
    }

    /**
     * Add a new course
     */
    public void addCourse() {
        System.out.println("\n=== ADD NEW COURSE ===");

        String courseId = getValidIdInput("Enter course ID: ", false);
        if (courseId == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        String courseName = getValidInput("Enter course name: ", InputValidator::isValidName, false);
        if (courseName == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        System.out.print("Enter course description: ");
        String description = scanner.nextLine();

        int credits = getValidCreditsInput("Enter credits (1-6): ");
        if (credits == -1) {
            System.out.println("Operation cancelled.");
            return;
        }

        String lecturerId = getValidIdInput("Enter lecturer ID (Enter to skip): ", true);
        String departmentId = getValidIdInput("Enter department ID (Enter to skip): ", true);
        String semesterId = getValidIdInput("Enter semester ID (Enter to skip): ", true);

        int maxStudents = getValidMaxStudentsInput("Enter maximum number of students: ");
        if (maxStudents == -1) {
            System.out.println("Operation cancelled.");
            return;
        }

        Course course = new Course(courseId, courseName, description, credits, lecturerId,
                departmentId, semesterId, maxStudents, 0);

        if (courseRepository.addCourse(course)) {
            System.out.println("Course added successfully!");
        } else {
            System.out.println("Error adding course!");
        }
    }

    /**
     * Update course information
     */
    public void updateCourse() {
        System.out.println("\n=== UPDATE COURSE INFORMATION ===");

        String courseId = getValidIdInput("Enter course ID to update: ", true);
        if (courseId == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        Course course = courseRepository.findById(courseId);
        if (course == null) {
            System.out.println("No course found with ID " + courseId);
            return;
        }

        System.out.println("Current information:");
        System.out.println(course);

        System.out.println("\nEnter new information (Enter to keep unchanged):");

        System.out.print("Course name [" + course.getCourseName() + "]: ");
        String courseName = scanner.nextLine();
        if (!InputValidator.isEmpty(courseName)) {
            if (InputValidator.isValidName(courseName)) {
                course.setCourseName(courseName);
            } else {
                courseName = getValidInput("Invalid course name. Re-enter course name (Enter to keep unchanged): ",
                        InputValidator::isValidName, true);
                if (courseName != null) {
                    course.setCourseName(courseName);
                }
            }
        }

        System.out.print("Description [" + course.getDescription() + "]: ");
        String description = scanner.nextLine();
        if (!InputValidator.isEmpty(description)) {
            course.setDescription(description);
        }

        System.out.print("Credits [" + course.getCredits() + "] (1-6): ");
        String creditsStr = scanner.nextLine();
        int credits;
        if (!InputValidator.isEmpty(creditsStr)) {
            try {
                credits = Integer.parseInt(creditsStr);
                if (credits >= 1 && credits <= 6) {
                    course.setCredits(credits);
                } else {
                    credits = getValidCreditsInput("Invalid credits. Re-enter credits (1-6, Enter to keep unchanged): ");
                    if (credits != -1) {
                        course.setCredits(credits);
                    }
                }
            } catch (NumberFormatException e) {
                credits = getValidCreditsInput("Invalid credits format. Re-enter credits (1-6, Enter to keep unchanged): ");
                if (credits != -1) {
                    course.setCredits(credits);
                }
            }
        }

        System.out.print("Lecturer ID [" + course.getLecturerId() + "]: ");
        String lecturerIdStr = scanner.nextLine();
        if (!InputValidator.isEmpty(lecturerIdStr)) {
            if (InputValidator.isValidId(lecturerIdStr)) {
                course.setLecturerId(lecturerIdStr);
            } else {
                String newLecturerId = getValidIdInput("Invalid lecturer ID. Re-enter lecturer ID (Enter to keep unchanged): ", true);
                if (newLecturerId != null) {
                    course.setLecturerId(newLecturerId);
                }
            }
        }

        System.out.print("Department ID [" + course.getDepartmentId() + "]: ");
        String departmentIdStr = scanner.nextLine();
        if (!InputValidator.isEmpty(departmentIdStr)) {
            if (InputValidator.isValidId(departmentIdStr)) {
                course.setDepartmentId(departmentIdStr);
            } else {
                String newDepartmentId = getValidIdInput("Invalid department ID. Re-enter department ID (Enter to keep unchanged): ", true);
                if (newDepartmentId != null) {
                    course.setDepartmentId(newDepartmentId);
                }
            }
        }

        System.out.print("Semester ID [" + course.getSemesterId() + "]: ");
        String semesterIdStr = scanner.nextLine();
        if (!InputValidator.isEmpty(semesterIdStr)) {
            if (InputValidator.isValidId(semesterIdStr)) {
                course.setSemesterId(semesterIdStr);
            } else {
                String newSemesterId = getValidIdInput("Invalid semester ID. Re-enter semester ID (Enter to keep unchanged): ", true);
                if (newSemesterId != null) {
                    course.setSemesterId(newSemesterId);
                }
            }
        }

        System.out.print("Maximum students [" + course.getMaxStudents() + "]: ");
        String maxStudentsStr = scanner.nextLine();
        if (!InputValidator.isEmpty(maxStudentsStr)) {
            int maxStudents;
            try {
                maxStudents = Integer.parseInt(maxStudentsStr);
                if (maxStudents > 0) {
                    course.setMaxStudents(maxStudents);
                } else {
                    maxStudents = getValidMaxStudentsInput("Invalid number. Re-enter maximum students (Enter to keep unchanged): ");
                    if (maxStudents != -1) {
                        course.setMaxStudents(maxStudents);
                    }
                }
            } catch (NumberFormatException e) {
                maxStudents = getValidMaxStudentsInput("Invalid format. Re-enter maximum students (Enter to keep unchanged): ");
                if (maxStudents != -1) {
                    course.setMaxStudents(maxStudents);
                }
            }
        }

        if (courseRepository.updateCourse(course)) {
            System.out.println("Course information updated successfully!");
        } else {
            System.out.println("Error updating course information!");
        }
    }

    /**
     * Delete a course
     */
    public void deleteCourse() {
        System.out.println("\n=== DELETE COURSE ===");

        String courseId = getValidIdInput("Enter course ID to delete: ", true);
        if (courseId == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        Course course = courseRepository.findById(courseId);
        if (course == null) {
            System.out.println("No course found with ID " + courseId);
            return;
        }

        System.out.println("Course information to be deleted:");
        System.out.println(course);

        System.out.print("Are you sure you want to delete? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.toLowerCase().equals("y") || confirm.toLowerCase().equals("yes")) {
            if (courseRepository.deleteCourse(courseId)) {
                System.out.println("Course deleted successfully!");
            } else {
                System.out.println("Error deleting course!");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    /**
     * View course information
     */
    public void viewCourse() {
        System.out.println("\n=== VIEW COURSE INFORMATION ===");

        String courseId = getValidIdInput("Enter course ID: ", true);
        if (courseId == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        Course course = courseRepository.findById(courseId);
        if (course != null) {
            System.out.println("\nCourse information:");
            System.out.println(course);
        } else {
            System.out.println("No course found with ID " + courseId);
        }
    }

    /**
     * List all courses
     */
    public void listAllCourses() {
        System.out.println("\n=== LIST ALL COURSES ===");

        List<Course> courses = courseRepository.findAll();
        if (courses.isEmpty()) {
            System.out.println("No courses found in the system.");
        } else {
            System.out.printf("%-10s %-30s %-8s %-15s %-15s %-15s %-10s %-10s%n",
                    "Course ID", "Course Name", "Credits", "Lecturer", "Department", "Semester", "Max", "Enrolled");
            System.out.println("-".repeat(120));

            for (Course course : courses) {
                System.out.printf("%-10s %-30s %-8s %-15s %-15s %-15s %-10s %-10s%n",
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getCredits(),
                        course.getLecturerId() != null ? course.getLecturerId() : "N/A",
                        course.getDepartmentId() != null ? course.getDepartmentId() : "N/A",
                        course.getSemesterId() != null ? course.getSemesterId() : "N/A",
                        course.getMaxStudents(),
                        course.getEnrolledStudents());
            }
        }
    }

    /**
     * Search courses by name
     */
    public void searchCoursesByName() {
        System.out.println("\n=== SEARCH COURSES BY NAME ===");

        String name = getValidInput("Enter course name to search: ", InputValidator::isValidName, false);
        if (name == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        List<Course> courses = courseRepository.findByName(name);
        if (courses.isEmpty()) {
            System.out.println("No courses found with name: " + name);
        } else {
            System.out.println("\nSearch results:");
            System.out.printf("%-10s %-30s %-8s %-15s %-15s %-15s %-10s %-10s%n",
                    "Course ID", "Course Name", "Credits", "Lecturer", "Department", "Semester", "Max", "Enrolled");
            System.out.println("-".repeat(120));

            for (Course course : courses) {
                System.out.printf("%-10s %-30s %-8s %-15s %-15s %-15s %-10s %-10s%n",
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getCredits(),
                        course.getLecturerId() != null ? course.getLecturerId() : "N/A",
                        course.getDepartmentId() != null ? course.getDepartmentId() : "N/A",
                        course.getSemesterId() != null ? course.getSemesterId() : "N/A",
                        course.getMaxStudents(),
                        course.getEnrolledStudents());
            }
        }
    }

    /**
     * Search courses by lecturer
     */
    public void searchCoursesByLecturer() {
        System.out.println("\n=== SEARCH COURSES BY LECTURER ===");

        String lecturerId = getValidIdInput("Enter lecturer ID: ", true);
        if (lecturerId == null) {
            System.out.println("Operation cancelled.");
            return;
        }

        List<Course> courses = courseRepository.findByLecturer(lecturerId);
        if (courses.isEmpty()) {
            System.out.println("No courses found for lecturer " + lecturerId);
        } else {
            System.out.println("\nCourses taught by lecturer " + lecturerId + ":");
            System.out.printf("%-10s %-30s %-8s %-15s %-15s %-10s %-10s%n",
                    "Course ID", "Course Name", "Credits", "Department", "Semester", "Max", "Enrolled");
            System.out.println("-".repeat(110));

            for (Course course : courses) {
                System.out.printf("%-10s %-30s %-8s %-15s %-15s %-10s %-10s%n",
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getCredits(),
                        course.getDepartmentId() != null ? course.getDepartmentId() : "N/A",
                        course.getSemesterId() != null ? course.getSemesterId() : "N/A",
                        course.getMaxStudents(),
                        course.getEnrolledStudents());
            }
        }
    }

    /**
     * Get valid ID input from user
     *
     * @param prompt    Prompt to display
     * @param allowNull Allow empty input (Enter to skip)
     * @return Valid ID or null if allowed and skipped
     */
    private String getValidIdInput(String prompt, boolean allowNull) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (allowNull && InputValidator.isEmpty(input)) {
                return null;
            }

            if (InputValidator.isValidId(input)) {
                return input;
            } else {
                System.out.println("ID must be 2-20 characters and contain only letters or numbers!");
            }
        }
    }

    /**
     * Get valid input with custom validator
     *
     * @param prompt    Prompt to display
     * @param validator Validation function
     * @param allowNull Allow empty input (Enter to skip)
     * @return Valid input or null if allowed and skipped
     */
    private String getValidInput(String prompt, java.util.function.Function<String, Boolean> validator, boolean allowNull) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (allowNull && InputValidator.isEmpty(input)) {
                return null;
            }

            if (InputValidator.isEmpty(input)) {
                System.out.println("Input cannot be empty!");
                continue;
            }

            if (validator.apply(input)) {
                return input;
            } else {
                System.out.println("Invalid input! Please try again.");
            }
        }
    }

    /**
     * Get valid credits input from user
     *
     * @param prompt Prompt to display
     * @return Valid credits or -1 if cancelled
     */
    private int getValidCreditsInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (InputValidator.isEmpty(input)) {
                System.out.println("Credits cannot be empty!");
                continue;
            }

            try {
                int credits = Integer.parseInt(input);
                if (credits >= 1 && credits <= 6) {
                    return credits;
                } else {
                    System.out.println("Credits must be between 1 and 6!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Credits must be an integer!");
            }
        }
    }

    /**
     * Get valid maximum students input from user
     *
     * @param prompt Prompt to display
     * @return Valid max students or -1 if cancelled
     */
    private int getValidMaxStudentsInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (InputValidator.isEmpty(input)) {
                System.out.println("Maximum students cannot be empty!");
                continue;
            }

            try {
                int maxStudents = Integer.parseInt(input);
                if (maxStudents > 0) {
                    return maxStudents;
                } else {
                    System.out.println("Maximum students must be greater than 0!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Maximum students must be an integer!");
            }
        }
    }
}