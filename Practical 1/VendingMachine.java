import java.util.Scanner;

enum Coin {     //enum which contains only constants
    ONE,
    TWO,
    FIVE,
    TEN
}

public class VendingMachine {
    public static void main(String[] args) {

        int snackPrice = 15;
        int total = 0;
        System.out.println("Enter the coin");
Scanner sc = new Scanner(System.in); //for user input
        while (total < 15) {
            Coin coin = Coin.valueOf(sc.next().toUpperCase());  //.toUpperCase method is used because constants are defined in uppercase.
            switch (coin) {
                case ONE -> {
                    total+=1;
                }
                case TWO -> {
                    total+=2;
                }
                case FIVE -> {
                    total+=5;
                }
                case TEN -> {
                    total+=10;
                }
                default ->
                        System.out.println("Invalid coin");
            } ;
            System.out.println("Total = " + total);
        }
        int change = total - snackPrice;

        System.out.println("Paid. Change: " + change);
    }
}

