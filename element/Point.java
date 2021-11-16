package element;

public class Point {
    public int index;
    public float weight;

    public Point(int index, float weight) {
        this.index = index;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "" + index + ": " + weight;
    }
}
