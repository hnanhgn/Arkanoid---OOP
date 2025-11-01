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
import javafx.stage.Stage;

public class BallManager {
    private final Canvas canvas;
    private final Ball ball;
    private final Paddle paddle;
    private final BrickManager brickManager;
    private final GameScreen gameScreen;
    private double ball_speed = 4;
    private double ball_radius = 12;
    private Image ballImage;
    private boolean ballActive = true;
    private long resetStartTime = 0;
    private final long RESET_DELAY = 1000;
    private AnimationTimer gameLoop;
    private boolean gameRunning = true;

    private final Score scoreManager;

    public BallManager(Canvas canvas, Paddle paddle, BrickManager brickManager, GameScreen gameScreen) {
        this.canvas = canvas;
        this.paddle = paddle;
        this.brickManager = brickManager;
        this.gameScreen = gameScreen;
        this.ballImage = new Image(getClass().getResourceAsStream("/images/ball1.png"));
        this.scoreManager = new Score();

        double defaultX = paddle.getX() + paddle.getWidth() / 2;
        double defaultY = paddle.getY() - ball_radius - 5;

        this.ball = new Ball(defaultX,
                defaultY,
                ball_radius,
                ballImage,
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
                    scoreManager.render(gc);
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
        GameOverController.showGameOver(isWin, currentStage);
    }

    // Hàm kiểm tra va chạm với tất cả gạch
    private void checkBrickCollisions() {
        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed() && brick.getType() != 1) { // bỏ qua gạch "không khí"

                // Kiểm tra có va chạm thật không
                if (ball.checkCollisionWithBrick(brick)) {
                    // Xử lý loại 2
                    if (brick.getType() == 2) {
                        int tmp = brick.getHitCount();
                        brick.setHitCount(tmp + 1);
                        if (tmp + 1 >= 2) {
                            brick.destroy();
                            scoreManager.increaseScore(1);
                        } else {
                            if (brick.getOverlay() != null)
                                brick.getOverlay().setVisible(false);
                        }
                    }
                    // Các loại gạch thường thì phá ngay
                    else if (brick.getType() == 0) {
                        brick.destroy();
                        scoreManager.increaseScore(1);
                    }

                    // Sau khi xử lý 1 gạch, thoát vòng lặp
                    break;
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

    public void pauseGameLoop() {
        gameRunning = false;
    }

    public void resumeGameLoop() {
        gameRunning = true;
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