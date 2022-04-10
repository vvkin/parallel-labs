package matrix.core;

public class Matrix {
    protected final double[] data;
    protected final int rowsNumber;
    protected final int columnsNumber;

    public Matrix(int rowsNumber, int columnsNumber) {
        this.data = new double[rowsNumber * columnsNumber];
        this.rowsNumber = rowsNumber;
        this.columnsNumber = columnsNumber;
    }

    public void fill(double value) {
        for (int i = 0; i < rowsNumber; ++i) {
            for (int j = 0; j < columnsNumber; ++j) {
                data[i * this.rowsNumber + j] = value;
            }
        }
    }

    public double get(int rowIdx, int columnIdx) {
        return data[rowIdx * this.columnsNumber + columnIdx];
    }

    public void set(int rowIdx, int columnIdx, double value) {
        data[rowIdx * this.columnsNumber + columnIdx] = value;
    }

    public int getRowsNumber() {
        return rowsNumber;
    }

    public int getColumnsNumber() {
        return columnsNumber;
    }

    public Matrix getSubMatrix(int rowStart, int rowEnd, int columnStart, int columnEnd) {
        Matrix subMatrix = new Matrix(rowEnd - rowStart, columnEnd - columnStart);
        for (int i = 0; i < subMatrix.getRowsNumber(); ++i) {
            for (int j = 0; j < subMatrix.getColumnsNumber(); ++j) {
                subMatrix.set(i, j, data[(rowStart + i) * this.columnsNumber + columnStart + j]);
            }
        }
        return subMatrix;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < rowsNumber; ++i) {
            for (int j = 0; j < columnsNumber; ++j) {
                buffer.append(data[i * this.columnsNumber + j]);
                buffer.append(' ');
            }
            buffer.append('\n');
        }
        return buffer.toString();
    }

    public boolean isEqualTo(Matrix other) {
        if (this.rowsNumber != other.rowsNumber || this.columnsNumber != other.columnsNumber) {
            return false;
        }
        for (int i = 0; i < this.rowsNumber; ++i) {
            for (int j = 0; j < this.columnsNumber; ++j) {
                if (this.get(i, j) != other.get(i, j)) return false;
            }
        }
        return true;
    }
}
