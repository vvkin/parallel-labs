package matrix.core;

public class Pair<T, V> {
    private final T left;
    private final V right;

    public Pair(T left, V right) {
        this.left = left;
        this.right = right;
    }

    public T left() {
        return left;
    }

    public V right() {
        return right;
    }
}