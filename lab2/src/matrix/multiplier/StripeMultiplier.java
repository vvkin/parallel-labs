package matrix.multiplier;

import matrix.core.Matrix;
import matrix.core.Pair;
import matrix.core.SquareMatrix;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StripeMultiplier extends Multiplier {
    private final ExecutorService threadPool;
    private final int threadsNumber;

    public StripeMultiplier(int threadsNumber) {
        this.threadPool = Executors.newFixedThreadPool(threadsNumber);
        this.threadsNumber = threadsNumber;
    }

    private static Matrix[] getHorizontalStripes(final SquareMatrix matrix, int stripeSize) {
        int stripesNumber = StripeMultiplier.getStripesNumber(matrix, stripeSize);
        Matrix[] stripes = new Matrix[stripesNumber];
        for (int i = 0; i < stripesNumber; ++i) {
            int startRow = i * stripeSize;
            stripes[i] = matrix.getSubMatrix(startRow, startRow + stripeSize, 0, matrix.getSize());
        }
        return stripes;
    }

    private static Matrix[] getVerticalStripes(final SquareMatrix matrix, int stripeSize) {
        int stripesNumber = StripeMultiplier.getStripesNumber(matrix, stripeSize);
        Matrix[] stripes = new Matrix[stripesNumber];
        for (int i = 0; i < stripesNumber; ++i) {
            int startColumn = i * stripeSize;
            stripes[i] = matrix.getSubMatrix(0, matrix.getSize(), startColumn, startColumn + stripeSize);
        }
        return stripes;
    }

    private static int getStripesNumber(final SquareMatrix matrix, int stripeSize) {
        return (int) (Math.ceil((double) matrix.getSize() / stripeSize));
    }

    @Override
    public SquareMatrix multiply(final SquareMatrix left, final SquareMatrix right) {
        assertCanBeMultiplied(left, right);

        final int stripeSize = left.getSize() / this.threadsNumber;
        Matrix[] horizontals = StripeMultiplier.getHorizontalStripes(left, stripeSize);
        Matrix[] verticals = StripeMultiplier.getVerticalStripes(right, stripeSize);

        return multiplyStripes(horizontals, verticals);
    }

    private SquareMatrix multiplyStripes(final Matrix[] horizontals, final Matrix[] verticals) {
        final SquareMatrix result = new SquareMatrix(horizontals[0].getColumnsNumber());
        final StripeCallable[] callables = new StripeCallable[this.threadsNumber];

        for (int iteration = 0; iteration < this.threadsNumber; ++iteration) {
            for (int processIdx = 0; processIdx < this.threadsNumber; ++processIdx) {
                int columnIdx = this.getVerticalIdx(iteration, processIdx);
                callables[processIdx] = new StripeCallable(processIdx, horizontals[processIdx], verticals[columnIdx]);
            }
            this.consumeCallablesByMatrix(result, callables, iteration);
        }

        return result;
    }

    private int getVerticalIdx(int iterationNumber, int processIdx) {
        return (this.threadsNumber + processIdx - 1 + iterationNumber) % this.threadsNumber;
    }

    private void consumeCallablesByMatrix(SquareMatrix matrix, final StripeCallable[] callables, int iterationIdx) {
        try {
            final int stripeSize = matrix.getSize() / this.threadsNumber;
            List<Future<Pair<Integer, Matrix>>> results = this.threadPool.invokeAll(Arrays.asList(callables));
            for (var result : results) {
                Pair<Integer, Matrix> pair = result.get();
                int rowStart = pair.left() * stripeSize;
                int columnStart = this.getVerticalIdx(iterationIdx, pair.left()) * stripeSize;
                Matrix subMatrix = pair.right();
                matrix.setSubMatrix(subMatrix, rowStart, columnStart);
            }
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void destroy() {
        this.threadPool.shutdown();
    }
}
