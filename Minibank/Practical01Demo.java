import java.io.Serializable;
import java.util.Scanner;

public class Practical01Demo {
    // Static nested data record
    static record BankInfo(String name, String branch) implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    // Static nested navigation enum
    enum MenuOption {
        OPEN_ACCOUNT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        EXIT
    }

    public static void main(String[] args) {
        BankInfo info = new BankInfo("MiniBank", "Ahmedabad Branch (D26CS116)");
        System.out.println(info);
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Open Account\n2. Deposit\n3. Withdraw\n4. Transfer\n5. Exit");
            System.out.print("Enter your choice: ");
            if (!sc.hasNextInt()) break;
            int choice = sc.nextInt();
            MenuOption option = MenuOption.values()[choice - 1];
            switch (option) {
                case OPEN_ACCOUNT -> System.out.println("Open Account - to be implemented later");
                case DEPOSIT -> System.out.println("Deposit - to be implemented later");
                case WITHDRAW -> System.out.println("Withdraw - to be implemented later");
                case TRANSFER -> System.out.println("Transfer - to be implemented later");
                case EXIT -> {
                    System.out.println("Thank you for using MiniBank. Goodbye!");
                    return;
                }
            }
        }
    }
}
