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

public class Ball3Manager {
    private final Canvas canvas;
    private final Ball ball;
    private final Paddle paddle;
    private final BrickManager brickManager;
    private final GameScreen gameScreen;
    private double ball_speed = 2;
    private double ball_radius = 10;
    private Image ballImage;
    private Image explosionBallImage;
    private boolean ballActive = true;
    private long resetStartTime = 0;
    private final long RESET_DELAY = 1000;
    private AnimationTimer gameLoop;
    private boolean gameRunning = true;

    // Biến cho chế độ bóng nổ
    private boolean explosionMode = false; // xem là bóng nổ không
    private int explosionCount = 0;// đếm số lần bóng nổ
    private final int MAX_EXPLOSIONS = 2;
    private final double EXPLOSION_RADIUS = 70; // Bán kính vùng nổ

    public Ball3Manager(Canvas canvas, Paddle paddle, BrickManager brickManager, GameScreen gameScreen) {
        this.canvas = canvas;
        this.paddle = paddle;
        this.brickManager = brickManager;
        this.gameScreen = gameScreen;
        this.ballImage = new Image(getClass().getResourceAsStream("/images/ball1.png"));
        this.explosionBallImage = new Image(getClass().getResourceAsStream("/images/explosion_ball.png"));

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

    // Phương thức công khai để GameScreen gọi khi bấm SPACE
    public void handleSpacePressed() {
        if (ballActive && explosionMode) {
            activateExplosion();
        }
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

                if (ballActive) {
                    ball.update();

                    // Kiểm tra va chạm với gạch
                    Brick collidedBrick = checkBrickCollisions();
                    if (collidedBrick != null) {
                        ball.handleBrickBounce(collidedBrick);
                        collidedBrick.destroy();

                        // KHI CHẠM GẠCH: TỰ ĐỘNG CHUYỂN SANG CHẾ ĐỘ BÓNG NỔ
                        if (!explosionMode) {
                            activateExplosionMode();
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
                            resetExplosionMode(); // Reset chế độ nổ khi reset bóng
                        }
                    }
                }
            }
        };
        gameLoop.start();
    }

    // Kích hoạt chế độ bóng nổ (khi chạm gạch)
    private void activateExplosionMode() {
        explosionMode = true;
        explosionCount = 0; // Reset số lần nổ
        System.out.println("Explosion mode activated! Press SPACE to explode");
    }

    // Kích hoạt vụ nổ (khi bấm SPACE)
    private void activateExplosion() {
        if (explosionMode && explosionCount < MAX_EXPLOSIONS) {
            explosionCount++;

            // Phá hủy các gạch trong vùng nổ
            destroyBricksInExplosionRadius();

            System.out.println("Explosion " + explosionCount + "/" + MAX_EXPLOSIONS + " activated!");

            // Nếu đã nổ 2 lần, trở về bóng bình thường
            if (explosionCount >= MAX_EXPLOSIONS) {
                deactivateExplosionMode();
            }
        }
    }

    // Tắt chế độ bóng nổ
    private void deactivateExplosionMode() {
        explosionMode = false;
        explosionCount = 0;
        System.out.println("Explosion mode deactivated - Back to normal ball");
    }

    // Reset chế độ nổ
    private void resetExplosionMode() {
        explosionMode = false;
        explosionCount = 0;
    }

    // Phá hủy gạch trong vùng nổ
    private void destroyBricksInExplosionRadius() {
        int bricksDestroyed = 0;

        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed()) {
                // Tính khoảng cách từ bóng đến gạch
                double distance = calculateDistance(ball.getX(), ball.getY(),
                        brick.getX() + brick.getWidth() / 2,
                        brick.getY() + brick.getHeight() / 2);

                // Nếu gạch nằm trong vùng nổ, phá hủy nó
                if (distance <= EXPLOSION_RADIUS) {
                    brick.destroy();
                    bricksDestroyed++;
                }
            }
        }

        System.out.println("Explosion destroyed " + bricksDestroyed + " bricks");

        // Hiệu ứng hình ảnh vụ nổ
        showExplosionEffect();
    }

    // Tính khoảng cách giữa hai điểm
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // Hiệu ứng vụ nổ
    private void showExplosionEffect() {
        // Có thể thêm animation vụ nổ ở đây
        System.out.println("BOOM! Explosion effect at (" + ball.getX() + ", " + ball.getY() + ")");
    }

    // Kiểm tra va chạm với gạch
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
            Image currentBallImage = explosionMode ? explosionBallImage : ballImage;

            gc.drawImage(currentBallImage,
                    ball.getX() - ball.getRadius(),
                    ball.getY() - ball.getRadius(),
                    diameter, diameter);

            // ĐÃ BỎ PHẦN HIỂN THỊ SỐ LẦN NỔ
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
        resetExplosionMode();

        resetToDefault();
        brickManager.resetBricks();
        startAnimation();
    }

    // Getter để kiểm tra trạng thái
    public boolean isExplosionMode() {
        return explosionMode;
    }

    public int getExplosionCount() {
        return explosionCount;
    }
}