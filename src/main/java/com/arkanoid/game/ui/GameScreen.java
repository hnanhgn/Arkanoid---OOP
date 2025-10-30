package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import com.arkanoid.game.entities.*;
import com.arkanoid.game.manager.*;
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

    private Stage gameStage;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private AnimationTimer paddleMover;
    private ImageView backgroundView;
    private int mode = 0;
    private Stage stage;

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public GameScreen(Stage stage) {
        this.gameStage = stage;
        this.root = new Pane();
    }

    public GameScreen(Stage stage, int mode) {
        this.gameStage = stage;
        this.root = new Pane();
        this.mode = mode;
    }


    public GameScreen() {
        root = new Pane();
        initializeGame();
        setupLivesDisplay();
    }

    private void initializeGame() {

        backgroundView = new ImageView(new Image(
                getClass().getResourceAsStream("/images/background.png")
        ));
        backgroundView.setFitWidth(Config.WIDTH_CANVAS);
        backgroundView.setFitHeight(Config.HEIGHT_CANVAS);
        backgroundView.setPreserveRatio(false);

        Canvas canvas = new Canvas(Config.WIDTH_CANVAS, Config.HEIGHT_CANVAS);

        paddle = new Paddle((Config.WIDTH_CANVAS - Config.PADDLE_WIDTH) / 2, 600,
                Config.PADDLE_WIDTH, Config.PADDLE_HEIGHT);
        paddle.setBoundary(10, Config.WIDTH_CANVAS - 10);

        root.getChildren().addAll(backgroundView, canvas, paddle.getNode());

        // Khởi tạo Brick

        switch (mode) {
            case 0 -> brickManager = new BrickManager0();
            case 1 -> brickManager = new BrickManager1();
            case 2 -> brickManager = new BrickManager2();
            case 3 -> brickManager = new BrickManager3();
            default -> brickManager = new BrickManager0();
        }

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
        initializeGame();
        setupLivesDisplay();
        return root;
    }


    public Pane getRoot() {
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
        paddle.setPosition((Config.WIDTH_CANVAS - Config.PADDLE_WIDTH) / 2, 600);
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

    public int getMode() {
        return mode;
    }


}