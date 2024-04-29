package snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static java.lang.Math.max;

public class SnakeApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        createStage(primaryStage);
        timeline = new Timeline(new KeyFrame(Duration.millis(initialSpeed), frameEventHandler));
        timeline.setCycleCount(Timeline.INDEFINITE);
        startGame();
        timeline.play();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void startGame() {
        maximumScore = max(maximumScore, currentScore);
        lastScore = currentScore;
        currentScore = 0;
        maximumScoreLabel.setText("Maximum score: " + maximumScore);
        lastScoreLabel.setText("Last score: " + lastScore);
        currentScoreLabel.setText("Current score: " + currentScore);
        isPaused = false;
        pauseLabel.setVisible(false);
        game = new Game(width, height, targetsCount);
        renderer = new Renderer(game.getRenderData(), root, viewportWidth, viewportHeight);
    }

    private EventHandler<ActionEvent> frameEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            // Getting pressed key
            KeyCode currentKey = null;
            if (lastPressedKey != null) {
                synchronized (lastPressedKey) {
                    currentKey = lastPressedKey;
                    lastPressedKey = null;
                }
            }

            // Checking pause request
            if (currentKey == KeyCode.SPACE) {
                pauseLabel.toFront();
                if (isPaused) {
                    pauseLabel.setVisible(false);
                } else {
                    pauseLabel.setVisible(true);
                }
                isPaused = !isPaused;
            }

            // Game loss check
            if (!isPaused && !game.update(currentKey)) {
                startGame();
            }

            renderer.Render(game.getRenderData());
            currentScore = game.getScore();
            currentScoreLabel.setText("Current score: " + currentScore);
            double currentSpeed = max(100, initialSpeed - game.getScore() * 10);
            timeline.setRate((double) initialSpeed / currentSpeed);
        }
    };

    private void createStage(Stage primaryStage) {
        // Setting scene
        Scene primaryScene = new Scene(root, viewportWidth, viewportHeight);
        root.requestFocus();
        primaryScene.setOnKeyPressed(event -> {
            lastPressedKey = event.getCode();
        });
        primaryScene.setOnMouseClicked(event -> {
            root.requestFocus();
        });

        pauseLabel = new Label("Pause");
        pauseLabel.setStyle("-fx-padding: 200; -fx-background-color: rgba(255, 255, 255, 0.8); -fx-font-size: 250; -fx-color: rgb(252,252,252); -fx-font-weight: bold");
        root.getChildren().add(pauseLabel);

        maximumScoreLabel = new Label();
        maximumScoreLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold");
        lastScoreLabel = new Label();
        lastScoreLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold");
        currentScoreLabel = new Label();
        currentScoreLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold");

        Label guideLabel = new Label("Controls - arrows\nPause - space\n");
        guideLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold");
        Label widthLabel = new Label("Field width:");
        Slider widthSlider = new Slider(10, 30, 15);
        setupSlider(widthSlider);

        Label heightLabel = new Label("Field height:");
        Slider heightSlider = new Slider(10, 30, 15);
        setupSlider(heightSlider);

        Label targetLabel = new Label("Field food amount:");
        Slider targetSlider = new Slider(1, 10, 2);
        setupSlider(targetSlider);
        targetSlider.setMajorTickUnit(1);
        targetSlider.setMinorTickCount(0);


        Button buttonApply = new Button("Apply");
        buttonApply.setOnMouseClicked(event -> {
            this.height = (int) heightSlider.getValue();
            this.width = (int) widthSlider.getValue();
            this.targetsCount = (int) targetSlider.getValue();
        });
        Button buttonApplyRestart = new Button("Apply and Restart");
        buttonApplyRestart.setOnMouseClicked(event -> {
            this.height = (int) heightSlider.getValue();
            this.width = (int) widthSlider.getValue();
            this.targetsCount = (int) targetSlider.getValue();
            startGame();
            root.requestFocus();
        });

        HBox hbox = new HBox(buttonApply, buttonApplyRestart);
        VBox scoreVbox = new VBox(currentScoreLabel, lastScoreLabel, maximumScoreLabel);
        scoreVbox.setTranslateX(1100);
        scoreVbox.setTranslateY(50);
        root.getChildren().add(scoreVbox);
        VBox vbox = new VBox(guideLabel, widthLabel, widthSlider, heightLabel, heightSlider, targetLabel, targetSlider, hbox);
        vbox.setTranslateX(1100);
        vbox.setTranslateY(250);
        root.getChildren().add(vbox);

        // Setting stage
        primaryStage.setTitle("Snake");
        primaryStage.setResizable(false);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    private void setupSlider(Slider slider) {
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(4);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);
    }

    private Game game;
    private Renderer renderer;
    private Timeline timeline;
    private KeyCode lastPressedKey = null;
    private Group root = new Group();
    private Label pauseLabel;
    private Label lastScoreLabel, maximumScoreLabel, currentScoreLabel;
    private int currentScore = 0, maximumScore = 0, lastScore = 0;
    private int initialSpeed = 400;
    private int viewportWidth = 1280, viewportHeight = 720;
    private int width = 15;
    private int height = 15;
    private int targetsCount = 2;
    private boolean isPaused = false;
}
