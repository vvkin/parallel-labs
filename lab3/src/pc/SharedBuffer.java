package pc;

import java.util.LinkedList;
import java.util.Queue;

public class SharedBuffer {
    private final Queue<Double> data;
    private final int maxSize;
    private boolean running;

    public SharedBuffer(int maxSize) {
        this.data = new LinkedList<>();
        this.maxSize = maxSize;
        this.running = true;
    }

    public synchronized double take() {
        while (this.data.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        double value = this.data.poll();
        System.out.format("TAKE: %.3f%n", value);
        notifyAll();
        return value;
    }

    public synchronized void put(double value) {
        while (this.data.size() == maxSize) {
            try {
                wait();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        this.data.add(value);
        System.out.format("PUT: %.3f%n", value);
        notifyAll();
    }

    public void stop() {
        this.running = false;
    }

    public boolean isRunning() {
        return this.running;
    }
}