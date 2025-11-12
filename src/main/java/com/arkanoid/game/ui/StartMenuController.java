
package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StartMenuController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Phát nhạc nền khi StartMenu load
        MusicMenuController.getInstance().playMenuMusic();
    }

    public void showMenu(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/arkanoid/game/StartMenu.fxml"));
            Scene scene = new Scene(loader.load());
            StartMenuController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(scene);
            stage.setTitle("Arkanoid - Start Menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onStartClick() {
        MusicClickController.getInstance().playClick();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/arkanoid/game/ModeSelect.fxml"));
            Scene scene = new Scene(loader.load());
            ModeSelectController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void onCloseClick() {
        MusicClickController.getInstance().playClick();
        MusicMenuController.getInstance().stopMusic(); // Dừng nhạc khi close
        if (stage != null) {
            stage.close();
        }
    }
}
