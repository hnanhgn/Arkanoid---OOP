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

public class BallManager {
    private final Canvas canvas;
    private final Ball ball;
    private final Paddle paddle;
    private final BrickManager brickManager;
    private final GameScreen gameScreen;
    private double ball_speed = 3;
    private double ball_radius = 12;
    private Image ballImage;
    private boolean ballActive = true;
    private long resetStartTime = 0;
    private final long RESET_DELAY = 1000;
    private AnimationTimer gameLoop;
    private boolean gameRunning = true;
    private int mode;


<<<<<<< Updated upstream
    public BallManager(Canvas canvas, Paddle paddle, BrickManager brickManager, GameScreen gameScreen) {
=======
    private final Score scoreManager;

    public BallManager(Canvas canvas, Paddle paddle, BrickManager brickManager, GameScreen gameScreen,int mode) {
>>>>>>> Stashed changes
        this.canvas = canvas;
        this.paddle = paddle;
        this.brickManager = brickManager;
        this.gameScreen = gameScreen;
        this.ballImage = new Image(getClass().getResourceAsStream("/images/ball1.png"));
<<<<<<< Updated upstream

=======
        this.scoreManager = new Score();
        this.mode = mode;
>>>>>>> Stashed changes
        double defaultX = paddle.getX() + paddle.getWidth() / 2;
        double defaultY = paddle.getY() - ball_radius - 5;

        this.ball = new Ball(defaultX,
                defaultY,
                ball_radius,
                ballImage,
                canvas.getWidth(),
                canvas.getHeight(),
                ball_speed);
        ball.setPaddle(paddle);
        startAnimation();
    }

    private void startAnimation() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameRunning) return;

                // Xóa nền
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                


                if (ballActive) {
                    ball.update();
                    // Kiểm tra va chạm với gạch
                    checkBrickCollisions();

                    // Kiểm tra chiến thắng (phá hết gạch)
                    if (allBricksDestroyed()) {
                        stopGameLoop();
                        Platform.runLater(() -> {
                            showGameOver(true);
                        });
                        return;
                    }

                    // Kiểm tra bóng rơi khỏi màn hình
                    if (ball.getY() + ball.getRadius() >= canvas.getHeight()) {
                        ballActive = false;
                        resetStartTime = System.currentTimeMillis();

                        // Giảm mạng khi bóng rơi khỏi màn hình
                        gameScreen.getLives().decreaseLife();

                        // Kiểm tra thua (hết mạng) - ĐIỀU KIỆN KẾT THÚC
                        if (gameScreen.getLives().getLives() <= 0) {
                            stopGameLoop();
                            Platform.runLater(() -> {
                                showGameOver(false);
                            });
                            return;
                        }
                    }

                    renderBall(gc);
                } else {
                    // Chờ hết thời gian delay
                    if (System.currentTimeMillis() - resetStartTime >= RESET_DELAY) {
                        // Chỉ reset nếu còn mạng
                        if (gameScreen.getLives().getLives() > 0) {
                            resetToDefault();
                            ballActive = true;
                        }
                    }
                }
            }
        };
        gameLoop.start();
    }

    // Hàm kiểm tra tất cả gạch đã bị phá hủy chưa
    private boolean allBricksDestroyed() {
        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    // Hàm dừng game loop
    private void stopGameLoop() {
        gameRunning = false;
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    // Hàm hiển thị màn hình game over
    private void showGameOver(boolean isWin) {
        Stage currentStage = (Stage) canvas.getScene().getWindow();
        GameOverController.showGameOver(isWin, currentStage, mode);
    }

    // Hàm kiểm tra va chạm với tất cả gạch
    private void checkBrickCollisions() {
        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed()) {
                if (ball.checkCollisionWithBrick(brick)) {
                    brick.destroy();
                    break; // Chỉ xử lý 1 brick mỗi frame
                }
            }
        }
    }

    public void resetToDefault() {
        double defaultX = paddle.getX() + paddle.getWidth() / 2;
        double defaultY = paddle.getY() - ball_radius - 5;
        ball.resetToDefault(defaultX, defaultY);
    }

    private void renderBall(GraphicsContext gc) {
        if (ballActive) {
            double diameter = ball.getRadius() * 2;
            gc.drawImage(ball.getBallImage(),
                    ball.getX() - ball.getRadius(),
                    ball.getY() - ball.getRadius(),
                    diameter, diameter);
        }
    }

    public void setBallImage(Image newImage) {
        this.ballImage = newImage;
    }

    // Phương thức để restart game - CẬP NHẬT LẠI GAME
    public void restartGame() {
        // Dừng game loop cũ
        stopGameLoop();

        // Reset trạng thái game
        gameRunning = true;
        ballActive = true;

        // Reset bóng
        resetToDefault();

        // Reset gạch
        brickManager.resetBricks();

        // Bắt đầu game loop mới
        startAnimation();
    }
}