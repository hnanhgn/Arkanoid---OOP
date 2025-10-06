package com.arkanoid.game.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    private double x, y;          // Tọa độ tâm bóng
    private double radius;        // Bán kính bóng
    private double dx, dy;        // Vận tốc theo trục X và Y
    private double speed;         // Tốc độ di chuyển
    private Color color;          // Màu bóng
    private double canvasWidth, canvasHeight;

    public Ball(double x, double y, double radius, Color color, double canvasWidth, double canvasHeight, double speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.speed = speed;

        // Hướng ngẫu nhiên (để bóng bay chéo)
        double angle = Math.random() * 2 * Math.PI; // 0 → 360°
        this.dx = speed * Math.cos(angle);
        this.dy = speed * Math.sin(angle);
    }

    // Cập nhật vị trí và xử lý va chạm tường
    public void update() {
        x += dx;
        y += dy;

        // Va chạm trái/phải
        if (x - radius <= 0 || x + radius >= canvasWidth) {
            dx *= -1;
        }

        // Va chạm trên/dưới
        if (y - radius <= 0 || y + radius >= canvasHeight) {
            dy *= -1;
        }
    }

    // Vẽ bóng
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        gc.setStroke(Color.WHITE);
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
    }
}
