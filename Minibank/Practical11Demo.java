import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Practical11Demo {

    static abstract class Account implements Serializable {
        private static final long serialVersionUID = 1L;
        protected final String accountNumber;
        protected final String ownerName;
        protected final long balance;

        public Account(String accountNumber, String ownerName, long balance) {
            this.accountNumber = accountNumber;
            this.ownerName = ownerName;
            this.balance = balance;
        }

        public String getAccountNumber() { return accountNumber; }
        public String getOwnerName() { return ownerName; }
        public long getBalance() { return balance; }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "[Number=" + accountNumber + ", Owner=" + ownerName + ", Balance=Rs. " + balance + "]";
        }
    }

    static class SavingsAccount extends Account {
        private static final long serialVersionUID = 1L;
        public SavingsAccount(String ownerName, long balance) {
            super("AC0001", ownerName, balance);
        }
    }

    static class CurrentAccount extends Account {
        private static final long serialVersionUID = 1L;
        public CurrentAccount(String ownerName, long balance) {
            super("AC0002", ownerName, balance);
        }
    }

    static class StatePersister {
        public static void save(Account[] accounts, Path path) throws IOException {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
                oos.writeObject(accounts);
            }
        }

        public static Account[] load(Path path) throws IOException, ClassNotFoundException {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
                return (Account[]) ois.readObject();
            }
        }
    }

    static class TransactionLog {
        public static void append(Path path, String message) throws IOException {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            String line = "[" + java.time.LocalDateTime.now() + "] " + message + System.lineSeparator();
            Files.write(path, line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

    static class ReportGenerator {
        public static void generateReport(Path logsDir, Path reportPath) throws IOException {
            if (reportPath.getParent() != null) Files.createDirectories(reportPath.getParent());
            List<String> reportLines = new ArrayList<>();
            reportLines.add("==================================================");
            reportLines.add("             MINIBANK AUDIT REPORT                ");
            reportLines.add("==================================================");
            
            if (Files.exists(logsDir)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "*.log")) {
                    for (Path entry : stream) {
                        reportLines.add("--- File: " + entry.getFileName() + " ---");
                        try (BufferedReader reader = Files.newBufferedReader(entry)) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                reportLines.add("  " + line);
                            }
                        }
                    }
                }
            }
            reportLines.add("==================================================");
            Files.write(reportPath, reportLines);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== PRACTICAL 11: Serialization Persistence, Audit Trail & Modern NIO Reporting ===");

        Path stateFile = Paths.get("data", "test_accounts.dat");
        Path logsDir = Paths.get("data", "logs");
        Path logFile = logsDir.resolve("test_transactions.log");
        Path reportFile = Paths.get("data", "reports", "test_report.txt");

        // 1. Serialization & Deserialization
        System.out.println("\n--- 1. Binary Object Serialization & Deserialization ---");
        Account[] originalAccounts = {
                new SavingsAccount("Arshit Vaghela", 25000),
                new CurrentAccount("Yug Patel", 50000)
        };

        System.out.println("Saving accounts to " + stateFile + " via StatePersister.save()...");
        StatePersister.save(originalAccounts, stateFile);

        System.out.println("Loading accounts from " + stateFile + " via StatePersister.load()...");
        Account[] restoredAccounts = StatePersister.load(stateFile);
        System.out.println("Successfully restored " + restoredAccounts.length + " accounts:");
        for (Account acc : restoredAccounts) {
            System.out.println(" * " + acc);
        }

        // 2. Audit Trail Logging via java.nio.file
        System.out.println("\n--- 2. Append-Only Audit Trail (NIO Files.write) ---");
        TransactionLog.append(logFile, "OPEN AC0001 [SavingsAccount] Owner: Arshit Vaghela Initial: Rs. 25,000");
        TransactionLog.append(logFile, "DEPOSIT AC0001 Amount: Rs. 5,000 NewBalance: Rs. 30,000");
        TransactionLog.append(logFile, "WITHDRAW AC0001 Amount: Rs. 2,000 NewBalance: Rs. 28,000");
        System.out.println("Appended 3 audit trail log entries to: " + logFile);

        // 3. NIO DirectoryStream & BufferedReader Report Generation
        System.out.println("\n--- 3. Report Generation via DirectoryStream & BufferedReader ---");
        ReportGenerator.generateReport(logsDir, reportFile);
        System.out.println("Report generated at: " + reportFile);
        System.out.println("\n--- Generated Report Content ---");
        Files.lines(reportFile).forEach(System.out::println);
    }
}
