package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartMenuController {

    @FXML
    private AnchorPane rootPane;
    private Stage stage;

    public void showMenu(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/arkanoid/game/StartMenu.fxml"));
            Scene scene = new Scene(loader.load());
            StartMenuController controller = loader.getController();
            controller.stage = stage;
            controller.loadBackground();
            stage.setScene(scene);
            stage.setTitle("Start Menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBackground() {
        try {
            // Đường dẫn đến ảnh nền trong resources
            Image backgroundImage = new Image(getClass().getResourceAsStream("/images/StartMenu.png"));

            // Tạo ImageView hiển thị nền
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(Config.WIDTH_CANVAS); // kích thước cửa sổ
            backgroundView.setFitHeight(Config.HEIGHT_CANVAS);
            backgroundView.setPreserveRatio(false);

            // Đưa background xuống dưới cùng layout
            rootPane.getChildren().add(0, backgroundView);
        } catch (Exception e) {
            System.err.println("Không thể load ảnh nền Start Menu: " + e.getMessage());
            // Fallback — nếu ảnh lỗi, tô nền màu mặc định
            if (rootPane != null) {
                rootPane.setStyle("-fx-background-color: linear-gradient(to bottom, #1a237e, #283593);");
            }
        }
    }

    @FXML
    protected void onStartClick() {
        try {
            GameScreen gameScreen = new GameScreen();

            Scene scene = new Scene(gameScreen.createContent(), Config.WIDTH_CANVAS, Config.HEIGHT_CANVAS);
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
    }
}