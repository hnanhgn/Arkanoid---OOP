package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ModeSelectController {

    @FXML
    private AnchorPane root;

    @FXML
    private Button mode1Button, mode2Button, mode3Button, mode4Button, backButton;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // set background here
        root.setStyle("-fx-background-image: url('/images/LevelGame.png');"
                + "-fx-background-size: cover;"
                + "-fx-background-position: center center;");

        mode1Button.setOnAction(e -> startGame(0));
        mode2Button.setOnAction(e -> startGame(1));
        mode3Button.setOnAction(e -> startGame(2));
        mode4Button.setOnAction(e -> startGame(3));
        backButton.setOnAction(e -> returnToMenu());
    }

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

    private void returnToMenu() {
        try {
            StartMenuController startMenu = new StartMenuController();
            startMenu.showMenu(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}