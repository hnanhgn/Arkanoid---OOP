package com.arkanoid.game.ui;

import com.arkanoid.game.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class PassedModeController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    private Stage stage;
    private int currentMode;
    private int currentScore; // Điểm hiện tại

    private MediaPlayer mediaPlayer;

    @FXML
    private Label currentScoreLabel;

    @FXML
    private Label highScoreLabel;

    public void setScore(int score) {
        this.currentScore = score;
        currentScoreLabel.setText("Your Score: " + score);

        int highScore = loadHighScore();
        if (score > highScore) {
            final int newHighScore = score;
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentMode(int mode) {
        this.currentMode = mode;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String musicFile = "/sound/Game_Passed.mp3";
            Media sound = new Media(getClass().getResource(musicFile).toExternalForm());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.7);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Không thể phát nhạc game_passed: " + e.getMessage());
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
    protected void onNextClick() {
        MusicClickController.getInstance().playClick();
        stopMusic();
        try {
            int nextMode = (currentMode + 1) % 7;

            GameScreen gameScreen = new GameScreen(stage, nextMode);
            Scene scene = new Scene(gameScreen.createContent(), Config.WIDTH_CANVAS, Config.HEIGHT_CANVAS);
            gameScreen.setupInputHandlers(scene);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onMenuClick() {
        MusicClickController.getInstance().playClick();
        stopMusic();
        try {
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
    protected void onCloseClick() {
        MusicClickController.getInstance().playClick();
        stopMusic();
        stage.close();
        System.exit(0);
    }

    public static void showPassedGame(Stage stage, int mode, int currentScore) {
        try {
            FXMLLoader loader = new FXMLLoader(PassedModeController.class.getResource("/com/arkanoid/game/PassedGame.fxml"));
            AnchorPane root = loader.load();

            PassedModeController controller = loader.getController();
            controller.setStage(stage);
            controller.setCurrentMode(mode);
            controller.setScore(currentScore); // Gọi ngay setScore để hiển thị đúng

            Scene scene = new Scene(root, Config.WIDTH_CANVAS, Config.HEIGHT_CANVAS);
            stage.setScene(scene);
            stage.setTitle("Passed Mode " + (mode + 1));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}