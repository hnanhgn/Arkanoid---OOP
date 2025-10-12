package com.arkanoid.game.ui;

import com.arkanoid.game.manager.BallManager;
import com.arkanoid.game.manager.BrickManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController implements Initializable {

    @FXML
    private Label titleLabel;

    @FXML
    private Button restartButton;

    @FXML
    private Button closeButton;


    @FXML
    private AnchorPane rootPane;

    private Stage stage;
    private boolean isWin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load background image
        loadBackground();
    }

    private void loadBackground() {
        try {
            // Đường dẫn đến ảnh background
            Image backgroundImage = new Image(getClass().getResourceAsStream("/images/you_lose.png"));
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(600);
            backgroundView.setFitHeight(500);
            backgroundView.setPreserveRatio(false);

            // Thêm background vào đầu rootPane
            rootPane.getChildren().add(0, backgroundView);

        } catch (Exception e) {
            System.err.println("Không thể load background image: " + e.getMessage());
            // Fallback: sử dụng màu nền gradient
            rootPane.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1a237e, #311b92);");
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setGameResult(boolean isWin) {
        this.isWin = isWin;
        updateUI();
    }

    private void updateUI() {
        if (isWin) {
            titleLabel.setText("YOU WIN!");
            titleLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-font-size: 50;");
            // Điều chỉnh vị trí nếu cần
            titleLabel.setLayoutX(150);
            titleLabel.setPrefWidth(300);
        } else {
            titleLabel.setText("YOU LOSE");
            titleLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold; -fx-font-size: 50;");
            // Điều chỉnh vị trí nếu cần
            titleLabel.setLayoutX(150);
            titleLabel.setPrefWidth(300);
        }
    }

    @FXML
    protected void onRestartClick() {
        try {
            // Tạo game mới với cùng stage
            GameScreen gameScreen = new GameScreen(stage);
            Scene scene = new Scene(gameScreen.createContent(), 600, 500);

            gameScreen.setupInputHandlers(scene);

            stage.setTitle("Arkanoid Game");
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

    // Phương thức hiển thị màn hình game over
    public void showGameOver(Stage stage, boolean isWin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/arkanoid/game/ui/game_over.fxml"));
            Scene scene = new Scene(loader.load());
            GameOverController controller = loader.getController();
            controller.stage = stage;
            controller.setGameResult(isWin);

            stage.setScene(scene);
            stage.setTitle(isWin ? "You Win!" : "Game Over");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức tĩnh để hiển thị game over từ bên ngoài
    public static void showGameOver(boolean isWin, Stage parentStage) {
        try {
            GameOverController controller = new GameOverController();
            controller.showGameOver(parentStage, isWin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}