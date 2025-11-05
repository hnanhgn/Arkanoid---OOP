package com.arkanoid.game.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GamePauseController {

    private GameScreen gameScreen;
    private Stage stage;

    private AnchorPane overlayRoot; // để sau này dễ remove khỏi GameScreen

    public static void showPause(Stage stage, GameScreen gameScreen) {
        try {
            FXMLLoader loader = new FXMLLoader(GamePauseController.class.getResource("/com/arkanoid/game/GamePause.fxml"));
            AnchorPane pausePane = loader.load();

            GamePauseController controller = loader.getController();
            controller.stage = stage;
            controller.gameScreen = gameScreen;
            controller.overlayRoot = pausePane;

            // Thêm overlay (Pause UI) lên màn hình game
            gameScreen.getRoot().getChildren().add(pausePane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Nút “Tiếp tục”
    @FXML
    protected void onResumeClick() {
        gameScreen.getRoot().getChildren().remove(overlayRoot); // xóa lớp pause
        gameScreen.resumeGame();
    }

    // Nút “Chơi lại”
    @FXML
    protected void onRestartClick() {
        gameScreen.getRoot().getChildren().remove(overlayRoot);
        gameScreen.restartGame();
        gameScreen.resumeGame();
    }

    // Nút “Về trang chủ”
    @FXML
    protected void onHomeClick() {
        try {
            StartMenuController startMenu = new StartMenuController();
            startMenu.showMenu(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
