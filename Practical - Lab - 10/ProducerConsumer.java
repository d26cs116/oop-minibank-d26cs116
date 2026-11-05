// Shared bounded buffer using wait() and notify() for thread coordination
class Buffer {
    private String[] buffer = new String[3]; // Small buffer of capacity 3
    private int count = 0;

    // Synchronized produce method: waits if buffer is full
    synchronized void produce(String value) throws InterruptedException {
        while (count == buffer.length) {
            System.out.println("  [Buffer Full] Producer waiting for consumer to free space...");
            wait();
        }

        buffer[count] = value;
        count++;
        System.out.println("Produced: " + value + " (Buffer count: " + count + "/3)");

        // Notifies consumer thread that new item is available
        notify();
    }

    // Synchronized consume method: waits if buffer is empty
    synchronized String consume() throws InterruptedException {
        while (count == 0) {
            System.out.println("  [Buffer Empty] Consumer waiting for producer to add items...");
            wait();
        }

        String value = buffer[0];

        // Shift remaining items forward
        for (int i = 0; i < count - 1; i++) {
            buffer[i] = buffer[i + 1];
        }
        count--;
        System.out.println("Consumed: " + value + " (Buffer count: " + count + "/3)");

        // Notifies producer thread that space is now available
        notify();
        return value;
    }
}

// Producer task adding items to the shared buffer
class Producer implements Runnable {
    private final Buffer buffer;

    Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            String[] items = { "Apple", "Banana", "Mango", "Orange", "Grapes" };
            for (String item : items) {
                buffer.produce(item);
                Thread.sleep(100); // small delay between productions
            }
        } catch (InterruptedException e) {
            System.out.println("Producer interrupted");
        }
    }
}

// Consumer task taking items from the shared buffer
class Consumer implements Runnable {
    private final Buffer buffer;

    Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                buffer.consume();
                Thread.sleep(150); // small delay between consumptions
            }
        } catch (InterruptedException e) {
            System.out.println("Consumer interrupted");
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Producer-Consumer with wait() / notify() ===");

        Buffer buffer = new Buffer();
        Thread producerThread = new Thread(new Producer(buffer), "ProducerThread");
        Thread consumerThread = new Thread(new Consumer(buffer), "ConsumerThread");

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        System.out.println("\nSuccess: All items produced and consumed in order with zero loss.");
    }
}
