package com.arkanoid.game.entities;

import com.arkanoid.game.Config;
import javafx.scene.image.Image;

public class Ball extends Entities {
    private double speed;
    private Image ballImage;
    private double canvasWidth, canvasHeight;
    private Paddle paddle;
    private double defaultX, defaultY;
    private final double MIN_BOUNCE_ANGLE = Math.PI / 6;
    private boolean active = true;
    public Ball(double x, double y, double radius, Image ballImage, double speed) {
        this.x = x;
        this.y = y;
        this.defaultX = x;
        this.defaultY = y;
        this.radius = radius;
        this.ballImage = ballImage;
        this.canvasWidth = Config.WIDTH_CANVAS;
        this.canvasHeight = Config.HEIGHT_CANVAS;
        this.speed = speed;

        double angle = getRandomSafeAngle();
        this.velocityX = speed * Math.cos(angle);
        this.velocityY = speed * Math.sin(angle);
    }

    // THÊM PHƯƠNG THỨC setSpeed Ở ĐÂY
    public void setSpeed(double newSpeed) {
        this.speed = newSpeed;

        // Tính tốc độ hiện tại
        double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

        if (currentSpeed > 0) {
            // Giữ nguyên hướng, chỉ thay đổi tốc độ
            double ratio = newSpeed / currentSpeed;
            velocityX *= ratio;
            velocityY *= ratio;
        } else {
            // Nếu bóng đang đứng yên, khởi tạo tốc độ mới
            double angle = getRandomSafeAngle();
            velocityX = newSpeed * Math.cos(angle);
            velocityY = newSpeed * Math.sin(angle);
        }
    }

    // Có thể thêm phương thức getSpeed() nếu cần
    public double getSpeed() {
        return speed;
    }

    private double getRandomSafeAngle() {
        double minAngle = Math.PI / 6;
        double maxAngle = 5 * Math.PI / 6;
        return minAngle + Math.random() * (maxAngle - minAngle);
    }

