import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRunner {

    public static void main(String[] args) {
        // Fixed thread pool of 3 worker threads reused across all tasks
        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("=== Thread Pool Runner (ExecutorService) ===");
        System.out.println("Submitting 10 tasks to a fixed pool of 3 threads...\n");

        for (int i = 1; i <= 10; i++) {
            final int taskId = i;

            // Defining task as a Runnable
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    System.out.println("Task " + taskId + " is running on " + Thread.currentThread().getName());
                    try {
                        // Short sleep simulating work
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        System.out.println("Task " + taskId + " interrupted");
                    }
                    System.out.println("Task " + taskId + " completed by " + Thread.currentThread().getName());
                }
            };

            executor.submit(task);
        }

        // Initiates an orderly shutdown after previously submitted tasks are executed
        executor.shutdown();

        try {
            // Awaiting completion of all tasks within 10 seconds
            if (executor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("\nAll 10 tasks completed successfully.");
            } else {
                System.out.println("\nTimeout: Some tasks did not complete.");
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted.");
        }
    }
}

