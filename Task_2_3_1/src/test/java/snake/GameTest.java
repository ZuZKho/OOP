package snake;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import snake.GameField.Cell;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @RepeatedTest(10)
    void targetsGameTest() {
        int width = 15, height = 15;
        Game game = new Game(width, height, 5);
        while (game.update(getRandomButton())) {
            var gameField = game.getRenderData();
            int targetsCount = 0;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (gameField.getCell(new Point(i, j)) == Cell.TARGET) {
                        targetsCount++;
                    }
                }
            }

            assertEquals(5, targetsCount);
        }
    }

    @RepeatedTest(10)
    void snakeIntegrityTest() {
        int width = 15, height = 15;
        Game game = new Game(width, height, 5);
        while (game.update(getRandomButton())) {
            var gameField = game.getRenderData();
            List<Point> snakePoints = new ArrayList<>();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (gameField.getCell(new Point(i, j)) == Cell.SNAKE || gameField.getCell(new Point(i, j)) == Cell.SNAKEHEAD) {
                        snakePoints.add(new Point(i, j));
                    }
                }
            }

            assertTrue(connected(snakePoints, width, height));
        }
    }

    boolean connected(List<Point> points, int width, int height) {
        if (points.isEmpty()) {
            return false;
        }

        Queue<Point> queue = new ArrayDeque<>();
        queue.add(points.get(0));
        points.remove(0);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            Point[] pts = new Point[]{new Point((current.getX() + 1) % width, current.getY()),
                          new Point((current.getX() - 1 + width) % width, current.getY()),
                          new Point(current.getX(), (current.getY() + 1) % height),
                          new Point(current.getX(), (current.getY() - 1 + height) % height)};

            for (var point : pts) {
                if (points.contains(point)) {
                    points.remove(point);
                    queue.add(point);
                }
            }
        }

        return points.isEmpty();
    }

    private Random random = new Random(System.currentTimeMillis());

    private KeyCode getRandomButton() {
        int current = random.nextInt(30);
        if (current == 0) {
            return KeyCode.LEFT;
        } else if (current == 1) {
            return KeyCode.RIGHT;
        } else if (current == 2) {
            return KeyCode.UP;
        } else if (current == 3) {
            return KeyCode.DOWN;
        }
        return null;
    }

}