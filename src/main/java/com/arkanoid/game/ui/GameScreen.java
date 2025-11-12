
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;

public class GameScreen {
    private Pane root;
    private Paddle paddle;
    private BallManager ballManager;
    private BrickManager brickManager;
    private Lives lives;
    private ItemManager itemManager;
    private PowerUpBall powerUpBall;

    private Stage gameStage;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private AnimationTimer paddleMover;
    private AnimationTimer itemLoop;
    private ImageView backgroundView;
    private int mode = 0;
    private Stage stage;
    private Text modeText;


    private boolean paused = false;

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

        paddle = new Paddle((250 + Config.WIDTH_CANVAS - Config.PADDLE_WIDTH) / 2, 600,
                Config.PADDLE_WIDTH, Config.PADDLE_HEIGHT);
        paddle.setBoundary(255, Config.WIDTH_CANVAS - 5);

        root.getChildren().addAll(backgroundView, canvas, paddle.getNode());

        modeText = new Text(String.valueOf(mode + 1));
        modeText.setFill(Color.web("#A8D8FF"));
        modeText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 60));
        modeText.setX(250 / 2 - 20);
        modeText.setY(180);

        root.getChildren().add(modeText);

        // Khởi tạo Brick

        switch (mode) {
            case 0:
                brickManager = new BrickManager0();
                break;
            case 1:
                brickManager = new BrickManager1();
                break;
            case 2:
                brickManager = new BrickManager2();
                break;
            case 3:
                brickManager = new BrickManager3();
                break;
            default:
                brickManager = new BrickManager0();
                break;
        }

        for (Brick brick : brickManager.getBricks()) {
            root.getChildren().add(brick.getNode());
        }
// KHỞI TẠO ITEM MANAGER TRƯỚC
        itemManager = new ItemManager(canvas, paddle);

        // Khởi tạo BallManager VỚI ITEM MANAGER
        ballManager = new BallManager(canvas, paddle, brickManager, this, mode, itemManager);

        // THIẾT LẬP BALL MANAGER CHO ITEM MANAGER
        itemManager.setBallManager(ballManager);

        // BẮT ĐẦU GAME LOOP CHO ITEMS
        startItemManagerLoop();
    }

    private void startItemManagerLoop() {
        itemLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // SỬA: Lấy điểm số từ BallManager và truyền cho ItemManager
                int currentScore = ballManager.getScore();
                itemManager.update(currentScore); // TRUYỀN SCORE VÀO ĐÂY

                // Vẽ items lên canvas
                renderItems();
            }
        };
        itemLoop.start();
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
                case LEFT:
                    leftPressed = true;
                    break;
                case RIGHT:
                    rightPressed = true;
                    break;
                case ESCAPE:
                    togglePause();
                    break;
                case SPACE:
                    // Gọi phương thức xử lý nổ từ BallManager
                    ballManager.handleSpacePressed();
                    break;
                default:
                    break;
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case LEFT:
                    leftPressed = false;
                    break;
                case RIGHT:
                    rightPressed = false;
                    break;
                default:
                    break;
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

    private void togglePause() {
        if (paused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    public void pauseGame() {
        paused = true;
        ballManager.pauseGameLoop();
        GamePauseController.showPause(gameStage, this);
    }

    public void resumeGame() {
        paused = false;
        ballManager.resumeGameLoop();
    }

    // Phương thức để restart game từ bên ngoài
    public void restartGame() {
        if (itemLoop != null) {
            itemLoop.stop();
        }
        // Reset lives
        lives.reset();

        // Reset paddle position
        paddle.setPosition((250 + Config.WIDTH_CANVAS - Config.PADDLE_WIDTH) / 2, 600);
        paddle.update();

        // Reset ball manager
        ballManager.restartGame();
        itemManager.reset();

        startItemManagerLoop();

    }
    private void startPaddleMover() {
        if (paddleMover != null) {
            paddleMover.stop();
        }

        paddleMover = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (leftPressed) paddle.moveLeft();
                if (rightPressed) paddle.moveRight();
            }
        };
        paddleMover.start();
    }


    private void renderItems() {
        for (javafx.scene.Node node : root.getChildren()) {
            if (node instanceof Canvas) {
                Canvas canvas = (Canvas) node;
                itemManager.render(canvas.getGraphicsContext2D());
                break;
            }
        }
    }

    public void checkGameOver() {
        if (lives.isGameOver()) {
            if (itemLoop != null) {
                itemLoop.stop();
            }
            int currentScore = ballManager.getScore();
            GameOverController.showGameOver(gameStage, false, mode, currentScore);
        }
    }

    public Stage getGameStage() {
        return gameStage;
    }

    public int getMode() {
        return mode;
    }
}
