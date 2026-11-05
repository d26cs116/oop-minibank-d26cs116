import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

// Custom @Run annotation to mark test methods for reflection-based execution
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {}

// Sample test suite class containing test cases and non-test helper methods
class CalculatorTestSuite {

    @Run
    public void testAddition() {
        int a = 10;
        int b = 20;
        int result = a + b;
        System.out.println("  [PASS] testAddition: 10 + 20 = " + result);
    }

    @Run
    public void testSubtraction() {
        int a = 50;
        int b = 15;
        int result = a - b;
        System.out.println("  [PASS] testSubtraction: 50 - 15 = " + result);
    }

    // Regular helper method without @Run annotation, must NOT be executed by the test runner
    public void utilityHelper() {
        System.out.println("  [HELPER] This helper should not be executed.");
    }

    @Run
    public void testMultiplication() {
        int a = 6;
        int b = 7;
        int result = a * b;
        System.out.println("  [PASS] testMultiplication: 6 * 7 = " + result);
    }
}

// Mini test runner demonstrating how testing frameworks like JUnit work using reflection
public class MiniTestRunner {
    public static void main(String[] args) {
        System.out.println("=== Starting Mini Test Runner ===");
        Class<?> testClass = CalculatorTestSuite.class;

        int executedCount = 0;

        try {
            // Creating an instance of the test class to invoke methods on
            Object testInstance = testClass.getDeclaredConstructor().newInstance();

            // Fetching all declared methods using reflection
            Method[] methods = testClass.getDeclaredMethods();

            for (Method method : methods) {
                // Checking if the method is annotated with @Run
                if (method.isAnnotationPresent(Run.class)) {
                    System.out.println("Executing test method: " + method.getName() + "()");
                    // Invoking the test method dynamically via reflection
                    method.invoke(testInstance);
                    executedCount++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error during test execution: " + e.getMessage());
        }

        System.out.println("----------------------------------------");
        System.out.println("Test Run Complete. Total @Run methods executed: " + executedCount);
    }
}

