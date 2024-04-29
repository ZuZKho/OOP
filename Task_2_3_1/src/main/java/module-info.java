module snake {
    requires javafx.controls;
    requires javafx.fxml;

    opens snake to javafx.fxml;
    exports snake;
    exports snake.snake;
    opens snake.snake to javafx.fxml;
    exports snake.GameField;
    opens snake.GameField to javafx.fxml;
}