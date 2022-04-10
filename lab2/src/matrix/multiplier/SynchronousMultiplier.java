package matrix.multiplier;

import matrix.core.SquareMatrix;

public class SynchronousMultiplier extends Multiplier {
    @Override
    public SquareMatrix multiply(final SquareMatrix left, final SquareMatrix right) {
        assertCanBeMultiplied(left, right);

        final int size = left.getSize();
        SquareMatrix result = new SquareMatrix(size);

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                for (int k = 0; k < size; ++k) {
                    double value = left.get(i, k) * right.get(k, j);
                    result.set(i, j, result.get(i, j) + value);
                }
            }
        }

        return result;
    }
}
