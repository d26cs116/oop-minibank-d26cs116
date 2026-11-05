import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

// Custom annotation to check that a string field is not null or blank
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
    String message() default "Field cannot be blank";
}

// Custom annotation to enforce maximum allowed string length
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
    String message() default "Field exceeds maximum allowed length";
}

// Form class representing user registration details with validation rules
class SignupForm {
    @NotBlank(message = "Username is required")
    @MaxLength(value = 10, message = "Username cannot exceed 10 characters")
    private String username;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @MaxLength(value = 8, message = "Password cannot exceed 8 characters")
    private String password;

    public SignupForm(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

// Validator engine using Java Reflection to inspect fields and annotations
public class FormValidatorDemo {

    public static List<String> validate(Object obj) {
        List<String> errors = new ArrayList<>();
        Class<?> clazz = obj.getClass();

        // Reading all declared fields via reflection
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // Allow access to private fields
            field.setAccessible(true);
            try {
                Object value = field.get(obj);

                // Checking @NotBlank annotation
                if (field.isAnnotationPresent(NotBlank.class)) {
                    NotBlank annotation = field.getAnnotation(NotBlank.class);
                    if (value == null || value.toString().trim().isEmpty()) {
                        errors.add(field.getName() + ": " + annotation.message());
                    }
                }

                // Checking @MaxLength annotation
                if (field.isAnnotationPresent(MaxLength.class)) {
                    MaxLength annotation = field.getAnnotation(MaxLength.class);
                    if (value != null && value.toString().length() > annotation.value()) {
                        errors.add(field.getName() + ": " + annotation.message() + " (Max: " + annotation.value() + ")");
                    }
                }
            } catch (IllegalAccessException e) {
                System.out.println("Error reading field: " + e.getMessage());
            }
        }
        return errors;
    }

    public static void main(String[] args) {
        System.out.println("=== Testing Valid Signup Form ===");
        SignupForm validForm = new SignupForm("Niraj123", "niraj@gmail.com", "pass123");
        List<String> validErrors = validate(validForm);
        if (validErrors.isEmpty()) {
            System.out.println("Form is valid! No validation errors found.");
        } else {
            validErrors.forEach(System.out::println);
        }

        System.out.println("\n=== Testing Invalid Signup Form ===");
        SignupForm invalidForm = new SignupForm("VeryLongUsernameExceedingLimit", "", "ExtremelyLongPassword123!");
        List<String> invalidErrors = validate(invalidForm);
        System.out.println("Validation errors encountered (" + invalidErrors.size() + "):");
        for (String err : invalidErrors) {
            System.out.println("- " + err);
        }
    }
}

