// Simple Account class used for resource locking demonstration
class BankAccount {
    private final int id;
    private final String name;
    private int balance;

    public BankAccount(int id, String name, int balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void adjust(int amount) {
        this.balance += amount;
    }
}

public class DeadlockDemo {

    // Method to transfer with opposite (inconsistent) lock order -> causes Deadlock
    public static void unsafeTransfer(BankAccount from, BankAccount to, int amount) {
        synchronized (from) {
            System.out.println("  " + Thread.currentThread().getName() + " locked " + from.getName());
            try {
                Thread.sleep(50); // delay forces circular wait condition
            } catch (InterruptedException ignored) {}

            System.out.println("  " + Thread.currentThread().getName() + " waiting to lock " + to.getName() + "...");
            synchronized (to) {
                from.adjust(-amount);
                to.adjust(amount);
                System.out.println("  " + Thread.currentThread().getName() + " transfer complete.");
            }
        }
    }

    // Method to transfer with consistent lock ordering (always lock lower ID first) -> avoids Deadlock
    public static void safeTransfer(BankAccount from, BankAccount to, int amount) {
        BankAccount firstLock = from.getId() < to.getId() ? from : to;
        BankAccount secondLock = from.getId() < to.getId() ? to : from;

        synchronized (firstLock) {
            System.out.println("  " + Thread.currentThread().getName() + " safely locked first: " + firstLock.getName());
            try {
                Thread.sleep(30);
            } catch (InterruptedException ignored) {}

            synchronized (secondLock) {
                System.out.println("  " + Thread.currentThread().getName() + " safely locked second: " + secondLock.getName());
                from.adjust(-amount);
                to.adjust(amount);
                System.out.println("  " + Thread.currentThread().getName() + " transfer of Rs. " + amount + " completed successfully.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Deadlock Reproduction and Resolution Demonstration ===\n");

        BankAccount acc1 = new BankAccount(101, "Account-A", 1000);
        BankAccount acc2 = new BankAccount(102, "Account-B", 1000);

        // --- Part 1: Reproducing Deadlock with Opposite Lock Ordering ---
        System.out.println("--- 1. Reproducing Deadlock (Opposite Lock Ordering) ---");
        Thread t1 = new Thread(() -> unsafeTransfer(acc1, acc2, 100), "TransferThread-1");
        Thread t2 = new Thread(() -> unsafeTransfer(acc2, acc1, 200), "TransferThread-2");

        // Mark as daemon threads so they automatically release when main completes
        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        // Safe watchdog timer: wait up to 500ms to detect circular deadlock
        t1.join(500);
        t2.join(500);

        if (t1.isAlive() && t2.isAlive()) {
            System.out.println("\n[DEADLOCK DETECTED]");
            System.out.println("TransferThread-1 holds Account-A and is waiting for Account-B.");
            System.out.println("TransferThread-2 holds Account-B and is waiting for Account-A.");
            System.out.println("Neither thread can proceed (circular wait). Proceeding to demonstration of fix...");
        }

        Thread.sleep(100);

        // --- Part 2: Deadlock Fixed Using Consistent Lock Ordering ---
        System.out.println("\n--- 2. Deadlock Fixed (Consistent Lower-ID First Lock Ordering) ---");
        BankAccount safeAcc1 = new BankAccount(101, "Account-A", 1000);
        BankAccount safeAcc2 = new BankAccount(102, "Account-B", 1000);

        Thread safeT1 = new Thread(() -> safeTransfer(safeAcc1, safeAcc2, 100), "SafeThread-1");
        Thread safeT2 = new Thread(() -> safeTransfer(safeAcc2, safeAcc1, 200), "SafeThread-2");

        safeT1.start();
        safeT2.start();

        safeT1.join();
        safeT2.join();

        System.out.println("\nFinal Balances: " + safeAcc1.getName() + " = " + safeAcc1.getBalance() + 
                           ", " + safeAcc2.getName() + " = " + safeAcc2.getBalance());
        System.out.println("Success: Both transfers finished without deadlocking!");
    }
}

