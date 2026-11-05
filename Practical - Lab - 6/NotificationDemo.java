// Functional interface having single abstract method for sending notifications
@FunctionalInterface
interface Notifier {
    void send(String message);
}

// Marker interface with no methods, used to mark senders that handle urgent alerts
interface Urgent {}

public class NotificationDemo {
    public static void main(String[] args) {
        // Email sender defined as a lambda expression
        Notifier emailSender = message ->
                System.out.println("[Email Notification] " + message);

        // SMS sender defined as a lambda implementing both Notifier and Urgent via intersection cast
        Notifier smsSender = (Notifier & Urgent) message ->
                System.out.println("[Urgent SMS Alert] " + message);

        // Push notification sender without Urgent tag
        Notifier pushSender = message ->
                System.out.println("[Push Notification] " + message);

        // Array holding all notification senders
        Notifier[] senders = { emailSender, smsSender, pushSender };

        String alertMessage = "Server temperature exceeded safe limit!";
        System.out.println("Broadcasting message: \"" + alertMessage + "\"\n");

        for (Notifier sender : senders) {
            sender.send(alertMessage);

            // Using instanceof with marker interface to send twice if urgent
            if (sender instanceof Urgent) {
                System.out.println("-> Urgent sender detected! Repeating broadcast:");
                sender.send(alertMessage);
            }
            System.out.println();
        }
    }
}


