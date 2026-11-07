import java.util.*;

public class Practical12Demo {

    static abstract class Account implements Comparable<Account> {
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
        public int compareTo(Account o) {
            return this.accountNumber.compareTo(o.accountNumber);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "[Number=" + accountNumber + ", Owner=" + ownerName + ", Balance=Rs. " + balance + "]";
        }
    }

    static class SavingsAccount extends Account {
        public SavingsAccount(String ownerName, long balance) {
            super("AC0001", ownerName, balance);
        }
    }

    static class CurrentAccount extends Account {
        public CurrentAccount(String ownerName, long balance) {
            super("AC0002", ownerName, balance);
        }
    }

    static class FixedDepositAccount extends Account {
        public FixedDepositAccount(String ownerName, long balance) {
            super("AC0003", ownerName, balance);
        }
    }

    static class AccountStore {
        private final Map<String, Account> map = new HashMap<>();
        private final List<String> history = new ArrayList<>();

        public void add(Account acc) {
            map.put(acc.getAccountNumber(), acc);
            history.add("Added " + acc.getAccountNumber() + " (" + acc.getOwnerName() + ") with balance Rs. " + acc.getBalance());
        }

        public Account get(String accNum) {
            return map.get(accNum);
        }

        public Collection<Account> getAll() {
            return map.values();
        }

        public List<Account> sortedByBalance() {
            List<Account> list = new ArrayList<>(map.values());
            list.sort(Comparator.comparingLong(Account::getBalance).reversed());
            return list;
        }

        public List<Account> sortedByAccountNumber() {
            List<Account> list = new ArrayList<>(map.values());
            Collections.sort(list);
            return list;
        }

        public List<String> getHistory() {
            return history;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PRACTICAL 12: Collections Framework: HashMap, ArrayList & Comparators ===");

        AccountStore store = new AccountStore();

        // Adding accounts to store
        Account acc1 = new SavingsAccount("Arshit Vaghela", 15000);
        Account acc2 = new CurrentAccount("Yug Patel", 45000);
        Account acc3 = new FixedDepositAccount("Devam Sharma", 100000);

        store.add(acc1);
        store.add(acc2);
        store.add(acc3);

        System.out.println("\n--- 1. AccountStore Contents (HashMap & ArrayList) ---");
        System.out.println("Total Accounts in HashMap: " + store.getAll().size());
        System.out.println("Direct Lookup for " + acc1.getAccountNumber() + ": " + store.get(acc1.getAccountNumber()));

        System.out.println("\n--- 2. Accounts Sorted by Balance (Custom Comparator - Descending) ---");
        List<Account> byBalance = store.sortedByBalance();
        for (Account a : byBalance) {
            System.out.printf(" * [%s] %-15s Balance: Rs. %,d%n",
                    a.getAccountNumber(), a.getOwnerName(), a.getBalance());
        }

        System.out.println("\n--- 3. Accounts Sorted by Natural Order (Comparable<Account> by AccountNumber) ---");
        List<Account> byAccNum = store.sortedByAccountNumber();
        for (Account a : byAccNum) {
            System.out.printf(" * [%s] %-15s Balance: Rs. %,d%n",
                    a.getAccountNumber(), a.getOwnerName(), a.getBalance());
        }

        System.out.println("\n--- 4. Recorded History in ArrayList ---");
        for (String entry : store.getHistory()) {
            System.out.println(" [History] " + entry);
        }
    }
}
