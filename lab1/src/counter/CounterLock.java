package counter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterLock implements Counter {
    private int value = 0;
    private final Lock mutex = new ReentrantLock();

    public void increment() {
        mutex.lock();
        try {
            value += 1;
        } finally {
            mutex.unlock();
        }
    }

    public void decrement() {
        mutex.lock();
        try {
            value -= 1;
        } finally {
            mutex.unlock();
        }
    }

    public int get() {
        return value;
    }
}
