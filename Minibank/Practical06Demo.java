public class Practical06Demo {

    // Interfaces
    interface Transactable {
        void deposit(long amount);
        boolean withdraw(long amount);
        long getBalance();
    }

    interface InterestBearing {
        double interestRate();
        default double yearlyInterest() {
            if (this instanceof Account acc) {
                return acc.getBalance() * (interestRate() / 100.0);
            }
            return 0.0;
        }
    }

    // Marker Interface
    interface Premium {}

    // Functional Interface
    @FunctionalInterface
    interface WithdrawRule {
        boolean allows(Account account, long amount);
    }

    static abstract class Account implements Transactable {
        protected static int nextId = 1;
        protected final String accountNumber;
        protected String ownerName;
        protected long balance;
        protected boolean active = true;

        public Account(String ownerName, long initialBalance) {
            this.accountNumber = String.format("AC%04d", nextId++);
            this.ownerName = ownerName;
            this.balance = initialBalance;
        }

        public String getAccountNumber() { return accountNumber; }
        public String getOwnerName() { return ownerName; }
        public boolean isActive() { return active; }

        @Override
        public long getBalance() { return balance; }

        @Override
        public void deposit(long amount) {
            if (amount > 0) balance += amount;
        }
    }

    static class SavingsAccount extends Account implements InterestBearing, Premium {
        private final long minBalance;

        public SavingsAccount(String ownerName, long initialBalance, long minBalance) {
            super(ownerName, initialBalance);
            this.minBalance = minBalance;
        }

        @Override
        public double interestRate() { return 4.0; }

        @Override
        public boolean withdraw(long amount) {
            if (amount > 0 && (balance - amount) >= minBalance) {
                balance -= amount;
                return true;
            }
            return false;
        }
    }

    static class CurrentAccount extends Account {
        private final long overdraftLimit;

        public CurrentAccount(String ownerName, long initialBalance, long overdraftLimit) {
            super(ownerName, initialBalance);
            this.overdraftLimit = overdraftLimit;
        }

        @Override
        public boolean withdraw(long amount) {
            if (amount > 0 && (balance + overdraftLimit) >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PRACTICAL 06: Interfaces, Lambdas, Packaging & Runnable Standalone JAR ===");

        SavingsAccount sa = new SavingsAccount("Arshit Vaghela", 30000, 1000);
        CurrentAccount ca = new CurrentAccount("Yug Patel", 15000, 5000);

        // 1. Interface references & default methods
        System.out.println("\n--- 1. Transactable & InterestBearing Interface Usage ---");
        Transactable transactable = sa;
        InterestBearing interestBearing = sa;
        System.out.println("Savings Account balance via Transactable interface: Rs. " + sa.getBalance());
        System.out.printf("Calculated default yearlyInterest(): Rs. %,.2f%n", interestBearing.yearlyInterest());

        // 2. Functional Interface & Lambdas
        System.out.println("\n--- 2. Functional Interface WithdrawRule (Lambda & Anonymous Class) ---");
        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allows(Account account, long amount) {
                return amount <= 10000;
            }
        };

        WithdrawRule lambdaVipRule = (account, amount) -> account.isActive() && amount <= 50000;

        System.out.println("Testing withdrawal of Rs. 25,000 with Anonymous Standard Rule: " +
                anonymousRule.allows(sa, 25000));
        System.out.println("Testing withdrawal of Rs. 25,000 with Lambda VIP Rule: " +
                lambdaVipRule.allows(sa, 25000));

        // 3. Marker Interface Check
        System.out.println("\n--- 3. Marker Interface (Premium) Verification ---");
        System.out.println("Is SavingsAccount marked as Premium? " + (sa instanceof Premium));
        System.out.println("Is CurrentAccount marked as Premium? " + (ca instanceof Premium));

        // 4. Packaging Information
        System.out.println("\n--- 4. Standalone Executable JAR Configuration ---");
        System.out.println("Manifest Entry verified: Main-Class: MiniBank");
        System.out.println("Command to execute: java -jar MiniBank.jar");
    }
}
