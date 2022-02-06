package counter;

public class CounterApp {
    public static void main(String[] args) throws InterruptedException {
        final int ITERATIONS_NUMBER = 10000;
        Counter counter = new CounterUnsafe();

        Thread incrementThread = new Thread(() -> {
            for (int i = 0; i < ITERATIONS_NUMBER; ++i) {
                counter.increment();
            }
        });

        Thread decrementThread = new Thread(() -> {
            for (int i = 0; i < ITERATIONS_NUMBER; ++i) {
                counter.decrement();
            }
        });

        incrementThread.start();
        decrementThread.start();

        incrementThread.join();
        decrementThread.join();

        System.out.println(counter.get());
    }
}
