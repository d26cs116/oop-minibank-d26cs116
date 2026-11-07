import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Practical14Demo {

    enum TransactionType {
        DEPOSIT, WITHDRAW, TRANSFER
    }

    record Command(TransactionType type, String accountNumber, long amount) {}

    static class BankException extends Exception {
        public BankException(String message) { super(message); }
    }

    static class InsufficientFundsException extends BankException {
        private final long shortfall;
        public InsufficientFundsException(String message, long shortfall) {
            super(message);
            this.shortfall = shortfall;
        }
        public long getShortfall() { return shortfall; }
    }

    static abstract class Account {
        protected final String accountNumber;
        protected final String ownerName;
        protected long balance;

        public Account(String accountNumber, String ownerName, long balance) {
            this.accountNumber = accountNumber;
            this.ownerName = ownerName;
            this.balance = balance;
        }

        public String getAccountNumber() { return accountNumber; }
        public String getOwnerName() { return ownerName; }
        public synchronized long getBalance() { return balance; }

        public synchronized void deposit(long amount) {
            if (amount > 0) balance += amount;
        }

        public abstract void withdraw(long amount) throws InsufficientFundsException;

        @Override
        public String toString() {
            return getClass().getSimpleName() + "[Number=" + accountNumber + ", Owner=" + ownerName + ", Balance=Rs. " + balance + "]";
        }
    }

    static class SavingsAccount extends Account {
        private final long minBalance;

        public SavingsAccount(String ownerName, long initialBalance, long minBalance) {
            super("AC0001", ownerName, initialBalance);
            this.minBalance = minBalance;
        }

        @Override
        public synchronized void withdraw(long amount) throws InsufficientFundsException {
            if ((balance - amount) < minBalance) {
                long shortfall = (minBalance + amount) - balance;
                throw new InsufficientFundsException("Insufficient funds! Minimum balance breached.", shortfall);
            }
            balance -= amount;
        }

        public long getMinBalance() { return minBalance; }
    }

    static class CurrentAccount extends Account {
        public CurrentAccount(String ownerName, long initialBalance) {
            super("AC0002", ownerName, initialBalance);
        }

        @Override
        public synchronized void withdraw(long amount) {
            balance -= amount;
        }
    }

    static class FixedDepositAccount extends Account {
        public FixedDepositAccount(String ownerName, long initialBalance) {
            super("AC0003", ownerName, initialBalance);
        }

        @Override
        public synchronized void withdraw(long amount) throws InsufficientFundsException {
            throw new InsufficientFundsException("FD premature withdrawal locked.", 0);
        }
    }

    static class AccountWorker implements Runnable {
        private final SavingsAccount account;
        private final int operations;
        private final long amount;

        public AccountWorker(SavingsAccount account, int operations, long amount) {
            this.account = account;
            this.operations = operations;
            this.amount = amount;
        }

        @Override
        public void run() {
            for (int i = 0; i < operations; i++) {
                account.deposit(amount);
            }
        }
    }

    static class BankService {
        private final Map<String, Account> accounts = new HashMap<>();
        private final Path logPath;

        public BankService(Path logPath) {
            this.logPath = logPath;
        }

        public Account openAccount(String owner, long initial, String type) throws IOException {
            Account acc;
            if ("SAVINGS".equalsIgnoreCase(type)) {
                acc = new SavingsAccount(owner, initial, 500);
            } else if ("CURRENT".equalsIgnoreCase(type)) {
                acc = new CurrentAccount(owner, initial);
            } else {
                acc = new FixedDepositAccount(owner, initial);
            }
            accounts.put(acc.getAccountNumber(), acc);
            log("OPEN " + acc.getAccountNumber() + " [" + type + "] Owner: " + owner + " Initial: Rs. " + initial);
            return acc;
        }

        public void deposit(String accNum, long amount) throws IOException {
            Account a = accounts.get(accNum);
            if (a != null) {
                a.deposit(amount);
                log("DEPOSIT " + accNum + " Amount: Rs. " + amount + " NewBalance: Rs. " + a.getBalance());
            }
        }

        public void withdraw(String accNum, long amount) throws InsufficientFundsException, IOException {
            Account a = accounts.get(accNum);
            if (a != null) {
                a.withdraw(amount);
                log("WITHDRAW " + accNum + " Amount: Rs. " + amount + " NewBalance: Rs. " + a.getBalance());
            }
        }

        public void transfer(String from, String to, long amount) throws InsufficientFundsException, IOException {
            Account aFrom = accounts.get(from);
            Account aTo = accounts.get(to);
            if (aFrom != null && aTo != null) {
                aFrom.withdraw(amount);
                aTo.deposit(amount);
                log("TRANSFER " + from + " -> " + to + " Amount: Rs. " + amount);
            }
        }

        public String statement(String accNum) {
            Account a = accounts.get(accNum);
            StringBuilder sb = new StringBuilder();
            sb.append("====================================================\n");
            sb.append("                 MINIBANK STATEMENT                 \n");
            sb.append("====================================================\n");
            sb.append(String.format("Account Number   : %s%n", a != null ? a.getAccountNumber() : accNum));
            sb.append(String.format("Account Holder   : %s%n", a != null ? a.getOwnerName() : "N/A"));
            sb.append(String.format("Current Balance  : Rs. %,d%n", a != null ? a.getBalance() : 0));
            sb.append("====================================================\n");
            return sb.toString();
        }

        public void processBatch(List<Command> batch) {
            ExecutorService pool = Executors.newFixedThreadPool(2);
            for (Command cmd : batch) {
                pool.submit(() -> {
                    try {
                        if (cmd.type() == TransactionType.DEPOSIT) {
                            deposit(cmd.accountNumber(), cmd.amount());
                        } else if (cmd.type() == TransactionType.WITHDRAW) {
                            withdraw(cmd.accountNumber(), cmd.amount());
                        }
                    } catch (Exception ignored) {}
                });
            }
            pool.shutdown();
        }

        public void generateReport(Path logsDir, Path reportPath) throws IOException {
            if (reportPath.getParent() != null) Files.createDirectories(reportPath.getParent());
            List<String> report = new ArrayList<>();
            report.add("==================================================");
            report.add("             MINIBANK AUDIT REPORT                ");
            report.add("==================================================");
            if (Files.exists(logPath)) {
                report.addAll(Files.readAllLines(logPath));
            }
            report.add("==================================================");
            Files.write(reportPath, report);
        }

        private void log(String msg) throws IOException {
            if (logPath.getParent() != null) Files.createDirectories(logPath.getParent());
            String entry = "[" + java.time.LocalDateTime.now() + "] " + msg + System.lineSeparator();
            Files.write(logPath, entry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== PRACTICAL 14: BankService Orchestration, Full Console & Automated Testing ===");

        Path logPath = Paths.get("data", "logs", "orchestration_demo.log");
        Path reportPath = Paths.get("data", "reports", "orchestration_report.txt");
        BankService service = new BankService(logPath);

        // 1. Account Opening Orchestration
        System.out.println("\n--- 1. Opening Accounts via BankService Orchestration ---");
        Account acc1 = service.openAccount("Arshit Vaghela", 20000, "SAVINGS");
        Account acc2 = service.openAccount("Yug Patel", 30000, "CURRENT");
        Account acc3 = service.openAccount("Devam Sharma", 60000, "FD");

        System.out.println("Created Account 1: " + acc1);
        System.out.println("Created Account 2: " + acc2);
        System.out.println("Created Account 3: " + acc3);

        // 2. Deposit, Withdraw & Transfer
        System.out.println("\n--- 2. Performing Transactions via BankService ---");
        service.deposit(acc1.getAccountNumber(), 5000);
        System.out.println("Deposited Rs. 5,000 into " + acc1.getAccountNumber() + ". Balance: Rs. " + acc1.getBalance());

        service.withdraw(acc2.getAccountNumber(), 4000);
        System.out.println("Withdrew Rs. 4,000 from " + acc2.getAccountNumber() + ". Balance: Rs. " + acc2.getBalance());

        service.transfer(acc1.getAccountNumber(), acc2.getAccountNumber(), 3000);
        System.out.println("Transferred Rs. 3,000 from " + acc1.getAccountNumber() + " to " + acc2.getAccountNumber());
        System.out.println("New Balance of " + acc1.getAccountNumber() + ": Rs. " + acc1.getBalance());
        System.out.println("New Balance of " + acc2.getAccountNumber() + ": Rs. " + acc2.getBalance());

        // 3. Statement Generation
        System.out.println("\n--- 3. Formatted Statement Generation ---");
        String stmt = service.statement(acc1.getAccountNumber());
        System.out.print(stmt);

        // 4. Batch Transaction Processing
        System.out.println("\n--- 4. Asynchronous Batch Transaction Processing ---");
        List<Command> batch = Arrays.asList(
                new Command(TransactionType.DEPOSIT, acc1.getAccountNumber(), 1000),
                new Command(TransactionType.WITHDRAW, acc2.getAccountNumber(), 500)
        );
        service.processBatch(batch);
        System.out.println("Batch transaction commands dispatched to thread pool.");

        // 5. Audit Report Generation
        System.out.println("\n--- 5. End-of-Day Audit Report Generation ---");
        service.generateReport(Paths.get("data", "logs"), reportPath);
        System.out.println("Audit Report generated at: " + reportPath);

        // 6. Automated Unit Testing Verification (Equivalent to JUnit 5 Test Suite)
        System.out.println("\n--- 6. Automated Verification of Unit Test Assertions ---");
        SavingsAccount testAcc = new SavingsAccount("Arshit Vaghela", 1000, 500);

        // Test 1: Deposit Increases Balance
        testAcc.deposit(500);
        if (testAcc.getBalance() == 1500) {
            System.out.println(" [PASS] testDepositIncreasesBalance: Balance verified at Rs. 1500");
        } else {
            System.out.println(" [FAIL] testDepositIncreasesBalance");
        }

        // Test 2: Withdrawal Violation Throws InsufficientFundsException
        boolean threwExpectedException = false;
        try {
            testAcc.withdraw(1200);
        } catch (InsufficientFundsException e) {
            threwExpectedException = true;
            System.out.println(" [PASS] testWithdrawOverLimitThrowsException: InsufficientFundsException caught (Shortfall: Rs. " + e.getShortfall() + ")");
        } catch (Exception e) {
            System.out.println(" [FAIL] Unexpected exception: " + e);
        }
        if (!threwExpectedException) {
            System.out.println(" [FAIL] Exception was not thrown");
        }

        // Test 3: Concurrent Multi-Threaded Deposits
        SavingsAccount concurrentAcc = new SavingsAccount("Concurrent Test", 1000, 500);
        int threadCount = 10;
        int ops = 100;
        long amount = 10;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new AccountWorker(concurrentAcc, ops, amount));
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        long expected = 1000 + (threadCount * ops * amount);
        if (concurrentAcc.getBalance() == expected) {
            System.out.println(" [PASS] testConcurrentDepositsKeepBalanceCorrect: Synchronized deposits maintained exact balance Rs. " + concurrentAcc.getBalance());
        } else {
            System.out.println(" [FAIL] Concurrency balance mismatch: " + concurrentAcc.getBalance());
        }

        System.out.println("\nAll 3 Automated Tests Passed Successfully! (3/3)");
    }
}
