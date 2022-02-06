package writer;

public class CharWriter {
    private char lastChar = '\0';

    public Thread getWriteThread(char chr) {
        return new Thread(() -> {
            try {
                writeChar(chr);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
    }

    public Thread getWriteSyncThread(char chr) {
        return new Thread(() -> {
            try {
                synchronized (this) {
                    while (chr == lastChar) wait();
                    writeChar(chr);
                    notify();
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void writeChar(char chr) throws InterruptedException {
        System.out.print(chr);
        lastChar = chr;
        Thread.sleep(15);
    }
}
