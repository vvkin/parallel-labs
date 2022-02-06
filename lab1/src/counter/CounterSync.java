package counter;

public class CounterSync implements Counter {
    private int value = 0;

    public synchronized void increment() {
        value += 1;
    }

    public synchronized void decrement() {
        value -= 1;
    }

    public int get() {
        return value;
    }
}
