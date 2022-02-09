package writer;

public class WriterApp {
    public static void main(String[] args) throws InterruptedException {
        var charWriter = new CharWriter(100, 10);

        Thread pipeThread = charWriter.getUnsafeWriteThread('|');
        Thread dashThread = charWriter.getUnsafeWriteThread('-');

        pipeThread.start();
        dashThread.start();
    }
}
