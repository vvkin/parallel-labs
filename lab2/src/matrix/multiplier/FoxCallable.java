package matrix.multiplier;

import matrix.core.Matrix;

import java.util.concurrent.Callable;

public class FoxCallable implements Callable<Matrix> {
    private final Matrix left;
    private final Matrix right;

    public FoxCallable(Matrix left, Matrix right) {
        this.left = left;
        this.right = right;
    }

    public Matrix call() {
        int size = this.left.getRowsNumber();
        double[] values = new double[size * size];

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                for (int k = 0; k < size; ++k) {
                    values[i * size + j] += left.get(i, k) * right.get(k, j);
                }
            }
        }

        return new Matrix(values, size);
    }
}
