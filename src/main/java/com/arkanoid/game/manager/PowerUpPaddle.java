
package com.arkanoid.game.manager;

import com.arkanoid.game.entities.Paddle;

public class PowerUpPaddle implements PowerUp{

    private final Paddle paddle;
    private boolean active = false;
    private long startTime;
    private final long DURATION = 8000; // 8 seconds
    private double originalWidth;

    public PowerUpPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    @Override
    public void activate() {
        if (active) return;

        active = true;
        startTime = System.currentTimeMillis();
        originalWidth = paddle.getWidth();
        paddle.setWidth(originalWidth * 1.5);
        paddle.update();
    }

    @Override
    public void update() {
        if (active && System.currentTimeMillis() - startTime > DURATION) {
            deactivate();
        }
    }

    @Override
    public void deactivate() {
        if (!active) return;
        active = false;
        paddle.setWidth(originalWidth);
        paddle.update();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return DURATION;
    }
}
