import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Practical07Demo {

    // Custom Annotations
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Id {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface MaxLength {
        int value();
        String message() default "Value exceeds maximum allowed length";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Positive {
        String message() default "Value must be positive";
    }

    // Reflection-based Validator
    static class AnnotationValidator {
        public static String[] validate(Object target) {
            if (target == null) return new String[0];
            List<String> errors = new ArrayList<>();
            Class<?> clazz = target.getClass();

            while (clazz != null && clazz != Object.class) {
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        Object val = field.get(target);

                        if (field.isAnnotationPresent(MaxLength.class)) {
                            MaxLength ml = field.getAnnotation(MaxLength.class);
                            if (val != null && val.toString().length() > ml.value()) {
                                errors.add("Field '" + field.getName() + "': " + ml.message() + " (max: " + ml.value() + ")");
                            }
                        }

                        if (field.isAnnotationPresent(Positive.class)) {
                            Positive pos = field.getAnnotation(Positive.class);
                            if (val instanceof Number num) {
                                if (num.doubleValue() <= 0) {
                                    errors.add("Field '" + field.getName() + "': " + pos.message() + " (actual: " + val + ")");
                                }
                            }
                        }
                    } catch (IllegalAccessException ignored) {}
                }
                clazz = clazz.getSuperclass();
            }
            return errors.toArray(new String[0]);
        }
    }

    static class SavingsAccount {
        @Id
        private String accountNumber = "AC0001";

        @MaxLength(30)
        private String ownerName;

        @Positive(message = "Initial balance must be positive")
        private long balance;

        public SavingsAccount(String ownerName, long balance) {
            this.ownerName = ownerName;
            this.balance = balance;
        }
    }

    static class UserProfile {
        @Id
        private String userId;

        @MaxLength(10)
        private String username;

        @Positive(message = "Age must be strictly greater than 0")
        private int age;

        @Positive(message = "Salary cannot be negative")
        private double salary;

        public UserProfile(String userId, String username, int age, double salary) {
            this.userId = userId;
            this.username = username;
            this.age = age;
            this.salary = salary;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PRACTICAL 07: Custom Annotations & Reflection-Based Validation ===");

        // 1. Valid Account validation
        System.out.println("\n--- 1. Validating Valid Domain Entity (SavingsAccount) ---");
        SavingsAccount validAcc = new SavingsAccount("Arshit Vaghela", 15000);
        String[] violations1 = AnnotationValidator.validate(validAcc);
        System.out.println("Violations found for valid SavingsAccount: " + violations1.length);

        // 2. Testing Entity with Constraint Violations
        System.out.println("\n--- 2. Validating Entity with @Positive and @MaxLength Violations ---");
        UserProfile invalidUser = new UserProfile("USR001", "ExtremelyLongUserNameHere", -5, -2500.50);
        String[] violations2 = AnnotationValidator.validate(invalidUser);

        System.out.println("Violations detected (" + violations2.length + "):");
        for (String v : violations2) {
            System.out.println(" [Violation] " + v);
        }

        // 3. Testing Valid User Profile
        System.out.println("\n--- 3. Validating Correctly Formatted Entity ---");
        UserProfile validUser = new UserProfile("USR002", "ArshitV", 20, 75000.0);
        String[] violations3 = AnnotationValidator.validate(validUser);
        System.out.println("Violations detected for valid UserProfile: " + violations3.length);
        System.out.println("Entity is completely valid and accepted!");
    }
}
