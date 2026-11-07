import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.*;

public class Practical13Demo {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Id {}

    static class Repository<T> {
        private final Map<String, T> storage = new LinkedHashMap<>();

        private String extractId(T entity) {
            Class<?> clazz = entity.getClass();
            while (clazz != null && clazz != Object.class) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        field.setAccessible(true);
                        try {
                            Object val = field.get(entity);
                            return val != null ? val.toString() : UUID.randomUUID().toString();
                        } catch (IllegalAccessException ignored) {}
                    }
                }
                clazz = clazz.getSuperclass();
            }
            return UUID.randomUUID().toString();
        }

        public void save(T entity) {
            String id = extractId(entity);
            storage.put(id, entity);
        }

        @SafeVarargs
        public final void saveAll(T... entities) {
            for (T entity : entities) {
                save(entity);
            }
        }

        public T findById(String id) {
            return storage.get(id);
        }

        public ArrayList<T> findAll() {
            return new ArrayList<>(storage.values());
        }

        // Bounded Generic Method
        public static <N extends Number> double average(N[] values) {
            if (values == null || values.length == 0) return 0.0;
            double sum = 0.0;
            for (N val : values) {
                sum += val.doubleValue();
            }
            return sum / values.length;
        }
    }

    static class Customer {
        @Id
        private final String customerId;
        private final String name;
        private final String email;
        private final String mobile;

        public Customer(String name, String email, String mobile) {
            this.customerId = "CUST" + (100 + (int)(Math.random() * 900));
            this.name = name;
            this.email = email;
            this.mobile = mobile;
        }

        public String getCustomerId() { return customerId; }
        public String getName() { return name; }

        @Override
        public String toString() {
            return "Customer[ID=" + customerId + ", Name=" + name + ", Email=" + email + "]";
        }
    }

    static abstract class Account {
        @Id
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
        public SavingsAccount(String ownerName, long balance) {
            super("AC" + (1000 + (int)(Math.random() * 9000)), ownerName, balance);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PRACTICAL 13: Generic Repository Pattern with Reflection Primary Key ===");

        // 1. Generic Repository for Customers
        System.out.println("\n--- 1. Generic Repository<Customer> with Reflection @Id Discovery ---");
        Repository<Customer> customerRepo = new Repository<>();
        Customer c1 = new Customer("Arshit Vaghela", "arshit@cspit.edu", "9876543210");
        Customer c2 = new Customer("Yug Patel", "yug@cspit.edu", "9812345678");

        customerRepo.save(c1);
        customerRepo.save(c2);

        System.out.println("Retrieved Customer by ID (" + c1.getCustomerId() + "): " + customerRepo.findById(c1.getCustomerId()));
        System.out.println("All Customers in Repository: " + customerRepo.findAll().size());

        // 2. Generic Repository for Accounts with @SafeVarargs saveAll
        System.out.println("\n--- 2. Generic Repository<Account> with @SafeVarargs saveAll ---");
        Repository<Account> accountRepo = new Repository<>();
        Account a1 = new SavingsAccount("Arshit Vaghela", 20000);
        Account a2 = new SavingsAccount("Yug Patel", 35000);
        Account a3 = new SavingsAccount("Devam Sharma", 50000);

        accountRepo.saveAll(a1, a2, a3);
        ArrayList<Account> accounts = accountRepo.findAll();
        System.out.println("Accounts saved via saveAll(): " + accounts.size());
        for (Account a : accounts) {
            System.out.println(" * Found in Repo: " + a);
        }

        // 3. Bounded Generic Method <N extends Number> double average(N[] values)
        System.out.println("\n--- 3. Bounded Generic Method <N extends Number> double average(N[]) ---");
        Double[] doubleBalances = {20000.0, 35000.0, 50000.0};
        double avgDouble = Repository.average(doubleBalances);
        System.out.printf("Average of Double balances: Rs. %,.2f%n", avgDouble);

        Integer[] intScores = {85, 92, 78, 95, 88};
        double avgInt = Repository.average(intScores);
        System.out.printf("Average of Integer scores  : %.2f%n", avgInt);
    }
}
