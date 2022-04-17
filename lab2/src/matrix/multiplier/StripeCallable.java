package matrix.multiplier;

import matrix.core.Matrix;
import matrix.core.Pair;

import java.util.concurrent.Callable;

/*
 * Class that represents calculation atom in striped algorithm.
 * It multiplies passed stripes one by one and return result as a matrix.
 * Resulting matrix size depends on stripe width.
 */
public class StripeCallable implements Callable<Pair<Integer, Matrix>> {
    private final int processIdx;
    private final Matrix rows;
    private final Matrix columns;

    public StripeCallable(int processIdx, Matrix rows, Matrix columns) {
        this.processIdx = processIdx;
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public Pair<Integer, Matrix> call() {
        double[] values = new double[rows.getRowsNumber() * columns.getColumnsNumber()];
        for (int i = 0; i < rows.getRowsNumber(); ++i) {
            for (int j = 0; j < columns.getColumnsNumber(); ++j) {
                for (int k = 0; k < rows.getColumnsNumber(); ++k) {
                    values[i * rows.getRowsNumber() + j] += rows.get(i, k) * columns.get(k, j);
                }
            }
        }
        return new Pair<>(this.processIdx, new Matrix(values, rows.getRowsNumber()));
    }
}
