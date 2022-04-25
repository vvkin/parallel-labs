package bank;

class TransferThread extends Thread {
    private static final int ITERATIONS_NUMBER = 1000;
    private final Bank bank;
    private final int fromAccount;
    private final int maxAmount;

    public TransferThread(Bank bank, int from, int maxAmount) {
        this.bank = bank;
        this.fromAccount = from;
        this.maxAmount = maxAmount;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (int i = 0; i < ITERATIONS_NUMBER; i++) {
                    int toAccount = (int) (bank.size() * Math.random());
                    int amount = (int) (maxAmount * Math.random() / ITERATIONS_NUMBER);
                    bank.transfer(fromAccount, toAccount, amount);
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                break;
            }
        }
    }
}