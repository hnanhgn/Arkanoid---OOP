package com.arkanoid;

import com.arkanoid.game.ui.GameScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static final int WiDTH = 600;
    public static final int HEIGHT = 500;

    public void start(Stage stage) {
        GameScreen gameScreen = new GameScreen();
        Scene scene = new Scene(gameScreen.createContent(), WiDTH, HEIGHT);

        gameScreen.setupInputHandlers(scene);

        stage.setTitle("Arkanoid - Paddle Test");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}