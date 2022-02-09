package bounce;

import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BallThreadRunner {
    private final Map<UUID, Thread> threadsMap = new ConcurrentHashMap<>();

    private final Map<Color, Integer> colorPriorityMap = Map.of(
            Color.darkGray, Thread.NORM_PRIORITY,
            Color.blue, Thread.MIN_PRIORITY,
            Color.red, Thread.MAX_PRIORITY,
            Color.yellow, 3
    );

    public void removeThread(UUID ballId) {
        Thread thread = threadsMap.get(ballId);
        if (thread != null) {
            threadsMap.remove(ballId);
        }
    }

    public void startThread(Ball ball, Runnable ballRunnable) {
        int priority = colorPriorityMap.getOrDefault(ball.getColor(), Thread.NORM_PRIORITY);
        startThread(ball, ballRunnable, priority);
    }

    public void startThread(Ball ball, Runnable ballRunnable, int priority) {
        Thread thread = new Thread(ballRunnable);
        threadsMap.put(ball.getId(), thread);
        thread.setPriority(priority);
        thread.start();
    }

    static void logCurrentThread() {
        Thread thread = Thread.currentThread();
        System.out.println("Thread name = " + thread.getName() +
                " | priority = " + thread.getPriority()
        );
    }

    Collection<Thread> getAllThreads() {
        return this.threadsMap.values();
    }
}
