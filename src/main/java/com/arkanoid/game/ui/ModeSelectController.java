package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModeSelectController {

    @FXML
    private Button mode1Button, mode2Button, mode3Button, mode4Button, backButton;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        mode1Button.setOnAction(e -> startGame(0));
        mode2Button.setOnAction(e -> startGame(1));
        mode3Button.setOnAction(e -> startGame(2));
        mode4Button.setOnAction(e -> startGame(3));
        backButton.setOnAction(e -> returnToMenu(stage));
    }

    private void startGame(int mode) {
        try {
            GameScreen gameScreen = new GameScreen(stage);
            gameScreen.setMode(mode);
            Scene scene = new Scene(gameScreen.createContent(), Config.WIDTH_CANVAS, Config.HEIGHT_CANVAS);
            gameScreen.setupInputHandlers(scene);

            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void returnToMenu(Stage stage) {
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
