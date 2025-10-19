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

public class Ball1Manager {
    private final Canvas canvas;
    private final Paddle paddle;
    private final BrickManager brickManager;
    private final GameScreen gameScreen;
    private double ball_speed = 2;
    private double ball_radius = 10;
    private Image ballImage;
    private AnimationTimer gameLoop;
    private boolean gameRunning = true;
    private boolean multiBallActivated = false;

    // Danh sách các bóng (tất cả đều bình đẳng)
    private List<Ball> balls;

    public Ball1Manager(Canvas canvas, Paddle paddle, BrickManager brickManager, GameScreen gameScreen) {
        this.canvas = canvas;
        this.paddle = paddle;
        this.brickManager = brickManager;
        this.gameScreen = gameScreen;
        this.ballImage = new Image(getClass().getResourceAsStream("/images/ball1.png"));
        this.balls = new ArrayList<>();

        // Tạo bóng đầu tiên
        createNewBall();

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

                // Cập nhật và vẽ tất cả các bóng
                boolean anyBallActive = false;
                List<Ball> ballsToRemove = new ArrayList<>();

                for (Ball ball : balls) {
                    ball.update();

                    // Kiểm tra va chạm với gạch
                    Brick collidedBrick = checkBrickCollisions(ball);

                    // Xử lý bật lại nếu có va chạm
                    if (collidedBrick != null) {
                        ball.handleBrickBounce(collidedBrick);
                        collidedBrick.destroy();

                        // Kích hoạt multi-ball khi chạm gạch lần đầu
                        if (!multiBallActivated) {
                            activateMultiBall();
                            multiBallActivated = true;
                        }
                    }

                    // Kiểm tra bóng rơi khỏi màn hình
                    if (ball.getY() + ball.getRadius() >= canvas.getHeight()) {
                        // Đánh dấu bóng để xóa
                        ballsToRemove.add(ball);
                    } else {
                        // Bóng vẫn đang hoạt động
                        anyBallActive = true;
                        renderBall(gc, ball);
                    }
                }

                // Xóa các bóng đã rơi xuống
                balls.removeAll(ballsToRemove);

                // Kiểm tra chiến thắng (phá hết gạch)
                if (allBricksDestroyed()) {
                    stopGameLoop();
                    Platform.runLater(() -> {
                        showGameOver(true);
                    });
                    return;
                }

                // CHỈ mất mạng khi TẤT CẢ bóng đều rơi xuống
                if (!anyBallActive && balls.isEmpty()) {
                    gameScreen.getLives().decreaseLife();

                    if (gameScreen.getLives().getLives() <= 0) {
                        stopGameLoop();
                        Platform.runLater(() -> {
                            showGameOver(false);
                        });
                        return;
                    } else {
                        // Tạo lại bóng mới
                        createNewBall();
                        multiBallActivated = false;
                    }
                }
            }
        };
        gameLoop.start();
    }

    // Kích hoạt hiệu ứng multi-ball
    private void activateMultiBall() {
        // Tạo thêm 2 bóng mới
        for (int i = 0; i < 2; i++) {
            createNewBall();
        }
    }

    // Tạo bóng mới với hướng ngẫu nhiên
    private void createNewBall() {
        double startX = paddle.getX() + paddle.getWidth() / 2;
        double startY = paddle.getY() - ball_radius - 5;

        Ball newBall = new Ball(startX, startY, ball_radius, ballImage,
                canvas.getWidth(), canvas.getHeight(), ball_speed);
        newBall.setPaddle(paddle);
        balls.add(newBall);
    }

    // Kiểm tra va chạm với gạch cho một bóng cụ thể
    private Brick checkBrickCollisions(Ball ball) {
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

    // Hàm hiển thị màn hình game over
    private void showGameOver(boolean isWin) {
        Stage currentStage = (Stage) canvas.getScene().getWindow();
        GameOverController.showGameOver(isWin, currentStage);
    }

    private void renderBall(GraphicsContext gc, Ball ball) {
        double diameter = ball.getRadius() * 2;
        gc.drawImage(ball.getBallImage(),
                ball.getX() - ball.getRadius(),
                ball.getY() - ball.getRadius(),
                diameter, diameter);
    }

    // Phương thức restart
    public void restartGame() {
        stopGameLoop();
        gameRunning = true;
        multiBallActivated = false;

        // Reset danh sách bóng
        balls.clear();

        // Tạo lại bóng đầu tiên
        createNewBall();

        // Reset gạch
        brickManager.resetBricks();

        startAnimation();
    }

    public void setBallImage(Image newImage) {
        this.ballImage = newImage;
    }
}