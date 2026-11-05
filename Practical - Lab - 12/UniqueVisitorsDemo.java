import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UniqueVisitorsDemo {
    public static void main(String[] args) {
        System.out.println("=== Unique Visitors Tracking: HashSet vs LinkedHashSet ===");

        // Simulated login audit logs containing repeated user logins
        List<String> loginStream = Arrays.asList(
            "arshit_vaghela",
            "pratham_mer",
            "meet_joshi",
            "arshit_vaghela",  // repeat login
            "rohan_mehta",
            "pratham_mer",   // repeat login
            "sneha_parikh",
            "priya_desai",
            "meet_joshi",   // repeat login
            "karan_sharma"
        );

        System.out.println("\nRaw Login Stream (" + loginStream.size() + " total login events):");
        for (int i = 0; i < loginStream.size(); i++) {
            System.out.printf("  Event %2d: %s%n", (i + 1), loginStream.get(i));
        }

        // HashSet automatically filters out duplicates based on hash code (unordered)
        Set<String> uniqueUsersHashSet = new HashSet<>(loginStream);

        // LinkedHashSet maintains insertion order (first-seen order) while filtering duplicates
        Set<String> orderedUsersLinkedHashSet = new LinkedHashSet<>(loginStream);

        // Display unique count from HashSet
        System.out.println("\n--- Unique User Statistics ---");
        System.out.println("  Total Login Attempts : " + loginStream.size());
        System.out.println("  Unique Users Count   : " + uniqueUsersHashSet.size());

        // Extracting first three distinct names in first-seen order from LinkedHashSet
        System.out.println("\n--- First Three Distinct Users (by First Login Order) ---");
        int count = 0;
        for (String user : orderedUsersLinkedHashSet) {
            count++;
            System.out.printf("  %d. %s%n", count, user);
            if (count == 3) {
                break;
            }
        }

        // Display full first-seen order list
        System.out.println("\n--- Full Distinct Visitor List (LinkedHashSet: First-Seen Order) ---");
        int rank = 1;
        for (String user : orderedUsersLinkedHashSet) {
            System.out.printf("  [%d] %s%n", rank++, user);
        }

        // Comparing internal iteration order
        System.out.println("\nOrder Comparison:");
        System.out.println("  HashSet Order (arbitrary bucket hash order):");
        System.out.println("    " + uniqueUsersHashSet);
        System.out.println("  LinkedHashSet Order (guaranteed arrival order):");
        System.out.println("    " + orderedUsersLinkedHashSet);
    }
}

