package com.arkanoid.game.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GamePauseController {

    private GameScreen gameScreen;
    private Stage stage;

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
            gameScreen.getRoot().getChildren().add(pane);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        gameScreen.getRoot().getChildren().remove(rootPane);
        gameScreen.restartGame();
        gameScreen.resumeGame();
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
