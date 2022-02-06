package counter;

public class CounterUnsafe implements Counter {
    private int value = 0;

    public void increment() {
        value += 1;
    }

    public void decrement() {
        value -= 1;
    }

    public int get() {
        return value;
    }
}