package com.arkanoid.game.ui;

import com.arkanoid.game.entities.Ball;
import com.arkanoid.game.manager.BallManager;
import com.arkanoid.game.entities.Paddle;
import javafx.scene.canvas.Canvas;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameScreen {
    private Pane root;
    private Paddle paddle;
    private BallManager ballManager;
    private int width_canvas = 600;
    private int height_canvas = 500;
    public GameScreen() {
        root = new Pane();
        initializeGame();
    }

    private void initializeGame() {
        // Tạo canvas cho ball
        Canvas canvas = new Canvas(width_canvas, height_canvas);
        root.getChildren().add(canvas);

        paddle = new Paddle(250, 450, 100, 20);
        paddle.setBoundary(0, 600); // Set boundary cho paddle
        root.getChildren().add(paddle.getNode());

        // Khởi tạo BallManager - nó sẽ tự động bắt đầu animation
        ballManager = new BallManager(canvas, paddle);
    }

    public Pane createContent() {
        return root;
    }

    public void setupInputHandlers(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT:
                    paddle.moveLeft();
                    break;
                case RIGHT:
                    paddle.moveRight();
                    break;
                default:
                    break;
            }
        });
    }


}