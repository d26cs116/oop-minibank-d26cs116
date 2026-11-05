public class CinemaShow {
    private String title;
    private int seatsAvailable;
    private final int capacity;
    private static int totalBooked=0;

    CinemaShow(String title , int capacity) {
        this.title = title;
        this.capacity = capacity;
        seatsAvailable = capacity;
    }

    CinemaShow(String title) {
        this(title,100);
    }

    public boolean book(int n) {
        if(n <= seatsAvailable) {
            seatsAvailable = seatsAvailable - n;
            totalBooked = totalBooked + n;
            return true;
        }
        else {
            return false;
        }
    }

    public void cancel(int n) {
        seatsAvailable = seatsAvailable + n;
        if (seatsAvailable > capacity) {
            seatsAvailable = capacity;
        }
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public static int getTotalBooked() {
        return totalBooked;
    }

    public static void main(String[] args) {
        CinemaShow show = new CinemaShow("dhoom", 50);
        System.out.println("total available seats:- " + show.getSeatsAvailable());
        System.out.println("book:- " + show.book(10));
        System.out.println("total booked :- " + CinemaShow.getTotalBooked());
        System.out.println("total available seats:- " + show.getSeatsAvailable());
    }

}
