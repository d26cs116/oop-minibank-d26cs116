public class Practical08Demo {

    // Custom Checked Exceptions
    static class BankException extends Exception {
        public BankException(String message) { super(message); }
    }

    static class InvalidAmountException extends BankException {
        public InvalidAmountException(String message) { super(message); }
    }

    static class InsufficientFundsException extends BankException {
        private final long shortfall;

        public InsufficientFundsException(String message, long shortfall) {
            super(message);
            this.shortfall = shortfall;
        }

        public long getShortfall() { return shortfall; }
    }

    // AutoCloseable AuditSession for try-with-resources
    static class AuditSession implements AutoCloseable {
        private final String operation;

        public AuditSession(String operation) {
            this.operation = operation;
            System.out.println(" [Audit] Session STARTED for: " + operation);
        }

        @Override
        public void close() {
            System.out.println(" [Audit] Session COMMITTED and closed safely for: " + operation);
        }
    }

    static class SavingsAccount {
        private final String accountNumber;
        private final String ownerName;
        private long balance;
        private final long minBalance;

        public SavingsAccount(String ownerName, long initialBalance, long minBalance) {
            this.accountNumber = "AC" + (int)(Math.random() * 9000 + 1000);
            this.ownerName = ownerName;
            this.balance = initialBalance;
            this.minBalance = minBalance;
        }

        public String getAccountNumber() { return accountNumber; }
        public String getOwnerName() { return ownerName; }
        public long getBalance() { return balance; }

        public void deposit(long amount) throws InvalidAmountException {
            if (amount <= 0) {
                throw new InvalidAmountException("Deposit amount must be strictly positive. Supplied: " + amount);
            }
            this.balance += amount;
        }

        public void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
            if (amount <= 0) {
                throw new InvalidAmountException("Withdrawal amount must be strictly positive. Supplied: " + amount);
            }
            if ((balance - amount) < minBalance) {
                long shortfall = (minBalance + amount) - balance;
                throw new InsufficientFundsException(
                        "Insufficient funds! Minimum balance requirement of Rs. " + minBalance + " violated.", shortfall);
            }
            this.balance -= amount;
        }

        public void transfer(SavingsAccount target, long amount) throws BankException {
            try (AuditSession session = new AuditSession("TRANSFER " + accountNumber + " -> " + target.getAccountNumber())) {
                this.withdraw(amount);
                target.deposit(amount);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PRACTICAL 08: Custom Checked Exceptions & Try-with-Resources ===");

        SavingsAccount sender = new SavingsAccount("Arshit Vaghela", 5000, 500);
        SavingsAccount receiver = new SavingsAccount("Yug Patel", 2000, 500);

        System.out.println("Initial Sender Balance  : Rs. " + sender.getBalance());
        System.out.println("Initial Receiver Balance: Rs. " + receiver.getBalance());

        // 1. InvalidAmountException demonstration
        System.out.println("\n--- 1. Testing InvalidAmountException (Negative Deposit) ---");
        try {
            System.out.println("Attempting to deposit -500 into sender account...");
            sender.deposit(-500);
        } catch (InvalidAmountException e) {
            System.out.println("Caught Expected Checked Exception: " + e.getClass().getSimpleName());
            System.out.println("Detail Message: " + e.getMessage());
        }

        // 2. InsufficientFundsException demonstration
        System.out.println("\n--- 2. Testing InsufficientFundsException (Excess Withdrawal) ---");
        try {
            System.out.println("Attempting to withdraw Rs. 6,000 from sender account (Balance: Rs. 5,000)...");
            sender.withdraw(6000);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught Expected Checked Exception: " + e.getClass().getSimpleName());
            System.out.println("Detail Message: " + e.getMessage());
            System.out.println("Shortfall Amount: Rs. " + e.getShortfall());
        } catch (BankException e) {
            System.out.println("Bank Error: " + e.getMessage());
        }

        // 3. Atomic transfer with try-with-resources AutoCloseable AuditSession
        System.out.println("\n--- 3. Testing Atomic Transfer with AutoCloseable AuditSession ---");
        try {
            System.out.println("Transferring Rs. 2,000 from " + sender.getAccountNumber() + " to " + receiver.getAccountNumber() + "...");
            sender.transfer(receiver, 2000);
            System.out.println("Transfer Succeeded!");
            System.out.println("New Sender Balance  : Rs. " + sender.getBalance());
            System.out.println("New Receiver Balance: Rs. " + receiver.getBalance());
        } catch (BankException e) {
            System.out.println("Transfer Failed: " + e.getMessage());
        }
    }
}
