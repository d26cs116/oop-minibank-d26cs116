// Class representing a counter without synchronization (prone to race condition)
class UnsafeCounter {
    private int count = 0;

    // Non-synchronized increment: read-modify-write is not atomic
    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

// Class representing a thread-safe counter using synchronized method
class SafeCounter {
    private int count = 0;

    // Synchronized increment ensures only one thread updates count at a time
    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}

public class CounterRaceDemo {
    private static final int NUM_THREADS = 5;
    private static final int INCREMENTS_PER_THREAD = 2000;
    private static final int EXPECTED_TOTAL = NUM_THREADS * INCREMENTS_PER_THREAD; // 10,000

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Multithreaded Counter Race Condition Demonstration ===");
        System.out.println("Number of Threads: " + NUM_THREADS);
        System.out.println("Increments per Thread: " + INCREMENTS_PER_THREAD);
        System.out.println("Expected Final Total: " + EXPECTED_TOTAL);

        // --- Part 1: Without Synchronization (Race Condition) ---
        System.out.println("\n--- 1. Running Unsynchronized Counter ---");
        UnsafeCounter unsafeCounter = new UnsafeCounter();
        Thread[] unsafeThreads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    unsafeCounter.increment();
                }
            }, "UnsafeWorker-" + (i + 1));
            unsafeThreads[i].start();
        }

        // Waiting for all threads to finish
        for (Thread t : unsafeThreads) {
            t.join();
        }
        System.out.println("Actual Result (Without Sync): " + unsafeCounter.getCount());
        System.out.println("Notice: Count is less than expected due to race condition!");

        // --- Part 2: With Synchronization (Thread Safe) ---
        System.out.println("\n--- 2. Running Synchronized Counter ---");
        SafeCounter safeCounter = new SafeCounter();
        Thread[] safeThreads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            safeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    safeCounter.increment();
                }
            }, "SafeWorker-" + (i + 1));
            safeThreads[i].start();
        }

        // Waiting for all threads to finish
        for (Thread t : safeThreads) {
            t.join();
        }
        System.out.println("Actual Result (With Sync): " + safeCounter.getCount());
        System.out.println("Success: Count matches expected " + EXPECTED_TOTAL + " exactly!");
    }
}
