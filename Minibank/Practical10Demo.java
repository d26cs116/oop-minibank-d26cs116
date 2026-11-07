import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Practical10Demo {

    static class TransactionProcessor {
        private final ExecutorService pool;

        public TransactionProcessor(int poolSize) {
            this.pool = Executors.newFixedThreadPool(poolSize);
        }

        public void submit(Runnable task) {
            pool.submit(task);
        }

        public void stop() {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                }
            } catch (InterruptedException e) {
                pool.shutdownNow();
            }
        }

        public static class TransactionBuffer {
            private final Queue<String> queue = new ArrayDeque<>();
            private final int capacity;

            public TransactionBuffer(int capacity) {
                this.capacity = capacity;
            }

            public synchronized void produce(String tx) throws InterruptedException {
                while (queue.size() >= capacity) {
                    wait();
                }
                queue.add(tx);
                notifyAll();
            }

            public synchronized String consume() throws InterruptedException {
                while (queue.isEmpty()) {
                    wait();
                }
                String tx = queue.poll();
                notifyAll();
                return tx;
            }
        }

        public static void runDeadlockDemo(Object accA, Object accB) throws InterruptedException {
            System.out.println("Initiating Lock-Ordering protocol to safely transfer concurrently...");

            Object lock1 = System.identityHashCode(accA) < System.identityHashCode(accB) ? accA : accB;
            Object lock2 = lock1 == accA ? accB : accA;

            Thread t1 = new Thread(() -> {
                synchronized (lock1) {
                    System.out.println(" [Thread-1] Acquired first lock: " + lock1);
                    try { Thread.sleep(20); } catch (InterruptedException ignored) {}
                    synchronized (lock2) {
                        System.out.println(" [Thread-1] Acquired second lock: " + lock2 + " -> Transfer Step 1 Done");
                    }
                }
            });

            Thread t2 = new Thread(() -> {
                synchronized (lock1) {
                    System.out.println(" [Thread-2] Acquired first lock: " + lock1);
                    try { Thread.sleep(20); } catch (InterruptedException ignored) {}
                    synchronized (lock2) {
                        System.out.println(" [Thread-2] Acquired second lock: " + lock2 + " -> Transfer Step 2 Done");
                    }
                }
            });

            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println("Deadlock resolved cleanly: Both threads executed without circular wait lock contention!");
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== PRACTICAL 10: Thread Pools, Producer-Consumer & Deadlock Resolution ===");

        // 1. ExecutorService Fixed Thread Pool Execution
        System.out.println("\n--- 1. ExecutorService Fixed Thread Pool (4 Workers) ---");
        TransactionProcessor processor = new TransactionProcessor(4);
        for (int i = 1; i <= 4; i++) {
            final int taskId = i;
            processor.submit(() -> {
                System.out.println(" [" + Thread.currentThread().getName() + "] Executed background async task #" + taskId);
            });
        }
        processor.stop();

        // 2. Thread-safe Producer-Consumer Buffer using wait() and notifyAll()
        System.out.println("\n--- 2. Thread-Safe Producer-Consumer Buffer (Capacity 2) ---");
        TransactionProcessor.TransactionBuffer buffer = new TransactionProcessor.TransactionBuffer(2);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    String tx = "TXN_BATCH_ITEM_" + i;
                    buffer.produce(tx);
                    System.out.println(" [Producer] Produced: " + tx);
                    Thread.sleep(50);
                }
            } catch (InterruptedException ignored) {}
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    String tx = buffer.consume();
                    System.out.println(" [Consumer] Consumed: " + tx);
                    Thread.sleep(80);
                }
            } catch (InterruptedException ignored) {}
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // 3. Controlled Deadlock & Lock-Ordering Demonstration
        System.out.println("\n--- 3. Controlled Deadlock Demonstration & Ordered Lock Resolution ---");
        String accA = "Account-A";
        String accB = "Account-B";
        TransactionProcessor.runDeadlockDemo(accA, accB);
    }
}
