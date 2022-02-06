package bounce;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.UUID;

public class Ball {
    private static final int X_SIZE = 20;
    private static final int Y_SIZE = 20;

    private final Component component;
    private final Color color;

    private final UUID id;
    private int x;
    private int y;
    private int dx = 2;
    private int dy = 2;

    public Ball(Component component) {
        this(component, Color.darkGray);
    }

    public Ball(Component component, Color color) {
        this(
                component,
                new Random().nextInt(component.getWidth() - X_SIZE) + X_SIZE,
                new Random().nextInt(component.getHeight() - Y_SIZE) + Y_SIZE,
                color
        );
    }

    public Ball(Component component, int x, int y, Color color) {
        this.component = component;
        this.id = UUID.randomUUID();
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(this.color);
        g2.fill(new Ellipse2D.Double(x, y, X_SIZE, Y_SIZE));
    }

    public void move() {
        x += dx;
        y += dy;

        if (x < X_SIZE) {
            x = X_SIZE;
            dx *= -1;
        }
        if (x + X_SIZE >= component.getWidth()) {
            x = component.getWidth() - X_SIZE;
            dx *= -1;
        }
        if (y < Y_SIZE) {
            y = Y_SIZE;
            dy *= -1;
        }
        if (y + Y_SIZE >= component.getHeight()) {
            y = component.getHeight() - Y_SIZE;
            dy *= -1;
        }

        this.component.repaint();
    }

    public Rectangle2D getRectangle() {
        return new Rectangle2D.Double(x, y, X_SIZE, Y_SIZE);
    }

    public UUID getId() {
        return this.id;
    }
    
    public Color getColor() {
        return this.color;
    }
}