
import java.io.File;
import java.io.IOException;

public class StudentServiceTest {

    // Test 1: Verify adding students increases count
    public void testAddStudentIncreasesCount() {
        StudentService service = new StudentService();

        check(service.getStudentCount() == 0,
                "Initial count must be 0");

        service.addStudent(new Student(
                "D26CS116", "Arshit Vaghela", "CSE", 9.45));

        check(service.getStudentCount() == 1,
                "Count should be 1 after first add");

        service.addStudent(new Student(
                "D26CE164", "Divya Shukla", "CSE", 8.80));

        check(service.getStudentCount() == 2,
                "Count should be 2 after second add");
    }

    // Test 2: Verify null and duplicate students are rejected
    public void testInvalidStudentThrowsException() {
        StudentService service = new StudentService();

        service.addStudent(new Student(
                "D26CS116", "Arshit Vaghela", "CSE", 9.45));

        boolean nullException = false;

        try {
            service.addStudent(null);
        } catch (IllegalArgumentException e) {
            nullException = true;
        }

        check(nullException,
                "Adding null student must throw exception");

        boolean duplicateException = false;

        try {
            service.addStudent(new Student(
                    "D26CS116", "Another Student", "IT", 8.50));
        } catch (IllegalArgumentException e) {
            duplicateException = true;
        }

        check(duplicateException,
                "Duplicate student ID must throw exception");
    }

    // Test 3: Verify saving and loading student data
    public void testSaveAndReloadDataUnchanged()
            throws IOException, ClassNotFoundException {

        StudentService service = new StudentService();

        service.addStudent(new Student(
                "D26CS116", "Arshit Vaghela", "CSE", 9.45));

        service.addStudent(new Student(
                "D26IT205", "Diya Joshi", "IT", 9.15));

        String testFile = "test_students_store.dat";

        try {
            service.saveData(testFile);

            StudentService freshService = new StudentService();

            check(freshService.getStudentCount() == 0,
                    "Fresh service must have 0 students");

            freshService.loadData(testFile);

            check(freshService.getStudentCount() == 2,
                    "Loaded count should be 2");

            Student s1 = freshService.findById("D26CS116");

            check(s1 != null,
                    "Arshit Vaghela must exist");

            check(s1.getName().equals("Arshit Vaghela"),
                    "Student name must match");

            check(Math.abs(s1.getCgpa() - 9.45) < 0.001,
                    "CGPA must match");

            Student s2 = freshService.findById("D26IT205");

            check(s2 != null,
                    "Diya Joshi must exist");

            check(s2.getName().equals("Diya Joshi"),
                    "Student name must match");

            check(Math.abs(s2.getCgpa() - 9.15) < 0.001,
                    "CGPA must match");

        } finally {
            new File(testFile).delete();
        }
    }

    // Simple assertion method without JUnit
    private void check(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException(message);
        }
    }

    // Custom test runner
    public static void main(String[] args) {

        System.out.println(
                "=== StudentServiceTest Runner ===");

        int passed = 0;
        int failed = 0;

        StudentServiceTest test = new StudentServiceTest();

        try {
            System.out.print(
                    "testAddStudentIncreasesCount() ... ");

            test.testAddStudentIncreasesCount();

            System.out.println("[PASSED]");
            passed++;

        } catch (Exception e) {
            System.out.println("[FAILED]");
            System.out.println("Reason: " + e.getMessage());
            failed++;
        }

        try {
            System.out.print(
                    "testInvalidStudentThrowsException() ... ");

            test.testInvalidStudentThrowsException();

            System.out.println("[PASSED]");
            passed++;

        } catch (Exception e) {
            System.out.println("[FAILED]");
            System.out.println("Reason: " + e.getMessage());
            failed++;
        }

        try {
            System.out.print(
                    "testSaveAndReloadDataUnchanged() ... ");

            test.testSaveAndReloadDataUnchanged();

            System.out.println("[PASSED]");
            passed++;

        } catch (Exception e) {
            System.out.println("[FAILED]");
            System.out.println("Reason: " + e.getMessage());
            failed++;
        }

        System.out.println("\n------------------------------");
        System.out.println("Total Tests : " + (passed + failed));
        System.out.println("Passed      : " + passed);
        System.out.println("Failed      : " + failed);

        if (failed == 0) {
            System.out.println(
                    "ALL TESTS PASSED SUCCESSFULLY");
        } else {
            System.out.println("TEST FAILURES DETECTED");
        }

        System.out.println("------------------------------");
    }
}
