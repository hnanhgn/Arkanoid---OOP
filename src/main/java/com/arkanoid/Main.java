package com.arkanoid;

import com.arkanoid.game.ui.GameScreen;
import com.arkanoid.game.ui.StartMenuController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        StartMenuController startMenu = new StartMenuController();
        startMenu.showMenu(stage);
    }c
    public static void main(String[] args) {
        launch();
    }
}