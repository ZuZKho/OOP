package snake;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import snake.gameField.GameField;

/**
 * Class that adds field rendering to JavaFX app.
 */
public class Renderer {

    private Rectangle[][] field;

    /**
     * Default constructor with primary Render.
     *
     * @param gameField game field taken from Game.
     * @param root main Group of JavaFX Scene.
     * @param viewportWidth viewport width in pixels.
     * @param viewportHeight viewport height in pixels.
     */
    public Renderer(GameField gameField, Group root, int viewportWidth, int viewportHeight) {
        var buff = root.getChildren().stream().filter(e -> e.getClass() != Rectangle.class).toList();
        root.getChildren().clear();
        buff.forEach(e -> root.getChildren().add(e));

        field = new Rectangle[gameField.getWidth()][gameField.getHeight()];
        viewportWidth -= 200;
        int cellWidth = viewportWidth / gameField.getWidth();
        int cellHeight = viewportHeight / gameField.getHeight();
        int cellSize = Math.min(cellWidth, cellHeight);
        int shiftX = (viewportWidth - cellSize * gameField.getWidth()) / 2;
        int shiftY = (viewportHeight - cellSize * gameField.getHeight()) / 2;
        ;
        for (int i = 0; i < gameField.getWidth(); i++) {
            for (int j = 0; j < gameField.getHeight(); j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setX(shiftX + cellSize * i);
                rectangle.setY(shiftY + cellSize * j);
                rectangle.setWidth(cellSize);
                rectangle.setHeight(cellSize);
                rectangle.setFill(getCellColor(gameField, new Point(i, j)));
                rectangle.setStroke(Color.BLACK);
                field[i][j] = rectangle;
                root.getChildren().add(rectangle);
            }
        }
        Render(gameField);
    }

    /**
     * Common render after game started.
     *
     * @param gameField game field taken from Game.
     */
    public void Render(GameField gameField) {
        for (int i = 0; i < gameField.getWidth(); i++) {
            for (int j = 0; j < gameField.getHeight(); j++) {
                field[i][j].setFill(getCellColor(gameField, new Point(i, j)));
            }
        }
    }

    /**
     * Get color of cell by coordinates.
     *
     * @param gameField game field.
     * @param point coordinates of cell.
     * @return color of cell.
     */
    private Color getCellColor(GameField gameField, Point point) {
        switch (gameField.getCell(point)) {
            case SNAKEHEAD:
                return Color.DARKRED;
            case SNAKE:
                return Color.RED;
            case TARGET:
                return Color.BLUE;
            default:
                return Color.LIGHTGREEN;
        }
    }
}
