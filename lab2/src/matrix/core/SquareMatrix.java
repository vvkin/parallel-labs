package matrix.core;

import java.util.Random;

public class SquareMatrix extends Matrix {

    public SquareMatrix(int size) {
        super(size, size);
    }

    public int getSize() {
        return rowsNumber;
    }

    // returns matrix (size * size) with random double values
    public static SquareMatrix ofRandom(int size) {
        SquareMatrix instance = new SquareMatrix(size);
        Random rnd = new Random();

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                instance.set(i, j, rnd.nextDouble());
            }
        }

        return instance;
    }
}
