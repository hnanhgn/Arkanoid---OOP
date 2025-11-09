package com.arkanoid.game.manager;

import com.arkanoid.game.entities.Ball;
import javafx.scene.image.Image;

public class PowerUpBall extends Ball {
    private boolean ghostActive = false;
    private long ghostStartTime = 0;
    private final long GHOST_DURATION = 10000;

    private boolean explosionActive = false;
    private int explosionCharges = 0;
    public static final int MAX_CHARGES = 2;

    private boolean speedBoostActive = false;
    private long speedStartTime = 0;
    private final long SPEED_DURATION = 10000;

    private Image normalBallImage;
    private Image ghostBallImage;
    private Image explosionBallImage;
    private Image speedBallImage;

    public PowerUpBall(double x, double y, double radius, Image ballImage, double speed) {
        super(x, y, radius, ballImage, speed);
        loadImages();
        updateBallImage();
    }

    private void loadImages() {
        try {
            this.normalBallImage = new Image(getClass().getResourceAsStream("/images/ball1.png"));
            this.ghostBallImage = new Image(getClass().getResourceAsStream("/images/Piercing_Ball.png"));
            this.explosionBallImage = new Image(getClass().getResourceAsStream("/images/ball2.png"));
            this.speedBallImage = new Image(getClass().getResourceAsStream("/images/speed_boost.png")); // Giả sử có ảnh riêng, nếu không dùng ball1.png
        } catch (Exception e) {
            System.out.println("Error loading ball images: " + e.getMessage());
        }
    }

    public void activateGhost(long startTime) {
        if (ghostActive) return;
        ghostActive = true;
        ghostStartTime = startTime;
        updateBallImage();
        System.out.println("Ghost mode activated! Duration: 10s");
    }

    public void activateGhost() {
        activateGhost(System.currentTimeMillis());
    }

    public boolean isGhostActive() {
        return ghostActive;
    }

    public long getGhostRemainingTime() {
        return Math.max(0, GHOST_DURATION - (System.currentTimeMillis() - ghostStartTime));
    }

    public long getGhostStartTime() {
        return ghostStartTime;
    }

    public void activateExplosionMode(int charges) {
        if (explosionActive) return;
        explosionActive = true;
        explosionCharges = charges;
        updateBallImage();
        System.out.println("Explosion mode activated! " + charges + " charges");
    }

    public void activateExplosionMode() {
        activateExplosionMode(MAX_CHARGES);
    }

    public boolean isExplosionActive() {
        return explosionActive;
    }

    public int getExplosionCharges() {
        return explosionCharges;
    }

    public int getMaxExplosionCharges() {
        return MAX_CHARGES;
    }

    public void consumeCharge() {
        if (explosionCharges > 0) {
            explosionCharges--;
        }
        if (explosionCharges <= 0) {
            explosionActive = false;
        }
        updateBallImage();
    }

    public void activateSpeedBoost(long startTime) {
        if (speedBoostActive) return;
        speedBoostActive = true;
        speedStartTime = startTime;
        setSpeed(getSpeed() + 1);
        updateBallImage();
        System.out.println("Speed boost activated! Duration: 10s");
    }

    public void activateSpeedBoost() {
        activateSpeedBoost(System.currentTimeMillis());
    }

    public boolean isSpeedBoostActive() {
        return speedBoostActive;
    }

    public long getSpeedBoostRemainingTime() {
        return Math.max(0, SPEED_DURATION - (System.currentTimeMillis() - speedStartTime));
    }

    public long getSpeedStartTime() {
        return speedStartTime;
    }

    public void checkModesDuration() {
        if (ghostActive && System.currentTimeMillis() - ghostStartTime >= GHOST_DURATION) {
            ghostActive = false;
            updateBallImage();
            System.out.println("Ghost mode deactivated");
        }
        if (speedBoostActive && System.currentTimeMillis() - speedStartTime >= SPEED_DURATION) {
            speedBoostActive = false;
            setSpeed(getSpeed() - 1);
            updateBallImage();
            System.out.println("Speed boost deactivated");
        }
    }

    private void updateBallImage() {
        Image image = normalBallImage;
        if (explosionActive) {
            image = explosionBallImage;
        } else if (ghostActive) {
            image = ghostBallImage;
        } else if (speedBoostActive) {
            image = speedBallImage;
        }
        setBallImage(image);
    }
}