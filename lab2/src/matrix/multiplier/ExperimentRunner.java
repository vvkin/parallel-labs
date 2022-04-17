package matrix.multiplier;

import matrix.core.SquareMatrix;

/*
 * Class encapsulating logic of parallel algorithms' efficiency testing
 */
public class ExperimentRunner {
    private static final int ITERATIONS = 5;

    // tests Fox algorithm for different number of threads and matrix sizes
    public void testFox() {
        System.out.println("Testing FOX...");

        int[] sizes = {512, 1024, 1512, 2048, 2520};
        int[] threadNumbers = {2, 4, 8};

        var syncMultiplier = new SerialMultiplier();

        for (int size : sizes) {
            System.out.println("Size: " + size);
            SquareMatrix left = SquareMatrix.ofRandom(size);
            SquareMatrix right = SquareMatrix.ofRandom(size);

            long startSync = System.nanoTime();
            var syncResult = syncMultiplier.multiply(left, right);
            double syncTime = (System.nanoTime() - startSync);
            System.out.println("Sync: " + syncTime / 1e9);

            for (int threads : threadNumbers) {
                double foxTime = 0;

                for (int i = 0; i < ExperimentRunner.ITERATIONS; ++i) {
                    var foxMultiplier = new FoxMultiplier(threads);

                    long startFox = System.nanoTime();
                    var foxResult = foxMultiplier.multiply(left, right);
                    foxTime += (System.nanoTime() - startFox);
                    foxMultiplier.destroy();

                    if (!foxResult.isEqualTo(syncResult)) {
                        System.out.println("FOX >>> Invalid multiplication result");
                    }
                }

                foxTime /= ITERATIONS;
                System.out.println("Fox(" + threads + "): " + (foxTime) / 1e9 + " | -> " + syncTime / foxTime);
            }
            System.out.println();
        }
    }

    // tests striped algorithm for different number of threads and matrix sizes
    public void testStripe() {
        System.out.println("Testing STRIPE...");

        int[] sizes = {512, 1024, 1512, 2048, 2520};
        int[] threadNumbers = {2, 4, 8};

        var syncMultiplier = new SerialMultiplier();

        for (int size : sizes) {
            System.out.println("Size: " + size);
            SquareMatrix left = SquareMatrix.ofRandom(size);
            SquareMatrix right = SquareMatrix.ofRandom(size);

            long startSync = System.nanoTime();
            var syncResult = syncMultiplier.multiply(left, right);
            double syncTime = (System.nanoTime() - startSync);
            System.out.println("Sync: " + syncTime / 1e9);

            for (int threads : threadNumbers) {
                double stripeTime = 0;

                for (int i = 0; i < ExperimentRunner.ITERATIONS; ++i) {
                    var stripeMultiplier = new StripeMultiplier(threads);

                    long startStripe = System.nanoTime();
                    var stripeResult = stripeMultiplier.multiply(left, right);
                    stripeTime += (System.nanoTime() - startStripe);
                    stripeMultiplier.destroy();

                    if (!stripeResult.isEqualTo(syncResult)) {
                        System.out.println("STRIPE >>> Invalid multiplication result");
                    }
                }

                stripeTime /= ITERATIONS;
                System.out.println("Stripe(" + threads + "): " + (stripeTime) / 1e9 + " | -> " + syncTime / stripeTime);
            }
            System.out.println();
        }
    }
}
