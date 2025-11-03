package com.arkanoid.game.manager;

import com.arkanoid.game.entities.Ball;
import com.arkanoid.game.entities.Brick;
import com.arkanoid.game.entities.Paddle;
import com.arkanoid.game.ui.GameScreen;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;

public class PowerUpBall {
    private final Canvas canvas;
    private final Paddle paddle;
    private final BrickManager brickManager;
    private final ItemManager itemManager;
    private final Score scoreManager;

    private List<Ball> balls = new ArrayList<>();
    private boolean ballActive = true;
    private long resetStartTime = 0;
    private final long RESET_DELAY = 1000;

    private final double INITIAL_BALL_SPEED = 2;
    private final double INITIAL_BALL_RADIUS = 12;

    private double ball_speed;
    private double ball_radius;
    private Image ballImage;
    private Image normalBallImage;
    private Image ghostBallImage;
    private Image explosionBallImage;
    private Image speedBallImage;

    // CÁC BIẾN CHO CHẾ ĐỘ ĐẶC BIỆT
    private boolean ghostMode = false;
    private long ghostModeStartTime = 0;
    private final long GHOST_MODE_DURATION = 10000;
    private boolean explosionMode = false;
    private int explosionCount = 0;
    private final int MAX_EXPLOSIONS = 2;
    private final double EXPLOSION_RADIUS = 70;
    private boolean speedBoost = false;
    private long speedBoostStartTime = 0;
    private final long SPEED_BOOST_DURATION = 10000;

    public PowerUpBall(Canvas canvas, Paddle paddle, BrickManager brickManager, GameScreen gameScreen, ItemManager itemManager, Score scoreManager) {
        this.canvas = canvas;
        this.paddle = paddle;
        this.brickManager = brickManager;
        this.itemManager = itemManager;
        this.scoreManager = scoreManager;

        // Load các loại ảnh cho bóng
        this.normalBallImage = new Image(getClass().getResourceAsStream("/images/ball1.png"));
        this.ghostBallImage = new Image(getClass().getResourceAsStream("/images/Piercing_Ball.png"));
        this.explosionBallImage = new Image(getClass().getResourceAsStream("/images/ball2.png"));
        this.speedBallImage = new Image(getClass().getResourceAsStream("/images/ball1.png"));
        this.ballImage = normalBallImage; // Default là normal

        this.ball_speed = INITIAL_BALL_SPEED;
        this.ball_radius = INITIAL_BALL_RADIUS;

        double defaultX = paddle.getX() + paddle.getWidth() / 2;
        double defaultY = paddle.getY() - ball_radius - 5;

        Ball ball = new Ball(defaultX, defaultY, ball_radius, ballImage, ball_speed);
        ball.setPaddle(paddle);
        balls.add(ball);
    }

