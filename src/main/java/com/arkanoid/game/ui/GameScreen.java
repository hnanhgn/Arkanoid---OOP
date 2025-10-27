package com.arkanoid.game.ui;

import com.arkanoid.game.entities.*;
import com.arkanoid.game.manager.BallManager;
import com.arkanoid.game.manager.BrickManager;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameScreen {
    private Pane root;
    private Paddle paddle;
    private BallManager ballManager;
    private BrickManager brickManager;
    private Lives lives;

    private int width_canvas = 600;
    private int height_canvas = 500;

    private Stage gameStage;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private AnimationTimer paddleMover;
    private ImageView backgroundView;


    public GameScreen() {
        root = new Pane();
        initializeGame();
        setupLivesDisplay();
    }


    public GameScreen(Stage stage) {
        this.gameStage = stage;
<<<<<<< Updated upstream
=======
        this.root = new Pane();
    }

    public GameScreen(Stage stage, int mode) {
        this.gameStage = stage;
        this.root = new Pane();
        this.mode = mode;
    }


    public GameScreen() {
>>>>>>> Stashed changes
        root = new Pane();
        initializeGame();
        setupLivesDisplay();
    }

    private void initializeGame() {

        backgroundView = new ImageView(new Image(
                getClass().getResourceAsStream("/images/background.png")
        ));
        backgroundView.setFitWidth(width_canvas);
        backgroundView.setFitHeight(height_canvas);
        backgroundView.setPreserveRatio(false);


        Canvas canvas = new Canvas(width_canvas, height_canvas);


        paddle = new Paddle(250, 450, 100, 20);
        paddle.setBoundary(0, width_canvas);

        root.getChildren().addAll(backgroundView, canvas, paddle.getNode());


        // Khởi tạo Brick
        brickManager = new BrickManager();
        for (Brick brick : brickManager.getBricks()) {
            root.getChildren().add(brick.getNode());
        }

        // Khởi tạo BallManager
        ballManager = new BallManager(canvas, paddle, brickManager, this, mode);
    }

    private void setupLivesDisplay() {
        // Tạo Lives với 3 mạng ban đầu, tối đa 3 mạng
        lives = new Lives(3, 3);
        root.getChildren().add(lives.getNode());
    }

    // Phương thức để BallManager truy cập
    public Lives getLives() {
        return lives;
    }

    public Pane createContent() {
        return root;
    }

    public void setupInputHandlers(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT -> leftPressed = true;
                case RIGHT -> rightPressed = true;
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case LEFT -> leftPressed = false;
                case RIGHT -> rightPressed = false;
            }
        });

        paddleMover = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (leftPressed) paddle.moveLeft();
                if (rightPressed) paddle.moveRight();
            }
        };
        paddleMover.start();
    }

    // Phương thức để restart game từ bên ngoài
    public void restartGame() {
        // Reset lives
        lives.reset();

        // Reset paddle position
        paddle.setPosition(250, 450);
        paddle.update();

        // Reset ball manager
        ballManager.restartGame();
    }

    public void checkGameOver() {
        if (lives.isGameOver()) {
            GameOverController.showGameOver(false, gameStage, mode);
        }
    }

    public Stage getGameStage() {
        return gameStage;
    }

}