package snake;

/**
 * Point class.
 */
@SuppressWarnings("MemberName")
public class Point {
    private final int x;
    private final int y;

    /**
     * Default constructor.
     *
     * @param x x coordinate of Point.
     * @param y y coordinate of Point.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * X coordinate getter.
     *
     * @return x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Y coordinate getter.
     *
     * @return y coordinate.
     */
    public int getY() {
        return y;
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
