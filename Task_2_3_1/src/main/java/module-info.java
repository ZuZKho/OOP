module snake {
    requires javafx.controls;
    requires javafx.fxml;

    opens snake to javafx.fxml;
    exports snake;
    exports snake.snake;
    opens snake.snake to javafx.fxml;
    exports snake.gamefield;
    opens snake.gamefield to javafx.fxml;
}