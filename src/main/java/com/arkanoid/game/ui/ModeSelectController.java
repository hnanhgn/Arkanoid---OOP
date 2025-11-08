package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModeSelectController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void startMode1() { startGame(0); }

    @FXML
    private void startMode2() { startGame(1); }

    @FXML
    private void startMode3() { startGame(2); }

    @FXML
    private void startMode4() { startGame(3); }


    private void startGame(int mode) {
        try {
            GameScreen gameScreen = new GameScreen(stage, mode);
            gameScreen.setMode(mode);
            Scene scene = new Scene(gameScreen.createContent(), Config.WIDTH_CANVAS, Config.HEIGHT_CANVAS);
            gameScreen.setupInputHandlers(scene);

            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void returnToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/arkanoid/game/StartMenu.fxml"));
            Scene scene = new Scene(loader.load());
            StartMenuController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}