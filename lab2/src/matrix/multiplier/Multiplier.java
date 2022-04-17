package matrix.multiplier;

import matrix.core.SquareMatrix;

/*
 * Class representing abstract square matrix multiplier.
 */
public abstract class Multiplier {
    protected final int threadsNumber;

    public Multiplier() {
        this.threadsNumber = 1;
    }

    // entry point of every multiplier
    public abstract SquareMatrix multiply(final SquareMatrix left, final SquareMatrix right);

    // checks it's possible to multiply passed matrix using current number of threads
    protected void assertCanBeMultiplied(final SquareMatrix left, final SquareMatrix right) throws IllegalArgumentException {
        if (left.getSize() == 0 || left.getSize() != right.getSize() || left.getSize() % this.threadsNumber != 0) {
            throw new IllegalArgumentException("Unable to multiply: matrices sizes are incompatible!");
        }
    }
}
