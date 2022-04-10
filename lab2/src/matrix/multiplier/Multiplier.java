package matrix.multiplier;

import matrix.core.SquareMatrix;

public abstract class Multiplier {
    public abstract SquareMatrix multiply(final SquareMatrix left, final SquareMatrix right);

    protected void assertCanBeMultiplied(final SquareMatrix left, final SquareMatrix right) throws IllegalArgumentException {
        if (left.getSize() == 0 || left.getSize() != right.getSize()) {
            throw new IllegalArgumentException("Unable to multiply: matrices sizes are incompatible!");
        }
    }
}
