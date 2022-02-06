package writer;

public class WriterApp {
    public static void main(String[] args) throws InterruptedException {
        final int CHARS_TO_WRITE = 100;

        var charWriter = new CharWriter();

        for (int i = 0; i < CHARS_TO_WRITE; ++i) {
            Thread dashThread = charWriter.getWriteThread('-');
            dashThread.start();
//            dashThread.join();

            Thread pipeThread = charWriter.getWriteThread('|');
            pipeThread.start();
//            pipeThread.join();
        }
    }
}
