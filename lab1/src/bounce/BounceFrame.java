package bounce;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BounceFrame extends JFrame {
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;
    private final static int THREAD_ITERATIONS = (int) 1e4;

    private final BallCanvas canvas;

    private final JLabel inHoleLabel = new JLabel();
    private final AtomicInteger inHoleNumber = new AtomicInteger(0);
    private final BallThreadRunner ballThreadRunner = new BallThreadRunner();

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");
        this.canvas = new BallCanvas(WIDTH, HEIGHT);
        this.constructFrame();
        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());
    }

    private void constructFrame() {
        Container content = getContentPane();
        content.add(canvas, BorderLayout.CENTER);

        content.add(constructControlPanel(), BorderLayout.SOUTH);
        content.add(inHoleLabel, BorderLayout.NORTH);
        updateInHoleCounter();
    }

    private JPanel constructControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.lightGray);

        controlPanel.add(createAddBallButton("+Default", null));
        controlPanel.add(createAddBallButton("+Blue", Color.blue));
        controlPanel.add(createAddBallButton("+Red", Color.red));
        controlPanel.add(createJoinButton());
        controlPanel.add(createStopButton());

        return controlPanel;
    }

    private JButton createAddBallButton(String text, Color color) {
        JButton button = new JButton(text);
        button.addActionListener(event -> {
            Ball ball = color == null ? new Ball(canvas) : new Ball(canvas, color);
            registerBall(ball);
        });
        return button;
    }

    public void registerBall(Ball ball) {
        canvas.addBall(ball);
        ballThreadRunner.startThread(ball, getBallRunnable(ball));
    }

    private JButton createJoinButton() {
        JButton joinButton = new JButton("Join");
        joinButton.addActionListener(event -> {
            try {
                Ball yellowBall = new Ball(canvas, 0, 0, Color.yellow);

                for (var thread : ballThreadRunner.getAllThreads()) {
                    if (thread.getPriority() == Thread.MAX_PRIORITY)
                        thread.join();
                }

                registerBall(yellowBall);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return joinButton;
    }

    private JButton createStopButton() {
        JButton buttonStop = new JButton("Stop");
        buttonStop.addActionListener(event -> System.exit(0));
        return buttonStop;
    }

    public Runnable getBallRunnable(Ball ball) {
        return getBallRunnable(ball, 5);
    }

    public Runnable getBallRunnable(Ball ball, int sleepTime) {
        return () -> {
            try {
                for (int i = 1; i < THREAD_ITERATIONS; ++i) {
                    ball.move();
                    if (canvas.isInsideHole(ball)) {
                        inHoleNumber.incrementAndGet();
                        break;
                    }
                    BallThreadRunner.logCurrentThread();
                    Thread.sleep(sleepTime);
                }
            } catch (Exception exception) {
                System.err.println("Error occurred in thread " +
                        Thread.currentThread().getName() + ": " +
                        exception.getMessage()
                );
            } finally {
                onBallRemove(ball.getId());
            }
        };
    }

    private void onBallRemove(UUID ballId) {
        canvas.removeBall(ballId);
        ballThreadRunner.removeThread(ballId);
        updateInHoleCounter();
    }

    private void updateInHoleCounter() {
        inHoleLabel.setText("In hole: " + inHoleNumber.get());
    }

    public BallCanvas getBallCanvas() {
        return this.canvas;
    }
}