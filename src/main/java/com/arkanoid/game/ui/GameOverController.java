package com.arkanoid.game.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button restartButton;

    @FXML
    private Button closeButton;

    @FXML
    private Button backMenuButton;

    private Stage stage;
    private boolean isWin;
    private int currentMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBackground(isWin);
        loadButtonImages();
    }

    public void setCurrentMode(int mode) {
        this.currentMode = mode;
    }

    private void loadBackground(boolean isWin) {
        try {
            String imagePath = isWin ? "/images/YouWin.png" : "/images/GameOver.png";
            Image backgroundImage = new Image(getClass().getResourceAsStream(imagePath));
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(600);
            backgroundView.setFitHeight(500);
            backgroundView.setPreserveRatio(false);
            rootPane.getChildren().add(0, backgroundView);
        } catch (Exception e) {
            System.err.println("Không thể load ảnh nền GameOver: " + e.getMessage());
            rootPane.setStyle("-fx-background-color: linear-gradient(to bottom, #1a237e, #283593);");
        }
    }

    private void loadButtonImages() {
        try {
            Image buttonImg = new Image(getClass().getResourceAsStream("/images/GameOverButton.png"));

            ImageView restartView = new ImageView(buttonImg);
            restartView.setFitWidth(150);
            restartView.setFitHeight(50);
            Label restartText = new Label("Restart");
            restartText.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            StackPane restartStack = new StackPane(restartView, restartText);
            restartButton.setGraphic(restartStack);
            restartButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

            ImageView closeView = new ImageView(buttonImg);
            closeView.setFitWidth(150);
            closeView.setFitHeight(50);
            Label closeText = new Label("Close");
            closeText.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            StackPane closeStack = new StackPane(closeView, closeText);
            closeButton.setGraphic(closeStack);
            closeButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

            ImageView menuView = new ImageView(buttonImg);
            menuView.setFitWidth(150);
            menuView.setFitHeight(50);
            Label menuText = new Label("Menu");
            menuText.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            StackPane menuStack = new StackPane(menuView, menuText);
            backMenuButton.setGraphic(menuStack);
            backMenuButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        } catch (Exception e) {
            System.err.println("Không thể load ảnh nút: " + e.getMessage());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setGameResult(boolean isWin) {
        this.isWin = isWin;
        loadBackground(isWin);
    }

    @FXML
    protected void onRestartClick() {
        try {
<<<<<<< Updated upstream
            GameScreen gameScreen = new GameScreen(stage);
            Scene scene = new Scene(gameScreen.createContent(), 600, 500);
=======
            GameScreen gameScreen = new GameScreen(stage, currentMode);
            Scene scene = new Scene(gameScreen.createContent(), Config.WIDTH_CANVAS, Config.HEIGHT_CANVAS);
>>>>>>> Stashed changes
            gameScreen.setupInputHandlers(scene);

            stage.setTitle("Arkanoid Game - Mode " + currentMode);
            stage.setScene(scene);
            stage.show();

            gameScreen.createContent().requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCloseClick() {
        if (stage != null) {
            stage.close();
        }
        System.exit(0);
    }

    @FXML
    protected void onBackMenuClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/arkanoid/game/ModeSelect.fxml"));
            Scene modeSelectScene = new Scene(loader.load());
            ModeSelectController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(modeSelectScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức hiển thị màn hình GameOver
    public void showGameOver(Stage stage, boolean isWin, int mode) {
        try {
            FXMLLoader loader = new FXMLLoader(GameOverController.class.getResource("/com/arkanoid/game/GameOver.fxml"));
            Scene scene = new Scene(loader.load());
            GameOverController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentMode(mode);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức tĩnh gọi từ BallManager
    public static void showGameOver(boolean isWin, Stage parentStage, int mode) {
        try {
            GameOverController controller = new GameOverController();
            controller.showGameOver(parentStage, isWin, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
