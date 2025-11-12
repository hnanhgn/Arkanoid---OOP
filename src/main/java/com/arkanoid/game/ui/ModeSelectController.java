
package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ModeSelectController implements Initializable {
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Tiếp tục phát nhạc menu nếu chưa phát, hoặc để MusicManager xử lý
        MusicMenuController.getInstance().playMenuMusic();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void startMode1() { playClickAndStart(0); }

    @FXML
    private void startMode2() { playClickAndStart(1); }

    @FXML
    private void startMode3() { playClickAndStart(2); }

    @FXML
    private void startMode4() { playClickAndStart(3); }
    private void playClickAndStart(int mode) {
        MusicClickController.getInstance().playClick(); // Phát click
        startGame(mode);
    }

    private void startGame(int mode) {
        MusicMenuController.getInstance().stopMusic(); // Dừng nhạc trước khi bắt đầu game
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
        MusicClickController.getInstance().playClick();
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
