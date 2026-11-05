import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

// Custom @Column annotation mapping class fields to CSV column headers
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name();
}

// Student entity representing a record mapped from CSV data
class StudentRecord {
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "cgpa")
    private double cgpa;

    // Field without @Column annotation or optional
    private String remarks;

    // Default no-argument constructor required for reflection instantiation
    public StudentRecord() {}

    @Override
    public String toString() {
        return "StudentRecord [ID=" + id + ", Name=" + name + ", Email=" + email + 
               ", CGPA=" + cgpa + ", Remarks=" + remarks + "]";
    }
}

// Reflection-based CSV data binder
public class CsvColumnMapper {

    public static <T> T mapRow(Class<T> clazz, String[] headers, String[] data) {
        try {
            // Instantiate target object dynamically
            T instance = clazz.getDeclaredConstructor().newInstance();

            // Build map of column name -> value from CSV rows
            Map<String, String> dataMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                if (i < data.length) {
                    dataMap.put(headers[i].trim().toLowerCase(), data[i].trim());
                }
            }

            // Inspect all fields using reflection
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column col = field.getAnnotation(Column.class);
                    String colName = col.name().trim().toLowerCase();

                    field.setAccessible(true);

                    // Check if column exists in the CSV data
                    if (dataMap.containsKey(colName)) {
                        String rawValue = dataMap.get(colName);
                        Class<?> type = field.getType();

                        // Type conversion for common primitive/String types
                        if (type == int.class || type == Integer.class) {
                            field.set(instance, Integer.parseInt(rawValue));
                        } else if (type == double.class || type == Double.class) {
                            field.set(instance, Double.parseDouble(rawValue));
                        } else if (type == String.class) {
                            field.set(instance, rawValue);
                        }
                    } else {
                        // Handling missing columns gracefully
                        System.out.println("Notice: Column '" + col.name() + "' missing in CSV; leaving default value for field " + field.getName());
                    }
                }
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Mapping error: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== CSV Column Mapper using Reflection ===");

        // Test Case 1: Complete CSV record matching all columns
        String[] headers1 = { "id", "name", "email", "cgpa" };
        String[] row1 = { "116", "Arshit", "d26cs116@charusat.edu.in", "9.45" };

        System.out.println("\nMapping complete record:");
        StudentRecord student1 = mapRow(StudentRecord.class, headers1, row1);
        System.out.println("Mapped Object: " + student1);

        // Test Case 2: Record with missing 'cgpa' column
        String[] headers2 = { "id", "name", "email" };
        String[] row2 = { "128", "Pathu", "meet@charusat.edu.in" };

        System.out.println("\nMapping record with missing 'cgpa' column:");
        StudentRecord student2 = mapRow(StudentRecord.class, headers2, row2);
        System.out.println("Mapped Object: " + student2);
    }
}

