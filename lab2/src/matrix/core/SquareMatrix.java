package matrix.core;

import java.util.Random;

public class SquareMatrix extends Matrix {

    public SquareMatrix(int size) {
        super(size, size);
    }

    public int getSize() {
        return rowsNumber;
    }

    public static SquareMatrix ofValue(int size, int value) {
        var instance = new SquareMatrix(size);
        instance.fill(value);
        return instance;
    }

    public static SquareMatrix ofRandom(int size) {
        SquareMatrix instance = new SquareMatrix(size);
        Random rnd = new Random();

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                instance.set(i, j, rnd.nextInt(50));
            }
        }

        return instance;
    }

    public static SquareMatrix ofSingle(int size) {
        var instance = new SquareMatrix(size);
        for (int i = 0; i < size; ++i) {
            instance.set(i, i, 1);
        }
        return instance;
    }
}
