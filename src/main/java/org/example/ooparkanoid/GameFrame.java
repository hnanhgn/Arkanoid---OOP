package org.example.ooparkanoid;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameFrame extends Application {
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();

        Paddle paddle = new Paddle(250, 450, 100, 20);
        root.getChildren().add(paddle.getNode());

        Scene scene = new Scene(root, 600, 500);
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT -> paddle.moveLeft();
                case RIGHT -> paddle.moveRight();
            }
        });

        stage.setTitle("Test Paddle");
        stage.setScene(scene);
        stage.show();

        root.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
}