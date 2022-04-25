package bank;

import java.util.Arrays;

public class Bank {
    public static final int TEST_ITERATIONS_NUMBER = 10000;
    private final int[] accounts;

//    private final Lock lock = new ReentrantLock();

    private int transactionsNumber;
//    private final AtomicInteger transactionsNumber;

    public Bank(int accountsNumber, int initialBalance) {
        this.accounts = new int[accountsNumber];
        Arrays.fill(accounts, initialBalance);
        this.transactionsNumber = 0;
//        this.transactionsNumber = new AtomicInteger(0);
    }

    // 1. Guarded block
    public synchronized void transfer(int from, int to, int amount) throws InterruptedException {
        while (this.accounts[from] < amount) this.wait();
        this.accounts[from] -= amount;
        this.accounts[to] += amount;

        this.transactionsNumber++;
        if (this.transactionsNumber % TEST_ITERATIONS_NUMBER == 0) test();

        this.notifyAll();
    }

    //  2. Lock
//    public void transfer(int from, int to, int amount) throws InterruptedException {
//        lock.lock();
//        try {
//            if (this.accounts[from] >= amount) {
//                this.accounts[from] -= amount;
//                this.accounts[to] += amount;
//                this.transactionsNumber++;
//                if (this.transactionsNumber % TEST_ITERATIONS_NUMBER == 0) test();
//            }
//        } finally {
//            lock.unlock();
//        }
//    }

    // 3. Synchronized block + atomic transactionsNumber
//    public void transfer(int from, int to, int amount) throws InterruptedException {
//        synchronized (this.transactionsNumber) {
//            if (this.accounts[from] >= amount) {
//                this.accounts[from] -= amount;
//                this.accounts[to] += amount;
//                if (this.transactionsNumber.incrementAndGet() % TEST_ITERATIONS_NUMBER == 0) test();
//            }
//        }
//    }

    public void test() {
        int sum = 0;
        for (int account : accounts) {
            sum += account;
        }
        System.out.println("Transactions:" + transactionsNumber + " Sum: " + sum);
    }

    public int size() {
        return accounts.length;
    }
}