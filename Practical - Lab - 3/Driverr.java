
import java.util.Objects;
import java.util.Scanner;

class Driverr {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Card[] cards = new Card[5];
        boolean duplicateFound=false;

        for(int i =0; i<cards.length; i++) {
            System.out.println("Enter rank:- ");
            String rank = sc.next();

            System.out.println("Enter suit:- ");
            String suit = sc.next();

            Card newCard = new Card(rank , suit);

            for(int j=0; j<i ; j++) {
                if(newCard.equals(cards[j])) {
                    System.out.println("Duplicate found: " + newCard);
                    duplicateFound = true;
                    break;
                }
            }

            cards[i] = newCard;

            if(duplicateFound)
                break;;
        }
    }
}
