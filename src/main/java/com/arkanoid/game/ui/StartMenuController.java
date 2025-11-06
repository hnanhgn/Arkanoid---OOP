package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartMenuController {

    @FXML
    private AnchorPane rootPane; // bạn đang thiếu @FXML

    @FXML
    private Button startButton;

    @FXML
    private Button closeButton;

    private Stage stage;

    @FXML
    public void initialize() {
        loadButtonImages(); // gọi khi FXML load xong
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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
            Image backgroundImage = new Image(getClass().getResourceAsStream("/images/StartMenu.png"));
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(Config.WIDTH_CANVAS);
            backgroundView.setFitHeight(Config.HEIGHT_CANVAS);
            backgroundView.setPreserveRatio(false);

            rootPane.getChildren().add(0, backgroundView);
        } catch (Exception e) {
            System.err.println("Không thể load ảnh nền Start Menu: " + e.getMessage());
            if (rootPane != null) {
                rootPane.setStyle("-fx-background-color: linear-gradient(to bottom, #1a237e, #283593);");
            }
        }
    }

    /** ✅ Load ảnh PNG vào nút */
    private void loadButtonImages() {
        try {
            Image startImg = new Image(getClass().getResourceAsStream("/images/MenuPlay.png"));
            Image exitImg = new Image(getClass().getResourceAsStream("/images/MenuExit.png"));

            ImageView startView = new ImageView(startImg);
            startView.setFitWidth(85);
            startView.setPreserveRatio(true);

            ImageView exitView = new ImageView(exitImg);
            exitView.setFitWidth(150);
            exitView.setPreserveRatio(true);

            startButton.setGraphic(startView);
            closeButton.setGraphic(exitView);

            startButton.setText("");
            closeButton.setText("");
            startButton.setStyle("-fx-background-color: transparent;");
            closeButton.setStyle("-fx-background-color: transparent;");

            addHoverEffect(startButton, startView);
            addHoverEffect(closeButton, exitView);
        } catch (Exception e) {
            System.out.println("Không load được ảnh nút: " + e.getMessage());
        }
    }

    @FXML
    protected void onStartClick() {
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
        if (stage != null) {
            stage.close();
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
