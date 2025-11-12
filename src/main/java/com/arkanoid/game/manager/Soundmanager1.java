
package com.arkanoid.game.manager;

import javafx.scene.media.AudioClip;
import java.util.HashMap;
import java.util.Map;

public class Soundmanager1 {
    private static Soundmanager1 instance;
    private final Map<String, AudioClip> sounds = new HashMap<>();

    private Soundmanager1() {
        // Preload sounds (AudioClip cho ngắn)
        preloadSound("hit_Brick.mp3", "/sound/hit_Brick.mp3");
        preloadSound("Paddle_hit_ball.mp3", "/sound/Paddle_hit_ball.mp3");
        preloadSound("Power_up.mp3", "/sound/Power_up.mp3");
        preloadSound("Explosion.mp3", "/sound/Explosion.wav");
    }

    public static Soundmanager1 getInstance() {
        if (instance == null) {
            instance = new Soundmanager1();
        }
        return instance;
    }

    private void preloadSound(String key, String path) {
        try {
            AudioClip clip = new AudioClip(getClass().getResource(path).toExternalForm());
            clip.setVolume(0.5);
            sounds.put(key, clip);
        } catch (Exception e) {
            System.err.println("Không thể tải sound: " + path + " - " + e.getMessage());
        }
    }

    public void play(String key) {
        AudioClip clip = sounds.get(key);
        if (clip != null) {
            clip.play(); // play nhanh, không giật
        } else {
            System.err.println("Sound không tồn tại: " + key);
        }
    }
}
