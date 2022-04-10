package matrix;

import matrix.core.SquareMatrix;
import matrix.multiplier.StripeMultiplier;
import matrix.multiplier.SynchronousMultiplier;

import java.util.concurrent.ExecutionException;

public class MatrixApp {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final int size = 100;

        var left = SquareMatrix.ofRandom(size);
        var right = SquareMatrix.ofRandom(size);

//        System.out.println(left);
//        System.out.println();
//        System.out.println(right);
//        System.out.println("===RESULT===");

        var syncMultiplier = new SynchronousMultiplier();
        var syncResult = syncMultiplier.multiply(left, right).get();

        var stripeMultiplier = new StripeMultiplier(5);
        var stripeResult = stripeMultiplier.multiply(left, right).get();

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (stripeResult.get(i, j) != syncResult.get(i, j)) {
                    System.out.println("Nope");
                }
            }
        }

        System.out.println("DONE");
    }
}
