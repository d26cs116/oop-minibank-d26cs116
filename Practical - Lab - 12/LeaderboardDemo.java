import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardDemo {
    public static void main(String[] args) {
        System.out.println("=== Competitive Game Leaderboard using Map and Sorting ===");

        // Map storing player usernames as keys and their high scores as values
        Map<String, Integer> scoresMap = new HashMap<>();
        scoresMap.put("Arshit_Vaghela", 1020);
        scoresMap.put("Yashvi_Patel", 950);
        scoresMap.put("Meet_Joshi", 950);      // Tied with Yashvi_Patel (950)
        scoresMap.put("Vikram_Rathod", 950);   // Tied with Yashvi & Diya (950)
        scoresMap.put("Pratham_Mer", 880);
        scoresMap.put("Param_Shah", 880);      // Tied with Sneha_Parikh (880)
        scoresMap.put("Rohan_Mehta", 760);
        scoresMap.put("Karan_Sharma", 640);

        System.out.println("\nRegistered Players & Scores in Map (Unordered):");
        scoresMap.forEach((player, score) -> 
            System.out.printf("  Player: %-15s | Score: %d%n", player, score)
        );

        // Convert Map entries to an ArrayList for custom sorting
        List<Map.Entry<String, Integer>> leaderboardList = new ArrayList<>(scoresMap.entrySet());

        // Custom Comparator: Sort primarily by score descending; break ties by name alphabetically
        Comparator<Map.Entry<String, Integer>> leaderboardComparator = new Comparator<>() {
            @Override
            public int compare(Map.Entry<String, Integer> p1, Map.Entry<String, Integer> p2) {
                // Primary criterion: highest score first
                int scoreCompare = p2.getValue().compareTo(p1.getValue());
                if (scoreCompare != 0) {
                    return scoreCompare;
                }
                // Secondary criterion (tie-breaker): alphabetical order of player names
                return p1.getKey().compareTo(p2.getKey());
            }
        };

        // Sort leaderboard list
        Collections.sort(leaderboardList, leaderboardComparator);

        // Configurable Top-N limit
        int topN = 5;
        System.out.println("\n--- Top-" + topN + " Players Leaderboard (Ties Broken Alphabetically) ---");
        System.out.printf("%-6s %-18s %-10s %-25s%n", "Rank", "Player Name", "Score", "Notes");
        System.out.println("-------------------------------------------------------------");

        for (int i = 0; i < Math.min(topN, leaderboardList.size()); i++) {
            Map.Entry<String, Integer> entry = leaderboardList.get(i);
            String note = "";
            
            // Highlight tie-breaking in notes
            if (i > 0 && entry.getValue().equals(leaderboardList.get(i - 1).getValue())) {
                note = "Tied score (ordered by name)";
            } else if (i < leaderboardList.size() - 1 && entry.getValue().equals(leaderboardList.get(i + 1).getValue())) {
                note = "Tied score (ordered by name)";
            }

            System.out.printf("#%-5d %-18s %-10d %-25s%n",
                    (i + 1), entry.getKey(), entry.getValue(), note);
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println("Total Active Players on Record: " + leaderboardList.size());
    }
}

