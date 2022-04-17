package matrix;

import matrix.multiplier.ExperimentRunner;

public class MatrixApp {
    public static void main(String[] args) {
        ExperimentRunner experiment = new ExperimentRunner();
        experiment.testFox();

        System.out.println();
        experiment.testStripe();
    }
}
