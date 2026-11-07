import java.util.regex.Pattern;

public class Practical04Demo {

    enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER, BALANCE, STATEMENT, CLOSE
    }

    record Command(TransactionType type, String accountNumber, long amount) {}

    static class Validator {
        private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
        private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        private static final Pattern PAN_PATTERN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");
        private static final Pattern IFSC_PATTERN = Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");

        public static boolean isValidMobile(String mobile) {
            return mobile != null && MOBILE_PATTERN.matcher(mobile).matches();
        }

        public static boolean isValidEmail(String email) {
            return email != null && EMAIL_PATTERN.matcher(email).matches();
        }

        public static boolean isValidPan(String pan) {
            return pan != null && PAN_PATTERN.matcher(pan.toUpperCase()).matches();
        }

        public static boolean isValidIfsc(String ifsc) {
            return ifsc != null && IFSC_PATTERN.matcher(ifsc).matches();
        }
    }

    static class CommandParser {
        public static Command parse(String input) {
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException("Empty command");
            }
            String[] parts = input.trim().split("\\s+");
            TransactionType type = TransactionType.valueOf(parts[0].toUpperCase());
            String acc = parts.length > 1 ? parts[1] : null;
            long amount = parts.length > 2 ? Long.parseLong(parts[2]) : 0;
            return new Command(type, acc, amount);
        }
    }

    static class SavingsAccount {
        private String accountNumber = "AC0001";
        private String ownerName;
        private long balance;
        private long minBalance;

        public SavingsAccount(String ownerName, long initialBalance, long minBalance) {
            this.ownerName = ownerName;
            this.balance = initialBalance;
            this.minBalance = minBalance;
        }

        public String getAccountNumber() { return accountNumber; }
        public String getOwnerName() { return ownerName; }
        public long getBalance() { return balance; }
        public long getMinBalance() { return minBalance; }
    }

    static class StatementFormatter {
        public static String buildStatement(SavingsAccount account) {
            StringBuilder sb = new StringBuilder();
            sb.append("====================================================\n");
            sb.append("                 MINIBANK STATEMENT                 \n");
            sb.append("====================================================\n");
            sb.append(String.format("Account Number   : %s%n", account.getAccountNumber()));
            sb.append(String.format("Account Holder   : %s%n", account.getOwnerName()));
            sb.append(String.format("Account Type     : %s%n", "SAVINGS"));
            sb.append(String.format("Current Balance  : Rs. %,d%n", account.getBalance()));
            sb.append(String.format("Minimum Balance  : Rs. %,d%n", account.getMinBalance()));
            sb.append("----------------------------------------------------\n");
            sb.append("Recent Transactions:\n");
            sb.append(" [2026-11-01] INITIAL DEPOSIT   +Rs. 25,000  (Success)\n");
            sb.append(" [2026-11-03] UPI TRANSFER      -Rs.  2,000  (Success)\n");
            sb.append(" [2026-11-05] ATM CASH WITHDRAW -Rs.  3,000  (Success)\n");
            sb.append("====================================================\n");
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PRACTICAL 04: Regex KYC Validation, Commands & Statement Formatting ===");

        // 1. Regex KYC Validations
        System.out.println("\n--- 1. Compiled Regex KYC Validation ---");
        String[] mobiles = {"9876543210", "5123456789", "98765"};
        for (String m : mobiles) {
            System.out.printf("Mobile '%s' valid? -> %s%n", m, Validator.isValidMobile(m));
        }

        String[] emails = {"arshit@cspit.edu", "student@charusat.ac.in", "invalid-email@"};
        for (String e : emails) {
            System.out.printf("Email '%s' valid? -> %s%n", e, Validator.isValidEmail(e));
        }

        String[] pans = {"ABCDE1234F", "abcde1234f", "12345ABCDE"};
        for (String p : pans) {
            System.out.printf("PAN '%s' valid? -> %s%n", p, Validator.isValidPan(p));
        }

        String[] ifscs = {"SBIN0001234", "HDFC0123456", "INVALID_IFSC"};
        for (String i : ifscs) {
            System.out.printf("IFSC '%s' valid? -> %s%n", i, Validator.isValidIfsc(i));
        }

        // 2. Command Parsing
        System.out.println("\n--- 2. Command Parsing Layer ---");
        String[] rawCommands = {
                "DEPOSIT AC0001 5000",
                "WITHDRAW AC0002 1500",
                "TRANSFER AC0001 2000"
        };
        for (String raw : rawCommands) {
            try {
                Command cmd = CommandParser.parse(raw);
                System.out.printf("Parsed: Type=%s, Account=%s, Amount=Rs.%,d%n",
                        cmd.type(), cmd.accountNumber(), cmd.amount());
            } catch (Exception ex) {
                System.out.println("Failed to parse command: " + ex.getMessage());
            }
        }

        // 3. Statement Formatting using StringBuilder
        System.out.println("\n--- 3. Formatted Account Statement (StringBuilder) ---");
        SavingsAccount acc = new SavingsAccount("Arshit Vaghela", 25000, 500);
        String statement = StatementFormatter.buildStatement(acc);
        System.out.print(statement);
    }
}
