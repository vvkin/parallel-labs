package matrix.multiplier;

import matrix.core.Matrix;
import matrix.core.SquareMatrix;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * Class encapsulating Fox matrix multiplication algorithm implementation
 */
public class FoxMultiplier extends Multiplier {
    private final int threadsNumber;
    private final ExecutorService threadPool;

    public FoxMultiplier(int threadsNumber) {
        this.threadsNumber = threadsNumber;
        this.threadPool = Executors.newFixedThreadPool(threadsNumber);
    }

    // splits matrix to blocks of size = blockSize using chess board partitioning
    private Matrix[] splitToBlocks(final SquareMatrix matrix, int blockSize) {
        final int blocksInRow = matrix.getSize() / blockSize;  // number of blocks that fit in the size of the matrix
        Matrix[] blocks = new Matrix[blocksInRow * blocksInRow];

        for (int i = 0; i < blocksInRow; ++i) {
            final int iShift = i * blockSize;
            for (int j = 0; j < blocksInRow; ++j) {
                final int jShift = j * blockSize;
                blocks[i * blocksInRow + j] = matrix.getSubMatrix(
                        iShift, iShift + blockSize,
                        jShift, jShift + blockSize
                );
            }
        }

        return blocks;
    }

    @Override
    public SquareMatrix multiply(final SquareMatrix left, final SquareMatrix right) {
        assertCanBeMultiplied(left, right);
        final int blockSize = left.getSize() / this.threadsNumber;

        final Matrix[] leftBlocks = this.splitToBlocks(left, blockSize);
        final Matrix[] rightBlocks = this.splitToBlocks(right, blockSize);

        return multiplyBlocks(leftBlocks, rightBlocks);
    }

    // multiplies passed chess board partitioned  square matrix matrices
    private SquareMatrix multiplyBlocks(final Matrix[] leftBlocks, final Matrix[] rightBlocks) {
        final int blocksInRow = (int) Math.sqrt(leftBlocks.length); // number of blocks that fit in the size of the matrix
        final int blockSize = leftBlocks[0].getColumnsNumber();

        SquareMatrix result = new SquareMatrix(blocksInRow * blockSize); // multiplication result
        FoxCallable[] callables = new FoxCallable[this.threadsNumber]; // array of data for every calculation node

        for (int i = 0; i < blocksInRow; ++i) {
            for (int j = 0; j < blocksInRow; ++j) {
                for (int r = 0; r < this.threadsNumber; ++r) {
                    int k = (i + r) % blocksInRow; // calculate current iteration shift
                    Matrix leftBlock = leftBlocks[i * blocksInRow + k];
                    Matrix rightBlock = rightBlocks[k * blocksInRow + j];
                    callables[r] = new FoxCallable(leftBlock, rightBlock); // assign data to node
                }
                this.consumeCallablesByMatrix(result, callables, i, j); // await for every node and collect data
            }
        }

        return result;
    }

    // makes calculation for every node and then consumes received results by passed matrix (mutates matrix)
    private void consumeCallablesByMatrix(SquareMatrix matrix, FoxCallable[] callables, int iStep, int jStep) {
        try {
            List<Future<Matrix>> results = this.threadPool.invokeAll(Arrays.asList(callables)); // calculate futures
            int blockSize = matrix.getSize() / this.threadsNumber;
            int startRow = iStep * blockSize; // vertical shift
            int startColumn = jStep * blockSize; // horizontal shift

            for (var result : results) {
                Matrix subMatrix = result.get(); // await for future completion
                for (int i = 0; i < subMatrix.getRowsNumber(); ++i) {
                    for (int j = 0; j < subMatrix.getColumnsNumber(); ++j) {
                        // "add" received sub matrix for to resulting one
                        matrix.add(i + startRow, j + startColumn, subMatrix.get(i, j));
                    }
                }
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
