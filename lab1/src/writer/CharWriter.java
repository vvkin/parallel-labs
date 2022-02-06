package writer;

public class CharWriter {
    private final char chr;

    public CharWriter(char chr) {
        this.chr = chr;
    }

    public Thread getWriteThread() {
        return new Thread(() -> {
            try {
                System.out.print(chr);
                Thread.sleep(15);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
    }
}
