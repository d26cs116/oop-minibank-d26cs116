import java.util.HashMap;
import java.util.Map;

// Custom checked exception thrown when requested quantity exceeds available stock
class OutOfStockException extends Exception {
    private final int shortfall;

    public OutOfStockException(String item, int requested, int available) {
        super("OutOfStock: Item '" + item + "' requested: " + requested + ", available: " + available);
        this.shortfall = requested - available;
    }

    public int getShortfall() {
        return shortfall;
    }
}

// Custom checked exception thrown when requested quantity is less than or equal to 0
class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String message) {
        super(message);
    }
}

// Warehouse class managing item inventory and stock issuing
class Warehouse {
    private final Map<String, Integer> inventory = new HashMap<>();

    public void addStock(String item, int qty) {
        inventory.put(item, inventory.getOrDefault(item, 0) + qty);
    }

    public int getAvailableStock(String item) {
        return inventory.getOrDefault(item, 0);
    }

    // Method to issue inventory items with custom exception handling
    public void issue(String item, int qty) throws OutOfStockException, InvalidQuantityException {
        if (qty <= 0) {
            throw new InvalidQuantityException("InvalidQuantityError: Quantity must be > 0 (Requested: " + qty + ")");
        }

        int available = inventory.getOrDefault(item, 0);
        if (qty > available) {
            throw new OutOfStockException(item, qty, available);
        }

        // Deduct quantity from inventory
        inventory.put(item, available - qty);
        System.out.println("Success: Issued " + qty + " units of '" + item + "'. Remaining: " + (available - qty));
    }
}

// Record representing an issue request
record IssueRequest(String item, int quantity) {}

public class StockIssueDemo {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        warehouse.addStock("Laptop", 5);
        warehouse.addStock("Keyboard", 10);
        warehouse.addStock("Mouse", 8);

        System.out.println("=== Warehouse Stock Issue Processing ===");
        System.out.println("Initial Stock: Laptop: 5, Keyboard: 10, Mouse: 8\n");

        IssueRequest[] requests = {
            new IssueRequest("Laptop", 2),      // Valid: leaves 3
            new IssueRequest("Keyboard", -4),   // Invalid quantity <= 0
            new IssueRequest("Laptop", 7),      // Out of stock (wants 7, only 3 left, shortfall = 4)
            new IssueRequest("Mouse", 5),       // Valid: leaves 3
            new IssueRequest("Keyboard", 12)    // Out of stock (wants 12, 10 left, shortfall = 2)
        };

        for (IssueRequest req : requests) {
            System.out.println("Processing Request: " + req.quantity() + "x " + req.item());
            try {
                warehouse.issue(req.item(), req.quantity());
            } catch (InvalidQuantityException e) {
                // Catching invalid quantity exception
                System.out.println("  [ERROR] " + e.getMessage());
            } catch (OutOfStockException e) {
                // Catching out of stock exception and reporting shortfall
                System.out.println("  [ERROR] " + e.getMessage());
                System.out.println("  -> Shortfall: " + e.getShortfall() + " units required to fulfill order.");
            }
            System.out.println();
        }

        System.out.println("All requests processed without crashing the warehouse application.");
    }
}
