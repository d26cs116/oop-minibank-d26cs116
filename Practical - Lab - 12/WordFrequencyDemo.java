
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyDemo {
    public static void main(String[] args) {
        System.out.println("=== Word Frequency Counter using HashMap and Comparator ===");

        // Sample input paragraph for frequency analysis
        String paragraph = "Java is a robust programming language. Java collections framework provides "
                         + "data structures like HashMap, ArrayList, and HashSet. Java is powerful and "
                         + "collections make data manipulation fast and convenient in Java.";

        System.out.println("\nOriginal Paragraph:");
        System.out.println("\"Java is a robust programming language. Java collections framework provides");
        System.out.println("data structures like HashMap, ArrayList, and HashSet. Java is powerful and");
        System.out.println("collections make data manipulation fast and convenient in Java.\"");

        // Normalizing text: removing punctuation and converting to lowercase
        String cleanedText = paragraph.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
        String[] words = cleanedText.split("\\s+");

        // HashMap to store word as key and frequency count as value
        Map<String, Integer> wordCountMap = new HashMap<>();

        // Counting occurrences of each word
        for (String word : words) {
            if (!word.isEmpty()) {
                // getOrDefault returns existing count or 0 if word is seen for the first time
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }
        }

        // Converting Map entry set to a List for sorting
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordCountMap.entrySet());

        // Custom Comparator sorting by frequency descending (highest first)
        // If two words have the same frequency, sort alphabetically by word name
        Comparator<Map.Entry<String, Integer>> frequencyComparator = new Comparator<>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                int freqComparison = e2.getValue().compareTo(e1.getValue()); // descending order
                if (freqComparison != 0) {
                    return freqComparison;
                }
                return e1.getKey().compareTo(e2.getKey()); // alphabetical tie-breaker
            }
        };

        // Sorting the list using Collections.sort with our custom comparator
        Collections.sort(entryList, frequencyComparator);

        // Printing results in tabular format
        System.out.println("\n--- Word Frequency Table (Sorted by Highest Frequency) ---");
        System.out.printf("%-18s %-10s%n", "Word", "Frequency");
        System.out.println("----------------------------");
        for (Map.Entry<String, Integer> entry : entryList) {
            System.out.printf("%-18s %-10d%n", entry.getKey(), entry.getValue());
        }

        System.out.println("----------------------------");
        System.out.println("Total Distinct Words: " + wordCountMap.size());
        System.out.println("Total Words Processed: " + words.length);
    }
}

