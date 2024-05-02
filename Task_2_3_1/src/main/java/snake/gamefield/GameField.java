package snake.gamefield;

import java.util.HashSet;
import snake.Point;
import snake.snake.Snake;

/**
 * Class that Game provides for rendering.
 */
public class GameField {

    private int width;
    private int height;
    private Snake snake;
    private HashSet<Point> targets;

    /**
     * Default constructor.
     *
     * @param width width of game field.
     * @param height height of game field.
     * @param snake snake of game field.
     * @param targets currently existing targets on game field.
     */
    public GameField(int width, int height, Snake snake, HashSet<Point> targets) {
        this.width = width;
        this.height = height;
        this.snake = snake;
        this.targets = targets;
    }

    /**
     * Get width of game field.
     *
     * @return width of game field.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get height of game field.
     *
     * @return height of game field.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get Cell of the field.
     *
     * @param point needed cell coordinates.
     * @return Cell.
     */
    public Cell getCell(Point point) {
        if (snake.getHead().equals(point)) {
            return Cell.SNAKEHEAD;
        }
        if (snake.isUsed(point)) {
            return Cell.SNAKE;
        }
        if (targets.contains(point)) {
            return Cell.TARGET;
        }
        return Cell.EMPTY;
    }

}
