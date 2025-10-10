package com.arkanoid.game.manager;

import com.arkanoid.game.entities.Ball;
import com.arkanoid.game.entities.Paddle;
import com.arkanoid.game.entities.Brick;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class BallManager {
    private final Canvas canvas;
    private final Ball ball;
    private final Paddle paddle;
    private final BrickManager brickManager ;
    private double ball_speed = 2;
    private double ball_radius = 12;
    private Image ballImage;
    private boolean ballActive = true;
    private long resetStartTime = 0;
    private final long RESET_DELAY = 1000;

    public BallManager(Canvas canvas, Paddle paddle, BrickManager brickManager) {
        this.canvas = canvas;
        this.paddle = paddle;
        this.brickManager = brickManager;
        this.ballImage = new Image(getClass().getResourceAsStream("/images/ball1.png"));

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

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Xóa nền
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                if (ballActive) {
                    ball.update();
                    // Kiểm tra va chạm với gạch
                    checkBrickCollisions();
                    if (ball.getY() + ball.getRadius() >= canvas.getHeight()) {
                        ballActive = false;
                        resetStartTime = System.currentTimeMillis();
                    }

                    renderBall(gc);
                } else {
                    // Chờ hết thời gian delay
                    if (System.currentTimeMillis() - resetStartTime >= RESET_DELAY) {
                        resetToDefault();
                        ballActive = true;
                    }
                }
            }
        }.start();
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
}