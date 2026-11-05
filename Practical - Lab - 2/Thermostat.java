public class Thermostat {
    private String location;
    private int startTemp;
    private int temp;
    private static final int MIN=16;
    private static final int MAX=30;
    private static int activeCount=0;

    Thermostat(String location, int startTemp) {
        this.location = location;
        if(startTemp > MAX || startTemp < MIN) {
            temp = 22;
        }
        else {
            temp = startTemp;
        }
        activeCount++;
    }
    Thermostat(String location) {
        this (location , 18);
    }

    public void raise(){
        if (temp < MAX ) {
            temp = temp +1;
        }
        else
            System.out.println("Already at maximum(30)");
    }

    public void lower() {
        if (temp > MIN)
            temp = temp -1;
        else
            System.out.println("Already at minimum(16)");
    }

    public int getTemp() {
        return temp;
    }

    public static int getActiveCount() {
        return activeCount;
    }

    public static void main(String[] args) {
        Thermostat t1 = new Thermostat("Living room");
        Thermostat t2 = new Thermostat("Bedroom" , 25);

        System.out.println("Initial Temperature of t1: " + t1.getTemp());

        // Raise temperature 10 times
        System.out.println("\nRaising Temperature:");
        for (int i = 1; i <= 10; i++) {
            t1.raise();
            System.out.println("Temperature: " + t1.getTemp());
        }

        // Lower temperature 20 times
        System.out.println("\nLowering Temperature:");
        for (int i = 1; i <= 20; i++) {
            t1.lower();
            System.out.println("Temperature: " + t1.getTemp());
        }

        // Print active thermostat count
        System.out.println("\nActive Thermostats: " + Thermostat.getActiveCount());
    }
}

