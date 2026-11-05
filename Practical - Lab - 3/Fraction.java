import java.util.Objects;

public class Fraction {
    private int num ;
    private int den;

    Fraction(int num, int den) {
        int g = gcd(num,den);
        this.num = num / g;
        this.den = den / g;

    }

    private int gcd(int a, int b) {
        while (b!=0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    public String toString() {
        return num + "/" + den;
    }
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;

        Fraction temp = (Fraction) obj;
        return num == temp.num && den == temp.den;
    }

    public int hashCode() {
        return Objects.hash(num, den);
    }

    public static void main(String[] args) {

        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        Fraction f3 = new Fraction(3, 6);
        Fraction f4 = new Fraction(5, 10);

        System.out.println(f1);
        System.out.println(f2);
        System.out.println(f3);
        System.out.println(f4);

        System.out.println();

        System.out.println("f1 equals f2 : " + f1.equals(f2));
        System.out.println("f2 equals f3 : " + f2.equals(f3));
        System.out.println("f3 equals f4 : " + f3.equals(f4));
        System.out.println("f1 equals f4 : " + f1.equals(f4));
    }
}
