package pc;

public class ProducerConsumerApp {
    public static void main(String[] args) {
        final int VALUES_NUMBER = 1000;
        final int BUSS_SIZE = 100;

        SharedBuffer buffer = new SharedBuffer(BUSS_SIZE);
        (new Thread(new Producer(buffer, VALUES_NUMBER))).start();
        (new Thread(new Consumer(buffer))).start();
    }
}
