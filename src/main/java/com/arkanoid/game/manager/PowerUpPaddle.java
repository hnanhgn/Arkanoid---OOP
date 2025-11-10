package com.arkanoid.game.entities;

public class PowerUpPaddle {

    private final Paddle paddle;

    private boolean active = false;
    private long startTime;
    private final long DURATION = 8000;

    private double originalWidth;

    public PowerUpPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public void activate() {
        if (active) return;

        active = true;
        startTime = System.currentTimeMillis();

        originalWidth = paddle.getWidth();
        paddle.setWidth(originalWidth * 1.5);
        paddle.update();
    }

    public void update() {
        if (active && System.currentTimeMillis() - startTime > DURATION) {
            deactivate();
        }
    }

    public void deactivate() {
        active = false;
        paddle.setWidth(originalWidth);
        paddle.update();
    }
}