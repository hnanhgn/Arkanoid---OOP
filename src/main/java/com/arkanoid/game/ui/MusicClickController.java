package com.arkanoid.game.ui;

import javafx.scene.media.AudioClip;

public class MusicClickController {
    // Singleton instance
    private static MusicClickController instance;

    // AudioClip chỉ load 1 lần
    private AudioClip clickSound;

    private MusicClickController() {
        try {
            // Load âm thanh click từ resource
            clickSound = new AudioClip(
                    MusicClickController.class.getResource("/sound/click.mp3").toExternalForm()
            );
            clickSound.setVolume(0.5); // Đặt âm lượng mặc định
        } catch (Exception e) {
            System.err.println("Không thể load âm thanh click: " + e.getMessage());
        }
    }

    public static MusicClickController getInstance() {
        if (instance == null) {
            instance = new MusicClickController();
        }
        return instance;
    }

    // Phát âm thanh click
    public void playClick() {
        if (clickSound != null) {
            clickSound.play();
        }
    }
}