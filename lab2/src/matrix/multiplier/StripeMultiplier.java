package matrix.multiplier;

import matrix.core.Matrix;
import matrix.core.Pair;
import matrix.core.SquareMatrix;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StripeMultiplier extends Multiplier {
    private final ExecutorService threadPool;

    public StripeMultiplier(int threadsNumber) {
        super(threadsNumber);
        this.threadPool = Executors.newFixedThreadPool(threadsNumber);
    }

    private static Matrix[] getHorizontalStripes(SquareMatrix matrix, int partitionSize) {
        int stripesNumber = StripeMultiplier.getStripesNumber(matrix, partitionSize);
        Matrix[] stripes = new Matrix[stripesNumber];
        for (int i = 0; i < stripesNumber; ++i) {
            int startRow = i * partitionSize;
            stripes[i] = matrix.getSubMatrix(startRow, startRow + partitionSize, 0, matrix.getSize());
        }
        return stripes;
    }

    private static Matrix[] getVerticalStripes(SquareMatrix matrix, int stripeSize) {
        int stripesNumber = StripeMultiplier.getStripesNumber(matrix, stripeSize);
        Matrix[] stripes = new Matrix[stripesNumber];
        for (int i = 0; i < stripesNumber; ++i) {
            int startColumn = i * stripeSize;
            stripes[i] = matrix.getSubMatrix(0, matrix.getSize(), startColumn, startColumn + stripeSize);
        }
        return stripes;
    }

    private static int getStripesNumber(SquareMatrix matrix, int stripeSize) {
        return (int) (Math.ceil((double) matrix.getSize() / stripeSize));
    }

    @Override
    public Optional<SquareMatrix> multiply(SquareMatrix left, SquareMatrix right) {
        if (!this.canBeMultiplied(left, right)) return Optional.empty();

        final int stripeSize = left.getSize() / this.threadsNumber;
        Matrix[] horizontals = StripeMultiplier.getHorizontalStripes(left, stripeSize);
        Matrix[] verticals = StripeMultiplier.getVerticalStripes(right, stripeSize);

        return multiplyStripes(horizontals, verticals);
    }

    private Optional<SquareMatrix> multiplyStripes(final Matrix[] horizontals, final Matrix[] verticals) {
        final SquareMatrix result = new SquareMatrix(horizontals[0].getColumnsNumber());
        final StripeCallable[] callables = new StripeCallable[this.threadsNumber];

        for (int iteration = 0; iteration < this.threadsNumber; ++iteration) {
            for (int processIdx = 0; processIdx < this.threadsNumber; ++processIdx) {
                int columnIdx = this.getVerticalIdx(iteration, processIdx);
                callables[processIdx] = new StripeCallable(processIdx, horizontals[processIdx], verticals[columnIdx]);
            }

            try {
                this.consumeCallablesByMatrix(result, callables, iteration);
            } catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace();
                return Optional.empty();
            }
            System.out.println();
        }

        return Optional.of(result);
    }

    private int getVerticalIdx(int iterationNumber, int processIdx) {
        return (this.threadsNumber + processIdx - 1 + iterationNumber) % this.threadsNumber;
    }

    private void consumeCallablesByMatrix(SquareMatrix matrix, final StripeCallable[] callables, int iterationIdx) throws InterruptedException, ExecutionException {
        final int stripeSize = matrix.getSize() / this.threadsNumber;
        List<Future<Pair<Integer, double[]>>> results = this.threadPool.invokeAll(Arrays.asList(callables));

        for (var result : results) {
            Pair<Integer, double[]> pair = result.get();
            int rowStart = pair.left() * stripeSize;
            int columnStart = this.getVerticalIdx(iterationIdx, pair.left()) * stripeSize;

            double[] values = pair.right();
            for (int i = 0; i < values.length; ++i) {
                double value = values[i];
                int rowIdx = rowStart + (i / stripeSize);
                int columnIdx = columnStart + (i % stripeSize);
                matrix.set(rowIdx, columnIdx, values[i]);
            }
        }
    }
}


