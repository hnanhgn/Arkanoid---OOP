package com.arkanoid;

import com.arkanoid.game.ui.GameScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage stage) {
        GameScreen gameScreen = new GameScreen();
        Scene scene = new Scene(gameScreen.createContent(), 600, 500);

        gameScreen.setupInputHandlers(scene);

        stage.setTitle("Arkanoid - Test");
        stage.setScene(scene);
        stage.show();

        gameScreen.createContent().requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
}