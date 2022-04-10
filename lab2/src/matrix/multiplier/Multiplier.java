package matrix.multiplier;

import matrix.core.SquareMatrix;

import java.util.Optional;

public abstract class Multiplier {
    protected final int threadsNumber;

    public Multiplier(int threadsNumber) {
        this.threadsNumber = threadsNumber;
    }

    public Multiplier() {
        this.threadsNumber = 1;
    }

    public abstract Optional<SquareMatrix> multiply(SquareMatrix left, SquareMatrix right);

    protected boolean canBeMultiplied(SquareMatrix left, SquareMatrix right) {
        return (left.getSize() != 0) && (left.getSize() == right.getSize()) && (left.getSize() % this.threadsNumber == 0);
    }
}
