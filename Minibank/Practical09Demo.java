public class Practical09Demo {

    static class SavingsAccount {
        private final String ownerName;
        private long balance;

        public SavingsAccount(String ownerName, long initialBalance) {
            this.ownerName = ownerName;
            this.balance = initialBalance;
        }

        public synchronized void deposit(long amount) {
            long current = this.balance;
            this.balance = current + amount;
        }

        public synchronized long getBalance() {
            return balance;
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

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== PRACTICAL 09: Multithreading, Race Conditions & Thread Synchronization ===");

        SavingsAccount account = new SavingsAccount("Arshit Vaghela", 1000);
        int threadCount = 10;
        int operationsPerThread = 100;
        long amountPerDeposit = 10;

        long expectedIncrease = (long) threadCount * operationsPerThread * amountPerDeposit;
        long expectedFinalBalance = account.getBalance() + expectedIncrease;

        System.out.println("Initial Balance       : Rs. " + account.getBalance());
        System.out.println("Concurrent Threads    : " + threadCount);
        System.out.println("Deposits per Thread   : " + operationsPerThread + " operations of Rs. " + amountPerDeposit);
        System.out.println("Expected Final Balance: Rs. " + expectedFinalBalance);

        System.out.println("\nSpawning 10 worker threads concurrently executing synchronized deposit()...");
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new AccountWorker(account, operationsPerThread, amountPerDeposit), "WorkerThread-" + (i + 1));
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\nAll worker threads completed.");
        System.out.println("Actual Final Balance  : Rs. " + account.getBalance());

        if (account.getBalance() == expectedFinalBalance) {
            System.out.println("Thread Safety Verified: Synchronization successfully eliminated race conditions!");
        } else {
            System.out.println("Race Condition Encountered: Discrepancy observed!");
        }
    }
}
