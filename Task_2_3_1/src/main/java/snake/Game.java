package snake;

import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Random;
import snake.gameField.GameField;
import snake.snake.Snake;

/**
 * Main class for snake game logic.
 */
public class Game {

    private final Random random = new Random(System.currentTimeMillis());
    private final int width, height;
    private final HashSet<Point> targets = new HashSet<>();
    private final Snake snake;
    private final int targetsCount;
    private int score = 0;

    /**
     * Check if all targets are present and update if need.
     */
    private void UpdateTargets() {
        while (targets.size() < targetsCount) {
            Point newTarget = new Point(random.nextInt(width), random.nextInt(height));
            if (snake.isUsed(newTarget) || targets.contains(newTarget)) continue;
            targets.add(newTarget);
        }
    }

    /**
     * Game constructor.
     *
     * @param width width of game field.
     * @param height height of game field.
     * @param targetsCount amount of targets on field existing in one moment.
     */
    public Game(int width, int height, int targetsCount) {
        this.width = width;
        this.height = height;
        this.snake = new Snake(width, height);
        this.targetsCount = targetsCount;
        UpdateTargets();
    }

    /**
     * Call it to update game state.
     *
     * @param pressedButton pressed between two updates button, null if no buttons was pressed.
     * @return is game ended.
     */
    public boolean update(KeyCode pressedButton) {
        boolean status = snake.update(pressedButton, targets);
        score += targetsCount - targets.size();
        UpdateTargets();
        return status;
    }

    /**
     * Get field information.
     *
     * @return GameField class, providing API for getting rendering information.
     */
    public GameField getRenderData() {
        return new GameField(width, height, snake, targets);
    }

    /**
     * Get current score of game.
     *
     * @return current score.
     */
    public int getScore() {
        return score;
    }
}
