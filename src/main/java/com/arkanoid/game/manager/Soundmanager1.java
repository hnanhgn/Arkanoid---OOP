package com.arkanoid.game.manager;

import javafx.scene.media.AudioClip;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Soundmanager1 {
    private static Soundmanager1 instance;

    private final Map<String, AudioClip> sounds = new HashMap<>();
    private final ExecutorService Thread = Executors.newSingleThreadExecutor();  // Thread riêng preload
    private final AtomicBoolean preloaded = new AtomicBoolean(false);  // Flag tránh preload nhiều lần

    private Soundmanager1() {
    }

    public static Soundmanager1 getInstance() {
        if (instance == null) {
            synchronized (Soundmanager1.class) {  // Thread-safe singleton
                if (instance == null) {
                    instance = new Soundmanager1();
                    instance.preloadSounds();  // Trigger preload trên background
                }
            }
        }
        return instance;
    }

    private void preloadSounds() {
        if (preloaded.get()) return;

        Thread.submit(() -> {  // Chạy preload trên thread nền
            try {
                preloadSound("hit_Brick.mp3", "/sound/hit_Brick.mp3");
                preloadSound("Paddle_hit_ball.mp3", "/sound/Paddle_hit_ball.mp3");
                preloadSound("Power_up.mp3", "/sound/Power_up.mp3");
                preloadSound("Explosion.mp3", "/sound/Explosion.wav");
                preloaded.set(true);
                System.out.println("Tất cả sounds đã preload xong (background thread)");
            } catch (Exception e) {
                System.err.println("Lỗi preload sounds: " + e.getMessage());
            }
        });
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
        // play() nhanh, chạy trực tiếp (AudioClip async, không block)
        AudioClip clip = sounds.get(key);
        if (clip != null) {
            new Thread(() -> clip.play()).start();  // Optional: Chạy play trên thread riêng nếu lo lag (hiếm)
        } else {
            System.err.println("Sound không tồn tại: " + key);
        }
    }

    // Cleanup khi thoát game
    public void shutdown() {
        Thread.shutdown();
    }
}