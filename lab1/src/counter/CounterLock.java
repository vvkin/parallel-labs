package counter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterLock extends Counter {
    private final Lock mutex = new ReentrantLock();

    @Override
    public void increment() {
        mutex.lock();
        try {
            value += 1;
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public void decrement() {
        mutex.lock();
        try {
            value -= 1;
        } finally {
            mutex.unlock();
        }
    }
}
