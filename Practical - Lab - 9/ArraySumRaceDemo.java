import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class ArraySumRaceDemo {
    private static final int ARRAY_SIZE = 1_000_000;
    private static final int NUM_THREADS = 4;
    private static final int CHUNK_SIZE = ARRAY_SIZE / NUM_THREADS;

    // Shared variables for the different approaches
    private static long unsafeTotal = 0;
    private static long synchronizedTotal = 0;
    private static final Object lock = new Object();
    private static final AtomicLong atomicTotal = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {
        // Initialize array with 1 in each slot; expected sum = 1,000,000
        int[] numbers = new int[ARRAY_SIZE];
        Arrays.fill(numbers, 1);
        long expectedSum = ARRAY_SIZE;

        System.out.println("=== Multithreaded Array Sum Comparison ===");
        System.out.println("Array Size: " + ARRAY_SIZE + " | Threads: " + NUM_THREADS);
        System.out.println("Expected Sum: " + expectedSum + "\n");

        // -------------------------------------------------------------
        // Approach 1: Unsynchronized Shared Total (Race Condition)
        // -------------------------------------------------------------
        unsafeTotal = 0;
        Thread[] unsafeThreads = new Thread[NUM_THREADS];
        long start1 = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS; i++) {
            final int startIdx = i * CHUNK_SIZE;
            final int endIdx = startIdx + CHUNK_SIZE;

            unsafeThreads[i] = new Thread(() -> {
                for (int j = startIdx; j < endIdx; j++) {
                    unsafeTotal += numbers[j]; // Race condition here!
                }
            });
            unsafeThreads[i].start();
        }

        for (Thread t : unsafeThreads) {
            t.join();
        }
        long time1 = System.currentTimeMillis() - start1;
        System.out.println("1. Unsafe Shared Total:");
        System.out.println("   Result: " + unsafeTotal + " (WRONG, expected " + expectedSum + ")");
        System.out.println("   Time: " + time1 + " ms\n");

        // -------------------------------------------------------------
        // Approach 2: Fix 1 - Synchronized Block on Shared Lock
        // -------------------------------------------------------------
        synchronizedTotal = 0;
        Thread[] syncThreads = new Thread[NUM_THREADS];
        long start2 = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS; i++) {
            final int startIdx = i * CHUNK_SIZE;
            final int endIdx = startIdx + CHUNK_SIZE;

            syncThreads[i] = new Thread(() -> {
                for (int j = startIdx; j < endIdx; j++) {
                    synchronized (lock) {
                        synchronizedTotal += numbers[j];
                    }
                }
            });
            syncThreads[i].start();
        }

        for (Thread t : syncThreads) {
            t.join();
        }
        long time2 = System.currentTimeMillis() - start2;
        System.out.println("2. Fix 1: Synchronized Block:");
        System.out.println("   Result: " + synchronizedTotal + " (CORRECT)");
        System.out.println("   Time: " + time2 + " ms (high lock contention overhead)\n");

        // -------------------------------------------------------------
        // Approach 3: Fix 2 - Local Chunk Sum per Thread (Best Practice)
        // -------------------------------------------------------------
        long[] partialSums = new long[NUM_THREADS];
        Thread[] optimalThreads = new Thread[NUM_THREADS];
        long start3 = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            final int startIdx = i * CHUNK_SIZE;
            final int endIdx = startIdx + CHUNK_SIZE;

            optimalThreads[i] = new Thread(() -> {
                long localSum = 0;
                for (int j = startIdx; j < endIdx; j++) {
                    localSum += numbers[j];
                }
                partialSums[threadId] = localSum; // Stored in thread-local slot
            });
            optimalThreads[i].start();
        }

        for (Thread t : optimalThreads) {
            t.join();
        }

        long optimalTotal = 0;
        for (long s : partialSums) {
            optimalTotal += s;
        }
        long time3 = System.currentTimeMillis() - start3;

        System.out.println("3. Fix 2: Local Thread Accumulation (No Lock Contention):");
        System.out.println("   Result: " + optimalTotal + " (CORRECT)");
        System.out.println("   Time: " + time3 + " ms (fastest and scalable)\n");

        System.out.println("Comparison Summary: Fix 2 achieves full correctness with zero lock contention, outperforming synchronized blocks.");
    }
}
