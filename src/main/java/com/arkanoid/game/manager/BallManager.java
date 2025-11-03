package com.arkanoid.game.manager;

import com.arkanoid.game.entities.Ball;
import com.arkanoid.game.entities.Paddle;
import com.arkanoid.game.entities.Brick;
import com.arkanoid.game.ui.GameScreen;
import com.arkanoid.game.ui.GameOverController;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class BallManager {
    private final Canvas canvas;
    private final Paddle paddle;
    private final BrickManager brickManager;
    private final GameScreen gameScreen;
    private final ItemManager itemManager;

    private PowerUpBall powerUpBall;

    private AnimationTimer gameLoop;
    private boolean gameRunning = true;

    // Thêm hệ thống score
    private final Score scoreManager;

    public BallManager(Canvas canvas, Paddle paddle, BrickManager brickManager, GameScreen gameScreen, ItemManager itemManager) {
        this.canvas = canvas;
        this.paddle = paddle;
        this.brickManager = brickManager;
        this.gameScreen = gameScreen;
        this.itemManager = itemManager;

        this.scoreManager = new Score();
        this.powerUpBall = new PowerUpBall(canvas, paddle, brickManager, gameScreen, itemManager, scoreManager);

        startAnimation();
    }

    // Constructor cũ cho tương thích
    public BallManager(Canvas canvas, Paddle paddle, BrickManager brickManager, GameScreen gameScreen) {
        this(canvas, paddle, brickManager, gameScreen, null);
    }

    private void startAnimation() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameRunning) return;

                // Xóa nền
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // KIỂM TRA ITEM EFFECTS
                powerUpBall.checkItemEffects();

                // KIỂM TRA THỜI GIAN GHOST MODE
                powerUpBall.checkModesDuration();

                if (powerUpBall.isActive()) {
                    // CẬP NHẬT TẤT CẢ BÓNG
                    powerUpBall.updateBalls();

                    // Kiểm tra chiến thắng
                    if (powerUpBall.isAllBricksDestroyed()) {
                        stopGameLoop();
                        Platform.runLater(() -> {
                            showGameOver(true);
                        });
                        return;
                    }

                    powerUpBall.render(gc);
                    scoreManager.render(gc);
                } else {
                    if (System.currentTimeMillis() - powerUpBall.getResetStartTime() >= powerUpBall.getResetDelay()) {
                        gameScreen.getLives().decreaseLife();

                        if (gameScreen.getLives().getLives() > 0) {
                            powerUpBall.resetToDefault();
                            powerUpBall.setActive(true);
                        } else {
                            stopGameLoop();
                            Platform.runLater(() -> {
                                showGameOver(false);
                            });
                        }
                    }
                }
            }
        };
        gameLoop.start();
    }

    public void handleSpacePressed() {
        powerUpBall.handleSpacePressed();
    }

    public void stopGameLoop() {
        gameRunning = false;
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public void showGameOver(boolean isWin) {
        Stage currentStage = (Stage) canvas.getScene().getWindow();
        GameOverController.showGameOver(isWin, currentStage);
    }

    // SỬA QUAN TRỌNG: Phương thức restartGame hoàn chỉnh
    public void restartGame() {
        stopGameLoop();
        gameRunning = true;

        // Reset hoàn toàn
        scoreManager.reset();

        // SỬA: Reset ItemManager để không còn item effect nào active
        if (itemManager != null) {
            itemManager.reset(); // Cần thêm phương thức reset() trong ItemManager
        }

        // Reset gạch
        brickManager.resetBricks();

        // Reset power-up ball
        powerUpBall.reset();

        // Khởi động lại game loop
        startAnimation();
    }

    public Score getScoreManager() {
        return scoreManager;
    }

    public int getScore() {
        return scoreManager.getScore();
    }
}