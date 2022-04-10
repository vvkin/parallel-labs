package matrix.multiplier;

import matrix.core.Matrix;
import matrix.core.Pair;

import java.util.concurrent.Callable;

public class StripeCallable implements Callable<Pair<Integer, double[]>> {
    private final int processIdx;
    private final Matrix rows;
    private final Matrix columns;

    public StripeCallable(int processIdx, Matrix rows, Matrix columns) {
        this.processIdx = processIdx;
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public Pair<Integer, double[]> call() {
        double[] values = new double[rows.getRowsNumber() * columns.getColumnsNumber()];
        for (int i = 0; i < rows.getRowsNumber(); ++i) {
            for (int j = 0; j < columns.getColumnsNumber(); ++j) {
                for (int k = 0; k < rows.getColumnsNumber(); ++k) {
                    values[i * rows.getRowsNumber() + j] += rows.get(i, k) * columns.get(k, j);
                }
            }
        }
        return new Pair<>(this.processIdx, values);
    }
}
