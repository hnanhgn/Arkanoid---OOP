package com.arkanoid.game.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicMenuController {
    private static MusicMenuController instance;
    private MediaPlayer mediaPlayer;

    private MusicMenuController() {
        // Private constructor để singleton
    }

    public static MusicMenuController getInstance() {
        if (instance == null) {
            instance = new MusicMenuController();
        }
        return instance;
    }

    public void playMenuMusic() {
        if (mediaPlayer == null || mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            try {
                String musicFile = "/sound/menustart.mp3";
                Media sound = new Media(getClass().getResource(musicFile).toExternalForm());
                mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp vô hạn
                mediaPlayer.setVolume(0.5); // Âm lượng 50%
                mediaPlayer.play();
            } catch (Exception e) {
                System.err.println("Không thể phát nhạc menu: " + e.getMessage());
            }
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
}