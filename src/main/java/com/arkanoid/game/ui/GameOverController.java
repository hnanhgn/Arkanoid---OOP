package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
import java.util.ConcurrentModificationException;
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
        loadBackground();
        loadButtonImages();
    }

    public void setCurrentMode(int mode) {
        this.currentMode = mode;
    }

    private void loadBackground() {
        try {
            Image backgroundImage = new Image(getClass().getResourceAsStream("/images/GameOver.png"));
            ImageView backgroundView = new ImageView(backgroundImage);

            // Trải full AnchorPane
            backgroundView.setFitWidth(rootPane.getPrefWidth());
            backgroundView.setFitHeight(rootPane.getPrefHeight());
            backgroundView.setPreserveRatio(false);

            // Neo ảnh vào 4 cạnh để không bị hở
            AnchorPane.setTopAnchor(backgroundView, 0.0);
            AnchorPane.setBottomAnchor(backgroundView, 0.0);
            AnchorPane.setLeftAnchor(backgroundView, 0.0);
            AnchorPane.setRightAnchor(backgroundView, 0.0);

            // Đảm bảo ảnh nằm dưới các nút
            rootPane.getChildren().add(0, backgroundView);

        } catch (Exception e) {
            System.err.println("Không thể load ảnh nền GameOver: " + e.getMessage());
        }
    }


    private void loadButtonImages() {
        try {
            // Load ảnh nút
            Image restartImg = new Image(getClass().getResourceAsStream("/images/GameOverRestart.png"));
            Image exitImg    = new Image(getClass().getResourceAsStream("/images/GameOverExit.png"));
            Image menuImg    = new Image(getClass().getResourceAsStream("/images/GameOverMenu.png"));

            setButtonImage(restartButton, restartImg);
            setButtonImage(closeButton, exitImg);
            setButtonImage(backMenuButton, menuImg);

        } catch (Exception e) {
            System.err.println("Không thể load ảnh nút GameOver: " + e.getMessage());
        }
    }

    private void setButtonImage(Button btn, Image img) {
        ImageView view = new ImageView(img);
        view.setFitWidth(220);   // chỉnh tùy kích thước nút của bạn
        view.setFitHeight(90);
        view.setPreserveRatio(true);

        btn.setGraphic(view);
        btn.setText(""); // Không chữ
        btn.setBackground(null);
        btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // Hiệu ứng hover
        btn.setOnMouseEntered(e -> btn.setOpacity(0.75));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void onRestartClick() {
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
            Parent root = loader.load();
            Scene scene = new Scene(root, 850, 700); // Kích thước khớp ảnh
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