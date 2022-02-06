package counter;

public class CounterSync extends Counter {
    @Override
    public synchronized void increment() {
        value += 1;
    }

    @Override
    public synchronized void decrement() {
        value -= 1;
    }
}
