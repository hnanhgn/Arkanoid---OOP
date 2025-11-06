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
        loadButtonImages();
    }

    private void loadBackground() {
        try {
            String imagePath = "/images/PassedGame.png";
            Image backgroundImage = new Image(getClass().getResourceAsStream(imagePath));
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(850);
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

    private void loadButtonImages() {
        try {
            // Load ảnh
            Image nextImg = new Image(getClass().getResourceAsStream("/images/GamePassedNextMode.png"));
            Image menuImg = new Image(getClass().getResourceAsStream("/images/GamePassedMenu.png"));
            Image closeImg = new Image(getClass().getResourceAsStream("/images/GamePassedExit.png"));

            // Tạo ImageView
            ImageView nextView = new ImageView(nextImg);
            ImageView menuView = new ImageView(menuImg);
            ImageView closeView = new ImageView(closeImg);

            // Kích thước nút (tuỳ bạn chỉnh theo UI thực tế)
            nextView.setFitWidth(90);
            nextView.setPreserveRatio(true);

            menuView.setFitWidth(150);
            menuView.setPreserveRatio(true);

            closeView.setFitWidth(150);
            closeView.setPreserveRatio(true);

            // Gán hình vào nút
            nextButton.setGraphic(nextView);
            menuButton.setGraphic(menuView);
            closeButton.setGraphic(closeView);

            // Xoá viền + nền nút
            nextButton.setText("");
            menuButton.setText("");
            closeButton.setText("");
            nextButton.setStyle("-fx-background-color: transparent;");
            menuButton.setStyle("-fx-background-color: transparent;");
            closeButton.setStyle("-fx-background-color: transparent;");

            // Thêm hiệu ứng hover
            addHoverEffect(nextButton, nextView);
            addHoverEffect(menuButton, menuView);
            addHoverEffect(closeButton, closeView);

        } catch (Exception e) {
            System.out.println("Không load được ảnh nút: " + e.getMessage());
        }
    }

    private void addHoverEffect(Button button, ImageView imageView) {
        // Khi di chuột vào → phóng to nhẹ và sáng hơn
        button.setOnMouseEntered(e -> {
            imageView.setScaleX(1.12);
            imageView.setScaleY(1.12);
            imageView.setOpacity(0.85); // mờ nhẹ đẹp
        });

        // Khi rời chuột → trở về bình thường
        button.setOnMouseExited(e -> {
            imageView.setScaleX(1.0);
            imageView.setScaleY(1.0);
            imageView.setOpacity(1.0);
        });

        // Khi nhấn chuột → thu nhỏ một chút cho cảm giác click
        button.setOnMousePressed(e -> {
            imageView.setScaleX(0.95);
            imageView.setScaleY(0.95);
        });

        // Nhả chuột → phục hồi lại hover state
        button.setOnMouseReleased(e -> {
            imageView.setScaleX(1.12);
            imageView.setScaleY(1.12);
        });
    }
}