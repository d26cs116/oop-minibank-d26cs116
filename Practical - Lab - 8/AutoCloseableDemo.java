// Custom resource modeling a secure database connection implementing AutoCloseable
class DatabaseConnection implements AutoCloseable {
    private final String connectionId;
    private boolean open;

    public DatabaseConnection(String connectionId) {
        this.connectionId = connectionId;
        this.open = true;
        System.out.println("[Resource Opened] Database connection '" + connectionId + "' established.");
    }

    public void executeQuery(String sql) throws Exception {
        if (!open) {
            throw new IllegalStateException("Connection is closed.");
        }
        System.out.println("Executing SQL Query: \"" + sql + "\" on " + connectionId);

        // Simulate an intentional failure during query execution
        if (sql.contains("INVALID_TABLE")) {
            throw new Exception("SQLError: Table 'INVALID_TABLE' does not exist in schema!");
        }
        System.out.println("Query executed successfully.");
    }

    // close() method mandated by AutoCloseable interface
    @Override
    public void close() {
        if (open) {
            open = false;
            System.out.println("[Resource Closed] Connection '" + connectionId + "' safely closed and released.");
        }
    }
}

public class AutoCloseableDemo {
    public static void main(String[] args) {
        System.out.println("=== Case 1: Normal Execution with Try-With-Resources ===");
        // The resource is opened inside try() and closed automatically at block exit
        try (DatabaseConnection conn = new DatabaseConnection("DB-POOL-01")) {
            conn.executeQuery("SELECT * FROM Customers WHERE active = true");
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        System.out.println("\n=== Case 2: Exception Thrown Inside Block ===");
        // Proves that close() runs even when an exception is thrown inside try block
        try (DatabaseConnection conn = new DatabaseConnection("DB-POOL-02")) {
            conn.executeQuery("SELECT * FROM INVALID_TABLE");
            System.out.println("This statement will not be reached due to exception.");
        } catch (Exception e) {
            // Proves that the original error thrown inside the try block is the one caught and reported
            System.out.println("[Caught Original Exception] " + e.getMessage());
        }

        System.out.println("\nDemonstration complete: AutoCloseable ensures resources are never leaked.");
    }
}
