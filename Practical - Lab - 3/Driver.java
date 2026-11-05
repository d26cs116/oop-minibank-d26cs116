import java.util.Objects;
import java.util.Scanner;


class Driver {
    public static void main(String[] args) {
        Point []p = new Point[5];
        Scanner sc = new Scanner(System.in);
        for(int i =0; i < p.length ; i++) {
            System.out.println("Enter elements:- ");
            p[i] = new Point(sc.nextInt() , sc.nextInt());
        }
        int count = 0;
        for(int i = 0; i < p.length ; i++) {
            boolean alreadyPresent = false;

            for(int j = 0; j < i; j++) {
                if (p[i].equals(p[j])) {
                    alreadyPresent = true;
                    break;
                }
            }
            if(!alreadyPresent)
                count++;
        }
        System.out.println("Distinct: " + count);
    }
}
