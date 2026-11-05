import java.util.Objects;
import java.util.Scanner;

public class Card {
    private String rank;
    private String suit;
    Card(String rank , String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        Card temp = (Card) object;
        return rank.equals(temp.rank) && suit.equals(temp.suit);
    }
    @Override
    public int hashCode() {
        return Objects.hash(rank,suit);
    }
}

