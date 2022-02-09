package writer;

public class CharWriter {
    private final int lineWidth;
    private final int charsToWrite;

    private char lastChr = '\0';
    private int charsPrinted = 0;
    private boolean stopped = false;

    public CharWriter(int lineWidth, int linesNumber) {
        this.lineWidth = lineWidth;
        this.charsToWrite = lineWidth * linesNumber;
    }

    private void writeUnsafe(char chr) {
        System.out.print(chr);
        charsPrinted += 1;
        lastChr = chr;

        if (charsPrinted % lineWidth == 0) {
            System.out.print('\n');
        }

        if (charsPrinted == charsToWrite - 1) {
            stopped = true;
        }
    }

    private synchronized void writeSync(char chr) throws InterruptedException {
        while (lastChr == chr) wait();
        writeUnsafe(chr);
        notifyAll();
    }

    public Thread getUnsafeWriteThread(char chr) {
        return new Thread(() -> {
            while (!stopped) {
                writeUnsafe(chr);
            }
        });
    }

    public Thread getSyncWriteThread(char chr) {
        return new Thread(() -> {
            while (!stopped) {
                try {
                    writeSync(chr);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
