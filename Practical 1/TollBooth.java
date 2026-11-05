import java.util.Scanner;

// (a) Define a record Vehicle(String number, String type) to reduce boilerplate code.
record Vehicle(String number, String type) {}

public class TollBooth {
    public static void main(String[] args) {
        // (b) Keep a running toll total and three counters.
        int totalToll = 0;
        int bike = 0;
        int car = 0;
        int truck = 0;
        
        Scanner sc = new Scanner(System.in);

        // (c) Loop until the user types “done”
        while (true) {
            System.out.println("Enter Vehicle number or done to exit:-");
            String number = sc.next();
            if (number.equals("done")) {
                break;
            }

            System.out.println("Enter Vehicle Type:-");
            String type = sc.next();

            // Build a Vehicle
            Vehicle v = new Vehicle(number, type);

            // Switch expression on its type for the toll and increment counters
            totalToll += switch (v.type().toLowerCase()) {
                case "bike" -> { 
                    bike++; 
                    yield 20; 
                }
                case "car" -> { 
                    car++; 
                    yield 50; 
                    }
                case "truck" -> { 
                    truck++; 
                    yield 150; 
                }
                default -> 0;
            };
        }

        // (d) Print total toll and highest count
        System.out.println("Total toll: " + totalToll);

        if (bike >= car && bike >= truck) {
            System.out.println("Most frequent: bike");
        } else if (car >= bike && car >= truck) {
            System.out.println("Most frequent: car");
        } else {
            System.out.println("Most frequent: truck");
        }

        sc.close();
    }
}
