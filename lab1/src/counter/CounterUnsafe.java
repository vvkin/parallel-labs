package counter;

public class CounterUnsafe extends Counter {
    @Override
    public void increment() {
        value += 1;
    }

    @Override
    public void decrement() {
        value -= 1;
    }
}
