package pc;

public class Util {
    public static final int MAX_SLEEP_TIME = 100;

    public static void sleepSafe(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
