import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Model Layer: Student entity representing learner details
class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String department;
    private double cgpa;

    public Student(String id, String name, String department, double cgpa) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty.");
        }
        if (cgpa < 0.0 || cgpa > 10.0) {
            throw new IllegalArgumentException("CGPA must be between 0.0 and 10.0.");
        }
        this.id = id;
        this.name = name;
        this.department = department;
        this.cgpa = cgpa;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getCgpa() {
        return cgpa;
    }

    @Override
    public String toString() {
        return String.format("Student[ID=%s, Name=%-14s, Dept=%-5s, CGPA=%.2f]",
                id, name, department, cgpa);
    }
}

// Service Layer: Manages student business logic and in-memory ArrayList store
class StudentService {
    // Internal repository backed by ArrayList
    private final List<Student> studentList = new ArrayList<>();

    // Adds a student with validation; throws exception on null or duplicate ID
    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Cannot add null student.");
        }
        if (findById(student.getId()) != null) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " already exists.");
        }
        studentList.add(student);
    }

    // Finds a student by unique ID
    public Student findById(String id) {
        for (Student s : studentList) {
            if (s.getId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    // Returns unmodifiable view of all registered students
    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(studentList);
    }

    // Returns total student count
    public int getStudentCount() {
        return studentList.size();
    }

    // Saves student list to disk using ObjectOutputStream
    public void saveData(String filepath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))) {
            oos.writeObject(new ArrayList<>(studentList));
        }
    }

    // Loads student list from disk using ObjectInputStream
    @SuppressWarnings("unchecked")
    public void loadData(String filepath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))) {
            List<Student> loaded = (List<Student>) ois.readObject();
            studentList.clear();
            studentList.addAll(loaded);
        }
    }
}

// User Interface / Presentation Layer: Menu-driven application entry point
public class StudentApp {
    public static void main(String[] args) {
        System.out.println("=== Layered Student Management Application ===");
        StudentService service = new StudentService();

        // 1. Simulating menu action: Add new students
        System.out.println("\n--- [Menu Choice 1] Adding Students via Service Layer ---");
        Student s1 = new Student("D26CS116", "Arshit Vaghela", "CSE", 9.45);
        Student s2 = new Student("D26CE164", "Divya Shukla", "CSE", 8.80);
        Student s3 = new Student("D26IT205", "Diya Joshi", "IT", 9.15);

        service.addStudent(s1);
        service.addStudent(s2);
        service.addStudent(s3);
        System.out.println("Added: " + s1);
        System.out.println("Added: " + s2);
        System.out.println("Added: " + s3);

        // 2. Simulating menu action: View all students
        System.out.println("\n--- [Menu Choice 2] Displaying All Students ---");
        System.out.println("Total Students Registered: " + service.getStudentCount());
        for (Student s : service.getAllStudents()) {
            System.out.println("  " + s);
        }

        // 3. Simulating menu action: Search student by ID
        System.out.println("\n--- [Menu Choice 3] Lookup Student by ID ---");
        String searchId = "D26CS116";
        Student found = service.findById(searchId);
        if (found != null) {
            System.out.println("Found Record for " + searchId + ": " + found.getName() + " (CGPA: " + found.getCgpa() + ")");
        } else {
            System.out.println("Student ID not found: " + searchId);
        }

        // 4. Demonstrating exception handling on invalid action
        System.out.println("\n--- [Validation Check] Attempting to Add Duplicate Student ID ---");
        try {
            service.addStudent(new Student("D26CS116", "Duplicate Arshit", "CSE", 9.00));
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught Expected Exception: " + e.getMessage());
        }
    }
}
