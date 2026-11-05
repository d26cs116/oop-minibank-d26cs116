// Generic single-item container providing type safety without casting
class Box<T> {
    private T item;

    // Sets item of generic type T
    public void set(T item) {
        this.item = item;
    }

    // Gets item of generic type T without explicit casting
    public T get() {
        return item;
    }
}

// Generic two-element pair container holding types A and B
class Pair<A, B> {
    private final A first;
    private final B second;

    // Constructor to initialize pair of two distinct generic types
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}

public class GenericContainersDemo {
    // Generic method that accepts an array of any reference type E and returns its first element
    public static <E> E getFirstElement(E[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[0]; // Type-safe return without downcasting
    }

    public static void main(String[] args) {
        System.out.println("=== Generic Containers (Box<T>, Pair<A,B>) & Generic Methods ===");

        // 1. Box<T> with String type parameter
        Box<String> stringBox = new Box<>();
        stringBox.set("Hello Java Generics");
        String message = stringBox.get(); // Compile-time type safety: no cast needed
        System.out.println("\n1. Box<String> Value : " + message);

        // 2. Box<T> with Integer type parameter
        Box<Integer> intBox = new Box<>();
        intBox.set(1001);
        Integer number = intBox.get();
        System.out.println("   Box<Integer> Value: " + number);

        // 3. Pair<A, B> holding String and Double
        Pair<String, Double> productPrice = new Pair<>("Laptop", 74999.50);
        System.out.println("\n2. Pair<String, Double> : " + productPrice);
        System.out.println("   Product Name (A)     : " + productPrice.getFirst());
        System.out.println("   Price in INR (B)     : " + productPrice.getSecond());

        // 4. Pair<A, B> holding Integer and Boolean (e.g. error code and status)
        Pair<Integer, Boolean> statusPair = new Pair<>(200, true);
        System.out.println("   Pair<Integer, Boolean>: " + statusPair);

        // 5. Generic method getFirstElement demonstrated on different array types
        String[] fruits = { "Apple", "Banana", "Cherry" };
        Integer[] scores = { 98, 85, 77, 92 };
        Double[] weights = { 12.5, 45.2, 33.8 };

        System.out.println("\n3. Generic Method getFirstElement():");
        System.out.println("   First fruit (String) : " + getFirstElement(fruits));
        System.out.println("   First score (Integer): " + getFirstElement(scores));
        System.out.println("   First weight (Double): " + getFirstElement(weights));
    }
}

