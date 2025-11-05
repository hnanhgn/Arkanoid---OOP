package com.arkanoid.game.entities;

import com.arkanoid.game.Config;
import javafx.scene.image.Image;

public class Ball extends Entities {
    private double speed;         // Tốc độ di chuyển
    private Image ballImage;      // Ảnh bóng
    private Paddle paddle;        // Reference đến paddle để check va chạm
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
        this.speed = speed;

        // Tạo góc ngẫu nhiên nhưng tránh các góc quá dốc hoặc quá ngang
        double angle = getRandomSafeAngle();
        this.velocityX = speed * Math.cos(angle);
        this.velocityY = speed * Math.sin(angle);
    }

    // Tạo góc an toàn
    private double getRandomSafeAngle() {
        double minAngle = Math.PI / 6;    // 30°
        double maxAngle = 5 * Math.PI / 6; // 150°
        return minAngle + Math.random() * (maxAngle - minAngle);
    }

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

    public double getSpeed() {
        return speed;
    }

    // Kiểm tra và điều chỉnh góc nếu quá ngang
    private void adjustBounceAngle() {
        double currentAngle = Math.atan2(velocityY, velocityX);
        double absAngle = Math.abs(currentAngle);

        // Nếu góc quá ngang (gần 0° hoặc 180°), điều chỉnh lại
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

    // để thiết lập paddle
    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    // Phương thức reset với vị trí mới
    public void resetToDefault(double newX, double newY) {
        this.defaultX = newX;
        this.defaultY = newY;
        this.x = newX;
        this.y = newY;

        // Reset vận tốc với hướng ngẫu nhiên mới
        double angle = Math.random() * 2 * Math.PI;
        this.velocityX = speed * Math.cos(angle);
        this.velocityY = speed * Math.sin(angle);
    }

    @Override
    public void update() {
        x += velocityX;
        y += velocityY;

        // Va chạm trái/phải
        if (x - radius <= 10 || x + radius >= Config.WIDTH_CANVAS - 10) {
            velocityX *= -1;
            if (x - radius <= 0) x = radius + 10;
            if (x + radius >= Config.WIDTH_CANVAS) x = Config.WIDTH_CANVAS - radius - 10;
            // điều chỉnh góc
            adjustBounceAngle();
        }

        // Va chạm trên
        if (y - radius <= 100) {
            velocityY *= -1;
            y = radius + 100;
        }

        // Va chạm với paddle
        if (paddle != null) {
            bounceOff();
        }
    }

    public boolean checkCollisionWithBrick(Brick brick) {
        double brickX = brick.getX();
        double brickY = brick.getY();
        double brickWidth = brick.getWidth();
        double brickHeight = brick.getHeight();

        // Kiểm tra va chạm
        boolean collision = (x + radius >= brickX) &&
                (x - radius <= brickX + brickWidth) &&
                (y + radius >= brickY) &&
                (y - radius <= brickY + brickHeight);

        if (collision) {
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

            // điều chỉnh góc
            adjustBounceAngle();

            // Chuẩn hóa lại vận tốc để giữ nguyên tốc độ
            double newSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            if (newSpeed > 0 && currentSpeed > 0) {
                double normalizeFactor = currentSpeed / newSpeed;
                velocityX *= normalizeFactor;
                velocityY *= normalizeFactor;
            }

            return true; // Có va chạm
        }

        return false; // Không va chạm
    }

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

    private void bounceOff() {
        double paddleX = paddle.getX();
        double paddleY = paddle.getY();
        double paddleWidth = paddle.getWidth();
        double paddleHeight = paddle.getHeight();

        // Kiểm tra va chạm đơn giản và chính xác
        boolean collision = (x + radius >= paddleX) &&
                (x - radius <= paddleX + paddleWidth) &&
                (y + radius >= paddleY) &&
                (y - radius <= paddleY + paddleHeight);

        if (collision) {
            // Xác định hướng va chạm dựa trên vận tốc hiện tại
            double overlapLeft = Math.abs((x + radius) - paddleX);
            double overlapRight = Math.abs((x - radius) - (paddleX + paddleWidth));
            double overlapTop = Math.abs((y + radius) - paddleY);
            double overlapBottom = Math.abs((y - radius) - (paddleY + paddleHeight));

            // Tìm hướng overlap nhỏ nhất
            double minOverlap = Math.min(Math.min(overlapLeft, overlapRight),
                    Math.min(overlapTop, overlapBottom));

            // Lưu tốc độ hiện tại
            double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

            if (minOverlap == overlapLeft || minOverlap == overlapRight) {
                // Va chạm ngang
                velocityX = -velocityX;

                // Điều chỉnh vị trí
                if (minOverlap == overlapLeft) {
                    x = paddleX - radius - 1;
                } else {
                    x = paddleX + paddleWidth + radius + 1;
                }
            } else {
                // Va chạm dọc
                velocityY = -velocityY;

                // Điều chỉnh vị trí
                if (minOverlap == overlapTop) {
                    y = paddleY - radius - 1;

                    // Thêm hiệu ứng góc khi va vào mặt trên paddle
                    double hitPosition = (x - paddleX) / paddleWidth;
                    double angleChange = (hitPosition - 0.5) * 4;
                    velocityX += angleChange;
                } else {
                    y = paddleY + paddleHeight + radius + 1;
                }
            }

            // điều chỉnh góc
            adjustBounceAngle();

            // Chuẩn hóa lại vận tốc để giữ nguyên tốc độ
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
