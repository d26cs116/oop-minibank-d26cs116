// File: DiscountRule.java
package discount.rule;

// Functional interface for applying a discount rule to a price
@FunctionalInterface
public interface DiscountRule {
    double apply(double price);
}

