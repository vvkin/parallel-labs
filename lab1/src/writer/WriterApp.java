package writer;

public class WriterApp {
    public static void main(String[] args) throws InterruptedException {
        final int CHARS_TO_WRITE = 100;

        var dashWriter = new CharWriter('-');
        var pipeWriter = new CharWriter('|');

        for (int i = 0; i < CHARS_TO_WRITE; ++i) {
            Thread dashThread = dashWriter.getWriteThread();
            dashThread.start();
            dashThread.join();

            Thread pipeThread = pipeWriter.getWriteThread();
            pipeThread.start();
            pipeThread.join();
        }
    }
}