    // KIỂM TRA VÀ ÁP DỤNG ITEM EFFECTS
    public void checkItemEffects() {
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
        }
    }

    // KÍCH HOẠT MULTI-BALL
    private void activateMultiBall() {
        // Luôn thêm 2 bóng mới bay lên từ paddle
        for (int i = 0; i < 2; i++) {
            createNewBall();
        }
        System.out.println("Multi-ball activated! Added 2 new balls from paddle");
    }

    // KÍCH HOẠT GHOST MODE
    private void activateGhostMode() {
        ghostMode = true;
        ghostModeStartTime = System.currentTimeMillis();
        updateBallImage(); // Cập nhật hình ảnh dựa trên mode hiện tại
        System.out.println("Ghost mode activated! Duration: 10 seconds");
    }

    // KÍCH HOẠT EXPLOSION MODE
    private void activateExplosionMode() {
        explosionMode = true;
        explosionCount = 0;
        updateBallImage(); // Cập nhật hình ảnh dựa trên mode hiện tại
        System.out.println("Explosion mode activated! Press SPACE to explode (" + MAX_EXPLOSIONS + " times available)");
    }

    // KÍCH HOẠT SPEED BOOST
    private void activateSpeedBoost() {
        speedBoost = true;
        speedBoostStartTime = System.currentTimeMillis();
        ball_speed += 1;
        updateBallImage(); // Cập nhật hình ảnh dựa trên mode hiện tại
        System.out.println("Speed boost activated! Ball speed increased by 1 for 10 seconds");
    }

    // SỬA: XỬ LÝ VỤ NỔ KHI NHẤN SPACE - CHO PHÉP NỔ 2 LẦN
    public void handleSpacePressed() {
        if (explosionMode && explosionCount < MAX_EXPLOSIONS) {
            System.out.println("SPACE pressed - Activating explosion! (" + (explosionCount + 1) + "/" + MAX_EXPLOSIONS + ")");
            activateExplosion();
        } else if (explosionMode && explosionCount >= MAX_EXPLOSIONS) {
            System.out.println("SPACE pressed but no explosion charges left");
            explosionMode = false;
        } else {
            System.out.println("SPACE pressed but no explosion mode active");
        }
    }

    private void activateExplosion() {
        if (balls.isEmpty()) return;

        explosionCount++;
        int bricksDestroyed = 0;
        int totalScore = 0;

        // Sử dụng bóng đầu tiên làm tâm vụ nổ
        Ball explosionCenter = balls.get(0);

        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed() && brick.getType() != 1) {
                // Tính khoảng cách từ bóng đến gạch
                double distance = calculateDistance(explosionCenter.getX(), explosionCenter.getY(),
                        brick.getX() + brick.getWidth() / 2,
                        brick.getY() + brick.getHeight() / 2);

                if (distance <= EXPLOSION_RADIUS) {
                    int points = getBrickPoints(brick.getType());
                    totalScore += points;
                    brick.destroy();
                    bricksDestroyed++;
                }
            }
        }

        if (totalScore > 0) {
            scoreManager.increaseScore(totalScore);
        }

        System.out.println("Explosion " + explosionCount + "/" + MAX_EXPLOSIONS + " destroyed " + bricksDestroyed + " bricks, earned " + totalScore + " points");

        // HIỆN THỊ HIỆU ỨNG VỤ NỔ
        showExplosionEffect(explosionCenter.getX(), explosionCenter.getY());

        // Tắt explosion mode nếu đã nổ đủ số lần
        if (explosionCount >= MAX_EXPLOSIONS) {
            explosionMode = false;
            updateBallImage(); // Cập nhật hình ảnh dựa trên mode còn lại
            System.out.println("Explosion mode deactivated - used all " + MAX_EXPLOSIONS + " explosions");
        }
    }

    private void showExplosionEffect(double x, double y) {
        // Có thể thêm hiệu ứng hình ảnh ở đây
        System.out.println("BOOM! Explosion at (" + x + ", " + y + ")");
    }

    // CẬP NHẬT TẤT CẢ BÓNG
    public void updateBalls() {
        List<Ball> ballsToRemove = new ArrayList<>();
        boolean anyBallActive = false;

        for (Ball currentBall : balls) {
            currentBall.update();

            // XỬ LÝ VA CHẠM VỚI GẠCH
            if (ghostMode) {
                // Chế độ xuyên: phá gạch mà không bật lại
                destroyBricksInPath(currentBall);
            } else {
                // Chế độ bình thường
                Brick collidedBrick = checkBrickCollisions(currentBall);
                if (collidedBrick != null) {
                    currentBall.handleBrickBounce(collidedBrick);
                    handleBrickHit(collidedBrick);
                }
            }

            // Kiểm tra bóng rơi khỏi màn hình
            if (currentBall.getY() + currentBall.getRadius() >= canvas.getHeight()) {
                ballsToRemove.add(currentBall);
            } else {
                anyBallActive = true;
            }
        }

        // Xóa các bóng đã rơi xuống
        balls.removeAll(ballsToRemove);

        // CHỈ mất mạng khi TẤT CẢ bóng đều rơi xuống
        if (!anyBallActive && balls.isEmpty()) {
            ballActive = false;
            resetStartTime = System.currentTimeMillis();
        }
    }

    // KIỂM TRA THỜI GIAN GHOST MODE
    private void checkGhostModeDuration() {
        if (ghostMode) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - ghostModeStartTime;

            if (elapsedTime >= GHOST_MODE_DURATION) {
                ghostMode = false;
                updateBallImage(); // Cập nhật hình ảnh dựa trên mode còn lại
                System.out.println("Ghost mode deactivated after 10 seconds");
            }
        }
    }

    // KIỂM TRA THỜI GIAN SPEED BOOST
    private void checkSpeedBoostDuration() {
        if (speedBoost) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - speedBoostStartTime;

            if (elapsedTime >= SPEED_BOOST_DURATION) {
                speedBoost = false;
                ball_speed -= 1;
                updateBallImage(); // Cập nhật hình ảnh dựa trên mode còn lại
                System.out.println("Speed boost deactivated after 10 seconds");
            }
        }
    }

    public void checkModesDuration() {
        checkGhostModeDuration();
        checkSpeedBoostDuration();
    }

    // PHÁ GẠCH TRONG CHẾ ĐỘ XUYÊN
    private void destroyBricksInPath(Ball ball) {
        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed() && brick.getType() != 1) {
                if (ball.checkCollisionWithBrick(brick)) {
                    handleBrickHit(brick);
                    brick.destroy();
                }
            }
        }
    }

    // TẠO BÓNG MỚI CHO MULTI-BALL
    private void createNewBall() {
        double startX = paddle.getX() + paddle.getWidth() / 2;
        double startY = paddle.getY() - ball_radius - 5;

        Ball newBall = new Ball(startX, startY, ball_radius, ballImage, ball_speed);
        newBall.setPaddle(paddle);
        balls.add(newBall);
    }

    // XỬ LÝ KHI BÓNG ĐẬP TRÚNG GẠCH
    private void handleBrickHit(Brick brick) {
        switch (brick.getType()) {
            case 1: // Gạch vô hình
                return;
            case 2: // Gạch cứng
                brick.onHit();
                if (brick.isDestroyed()) {
                    scoreManager.increaseScore(2);
                    // SPAWN ITEM TỪ GẠCH BỊ VỠ KHI ĐIỂM CHIA HẾT CHO 10
                    if (itemManager != null) {
                        int currentScore = scoreManager.getScore();
                        itemManager.spawnItemFromBrick(brick, currentScore);
                    }
                }
                break;
            case 3: // Gạch thường
            default:
                brick.onHit();
                if (brick.isDestroyed()) {
                    scoreManager.increaseScore(1);
                    // SPAWN ITEM TỪ GẠCH BỊ VỠ KHI ĐIỂM CHIA HẾT CHO 10
                    if (itemManager != null) {
                        int currentScore = scoreManager.getScore();
                        itemManager.spawnItemFromBrick(brick, currentScore);
                    }
                }
                break;
        }
    }

    // CÁC PHƯƠNG THỨC HỖ TRỢ
    private Brick checkBrickCollisions(Ball ball) {
        for (Brick brick : brickManager.getBricks()) {
            if (!brick.isDestroyed() && brick.getType() != 1) {
                if (ball.checkCollisionWithBrick(brick)) {
                    return brick;
                }
            }
        }
        return null;
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private int getBrickPoints(int brickType) {
        switch (brickType) {
            case 2: return 2;
            case 3: default: return 1;
        }
    }

    public boolean isAllBricksDestroyed() {
        for (Brick brick : brickManager.getBricks()) {
            if (brick.getType() != 1 && !brick.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    public void render(GraphicsContext gc) {
        for (Ball currentBall : balls) {
            double diameter = currentBall.getRadius() * 2;
            gc.drawImage(currentBall.getBallImage(),
                    currentBall.getX() - currentBall.getRadius(),
                    currentBall.getY() - currentBall.getRadius(),
                    diameter, diameter);
        }

        // HIỂN THỊ THÔNG BÁO GHOST MODE
        if (ghostMode) {
            long remainingTime = GHOST_MODE_DURATION - (System.currentTimeMillis() - ghostModeStartTime);
            double secondsLeft = Math.max(0, remainingTime) / 1000.0;

            String text = String.format("Ghost Mode: %.1fs", secondsLeft);
            gc.setFont(Font.font("Arial", 14));
            double textWidth = gc.getFont().getSize() * text.length() * 0.5;
            double boxWidth = textWidth + 16;
            double boxHeight = 24;
            double x = (canvas.getWidth() - boxWidth) / 2;
            double y = 10;

            gc.setFill(Color.rgb(0, 0, 0, 0.5));
            gc.fillRoundRect(x, y, boxWidth, boxHeight, 10, 10);
            gc.setFill(Color.WHITE);
            gc.fillText(text, x + 8, y + 16);
        }

        // HIỂN THỊ THÔNG BÁO EXPLOSION MODE
        if (explosionMode) {
            String text = "Explosion Mode: " + (MAX_EXPLOSIONS - explosionCount) + "/" + MAX_EXPLOSIONS + " - Press SPACE!";
            gc.setFont(Font.font("Arial", 14));
            double textWidth = gc.getFont().getSize() * text.length() * 0.5;
            double boxWidth = textWidth + 16;
            double boxHeight = 24;
            double x = (canvas.getWidth() - boxWidth) / 2;
            double y = 40;

            gc.setFill(Color.rgb(255, 165, 0, 0.7));
            gc.fillRoundRect(x, y, boxWidth, boxHeight, 10, 10);
            gc.setFill(Color.WHITE);
            gc.fillText(text, x + 8, y + 16);
        }

        // HIỂN THỊ THÔNG BÁO SPEED BOOST
        if (speedBoost) {
            long remainingTime = SPEED_BOOST_DURATION - (System.currentTimeMillis() - speedBoostStartTime);
            double secondsLeft = Math.max(0, remainingTime) / 1000.0;

            String text = String.format("Speed Boost: %.1fs", secondsLeft);
            gc.setFont(Font.font("Arial", 14));
            double textWidth = gc.getFont().getSize() * text.length() * 0.5;
            double boxWidth = textWidth + 16;
            double boxHeight = 24;
            double x = (canvas.getWidth() - boxWidth) / 2;
            double y = explosionMode ? 70 : 10;

            gc.setFill(Color.rgb(0, 255, 0, 0.5));
            gc.fillRoundRect(x, y, boxWidth, boxHeight, 10, 10);
            gc.setFill(Color.WHITE);
            gc.fillText(text, x + 8, y + 16);
        }
    }

    public void resetToDefault() {
        double defaultX = paddle.getX() + paddle.getWidth() / 2;
        double defaultY = paddle.getY() - ball_radius - 5;

        // Reset tất cả bóng
        balls.clear();

        // SỬA: Reset tốc độ và kích thước về giá trị ban đầu
        this.ball_speed = INITIAL_BALL_SPEED;
        this.ball_radius = INITIAL_BALL_RADIUS;

        // Tạo lại ball với tốc độ mặc định
        Ball ball = new Ball(defaultX, defaultY, ball_radius, normalBallImage, ball_speed);
        ball.setPaddle(paddle);
        balls.add(ball);

        // Reset các chế độ
        ghostMode = false;
        explosionMode = false;
        explosionCount = 0;
        speedBoost = false;
        this.ballImage = normalBallImage;
    }

    public void reset() {
        resetToDefault();
        ballActive = true;
    }

    // Phương thức cập nhật hình ảnh bóng dựa trên mode hiện tại
    private void updateBallImage() {
        if (explosionMode) {
            changeBallType(explosionBallImage, ball_speed);
        } else if (ghostMode) {
            changeBallType(ghostBallImage, ball_speed);
        } else if (speedBoost) {
            changeBallType(normalBallImage, ball_speed); // Sử dụng ảnh bóng gốc cho speed boost
        } else {
            changeBallType(normalBallImage, ball_speed);
        }
    }

    // SỬA: Thêm phương thức để thay đổi loại bóng (cho item)
    public void changeBallType(Image newBallImage, double newSpeed) {
        this.ballImage = newBallImage;
        this.ball_speed = newSpeed;

        // Cập nhật cho tất cả các ball hiện có
        for (Ball ball : balls) {
            ball.setBallImage(newBallImage);
            ball.setSpeed(newSpeed);
        }
    }

    // SỬA: Thêm phương thức để reset về ball mặc định
    public void resetBallToDefault() {
        this.ballImage = normalBallImage;
        this.ball_speed = INITIAL_BALL_SPEED;

        for (Ball ball : balls) {
            ball.setBallImage(ballImage);
            ball.setSpeed(ball_speed);
        }
    }

    public boolean isActive() {
        return ballActive;
    }

    public void setActive(boolean active) {
        this.ballActive = active;
    }

    public long getResetStartTime() {
        return resetStartTime;
    }

    public long getResetDelay() {
        return RESET_DELAY;
    }
}