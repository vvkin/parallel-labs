package matrix.multiplier;

import matrix.core.SquareMatrix;

import java.util.Optional;

public class SynchronousMultiplier extends Multiplier {
    @Override
    public Optional<SquareMatrix> multiply(SquareMatrix left, SquareMatrix right) {
        if (!this.canBeMultiplied(left, right)) return Optional.empty();

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

        return Optional.of(result);
    }
}
