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

        var syncMultiplier = new SynchronousMultiplier();
        var stripeMultiplier = new StripeMultiplier(threadsNumber);

        // sync
        long startSync = System.nanoTime();
        var syncResult = syncMultiplier.multiply(left, right);
        double syncTime = (System.nanoTime() - startSync);
        System.out.println("Sync took: " + syncTime / 1e9);

        // stripe
        long startStripe = System.nanoTime();
        var stripeResult = stripeMultiplier.multiply(left, right);
        double stripeTime = (System.nanoTime() - startStripe);
        System.out.println("Stripe took: " + (stripeTime) / 1e9 + " | -> " + syncTime / stripeTime);
        stripeMultiplier.destroy();
        
        if (!stripeResult.isEqualTo(syncResult)) {
            System.out.println("STRIPE >>> Invalid multiplication result");
        }
    }
}
