package matrix.multiplier;

import matrix.core.Matrix;
import matrix.core.SquareMatrix;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FoxMultiplier extends Multiplier {
    private final int threadsNumber;
    private final ExecutorService threadPool;

    public FoxMultiplier(int threadsNumber) {
        this.threadsNumber = threadsNumber;
        this.threadPool = Executors.newFixedThreadPool(threadsNumber);
    }

    private Matrix[] splitToBlocks(final SquareMatrix matrix, int blockSize) {
        final int blocksInRow = matrix.getSize() / blockSize;
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

    private SquareMatrix multiplyBlocks(final Matrix[] leftBlocks, final Matrix[] rightBlocks) {
        final int blocksInRow = (int) Math.sqrt(leftBlocks.length);
        final int blockSize = leftBlocks[0].getColumnsNumber();

        SquareMatrix result = new SquareMatrix(blocksInRow * blockSize);
        FoxCallable[] callables = new FoxCallable[this.threadsNumber];

        for (int i = 0; i < blocksInRow; ++i) {
            for (int j = 0; j < blocksInRow; ++j) {
                for (int r = 0; r < this.threadsNumber; ++r) {
                    int k = (i + r) % blocksInRow;
                    Matrix leftBlock = leftBlocks[i * blocksInRow + k];
                    Matrix rightBlock = rightBlocks[k * blocksInRow + j];
                    callables[r] = new FoxCallable(leftBlock, rightBlock);
                }
                this.consumeCallablesByMatrix(result, callables, i, j);
            }
        }

        return result;
    }

    private void consumeCallablesByMatrix(SquareMatrix matrix, FoxCallable[] callables, int iStep, int jStep) {
        try {
            List<Future<Matrix>> results = this.threadPool.invokeAll(Arrays.asList(callables));
            int blockSize = matrix.getSize() / this.threadsNumber;
            int startRow = iStep * blockSize;
            int startColumn = jStep * blockSize;

            for (var result : results) {
                Matrix subMatrix = result.get();
                for (int i = 0; i < subMatrix.getRowsNumber(); ++i) {
                    for (int j = 0; j < subMatrix.getColumnsNumber(); ++j) {
                        matrix.add(i + startRow, j + startColumn, subMatrix.get(i, j));
                    }
                }
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
