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

/*
 * Class encapsulating striped matrix multiplication algorithm implementation
 */
public class StripeMultiplier extends Multiplier {
    private final ExecutorService threadPool;
    private final int threadsNumber;

    public StripeMultiplier(int threadsNumber) {
        this.threadPool = Executors.newFixedThreadPool(threadsNumber);
        this.threadsNumber = threadsNumber;
    }

    // returns row (horizontal) stripes of height = stripeSize
    private static Matrix[] getHorizontalStripes(final SquareMatrix matrix, int stripeSize) {
        int stripesNumber = StripeMultiplier.getStripesNumber(matrix, stripeSize);
        Matrix[] stripes = new Matrix[stripesNumber];
        for (int i = 0; i < stripesNumber; ++i) {
            int startRow = i * stripeSize;
            stripes[i] = matrix.getSubMatrix(startRow, startRow + stripeSize, 0, matrix.getSize());
        }
        return stripes;
    }

    // returns column (vertical) stripes of width = stripeSize
    private static Matrix[] getVerticalStripes(final SquareMatrix matrix, int stripeSize) {
        int stripesNumber = StripeMultiplier.getStripesNumber(matrix, stripeSize);
        Matrix[] stripes = new Matrix[stripesNumber];
        for (int i = 0; i < stripesNumber; ++i) {
            int startColumn = i * stripeSize;
            stripes[i] = matrix.getSubMatrix(0, matrix.getSize(), startColumn, startColumn + stripeSize);
        }
        return stripes;
    }

    // calculates number of stripes width size = stripeSize for passed matrix
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

    // multiplies matrices split to horizontal and vertical stripes
    private SquareMatrix multiplyStripes(final Matrix[] horizontals, final Matrix[] verticals) {
        final SquareMatrix result = new SquareMatrix(horizontals[0].getColumnsNumber()); // multiplication result
        final StripeCallable[] callables = new StripeCallable[this.threadsNumber]; // array of data for every calculation node

        for (int iteration = 0; iteration < this.threadsNumber; ++iteration) {
            for (int processIdx = 0; processIdx < this.threadsNumber; ++processIdx) {
                int columnIdx = this.getVerticalIdx(iteration, processIdx); // vertical stripe index
                callables[processIdx] = new StripeCallable(processIdx, horizontals[processIdx], verticals[columnIdx]);
            }
            this.consumeCallablesByMatrix(result, callables, iteration); // calculate for every node and collect
        }

        return result;
    }

    // calculates index of vertical stripe for current iteration and process
    private int getVerticalIdx(int iterationNumber, int processIdx) {
        return (this.threadsNumber + processIdx - 1 + iterationNumber) % this.threadsNumber;
    }

    // makes calculation for every node and consumes results by passed matrix (mutates matrix)
    private void consumeCallablesByMatrix(SquareMatrix matrix, final StripeCallable[] callables, int iterationIdx) {
        try {
            final int stripeSize = matrix.getSize() / this.threadsNumber;
            // get futures for every node
            List<Future<Pair<Integer, Matrix>>> results = this.threadPool.invokeAll(Arrays.asList(callables));
            for (var result : results) {
                Pair<Integer, Matrix> pair = result.get(); // await for future
                int rowStart = pair.left() * stripeSize; // vertical shift
                int columnStart = this.getVerticalIdx(iterationIdx, pair.left()) * stripeSize; // horizontal shift
                Matrix subMatrix = pair.right();
                matrix.setSubMatrix(subMatrix, rowStart, columnStart); // set sub matrix to resulting one
            }
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    // stops threadPool and releases all its resources
    public void destroy() {
        this.threadPool.shutdown();
    }
}
