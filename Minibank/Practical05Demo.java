public class Practical05Demo {

    static abstract class Account {
        protected static int nextId = 1;
        protected final String accountNumber;
        protected String ownerName;
        protected long balance;

        public Account(String ownerName, long initialBalance) {
            this.accountNumber = String.format("AC%04d", nextId++);
            this.ownerName = ownerName;
            this.balance = initialBalance;
        }

        public String getAccountNumber() { return accountNumber; }
        public String getOwnerName() { return ownerName; }
        public long getBalance() { return balance; }

        public abstract double interestRate();

        public double yearlyInterest() {
            return balance * (interestRate() / 100.0);
        }

        public abstract boolean canWithdraw(long amount);

        @Override
        public String toString() {
            return getClass().getSimpleName() + " [Number: " + accountNumber + ", Owner: " + ownerName + ", Balance: Rs. " + balance + "]";
        }
    }

    static class SavingsAccount extends Account {
        private final long minBalance;

        public SavingsAccount(String ownerName, long initialBalance, long minBalance) {
            super(ownerName, initialBalance);
            this.minBalance = minBalance;
        }

        @Override
        public double interestRate() {
            return 4.0;
        }

        @Override
        public boolean canWithdraw(long amount) {
            return (balance - amount) >= minBalance;
        }

        public long getMinBalance() { return minBalance; }
    }

    static class CurrentAccount extends Account {
        private final long overdraftLimit;

        public CurrentAccount(String ownerName, long initialBalance, long overdraftLimit) {
            super(ownerName, initialBalance);
            this.overdraftLimit = overdraftLimit;
        }

        @Override
        public double interestRate() {
            return 0.0;
        }

        @Override
        public boolean canWithdraw(long amount) {
            return (balance + overdraftLimit) >= amount;
        }

        public long getOverdraftLimit() { return overdraftLimit; }
    }

    static class FixedDepositAccount extends Account {
        private final int tenureMonths;

        public FixedDepositAccount(String ownerName, long initialBalance, int tenureMonths) {
            super(ownerName, initialBalance);
            this.tenureMonths = tenureMonths;
        }

        @Override
        public double interestRate() {
            return 7.25;
        }

        @Override
        public boolean canWithdraw(long amount) {
            return false;
        }

        public int getTenureMonths() { return tenureMonths; }
    }

    public static void main(String[] args) {
        System.out.println("=== PRACTICAL 05: Abstract Account Hierarchy & Dynamic Polymorphism ===");

        // Polymorphic collection of accounts
        Account[] portfolio = {
                new SavingsAccount("Arshit Vaghela", 20000, 1000),
                new CurrentAccount("Yug Patel", 15000, 10000),
                new FixedDepositAccount("Devam Sharma", 100000, 24)
        };

        System.out.println("\n--- Polymorphic Account Details & Interest Calculation ---");
        for (Account acc : portfolio) {
            System.out.println("Account Class : " + acc.getClass().getSimpleName());
            System.out.println("Account Details: " + acc);
            System.out.printf("Interest Rate  : %.2f%%%n", acc.interestRate());
            System.out.printf("Yearly Interest: Rs. %,.2f%n", acc.yearlyInterest());
            System.out.println("--------------------------------------------------");
        }

        System.out.println("\n--- Dynamic Polymorphic Withdrawal Rules Validation ---");
        long withdrawAmount = 18000;
        System.out.printf("Testing withdrawal of Rs. %,d across all account types:%n", withdrawAmount);
        for (Account acc : portfolio) {
            boolean allowed = acc.canWithdraw(withdrawAmount);
            System.out.printf(" * [%s] %s (Bal: Rs. %,d) -> canWithdraw(%d)? %s%n",
                    acc.getClass().getSimpleName(), acc.getAccountNumber(), acc.getBalance(), withdrawAmount,
                    allowed ? "YES (Permitted)" : "NO (Blocked by domain policy)");
        }
    }
}
