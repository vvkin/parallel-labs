package bank;

public class BankApp {
    public static final int ACCOUNTS_NUMBER = 10;
    public static final int INITIAL_BALANCE = 10000;

    public static void main(String[] args) {
        Bank bank = new Bank(ACCOUNTS_NUMBER, INITIAL_BALANCE);

        for (int i = 0; i < ACCOUNTS_NUMBER; i++) {
            var thread = new TransferThread(bank, i, INITIAL_BALANCE);
            thread.setPriority(Thread.NORM_PRIORITY + i % 2);
            thread.start();
        }
    }
}