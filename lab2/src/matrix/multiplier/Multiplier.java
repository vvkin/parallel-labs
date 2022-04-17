package matrix.multiplier;

import matrix.core.SquareMatrix;

public abstract class Multiplier {
    protected final int threadsNumber;

    public Multiplier() {
        this.threadsNumber = 1;
    }
    
    public abstract SquareMatrix multiply(final SquareMatrix left, final SquareMatrix right);

    protected void assertCanBeMultiplied(final SquareMatrix left, final SquareMatrix right) throws IllegalArgumentException {
        if (left.getSize() == 0 || left.getSize() != right.getSize() || left.getSize() % this.threadsNumber != 0) {
            throw new IllegalArgumentException("Unable to multiply: matrices sizes are incompatible!");
        }
    }
}
