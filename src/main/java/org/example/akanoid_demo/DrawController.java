package org.example.akanoid_demo;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawController {

    @FXML
    private Canvas canvas;
    @FXML private Double ballX;
    @FXML private Double ballY;
    @FXML private Double ballRadius;
    @FXML private Color ballColor;
    private Ball ball;

    @FXML
    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Tạo bóng
        ball = new Ball(ballX, ballY, ballRadius, ballColor, canvas.getWidth(), canvas.getHeight(), 3);

        // Bắt đầu vòng lặp hoạt hình
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Xoá nền
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Cập nhật và vẽ bóng
                ball.update();
                ball.draw(gc);
            }
        }.start();
    }
}
