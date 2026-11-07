import java.util.Objects;

public class Practical03Demo {

    // Static nested Address composition
    static class Address implements Cloneable {
        private String street;
        private String city;
        private String postalCode;

        public Address(String street, String city, String postalCode) {
            this.street = street;
            this.city = city;
            this.postalCode = postalCode;
        }

        public String getStreet() { return street; }
        public String getCity() { return city; }
        public String getPostalCode() { return postalCode; }

        @Override
        public Address clone() {
            try {
                return (Address) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError("Clone not supported", e);
            }
        }

        @Override
        public String toString() {
            return street + ", " + city + " - " + postalCode;
        }
    }

    // Static nested Customer with deep cloning
    static class Customer implements Cloneable {
        private String name;
        private String email;
        private String mobile;
        private Address address;

        public Customer(String name, String email, String mobile) {
            this.name = name;
            this.email = email;
            this.mobile = mobile;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Address getAddress() {
            return address;
        }

        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getMobile() { return mobile; }

        @Override
        public Customer clone() {
            try {
                Customer cloned = (Customer) super.clone();
                if (this.address != null) {
                    cloned.address = this.address.clone(); // Deep copy
                }
                return cloned;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError("Clone not supported", e);
            }
        }

        @Override
        public String toString() {
            return "Customer[Name=" + name + ", Email=" + email + ", Mobile=" + mobile + ", Address=" + address + "]";
        }
    }

    // Static nested Account class with equals, hashCode, toString
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Account)) return false;
            Account account = (Account) o;
            return Objects.equals(accountNumber, account.accountNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountNumber);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "[Number=" + accountNumber + ", Owner=" + ownerName + ", Balance=Rs. " + balance + "]";
        }
    }

    static class SavingsAccount extends Account {
        public SavingsAccount(String ownerName, long initialBalance) {
            super(ownerName, initialBalance);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PRACTICAL 03: Object Identity, Address Composition & Deep Cloning ===");

        // 1. Static nested Address composition & deep cloning
        Customer original = new Customer("Arshit Vaghela", "arshit@cspit.edu", "9876543210");
        Address address = new Address("123 College Road", "Changa", "388421");
        original.setAddress(address);

        System.out.println("Original Customer before cloning:");
        System.out.println(" * " + original);

        // Perform deep clone
        Customer cloned = original.clone();
        System.out.println("\nCloned Customer:");
        System.out.println(" * " + cloned);

        System.out.println("\n--- Deep Cloning Reference Independence Verification ---");
        System.out.println("Are object references identical? (original == cloned): " + (original == cloned));
        System.out.println("Are nested Address references identical? (original.getAddress() == cloned.getAddress()): " 
                + (original.getAddress() == cloned.getAddress()));
        System.out.println("Is original an instance of Cloneable? " + (original instanceof Cloneable));

        // 2. Account Object Identity (equals, hashCode, toString)
        System.out.println("\n--- Account Identity Verification (equals, hashCode, toString) ---");
        Account acc1 = new SavingsAccount("Arshit Vaghela", 10000);
        Account acc2 = new SavingsAccount("Arshit Vaghela Copy", 10000);

        System.out.println("Account 1: " + acc1);
        System.out.println("Account 2: " + acc2);
        System.out.println("acc1.equals(acc2): " + acc1.equals(acc2) + " (different account numbers)");
        System.out.println("acc1.equals(acc1): " + acc1.equals(acc1) + " (same object)");
        System.out.println("acc1.hashCode(): " + acc1.hashCode());
        System.out.println("acc2.hashCode(): " + acc2.hashCode());
    }
}
