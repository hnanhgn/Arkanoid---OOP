package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GamePauseController {

    private GameScreen gameScreen;
    private Stage stage;
    private int currentMode;

    @FXML
    private AnchorPane rootPane;

    public static void showPause(Stage stage, GameScreen gameScreen) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    GamePauseController.class.getResource("/com/arkanoid/game/GamePause.fxml")
            );
            AnchorPane pane = loader.load();

            GamePauseController controller = loader.getController();
            controller.stage = stage;
            controller.gameScreen = gameScreen;
            controller.rootPane = pane;
            controller.setCurrentMode(gameScreen.getMode());
            gameScreen.getRoot().getChildren().add(pane);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentMode(int mode) {
        this.currentMode = mode;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    protected void onContinueClick() {
        MusicClickController.getInstance().playClick();
        gameScreen.getRoot().getChildren().remove(rootPane);
        gameScreen.resumeGame();
    }

    @FXML
    protected void onRestartClick() {
        MusicClickController.getInstance().playClick();
        try {
            GameScreen gameScreen = new GameScreen(stage, currentMode);

            Parent root = gameScreen.createContent();

            Scene scene = new Scene(root, Config.WIDTH_CANVAS, Config.HEIGHT_CANVAS);
            gameScreen.setupInputHandlers(scene);

            stage.setTitle("Arkanoid Game - Mode " + currentMode);
            stage.setScene(scene);
            stage.show();
            root.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onMenuClick() {
        MusicClickController.getInstance().playClick();
        try {
            StartMenuController menu = new StartMenuController();
            menu.showMenu(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}