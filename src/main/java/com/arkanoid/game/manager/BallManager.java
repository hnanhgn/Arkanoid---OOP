package com.arkanoid.game.manager;

import com.arkanoid.game.entities.Ball;
import com.arkanoid.game.manager.Soundmanager1;
import com.arkanoid.game.entities.Paddle;
import com.arkanoid.game.entities.Brick;
import com.arkanoid.game.entities.PowerUpPaddle;
import com.arkanoid.game.ui.GameScreen;
import com.arkanoid.game.ui.GameOverController;
import com.arkanoid.game.ui.PassedModeController;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class BallManager {
    private final Canvas canvas;
    private final Paddle paddle;
    private final BrickManager brickManager;
    private final GameScreen gameScreen;
    private final ItemManager itemManager;

    private AnimationTimer gameLoop;
    private boolean gameRunning = true;
    private int mode;
    private final Score scoreManager;

    private List<PowerUpBall> balls = new ArrayList<>();
    private boolean ballActive = true;
    private long resetStartTime = 0;
    private final long RESET_DELAY = 1000;

    private final double INITIAL_BALL_SPEED = 3;
    private final double INITIAL_BALL_RADIUS = 12;
    private final double EXPLOSION_RADIUS = 100;

    private PowerUpPaddle powerUpPaddle;

    private List<ExplosionEffect> explosions = new ArrayList<>();

    private Image explosionImage;

    public BallManager(Canvas canvas, Paddle paddle, BrickManager brickManager, GameScreen gameScreen, int mode, ItemManager itemManager) {
        this.canvas = canvas;
        this.paddle = paddle;
        this.brickManager = brickManager;
        this.gameScreen = gameScreen;
        this.mode = mode;
        this.itemManager = itemManager;
        this.scoreManager = new Score();
        this.powerUpPaddle = new PowerUpPaddle(paddle);

        // Load explosion image from resources
        explosionImage = new Image(getClass().getResourceAsStream("/images/explosion.png"));

        resetToDefault();
        startAnimation();
    }

    // Constructor cũ cho tương thích
    public BallManager(Canvas canvas, Paddle paddle, BrickManager brickManager, GameScreen gameScreen) {
        this(canvas, paddle, brickManager, gameScreen, 0, null);
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
                checkItemEffects();

                if (ballActive) {
                    // CẬP NHẬT TẤT CẢ BÓNG
                    updateBalls();

                    // CẬP NHẬT HIỆU ỨNG NỔ
                    updateExplosions();

                    // Kiểm tra chiến thắng
                    if (isAllBricksDestroyed()) {
                        stopGameLoop();
                        Platform.runLater(() -> {
                            PassedModeController.showPassedGame(
                                    (Stage) canvas.getScene().getWindow(),
                                    gameScreen.getMode()
                            );
                        });
                        return;
                    }

                    render(gc);
                    scoreManager.render(gc);
                } else {
                    if (System.currentTimeMillis() - resetStartTime >= RESET_DELAY) {
                        gameScreen.getLives().decreaseLife();

                        if (gameScreen.getLives().getLives() > 0) {
                            resetToDefault();
                            ballActive = true;
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
        if (balls.isEmpty()) return;

        // Áp dụng nổ cho tất cả bóng nếu có explosion mode
        for (PowerUpBall ball : balls) {
            if (ball.isExplosionActive() && ball.getExplosionCharges() > 0) {
                int bricksDestroyed = 0;
                int totalScore = 0;

                for (Brick brick : brickManager.getBricks()) {
                    if (!brick.isDestroyed() && brick.getType() != 1 && brick.getType() != 3) {
                        double distance = calculateDistance(ball.getX(), ball.getY(),
                                brick.getX() + brick.getWidth() / 2,
                                brick.getY() + brick.getHeight() / 2);

                        if (distance <= EXPLOSION_RADIUS) {
                            int points = getBrickPoints(brick.getType());
                            totalScore += points;
                            brick.destroy();
                            bricksDestroyed++;
                            itemManager.spawnItemFromBrick(brick, scoreManager.getScore() + totalScore);
                        }
                    }
                }

                if (totalScore > 0) {
                    scoreManager.increaseScore(totalScore);
                }

                ball.consumeCharge();

                // Thêm âm thanh nổ
                Soundmanager1.getInstance().play("hit_Brick.mp3");

                // Thêm hiệu ứng nổ
                explosions.add(new ExplosionEffect(ball.getX(), ball.getY(), System.currentTimeMillis()));

                System.out.println("Explosion from ball at (" + ball.getX() + ", " + ball.getY() + ") destroyed " + bricksDestroyed + " bricks, earned " + totalScore + " points");
            }
        }
    }

    public void stopGameLoop() {
        gameRunning = false;
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public void showGameOver(boolean isWin) {
        Stage currentStage = (Stage) canvas.getScene().getWindow();
        GameOverController.showGameOver(isWin, currentStage, mode);
    }

    public void restartGame() {
        stopGameLoop();
        gameRunning = true;

        scoreManager.reset();

        if (itemManager != null) {
            itemManager.reset();
        }

        brickManager.resetBricks();

        balls.clear();
        powerUpPaddle.deactivate();

        explosions.clear();

        resetToDefault();

        startAnimation();
    }

    private void checkItemEffects() {
        if (itemManager != null) {
            if (itemManager.isMultiBallRequested()) {
                activateMultiBall();
            }
            if (itemManager.isGhostModeRequested()) {
                activateGhostMode();
            }
            if (itemManager.isExplosionModeRequested()) {
                activateExplosionMode();
            }
            if (itemManager.isSpeedBoostRequested()) {
                activateSpeedBoost();
            }
            if (itemManager.isPaddleExpandRequested()) {
                powerUpPaddle.activate();
            }
        }
    }

    private void activateMultiBall() {
        for (int i = 0; i < 2; i++) {
            createNewBall();
        }
        System.out.println("Multi-ball activated! Added 2 new balls from paddle");
    }

    private void activateGhostMode() {
        long startTime = System.currentTimeMillis();
        for (PowerUpBall ball : balls) {
            ball.activateGhost(startTime);
        }
    }

    private void activateExplosionMode() {
        int charges = PowerUpBall.MAX_CHARGES;
        for (PowerUpBall ball : balls) {
            ball.activateExplosionMode(charges);
        }
    }

    private void activateSpeedBoost() {
        long startTime = System.currentTimeMillis();
        for (PowerUpBall ball : balls) {
            ball.activateSpeedBoost(startTime);
        }
    }

    private void updateBalls() {
        List<PowerUpBall> ballsToRemove = new ArrayList<>();
        boolean anyBallActive = false;

        for (PowerUpBall ball : balls) {
            ball.update();
            powerUpPaddle.update();
            ball.checkModesDuration();

            if (ball.isGhostActive()) {
                destroyBricksInPath(ball);
            } else {
                checkAndHandleBrickCollisions(ball);
            }

            if (ball.getY() + ball.getRadius() >= canvas.getHeight()) {
                ballsToRemove.add(ball);
            } else {
                anyBallActive = true;
            }
        }

        balls.removeAll(ballsToRemove);

        if (!anyBallActive && balls.isEmpty()) {
            ballActive = false;
            resetStartTime = System.currentTimeMillis();
        }
    }

    private void updateExplosions() {
        Iterator<ExplosionEffect> iterator = explosions.iterator();
        while (iterator.hasNext()) {
            ExplosionEffect exp = iterator.next();
            if (System.currentTimeMillis() - exp.startTime > exp.duration) {
                iterator.remove();
            }
        }
    }

    private void destroyBricksInPath(PowerUpBall ball) {
        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed() && brick.getType() != 1 && brick.getType() != 3) {
                if (ball.checkCollisionWithBrick(brick)) {
                    handleBrickHit(brick);
                    // Trong ghost mode, không bounce nhưng vẫn phát sound brick_hit
                    Soundmanager1.getInstance().play("hit_Brick.mp3");
                }
            }
        }
    }

    private void checkAndHandleBrickCollisions(PowerUpBall ball) {
        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed() && ball.checkCollisionWithBrick(brick)) {
                if (brick.getType() == 1) {
                    continue;
                }
                if (brick.getType() == 3) {
                    ball.handleBrickBounce(brick);
                    return;
                }
                handleBrickHit(brick);
                ball.handleBrickBounce(brick);
                return;
            }
        }
    }

    private void handleBrickHit(Brick brick) {
        switch (brick.getType()) {
            case 1:
                return;
            case 2:
                brick.onHit();
                if (brick.isDestroyed()) {
                    scoreManager.increaseScore(2);
                    itemManager.spawnItemFromBrick(brick, scoreManager.getScore());
                }
                break;
            case 3:
                return;
            default:
                brick.onHit();
                if (brick.isDestroyed()) {
                    scoreManager.increaseScore(1);
                    itemManager.spawnItemFromBrick(brick, scoreManager.getScore());
                }
                break;
        }
    }

    private int getBrickPoints(int brickType) {
        switch (brickType) {
            case 2: return 2;
            default: return 1;
        }
    }

    private void createNewBall() {
        double startX = paddle.getX() + paddle.getWidth() / 2;
        double startY = paddle.getY() - INITIAL_BALL_RADIUS - 5;
        PowerUpBall newBall = new PowerUpBall(startX, startY, INITIAL_BALL_RADIUS, null, INITIAL_BALL_SPEED);
        newBall.setPaddle(paddle);

        // Áp dụng các mode hiện tại cho bóng mới từ bóng đầu tiên (nếu có)
        if (!balls.isEmpty()) {
            PowerUpBall refBall = balls.get(0);
            if (refBall.isGhostActive()) {
                newBall.activateGhost(refBall.getGhostStartTime());
            }
            if (refBall.isExplosionActive()) {
                newBall.activateExplosionMode(refBall.getExplosionCharges());
            }
            if (refBall.isSpeedBoostActive()) {
                newBall.activateSpeedBoost(refBall.getSpeedStartTime());
            }
        }

        balls.add(newBall);
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private boolean isAllBricksDestroyed() {
        for (Brick brick : brickManager.getBricks()) {
            if (brick.getType() != 1 && !brick.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    public void pauseGameLoop() {
        gameRunning = false;
    }

    public void resumeGameLoop() {
        gameRunning = true;
    }

    public Score getScoreManager() {
        return scoreManager;
    }

    public int getScore() {
        return scoreManager.getScore();
    }

    private void resetToDefault() {
        double defaultX = paddle.getX() + paddle.getWidth() / 2;
        double defaultY = paddle.getY() - INITIAL_BALL_RADIUS - 5;

        balls.clear();

        PowerUpBall ball = new PowerUpBall(defaultX, defaultY, INITIAL_BALL_RADIUS, null, INITIAL_BALL_SPEED);
        ball.setPaddle(paddle);
        balls.add(ball);
    }

    private void render(GraphicsContext gc) {
        for (PowerUpBall ball : balls) {
            double diameter = ball.getRadius() * 2;
            gc.drawImage(ball.getBallImage(), ball.getX() - ball.getRadius(), ball.getY() - ball.getRadius(), diameter, diameter);
        }

        // VẼ HIỆU ỨNG NỔ SỬ DỤNG ẢNH
        for (ExplosionEffect exp : explosions) {
            long elapsed = System.currentTimeMillis() - exp.startTime;
            double alpha = 1.0 - ((double) elapsed / exp.duration);
            if (alpha > 0) {
                gc.setGlobalAlpha(alpha);
                gc.drawImage(explosionImage, exp.x - EXPLOSION_RADIUS, exp.y - EXPLOSION_RADIUS, EXPLOSION_RADIUS * 2, EXPLOSION_RADIUS * 2);
                gc.setGlobalAlpha(1.0);
            }
        }

        // HIỂN THỊ THÔNG BÁO (lấy từ bóng đầu tiên vì đồng bộ)
        if (!balls.isEmpty()) {
            PowerUpBall refBall = balls.get(0);

            double yOffset = 10;

            if (refBall.isGhostActive()) {
                long remainingTime = refBall.getGhostRemainingTime();
                String text = String.format("Ghost Mode: %.1fs", remainingTime / 1000.0);
                drawStatusText(gc, text, yOffset, Color.rgb(0, 0, 255, 0.5));
                yOffset += 30;
            }

            if (refBall.isExplosionActive()) {
                String text = "Explosion Mode: " + refBall.getExplosionCharges() + "/" + refBall.getMaxExplosionCharges() + " - Press SPACE!";
                drawStatusText(gc, text, yOffset, Color.rgb(255, 165, 0, 0.7));
                yOffset += 30;
            }

            if (refBall.isSpeedBoostActive()) {
                long remainingTime = refBall.getSpeedBoostRemainingTime();
                String text = String.format("Speed Boost: %.1fs", remainingTime / 1000.0);
                drawStatusText(gc, text, yOffset, Color.rgb(0, 255, 0, 0.5));
            }
        }
    }

    private void drawStatusText(GraphicsContext gc, String text, double y, Color bgColor) {
        Text tempText = new Text(text);
        tempText.setFont(Font.font("Arial", 12));

        gc.setFont(Font.font("Arial", 12));

        double textWidth = tempText.getLayoutBounds().getWidth();
        double textHeight = tempText.getLayoutBounds().getHeight();

        double paddingX = 12;
        double paddingY = 7;
        double boxWidth = textWidth + paddingX * 2;
        double boxHeight = textHeight + paddingY * 2;
        double x = (canvas.getWidth() - boxWidth) / 2;

        // Nền bán trong suốt + bo góc
        gc.setFill(bgColor);
        gc.fillRoundRect(x, y, boxWidth, boxHeight, 10, 10);

        // Text trắng, căn giữa
        gc.setFill(Color.WHITE);
        gc.fillText(text, x + paddingX, y + paddingY + textHeight - 1);
    }

    private static class ExplosionEffect {
        double x;
        double y;
        long startTime;
        long duration = 500; // Thời gian hiệu ứng nổ (ms)

        ExplosionEffect(double x, double y, long startTime) {
            this.x = x;
            this.y = y;
            this.startTime = startTime;
        }
    }
}