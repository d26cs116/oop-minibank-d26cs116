public class ParkingLot {
    private int twoWheelers;
    private int fourWheelers;
    private final int twoCap;
    private final int fourCap;
    private static long revenue=0;

    public ParkingLot(int twoCap, int fourCap) {
        this.twoCap = twoCap;
        this.fourCap = fourCap;
    }

    public void park(String type) {
        if (type == "two") {
            if (twoWheelers < twoCap) {
                twoWheelers++;
                revenue += 20;
                System.out.println("Success");
            }
            else {
                System.out.println("Full");
            }
        }
        else if (type == "four") {
            if (fourWheelers < fourCap) {
                fourWheelers++;
                revenue+=40;
                System.out.println("Success");
            }
            else
                System.out.println("Full");
        }
        else
            System.out.println("Enter a valid type");
    }

    public void leave(String type) {
        if (type == "two") {
            if (twoWheelers > 0) {
                twoWheelers--;
                System.out.println("Two wheeler left.");
            }
        }
        else if (type == "four") {
            if (fourWheelers > 0) {
                fourWheelers--;
                System.out.println("Four wheeler left.");
            }
        }
    }

    public static void main(String[] args) {
        ParkingLot p = new ParkingLot(2 ,4);
        p.park("two");
        p.park("two");
        p.park("two");      // Full

        p.park("four");
        p.park("four");     // Full

        p.leave("two");
        p.leave("four");
        
    }
}
