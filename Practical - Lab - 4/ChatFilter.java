import java.util.Scanner;

public class ChatFilter {
    public static String[] filter(String line) {
        String []parts = line.split(" ",3);

        if(parts.length < 3) {
            return null;
        }
        return parts;
    }

    public static boolean match(String message, String keyboard) {
        return message.toLowerCase().contains(keyboard.toLowerCase());
    }


    public static void main(String[] args) {
        String []log = {
                "10:05 alice Hello there" ,
                "10:06 bob",
                "10:07 carol Good morning everyone"
        };
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Keyword:- ");
        String keyboard = sc.nextLine().toLowerCase();
        int matches=0;
        StringBuilder report = new StringBuilder();

        for(String line: log) {
            String parts[] = ChatFilter.filter(line);
            if(parts == null)
                continue;
            String time = parts[0];
            String name = parts[1];
            String message = parts[2];

            if(ChatFilter.match(message, keyboard)) {
                matches++;
                report.append(time).append(" ").append(name).append(": ").append(message).append("\n");
            }
        }
        System.out.println("Matches: " + matches);
        System.out.println(report);
    }
}
