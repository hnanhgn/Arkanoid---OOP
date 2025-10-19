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

public class Ball2Manager {
    private final Canvas canvas;
    private final Ball ball;
    private final Paddle paddle;
    private final BrickManager brickManager;
    private final GameScreen gameScreen;
    private double ball_speed = 2;
    private double ball_radius = 12;
    private Image ballImage;
    private boolean ballActive = true;
    private long resetStartTime = 0;
    private final long RESET_DELAY = 1000;
    private AnimationTimer gameLoop;
    private boolean gameRunning = true;

    // Biến cho chế độ xuyên gạch
    private boolean ghostMode = false;
    private long ghostModeStartTime = 0;
    private final long GHOST_MODE_DURATION = 5000; // 5 giây
    private Image ghostBallImage;

    public Ball2Manager(Canvas canvas, Paddle paddle, BrickManager brickManager, GameScreen gameScreen) {
        this.canvas = canvas;
        this.paddle = paddle;
        this.brickManager = brickManager;
        this.gameScreen = gameScreen;
        this.ballImage = new Image(getClass().getResourceAsStream("/images/ball1.png"));
        this.ghostBallImage = new Image(getClass().getResourceAsStream("/images/ghost_ball.png"));

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
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Kiểm tra thời gian chế độ xuyên gạch
                checkGhostModeDuration();

                if (ballActive) {
                    ball.update();

                    // Xử lý va chạm với gạch tùy theo chế độ
                    if (ghostMode) {
                        // Chế độ xuyên: phá gạch mà không bật lại
                        destroyBricksInPath();
                    } else {
                        // Chế độ bình thường: kiểm tra va chạm và bật lại
                        Brick collidedBrick = checkBrickCollisions();
                        if (collidedBrick != null) {
                            ball.handleBrickBounce(collidedBrick);
                            collidedBrick.destroy();

                            // Kích hoạt chế độ xuyên gạch
                            activateGhostMode();
                        }
                    }

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

                        gameScreen.getLives().decreaseLife();

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
                    if (System.currentTimeMillis() - resetStartTime >= RESET_DELAY) {
                        if (gameScreen.getLives().getLives() > 0) {
                            resetToDefault();
                            ballActive = true;
                            deactivateGhostMode(); // Tắt chế độ xuyên khi reset
                        }
                    }
                }
            }
        };
        gameLoop.start();
    }

    // Kích hoạt chế độ xuyên gạch
    private void activateGhostMode() {
        ghostMode = true;
        ghostModeStartTime = System.currentTimeMillis();
        System.out.println("Ghost mode activated! Duration: 5 seconds");
    }

    // Tắt chế độ xuyên gạch
    private void deactivateGhostMode() {
        ghostMode = false;
        System.out.println("Ghost mode deactivated");
    }

    // Kiểm tra thời gian chế độ xuyên gạch
    private void checkGhostModeDuration() {
        if (ghostMode) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - ghostModeStartTime;

            if (elapsedTime >= GHOST_MODE_DURATION) {
                deactivateGhostMode();
            }
        }
    }

    // Phá gạch trên đường đi mà không bật lại (chế độ xuyên)
    private void destroyBricksInPath() {
        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed()) {
                if (ball.checkCollisionWithBrick(brick)) {
                    brick.destroy();
                }
            }
        }
    }

    // Kiểm tra va chạm với gạch (chỉ dùng khi không ở chế độ xuyên)
    private Brick checkBrickCollisions() {
        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed()) {
                if (ball.checkCollisionWithBrick(brick)) {
                    return brick;
                }
            }
        }
        return null;
    }

    private boolean allBricksDestroyed() {
        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    private void stopGameLoop() {
        gameRunning = false;
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    private void showGameOver(boolean isWin) {
        Stage currentStage = (Stage) canvas.getScene().getWindow();
        GameOverController.showGameOver(isWin, currentStage);
    }

    public void resetToDefault() {
        double defaultX = paddle.getX() + paddle.getWidth() / 2;
        double defaultY = paddle.getY() - ball_radius - 5;
        ball.resetToDefault(defaultX, defaultY);
    }

    private void renderBall(GraphicsContext gc) {
        if (ballActive) {
            double diameter = ball.getRadius() * 2;

            // Sử dụng ảnh khác nhau tùy theo chế độ
            Image currentBallImage = ghostMode ? ghostBallImage : ballImage;

            gc.drawImage(currentBallImage,
                    ball.getX() - ball.getRadius(),
                    ball.getY() - ball.getRadius(),
                    diameter, diameter);

            // Hiển thị thời gian còn lại của chế độ xuyên (nếu đang active)
            if (ghostMode) {
                long remainingTime = GHOST_MODE_DURATION - (System.currentTimeMillis() - ghostModeStartTime);
                double secondsLeft = Math.max(0, remainingTime) / 1000.0;

                // Style giống Lives display: nền đen trong suốt, bo góc, màu trắng
                String text = String.format("Ghost Mode: %.1fs", secondsLeft);

                // Tính toán kích thước và vị trí
                gc.setFont(javafx.scene.text.Font.font("Arial", 14)); // Giảm cỡ chữ xuống 14
                double textWidth = gc.getFont().getSize() * text.length() * 0.5;
                double boxWidth = textWidth + 16; // Thêm padding
                double boxHeight = 24; // Giảm chiều cao box
                double x = (canvas.getWidth() - boxWidth) / 2;
                double y = 10;

                // Vẽ nền giống Lives display
                gc.setFill(Color.rgb(0, 0, 0, 0.5));
                gc.fillRoundRect(x, y, boxWidth, boxHeight, 10, 10);

                // Vẽ text màu trắng
                gc.setFill(Color.WHITE);
                gc.fillText(text, x + 8, y + 16); // Căn chỉnh text trong box
            }
        }
    }

    public void setBallImage(Image newImage) {
        this.ballImage = newImage;
    }

    // Phương thức để restart game
    public void restartGame() {
        stopGameLoop();
        gameRunning = true;
        ballActive = true;
        deactivateGhostMode(); // Tắt chế độ xuyên khi restart

        resetToDefault();
        brickManager.resetBricks();
        startAnimation();
    }
}