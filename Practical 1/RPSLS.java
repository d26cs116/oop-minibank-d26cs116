import java.util.Scanner;
import java.util.Random;

// (a) Enum defining the 5 valid moves for the game
enum Move {
    ROCK,
        PAPER,
    SCISSORS,
    LIZARD,
    SPOCK
}

public class RPSLS {

    // Method to determine the winner: returns 1 for player 'a' win, -1 for 'b' win, 0 for tie
    public static int winner(Move a, Move b) {
        if(a == b)
            return 0; // Return 0 immediately if both players make the same move
            
        // Modern switch expression evaluating winning rules based on Move 'a'
        return switch (a) {
            case ROCK ->
                    (b == Move.SCISSORS || b == Move.LIZARD) ? 1 : -1;
            case PAPER ->
                    (b == Move.ROCK || b == Move.SPOCK) ? 1 : -1 ;
            case SCISSORS ->
                    (b == Move.PAPER || b == Move.LIZARD) ? 1 : -1;
            case LIZARD ->
                    (b == Move.SPOCK || b == Move.PAPER) ? 1 : -1;
            case SPOCK ->
                    (b == Move.SCISSORS || b == Move.ROCK) ? 1 : -1;
        };
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        int playerScore = 0;
        int computerScore = 0;

        // Fetching an array of all available enum values for the computer's selection
        Move[] moves = Move.values();
        
        // Loop running a fixed set of 5 rounds
        for(int round = 1; round <= 5 ; round++) {
            System.out.println("Round = " + round);
            System.out.println("Enter your Move(ROCK,PAPER,SCISSORS,LIZARD,SPOCK) :- ");
            
            // Convert user input string into the corresponding Enum constant
            Move player = Move.valueOf(sc.next().toUpperCase());
            
            // Randomly pick a move from the enum array for the computer
            Move computer = moves[random.nextInt(moves.length)];
            
            System.out.println("Your move = " + player);
            System.out.println("Computer move = " + computer);

            // Evaluate the winner of the current round
            int result = RPSLS.winner(player, computer);
            if(result == 1) {
                System.out.println("You Win this Round");
                playerScore++;
            }
            else if(result == -1) {
                System.out.println("Computer wins this Round");
                computerScore++;
            }
            else
                System.out.println("It's TIE");
                
            System.out.println("Your Score = " + playerScore + " Computer Score = " + computerScore);
        }
        
        // Final match result evaluation based on accumulated round scores
        if(playerScore > computerScore)
            System.out.println("You WIN " + playerScore + "-" + computerScore);
        else if(playerScore < computerScore)
            System.out.println("Computer WIN " + playerScore + "-" + computerScore);
        else
            System.out.println("Match Tied " + playerScore + "-" + computerScore);
            
        sc.close();
    }
}

