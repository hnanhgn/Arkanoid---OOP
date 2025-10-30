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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PassedModeController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button nextButton;

    @FXML
    private Button menuButton;

    @FXML
    private Button closeButton;

    private static int mode;

    public static void showPassedGame(Stage stage, int currentMode) {
        try {
            mode = currentMode;
            FXMLLoader loader = new FXMLLoader(PassedModeController.class.getResource("/com/arkanoid/game/PassedGame.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Passed Game");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBackground();
    }

    private void loadBackground() {
        try {
            String imagePath = "/images/PassedGame.png";
            Image backgroundImage = new Image(getClass().getResourceAsStream(imagePath));
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(600);
            backgroundView.setFitHeight(700);
            backgroundView.setPreserveRatio(false);
            rootPane.getChildren().add(0, backgroundView);
        } catch (Exception e) {
            System.err.println("Không thể load ảnh nền" );
            rootPane.setStyle("-fx-background-color: linear-gradient(to bottom, #1a237e, #283593);");
        }
    }

    @FXML
    public void onNextClick() {
        Stage stage = (Stage) nextButton.getScene().getWindow();
        int nextMode = (mode + 1) % 4; // quay lại mode 0 sau mode 3

        GameScreen gameScreen = new GameScreen(stage, nextMode);
        gameScreen.setMode(nextMode);
        Scene scene = new Scene(gameScreen.createContent(), Config.WIDTH_CANVAS, Config.HEIGHT_CANVAS);
        gameScreen.setupInputHandlers(scene);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onMenuClick() {
        try {
            Stage stage = (Stage) menuButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/arkanoid/game/ModeSelect.fxml"));
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
    public void onCloseClick() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

}
