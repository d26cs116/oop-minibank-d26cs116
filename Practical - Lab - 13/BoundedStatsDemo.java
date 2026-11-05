import java.util.Arrays;

public class BoundedStatsDemo {
    // Bounded generic method: type parameter N is restricted to Number or its subclasses
    // This allows calling methods of java.lang.Number (such as doubleValue()) safely
    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            return 0.0;
        }

        double sum = 0.0;
        for (N val : values) {
            // doubleValue() is guaranteed to exist because N extends Number
            sum += val.doubleValue();
        }
        return sum / values.length;
    }

    public static void main(String[] args) {
        System.out.println("=== Bounded Type Parameters Demo (<N extends Number>) ===");

        // 1. Array of Integer objects
        Integer[] integerArray = { 10, 25, 40, 55, 70 };
        double intAvg = average(integerArray);
        System.out.println("\n1. Integer Array : " + Arrays.toString(integerArray));
        System.out.printf("   Calculated Average: %.2f%n", intAvg);

        // 2. Array of Double objects
        Double[] doubleArray = { 12.5, 24.8, 36.2, 48.9 };
        double dblAvg = average(doubleArray);
        System.out.println("\n2. Double Array  : " + Arrays.toString(doubleArray));
        System.out.printf("   Calculated Average: %.2f%n", dblAvg);

        // 3. Array of Long objects
        Long[] longArray = { 100000L, 250000L, 500000L };
        double lngAvg = average(longArray);
        System.out.println("\n3. Long Array    : " + Arrays.toString(longArray));
        System.out.printf("   Calculated Average: %.2f%n", lngAvg);

        // 4. Array of Float objects
        Float[] floatArray = { 3.5f, 7.2f, 9.1f };
        double fltAvg = average(floatArray);
        System.out.println("\n4. Float Array   : " + Arrays.toString(floatArray));
        System.out.printf("   Calculated Average: %.2f%n", fltAvg);

        // Explanation of bounded type restrictions
        System.out.println("\n--- Why Non-Number Types Are Refused ---");
        System.out.println("  If we call average(new String[]{\"A\", \"B\"}), the compiler produces:");
        System.out.println("  'Error: inferred type does not conform to upper bound(s) [Number]'");
        System.out.println("  This compile-time safety prevents invoking arithmetic logic on non-numeric types.");
    }
}
