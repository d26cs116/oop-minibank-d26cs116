abstract class MediaItem {
    private String title;
    private int daysLate;

    MediaItem(String title, int daysLate) {
        this.title = title;
        this.daysLate = daysLate;
    }

    public String getTitle() {
        return title;
    }

    public int getDaysLate() {
        return daysLate;
    }

    public abstract double calculateLateFee();
}

class Book extends MediaItem {
    private static final double DAILY_RATE = 2.0;

    Book(String title, int daysLate) {
        super(title, daysLate);
    }

    @Override
    public double calculateLateFee() {
        return getDaysLate() * DAILY_RATE;
    }
}

class DVD extends MediaItem {
    private static final double DAILY_RATE = 5.0;

    DVD(String title, int daysLate) {
        super(title, daysLate);
    }

    @Override
    public double calculateLateFee() {
        return getDaysLate() * DAILY_RATE;
    }
}

class Magazine extends MediaItem {
    private static final double DAILY_RATE = 1.0;

    Magazine(String title, int daysLate) {
        super(title, daysLate);
    }

    @Override
    public double calculateLateFee() {
        return getDaysLate() * DAILY_RATE;
    }
}

public class MediaDemo {
    public static void main(String[] args) {
        MediaItem[] batch = {
            new Book("Effective Java", 5),
            new DVD("Interstellar", 3),
            new Magazine("IEEE Spectrum", 4)
        };

        double totalLateFee = 0.0;
        System.out.println("Returned Media Batch Late Fee Calculation:");
        System.out.println("-------------------------------------------");

        for (MediaItem item : batch) {
            double fee = item.calculateLateFee();
            System.out.println(item.getClass().getSimpleName() + " : \"" + item.getTitle() + 
                               "\" | Days Late: " + item.getDaysLate() + 
                               " | Late Fee: Rs. " + fee);
            totalLateFee += fee;
        }

        System.out.println("-------------------------------------------");
        System.out.println("Total Late Fee for Batch: Rs. " + totalLateFee);
    }
}
