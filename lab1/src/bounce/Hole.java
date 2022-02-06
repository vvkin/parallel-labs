package bounce;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Hole {
    private static final int X_SIZE = 30;
    private static final int Y_SIZE = 30;

    private final int x;
    private final int y;

    public Hole(Component component) {
        var rnd = new Random();
        this.x = rnd.nextInt(component.getWidth() - X_SIZE) + X_SIZE;
        this.y = rnd.nextInt(component.getHeight() - Y_SIZE) + Y_SIZE;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.black);
        g2.fill(new Ellipse2D.Double(x, y, X_SIZE, Y_SIZE));
    }

    public boolean contains(Rectangle2D another) {
        Rectangle2D that = new Rectangle2D.Double(x, y, X_SIZE, Y_SIZE);
        return that.contains(another);
    }
}
