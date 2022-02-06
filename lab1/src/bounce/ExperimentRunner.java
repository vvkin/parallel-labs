package bounce;

import java.awt.*;

public class ExperimentRunner {

    private final BounceFrame frame;

    public ExperimentRunner(BounceFrame frame) {
        this.frame = frame;
    }

    void run(int redNumber, int blueNumber) {
        BallCanvas canvas = frame.getBallCanvas();

        int x = 0;
        int y = 0;

        for (int i = 0; i < blueNumber; ++i) {
            Ball blueBall = new Ball(canvas, x, y, Color.blue);
            frame.registerBall(blueBall);
        }

        for (int i = 0; i < redNumber; ++i) {
            Ball redBall = new Ball(canvas, x, y, Color.red);
            frame.registerBall(redBall);
        }
    }
}