    private void adjustBounceAngle() {
        double currentAngle = Math.atan2(velocityY, velocityX);
        double absAngle = Math.abs(currentAngle);

        if (absAngle < MIN_BOUNCE_ANGLE || absAngle > Math.PI - MIN_BOUNCE_ANGLE) {
            double newAngle;
            if (absAngle < MIN_BOUNCE_ANGLE) {
                newAngle = Math.signum(currentAngle) * MIN_BOUNCE_ANGLE;
            } else {
                newAngle = Math.signum(currentAngle) * (Math.PI - MIN_BOUNCE_ANGLE);
            }

            double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            velocityX = Math.cos(newAngle) * currentSpeed;
            velocityY = Math.sin(newAngle) * currentSpeed;
        }
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public void resetToDefault(double newX, double newY) {
        this.defaultX = newX;
        this.defaultY = newY;
        this.x = newX;
        this.y = newY;

        double angle = getRandomSafeAngle();
        this.velocityX = speed * Math.cos(angle);
        this.velocityY = speed * Math.sin(angle);
    }

    @Override
    public void update() {
        x += velocityX;
        y += velocityY;

        // Va chạm trái/phải
        if (x - radius <= 10 || x + radius >= canvasWidth - 10) {
            handleWallCollision();
        }

        // Va chạm trên
        if (y - radius <= 100) {
            velocityY *= -1;
            y = radius + 100;
        }

        // Va chạm với paddle
        if (paddle != null) {
            bounceOffPaddle();
        }
    }

    private void handleWallCollision() {
        velocityX *= -1;
        if (x - radius <= 10) x = radius + 10;
        if (x + radius >= canvasWidth - 10) x = canvasWidth - radius - 10;
        adjustBounceAngle();
    }

    // kiểm tra va chạm với gạch
    public boolean checkCollisionWithBrick(Brick brick) {
        double brickX = brick.getX();
        double brickY = brick.getY();
        double brickWidth = brick.getWidth();
        double brickHeight = brick.getHeight();

        boolean collision = (x + radius >= brickX) &&
                (x - radius <= brickX + brickWidth) &&
                (y + radius >= brickY) &&
                (y - radius <= brickY + brickHeight);

        return collision;
    }

    // Xử lý bật lại khi va chạm với gạch
    public void handleBrickBounce(Brick brick) {
        double brickX = brick.getX();
        double brickY = brick.getY();
        double brickWidth = brick.getWidth();
        double brickHeight = brick.getHeight();

        // Xác định hướng va chạm
        double overlapLeft = Math.abs((x + radius) - brickX);
        double overlapRight = Math.abs((x - radius) - (brickX + brickWidth));
        double overlapTop = Math.abs((y + radius) - brickY);
        double overlapBottom = Math.abs((y - radius) - (brickY + brickHeight));

        // Tìm hướng overlap nhỏ nhất
        double minOverlap = Math.min(Math.min(overlapLeft, overlapRight),
                Math.min(overlapTop, overlapBottom));

        // Lưu tốc độ hiện tại
        double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

        if (minOverlap == overlapLeft || minOverlap == overlapRight) {
            // Va chạm ngang - đảo chiều X
            velocityX = -velocityX;

            // Điều chỉnh vị trí
            if (minOverlap == overlapLeft) {
                x = brickX - radius - 1;
            } else {
                x = brickX + brickWidth + radius + 1;
            }
        } else {
            // Va chạm dọc - đảo chiều Y
            velocityY = -velocityY;

            // Điều chỉnh vị trí
            if (minOverlap == overlapTop) {
                y = brickY - radius - 1;
            } else {
                y = brickY + brickHeight + radius + 1;
            }
        }

        // Điều chỉnh góc
        adjustBounceAngle();

        // Chuẩn hóa lại vận tốc để giữ nguyên tốc độ
        double newSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        if (newSpeed > 0 && currentSpeed > 0) {
            double normalizeFactor = currentSpeed / newSpeed;
            velocityX *= normalizeFactor;
            velocityY *= normalizeFactor;
        }
    }

    private void bounceOffPaddle() {
        double paddleX = paddle.getX();
        double paddleY = paddle.getY();
        double paddleWidth = paddle.getWidth();
        double paddleHeight = paddle.getHeight();

        boolean collision = (x + radius >= paddleX) &&
                (x - radius <= paddleX + paddleWidth) &&
                (y + radius >= paddleY) &&
                (y - radius <= paddleY + paddleHeight);

        if (collision) {
            double overlapLeft = Math.abs((x + radius) - paddleX);
            double overlapRight = Math.abs((x - radius) - (paddleX + paddleWidth));
            double overlapTop = Math.abs((y + radius) - paddleY);
            double overlapBottom = Math.abs((y - radius) - (paddleY + paddleHeight));

            double minOverlap = Math.min(Math.min(overlapLeft, overlapRight),
                    Math.min(overlapTop, overlapBottom));

            double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

            if (minOverlap == overlapLeft || minOverlap == overlapRight) {
                velocityX = -velocityX;

                if (minOverlap == overlapLeft) {
                    x = paddleX - radius - 1;
                } else {
                    x = paddleX + paddleWidth + radius + 1;
                }
            } else {
                velocityY = -velocityY;

                if (minOverlap == overlapTop) {
                    y = paddleY - radius - 1;

                    double hitPosition = (x - paddleX) / paddleWidth;
                    double angleChange = (hitPosition - 0.5) * 4;
                    velocityX += angleChange;
                } else {
                    y = paddleY + paddleHeight + radius + 1;
                }
            }

            adjustBounceAngle();

            double newSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            if (newSpeed > 0 && currentSpeed > 0) {
                double normalizeFactor = currentSpeed / newSpeed;
                velocityX *= normalizeFactor;
                velocityY *= normalizeFactor;
            }
        }
    }

    public Image getBallImage() {
        return ballImage;
    }

    public double getRadius() {
        return radius;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
    public void setBallImage(Image newImage) {
        this.ballImage = newImage;
    }

}