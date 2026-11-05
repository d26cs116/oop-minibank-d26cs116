// Interface Switchable defines standard methods for devices
interface Switchable {
    void on();
    void off();
    boolean isOn();

    // Default method to toggle state without requiring re-implementation in every class
    default void toggle() {
        if (isOn()) {
            off();
        } else {
            on();
        }
    }
}

// Fan class implementing Switchable interface
class Fan implements Switchable {
    private boolean running = false;

    @Override
    public void on() {
        running = true;
        System.out.println("Fan is turned ON");
    }

    @Override
    public void off() {
        running = false;
        System.out.println("Fan is turned OFF");
    }

    @Override
    public boolean isOn() {
        return running;
    }
}

// Light class implementing Switchable interface
class Light implements Switchable {
    private boolean lit = false;

    @Override
    public void on() {
        lit = true;
        System.out.println("Light is turned ON");
    }

    @Override
    public void off() {
        lit = false;
        System.out.println("Light is turned OFF");
    }

    @Override
    public boolean isOn() {
        return lit;
    }
}

// Functional interface with a single abstract method
@FunctionalInterface
interface DeviceRule {
    boolean allow(Switchable device, int hour);
}

public class RemoteControlDemo {
    public static void main(String[] args) {
        // Polymorphic array of Switchable devices
        Switchable[] devices = { new Fan(), new Light() };

        System.out.println("--- Toggling all devices ---");
        for (Switchable device : devices) {
            device.toggle(); // switches ON
            device.toggle(); // switches OFF
        }

        System.out.println("\n--- Testing Device Rules ---");
        int currentHour = 20; // 8 PM (night time)

        // Rule implemented using an anonymous class
        DeviceRule nightRule = new DeviceRule() {
            @Override
            public boolean allow(Switchable device, int hour) {
                // Allows device only during evening/night hours (18 to 23)
                return hour >= 18 && hour <= 23;
            }
        };

        // Rule implemented using a modern lambda expression
        DeviceRule dayRule = (device, hour) -> hour >= 6 && hour < 18;

        System.out.println("Hour = " + currentHour);
        System.out.println("Night rule (anonymous class) allows: " + nightRule.allow(devices[0], currentHour));
        System.out.println("Day rule (lambda expression) allows: " + dayRule.allow(devices[0], currentHour));
    }
}
