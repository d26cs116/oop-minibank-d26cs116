public class Practical02Demo {

    // Static nested Customer class
    static class Customer {
        private static int nextId = 101;
        private final String customerId;
        private final String name;
        private final String mobile;

        public Customer(String name, String mobile) {
            this.customerId = "CUST" + (nextId++);
            this.name = name;
            this.mobile = mobile;
        }

        public String getCustomerId() { return customerId; }
        public String getName() { return name; }
        public String getMobile() { return mobile; }

        @Override
        public String toString() {
            return "[Customer Created] ID: " + customerId + ", Name: " + name + ", Mobile: " + mobile;
        }
    }

    // Static nested Account class
    static class Account {
        private static int nextAccNum = 1;
        private final String accountNumber;
        private final Customer customer;
        private long balance;

        public Account(Customer customer, long initialBalance) {
            this.accountNumber = String.format("AC%04d", nextAccNum++);
            this.customer = customer;
            this.balance = initialBalance;
        }

        public Account(Customer customer) {
            this(customer, 0);
        }

        public String getAccountNumber() { return accountNumber; }
        public Customer getCustomer() { return customer; }
        public long getBalance() { return balance; }

        public void deposit(long amount) {
            if (amount > 0) {
                this.balance += amount;
            }
        }

        public boolean withdraw(long amount) {
            if (amount > 0 && this.balance >= amount) {
                this.balance -= amount;
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "[Account Created] Number: " + accountNumber + ", Owner: " + customer.getName() + ", Balance: Rs. " + balance;
        }
    }

    public static void main(String[] args) {
        Customer c1 = new Customer("Arshit Vaghela", "9876543210");
        Customer c2 = new Customer("Yug Patel", "9876543211");

        Account a1 = new Account(c1, 5000);
        Account a2 = new Account(c2, 3000);
        Account a3 = new Account(new Customer("Devam Sharma", "9876543212"));

        System.out.println(c1);
        System.out.println(c2);
        System.out.println(a1);
        System.out.println(a2);
        System.out.println(a3);
        System.out.println();

        a1.deposit(2000);
        System.out.println("Deposit Rs. 2000 into " + a1.getAccountNumber() + " -> New Balance: Rs. " + a1.getBalance());

        boolean ok = a2.withdraw(1500);
        System.out.println("Withdraw Rs. 1500 from " + a2.getAccountNumber() + " -> Success: " + ok + ", New Balance: Rs. " + a2.getBalance());

        boolean denied = a3.withdraw(500);
        System.out.println("Withdraw Rs. 500 from " + a3.getAccountNumber() + " -> Success: " + denied + " (Denied, Insufficient Balance)");
    }
}
