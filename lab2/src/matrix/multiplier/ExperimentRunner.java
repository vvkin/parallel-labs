package matrix.multiplier;

import matrix.core.SquareMatrix;

public class ExperimentRunner {
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
                var foxMultiplier = new FoxMultiplier(threads);

                long startFox = System.nanoTime();
                var foxResult = foxMultiplier.multiply(left, right);
                double foxTime = (System.nanoTime() - startFox);
                System.out.println("Fox(" + threads + "): " + (foxTime) / 1e9 + " | -> " + syncTime / foxTime);
                foxMultiplier.destroy();

                if (!foxResult.isEqualTo(syncResult)) {
                    System.out.println("FOX >>> Invalid multiplication result");
                }
            }
            System.out.println();
        }
    }

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
                var stripeMultiplier = new StripeMultiplier(threads);

                long startStripe = System.nanoTime();
                var stripeResult = stripeMultiplier.multiply(left, right);
                double stripeTime = (System.nanoTime() - startStripe);
                System.out.println("Stripe(" + threads + "): " + (stripeTime) / 1e9 + " | -> " + syncTime / stripeTime);
                stripeMultiplier.destroy();

                if (!stripeResult.isEqualTo(syncResult)) {
                    System.out.println("STRIPE >>> Invalid multiplication result");
                }
            }
            System.out.println();
        }
    }
}
