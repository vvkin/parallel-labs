package matrix;

import matrix.core.SquareMatrix;
import matrix.multiplier.StripeMultiplier;
import matrix.multiplier.SynchronousMultiplier;

public class MatrixApp {
    public static void main(String[] args) {
        final int size = Integer.parseInt(args[0]);
        final int threadsNumber = Integer.parseInt(args[1]);

        System.out.println("Size: " + size);
        System.out.println("Threads: " + threadsNumber);
        System.out.println("===RESULT===");

        var left = SquareMatrix.ofRandom(size);
        var right = SquareMatrix.ofRandom(size);

        var stripeMultiplier = new StripeMultiplier(threadsNumber);
        var syncMultiplier = new SynchronousMultiplier();

        // sync
        long startSync = System.nanoTime();
        var syncResult = syncMultiplier.multiply(left, right);
        System.out.println("Sync took: " + (System.nanoTime() - startSync) / 1e9);

        // stripe
        long startStripe = System.nanoTime();
        var stripeResult = stripeMultiplier.multiply(left, right);
        System.out.println("Stripe took: " + (System.nanoTime() - startStripe) / 1e9);

        stripeMultiplier.destroy();

        if (!stripeResult.isEqualTo(syncResult)) {
            System.out.println("Invalid multiplication result");
            System.out.println("EXPECTED:\n" + syncResult);
            System.out.println("\nACTUAL:\n" + stripeResult);
        }
    }
}
