package com.arkanoid.game.ui;

import javafx.scene.media.AudioClip;

public class MusicClickController {
    private static MusicClickController instance;
    private AudioClip clickSound;

    private MusicClickController() {
        loadClickSound();  // Load synchronously in constructor
    }

    public static MusicClickController getInstance() {
        if (instance == null) {
            synchronized (MusicClickController.class) {
                if (instance == null) {
                    instance = new MusicClickController();
                }
            }
        }
        return instance;
    }

    private void loadClickSound() {
        try {
            clickSound = new AudioClip(
                    MusicClickController.class.getResource("/sound/click.mp3").toExternalForm()
            );
            clickSound.setVolume(0.5);
            System.out.println("Click sound preloaded");
        } catch (Exception e) {
            System.err.println("Không thể load click sound: " + e.getMessage());
        }
    }

    public void playClick() {
        if (clickSound != null) {
            clickSound.play();  // play async, no need for new Thread
        }
    }

    public void shutdown() {
        // No executor to shutdown anymore
    }
}