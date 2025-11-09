package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PassedModeController {

    @FXML
    private AnchorPane rootPane;

    private Stage stage;
    private int currentMode;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentMode(int mode) {
        this.currentMode = mode;
    }

    public static void showPassedGame(Stage stage, int mode) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    PassedModeController.class.getResource("/com/arkanoid/game/PassedGame.fxml")
            );

            AnchorPane root = loader.load();

            PassedModeController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentMode(mode);

            Scene scene = new Scene(root, Config.WIDTH_CANVAS, Config.HEIGHT_CANVAS);

            stage.setScene(scene);
            stage.setTitle("Passed Mode");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onNextClick() {
        try {
            int nextMode = (currentMode + 1) % 4;

            GameScreen gameScreen = new GameScreen(stage, nextMode);
            Scene scene = new Scene(
                    gameScreen.createContent(),
                    Config.WIDTH_CANVAS,
                    Config.HEIGHT_CANVAS
            );

            gameScreen.setupInputHandlers(scene);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onMenuClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/arkanoid/game/ModeSelect.fxml")
            );
            Scene scene = new Scene(loader.load());

            ModeSelectController controller = loader.getController();
            controller.setStage(stage);

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCloseClick() {
        stage.close();
        System.exit(0);
    }
}
