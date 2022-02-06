package counter;

public abstract class Counter {
    protected int value = 0;

    public abstract void increment();

    public abstract void decrement();

    public int get() {
        return this.value;
    }
}
