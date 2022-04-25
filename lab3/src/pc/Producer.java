package pc;

import java.util.Arrays;
import java.util.Random;

public class Producer implements Runnable {
    private final SharedBuffer buffer;
    private final int valuesNumber;

    public Producer(SharedBuffer buffer, int valuesNumber) {
        this.buffer = buffer;
        this.valuesNumber = valuesNumber;
    }

    public void run() {
        var random = new Random();
        double[] values = new double[valuesNumber];
        Arrays.setAll(values, i -> random.nextDouble() * 100);

        for (var value : values) {
            buffer.put(value);
            Util.sleepSafe(random.nextInt(Util.MAX_SLEEP_TIME));
        }

        buffer.stop();
    }
}