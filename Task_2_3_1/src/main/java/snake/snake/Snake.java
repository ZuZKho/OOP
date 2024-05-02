package snake.snake;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import javafx.scene.input.KeyCode;
import snake.Point;

/**
 * Snake class, storing all information about snake position in Game.
 */
public class Snake {

    private final int width;
    private final int height;
    private int headX;
    private int headY;
    private Direction direction = Direction.UP;
    private final Queue<Point> queue = new ArrayDeque<>();
    private final HashSet<Point> used = new HashSet<>();

    /**
     * Default constructor.
     *
     * @param width width of game field.
     * @param height height of game field.
     */
    public Snake(int width, int height) {
        this.width = width;
        this.height = height;
        this.headX = 5;
        this.headY = 5;
        queue.add(new Point(headX, headY));
        used.add(new Point(headX, headY));
    }

    /**
     * Get position of snake's head.
     *
     * @return point of snake's head.
     */
    public Point getHead() {
        return new Point(headX, headY);
    }

    /**
     * Is point used by snake.
     *
     * @param point checking point.
     * @return is used.
     */
    public boolean isUsed(Point point) {
        return used.contains(point);
    }

    /**
     * Update snake.
     *
     * @param pressedButton button pressed between frames.
     * @param targets positions of food on field to check if snake ate something.
     * @return is game ended.
     */
    public boolean update(KeyCode pressedButton, HashSet<Point> targets) {
        if (pressedButton != null) {
            switch (pressedButton) {
                case LEFT:
                    if (direction != direction.RIGHT) {
                        direction = direction.LEFT;
                    }
                    break;
                case RIGHT:
                    if (direction != direction.LEFT) {
                        direction = direction.RIGHT;
                    }
                    break;
                case UP:
                    if (direction != direction.DOWN) {
                        direction = direction.UP;
                    }
                    break;
                case DOWN:
                    if (direction != direction.UP) {
                        direction = direction.DOWN;
                    }
                    break;
                default:
                    break;
            }
        }

        switch (direction) {
            case RIGHT -> headX = (headX + 1) % width;
            case LEFT -> headX = (headX - 1 + width) % width;
            case DOWN -> headY = (headY + 1) % height;
            case UP -> headY = (headY - 1 + height) % height;
            default -> { }
        }

        Point head = new Point(headX, headY);
        if (used.contains(head)) {
            return false;
        }

        used.add(head);
        queue.add(head);
        if (!targets.contains(head)) {
            used.remove(queue.poll());
        } else {
            targets.remove(head);
        }
        return true;
    }

}
