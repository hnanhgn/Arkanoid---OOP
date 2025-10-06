package com.arkanoid.game.ui;

import com.arkanoid.game.entities.Paddle;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameScreen {
    private Pane root;
    private Paddle paddle;

    public GameScreen() {
        root = new Pane();
        initializeGame();
    }

    private void initializeGame() {
        paddle = new Paddle(250, 450, 100, 20);
        paddle.setBoundary(0, 600); // Set boundary cho paddle
        root.getChildren().add(paddle.getNode());
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