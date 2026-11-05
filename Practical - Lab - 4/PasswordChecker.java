import java.util.Scanner;

public class PasswordChecker {
    public static boolean hasMinLength(String pw) {
        return (pw.length() >= 8);
    }

    public static boolean hasUpperCase(String pw) {
        return (pw.matches(".*[A-Z].*"));
    }
    public static boolean hasDigit(String pw) {
        return (pw.matches(".*[0-9].*"));
    }
    public static boolean hasSpecialChar(String pw) {
        return (pw.matches(".*[&@$,].*"));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter String :- ");
        String pw = sc.next();
        int count = 0;
        if (PasswordChecker.hasMinLength(pw))
            count++;
        if (PasswordChecker.hasDigit(pw))
            count++;
        if(PasswordChecker.hasUpperCase(pw))
            count++;
        if(PasswordChecker.hasSpecialChar(pw))
            count++;
        if (count <= 1)
            System.out.println("Password is Weak");
        else if (count <= 2)
            System.out.println("Password is medium");
        else
            System.out.println("Password is Strong");
    }
}
