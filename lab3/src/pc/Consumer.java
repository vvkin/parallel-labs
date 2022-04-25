package pc;

import java.util.Random;

public class Consumer implements Runnable {
    private final SharedBuffer buffer;

    public Consumer(SharedBuffer buss) {
        this.buffer = buss;
    }

    public void run() {
        Random random = new Random();
        while (buffer.isRunning()) {
            buffer.take();
            Util.sleepSafe(random.nextInt(Util.MAX_SLEEP_TIME));
        }
    }
}