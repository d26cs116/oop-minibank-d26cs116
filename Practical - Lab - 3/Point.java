import java.util.Objects;
import java.util.Scanner;

public class Point {
    private int x,y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        Point temp = (Point)obj;
        if(this.x == temp.x && this.y == temp.y){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }
}

