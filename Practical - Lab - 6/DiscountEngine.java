// File: DiscountRule.java
package discount.rule;


// Functional interface for applying a discount rule to a price
@FunctionalInterface
public interface DiscountRule {
    double apply(double price);
}

// File: DiscountEngine.java
package discount.app;

import discount.rule.DiscountRule;
import java.util.Scanner;

public class DiscountEngine {
    public static void main(String[] args) {
        double[] prices = { 100.0, 250.0, 500.0, 1200.0 };

        System.out.println("=== Discount Engine ===");
        System.out.println("Available Discount Rules:");
        System.out.println("1. Festival Sale (10% OFF)");
        System.out.println("2. Student Discount (20% OFF)");
        System.out.println("3. Flat Rs. 50 OFF");
        System.out.print("Select discount rule (1-3): ");

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        // Selecting a lambda discount rule based on user runtime choice
        DiscountRule rule = switch (choice) {
            case 1 -> price -> price * 0.90;
            case 2 -> price -> price * 0.80;
            case 3 -> price -> Math.max(0.0, price - 50.0);
            default -> {
                System.out.println("Invalid choice, applying no discount.");
                yield price -> price;
            }
        };

        System.out.println("\n--- Price Calculation Results ---");
        for (double p : prices) {
            double finalPrice = rule.apply(p);
            System.out.println("Original: Rs. " + p + " -> Discounted: Rs. " + finalPrice);
        }
        sc.close();
    }
}