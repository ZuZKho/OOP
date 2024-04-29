package snake;

/**
 * Point class.
 */
public class Point {
    private int x, y;

    /**
     * Default constructor
     *
     * @param x x coordinate of Point.
     * @param y y coordinate of Point.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Some hashCode realization.
     *
     * @return hashCode of Point.
     */
    @Override
    public int hashCode() {
        return 13 * x + 11 * y;
    }

    /**
     * Equals method override.
     *
     * @param obj comparing object.
     * @return is objects equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        return this.x == other.x && this.y == other.y;
    }

}
