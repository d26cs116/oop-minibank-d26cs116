import java.util.concurrent.atomic.AtomicInteger;

// Unsafe ticket booking system demonstrating overselling bug
class UnsafeTicketSystem {
    private int seatsLeft = 5;
    private int successfulBookings = 0;

    public void bookSeat(String passenger) {
        if (seatsLeft > 0) {
            // Small sleep increases likelihood of race condition context-switch
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}

            seatsLeft--;
            successfulBookings++;
            System.out.println("  [CONFIRMED] " + passenger + " booked seat! Seats remaining: " + seatsLeft);
        } else {
            System.out.println("  [REJECTED] " + passenger + " - No seats available.");
        }
    }

    public int getSuccessfulBookings() {
        return successfulBookings;
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }
}

// Safe ticket booking system using synchronized method
class SafeTicketSystem {
    private int seatsLeft = 5;
    private int successfulBookings = 0;

    // synchronized keyword ensures mutual exclusion
    public synchronized void bookSeat(String passenger) {
        if (seatsLeft > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}

            seatsLeft--;
            successfulBookings++;
            System.out.println("  [CONFIRMED] " + passenger + " booked seat! Seats remaining: " + seatsLeft);
        } else {
            System.out.println("  [REJECTED] " + passenger + " - Sold out!");
        }
    }

    public synchronized int getSuccessfulBookings() {
        return successfulBookings;
    }

    public synchronized int getSeatsLeft() {
        return seatsLeft;
    }
}

public class SeatBookingRaceDemo {
    public static void main(String[] args) throws InterruptedException {
        int totalPassengers = 10;
        int totalSeats = 5;

        System.out.println("=== Seat Booking Race Demonstration ===");
        System.out.println("Total Seats Available: " + totalSeats);
        System.out.println("Total Competing Passengers: " + totalPassengers);

        // --- Run 1: Unsynchronized Booking (Overselling Occurs) ---
        System.out.println("\n--- 1. Without Synchronization (Oversell Race Condition) ---");
        UnsafeTicketSystem unsafeSystem = new UnsafeTicketSystem();
        Thread[] unsafeThreads = new Thread[totalPassengers];

        for (int i = 0; i < totalPassengers; i++) {
            final String passengerName = "Passenger-" + (i + 1);
            unsafeThreads[i] = new Thread(() -> unsafeSystem.bookSeat(passengerName));
            unsafeThreads[i].start();
        }

        for (Thread t : unsafeThreads) {
            t.join();
        }

        System.out.println("Result: Total seats booked = " + unsafeSystem.getSuccessfulBookings() + 
                           " (Seats remaining = " + unsafeSystem.getSeatsLeft() + ")");
        if (unsafeSystem.getSuccessfulBookings() > totalSeats) {
            System.out.println("ALERT: Oversold seats! More tickets were issued than available capacity.");
        }

        // --- Run 2: Synchronized Booking (Thread-Safe) ---
        System.out.println("\n--- 2. With Synchronization (Strict Capacity Enforced) ---");
        SafeTicketSystem safeSystem = new SafeTicketSystem();
        Thread[] safeThreads = new Thread[totalPassengers];

        for (int i = 0; i < totalPassengers; i++) {
            final String passengerName = "Passenger-" + (i + 1);
            safeThreads[i] = new Thread(() -> safeSystem.bookSeat(passengerName));
            safeThreads[i].start();
        }

        for (Thread t : safeThreads) {
            t.join();
        }

        System.out.println("Result: Total seats booked = " + safeSystem.getSuccessfulBookings() + 
                           " (Seats remaining = " + safeSystem.getSeatsLeft() + ")");
        System.out.println("Success: Exactly " + totalSeats + " seats booked. No overselling occurred.");
    }
}

