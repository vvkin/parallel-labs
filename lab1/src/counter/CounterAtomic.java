package counter;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private final AtomicInteger value = new AtomicInteger(0);

    public int increment() {
        return value.incrementAndGet();
    }

    public int decrement() {
        return value.decrementAndGet();
    }

    public int get() {
        return value.get();
    }
}
