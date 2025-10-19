package com.arkanoid.game.ui;

import com.arkanoid.game.entities.Ball;
import com.arkanoid.game.entities.Brick;
import com.arkanoid.game.manager.BallManager;
import com.arkanoid.game.manager.Ball1Manager;
import com.arkanoid.game.manager.Ball2Manager;
import com.arkanoid.game.manager.Ball3Manager;
import com.arkanoid.game.manager.BrickManager;
import com.arkanoid.game.entities.Paddle;
import com.arkanoid.game.entities.Lives;
import javafx.scene.canvas.Canvas;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameScreen {
    private Pane root;
    private Paddle paddle;
    private Ball3Manager ballManager;
    private BrickManager brickManager;
    private Lives lives;
    private int width_canvas = 600;
    private int height_canvas = 500;
    private Stage gameStage;

    public GameScreen() {
        root = new Pane();
        initializeGame();
        setupLivesDisplay();
    }

    public GameScreen(Stage stage) {
        this.gameStage = stage;
        root = new Pane();
        initializeGame();
        setupLivesDisplay();
    }

    private void initializeGame() {
        // Tạo canvas cho ball
        Canvas canvas = new Canvas(width_canvas, height_canvas);
        root.getChildren().add(canvas);

        paddle = new Paddle(250, 450, 100, 20);
        paddle.setBoundary(0, width_canvas);
        root.getChildren().add(paddle.getNode());

        // Khởi tạo Brick
        brickManager = new BrickManager();
        for (Brick brick : brickManager.getBricks()) {
            root.getChildren().add(brick.getNode());
        }

        // Khởi tạo BallManager
        ballManager = new Ball3Manager(canvas, paddle, brickManager, this);
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
                case LEFT:
                    paddle.moveLeft();
                    break;
                case RIGHT:
                    paddle.moveRight();
                    break;
                case SPACE:
                    // Gọi phương thức xử lý nổ từ Ball3Manager
                    ballManager.handleSpacePressed();
                    break;
                default:
                    break;
            }
        });
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
            GameOverController.showGameOver(false, gameStage);
        }
    }

    public Stage getGameStage() {
        return gameStage;
    }
}