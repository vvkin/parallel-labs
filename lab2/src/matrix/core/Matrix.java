package matrix.core;

public class Matrix {
    protected final double[] data;
    protected final int rowsNumber;
    protected final int columnsNumber;
    protected static final double EPSILON = 1e12;

    public Matrix(int rowsNumber, int columnsNumber) {
        this.data = new double[rowsNumber * columnsNumber];
        this.rowsNumber = rowsNumber;
        this.columnsNumber = columnsNumber;
    }

    public Matrix(double[] data, int rowsNumber) {
        this.data = data;
        this.rowsNumber = rowsNumber;
        this.columnsNumber = data.length / rowsNumber;
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
            int thisShift = (rowStart + i) * this.columnsNumber + columnStart;
            System.arraycopy(this.data, thisShift, subMatrix.data, i * subMatrix.columnsNumber, subMatrix.columnsNumber);
        }
        return subMatrix;
    }

    public void setSubMatrix(Matrix subMatrix, int rowStart, int columnStart) {
        for (int i = 0; i < subMatrix.rowsNumber; ++i) {
            int thisShift = (rowStart + i) * this.columnsNumber + columnStart;
            System.arraycopy(subMatrix.data, i * subMatrix.columnsNumber, this.data, thisShift, subMatrix.columnsNumber);
        }
    }

    public double[] getSubMatrixFlat(int rowStart, int rowEnd, int columnStart, int columnEnd) {
        return this.getSubMatrix(rowStart, rowEnd, columnStart, columnEnd).getData();
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
        if (this.rowsNumber != other.rowsNumber || this.columnsNumber != other.columnsNumber) return false;
        for (int i = 0; i < this.rowsNumber; ++i) {
            for (int j = 0; j < this.columnsNumber; ++j) {
                if (Math.abs(this.get(i, j) - other.get(i, j)) > EPSILON) {
                    return false;
                }
            }
        }
        return true;
    }

    public double[] getData() {
        return this.data;
    }
}
