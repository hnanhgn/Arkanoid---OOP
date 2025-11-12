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
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    private Stage stage;
    private boolean isWin;
    private int currentMode;

    private MediaPlayer mediaPlayer;
    private AudioClip clickSound;

    @FXML private Label currentScoreLabel;
    @FXML private Label highScoreLabel;

    private int currentScore;

    public void setScore(int score) {
        this.currentScore = score;
        currentScoreLabel.setText("Your Score: " + score);

        int highScore = loadHighScore();
        if (score > highScore) {
            final int newHighScore = score; // dùng lambda thread
            new Thread(() -> saveHighScore(newHighScore)).start();
            highScore = score;
        }
        highScoreLabel.setText("High Score: " + highScore);
    }

    private int loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            return 0;
        }
    }

    private void saveHighScore(int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"))) {
            writer.write(String.valueOf(score));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setCurrentMode(int mode) {
        this.currentMode = mode;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String musicFile = "/sound/Game_Over.mp3";
            Media sound = new Media(getClass().getResource(musicFile).toExternalForm());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp vô hạn
            mediaPlayer.setVolume(0.7);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Không thể phát nhạc game_over: " + e.getMessage());
            // Không throw exception để tránh crash game
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }


    @FXML
    protected void onRestartClick() {
        MusicClickController.getInstance().playClick();
        stopMusic(); // Dừng nhạc trước khi chuyển cảnh
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
        MusicClickController.getInstance().playClick();
        stopMusic();
        if (stage != null) {
            stage.close();
        }
        System.exit(0);
    }

    @FXML
    protected void onBackMenuClick() {
        MusicClickController.getInstance().playClick();
        stopMusic(); // Dừng nhạc trước khi chuyển cảnh
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

    public static void showGameOver(Stage stage, boolean isWin, int mode, int score) {
        try {
            FXMLLoader loader = new FXMLLoader(GameOverController.class.getResource("/com/arkanoid/game/GameOver.fxml"));
            AnchorPane root = loader.load();

            GameOverController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentMode(mode);
            controller.isWin = isWin;
            controller.setScore(score);

            Scene scene = new Scene(root, Config.WIDTH_CANVAS, Config.HEIGHT_CANVAS);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}