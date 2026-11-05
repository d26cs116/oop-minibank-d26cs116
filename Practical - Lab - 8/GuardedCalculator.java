import java.util.InputMismatchException;
import java.util.Scanner;

// Custom checked exception thrown when division by zero is attempted
class DivideByZeroException extends Exception {
    public DivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {

    // Method to perform calculation, throws custom DivideByZeroException if divisor is zero
    public static double calculate(double a, double b, char op) throws DivideByZeroException {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) {
                    throw new DivideByZeroException("Error: Cannot divide by zero!");
                }
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean success = false;
        int attempt = 1;

        System.out.println("=== Guarded Calculator ===");

        // Loop runs until a calculation succeeds
        while (!success) {
            System.out.println("\n--- Attempt #" + attempt + " ---");
            try {
                System.out.print("Enter first number: ");
                double num1 = sc.nextDouble();

                System.out.print("Enter operator (+, -, *, /): ");
                char op = sc.next().charAt(0);

                System.out.print("Enter second number: ");
                double num2 = sc.nextDouble();

                double result = calculate(num1, num2, op);
                System.out.println("Result: " + num1 + " " + op + " " + num2 + " = " + result);
                success = true; // exits loop on valid calculation

            } catch (InputMismatchException e) {
                System.out.println("InvalidInputError: Please enter valid numeric values!");
                sc.nextLine(); // clear buffer
            } catch (DivideByZeroException e) {
                System.out.println("MathError: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("OperatorError: " + e.getMessage());
            } finally {
                // finally block executes on every attempt regardless of exceptions
                System.out.println("[Log] Attempt #" + attempt + " completed and logged.");
                attempt++;
            }
        }
        System.out.println("\nCalculator session terminated successfully.");
        sc.close();
    }
}