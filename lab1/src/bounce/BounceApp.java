package bounce;

import javax.swing.*;

public class BounceApp {
    public static void main(String[] args) {
        BounceFrame frame = new BounceFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);

//        ExperimentRunner experimentRunner = new ExperimentRunner(frame);
//        experimentRunner.run(1, 25);

        System.out.println("Thread name = " + Thread.currentThread().getName());
    }
}
