package bounce;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BallCanvas extends JPanel {
    private static final int HOLES_NUMBER = 3;

    private final Map<UUID, Ball> ballsMap = new ConcurrentHashMap<>();
    private final Hole[] holes = new Hole[BallCanvas.HOLES_NUMBER];

    public BallCanvas(int width, int height) {
        super();
        this.setSize(width, height);

        for (int i = 0; i < BallCanvas.HOLES_NUMBER; ++i) {
            this.holes[i] = new Hole(this);
        }
    }

    public void addBall(Ball ball) {
        this.ballsMap.put(ball.getId(), ball);
    }

    public void removeBall(UUID id) {
        this.ballsMap.remove(id);
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        for (Ball ball : ballsMap.values())
            ball.draw(graphics2D);

        for (Hole hole : holes)
            hole.draw(graphics2D);
    }

    public boolean isInsideHole(Ball ball) {
        Rectangle2D ballRec = ball.getRectangle();
        for (Hole hole : holes) {
            if (hole.contains(ballRec)) return true;
        }
        return false;
    }
}
