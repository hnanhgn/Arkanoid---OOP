package com.arkanoid.game.ui;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MusicMenuController {
    private static MusicMenuController instance;

    // Map lưu các loại nhạc: "menu", "gameover", "gamepassed"
    private final Map<String, MediaPlayer> musicPlayers = new HashMap<>();
    private String currentPlaying = null;

    private final ExecutorService bgExecutor = Executors.newSingleThreadExecutor();
    private final AtomicBoolean preloaded = new AtomicBoolean(false);

    private MusicMenuController() {
        preloadAllMusic();  // Tự động preload khi khởi tạo
    }

    public static MusicMenuController getInstance() {
        if (instance == null) {
            synchronized (MusicMenuController.class) {
                if (instance == null) {
                    instance = new MusicMenuController();
                }
            }
        }
        return instance;
    }

    /**
     * Preload TẤT CẢ nhạc background (async, không lag UI)
     */
    private void preloadAllMusic() {
        if (preloaded.get()) return;

        CompletableFuture.runAsync(() -> {
            try {
                loadMusic("menu", "/sound/menustart.mp3");
                loadMusic("gameover", "/sound/Game_Over.mp3");
                loadMusic("gamepassed", "/sound/Game_Passed.mp3");

                preloaded.set(true);
                System.out.println("TẤT CẢ NHẠC ĐÃ PRELOAD XONG (background)!");

                // Sau khi preload xong → ĐẢM BẢO PHÁT NHẠC MENU NGAY
                Platform.runLater(() -> {
                    if (currentPlaying == null) {
                        playMusic("menu");
                    }
                });
            } catch (Exception e) {
                System.err.println("Lỗi preload nhạc: " + e.getMessage());
            }
        }, bgExecutor);
    }

    private void loadMusic(String type, String path) {
        try {
            Media media = new Media(getClass().getResource(path).toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(0.3);
            musicPlayers.put(type, player);
            System.out.println("Loaded: " + type);
        } catch (Exception e) {
            System.err.println("Không load được: " + path);
        }
    }

    public void playMusic(String type) {
        if (currentPlaying != null && currentPlaying.equals(type)) {
            return; // Đã đang phát nhạc này, không cần restart để tránh gián đoạn
        }

        stopCurrentMusic();

        MediaPlayer player = musicPlayers.get(type);
        if (player != null) {
            CompletableFuture.runAsync(() -> {
                player.play();
                currentPlaying = type;
            }, bgExecutor);
        } else {
            System.err.println("Nhạc không tồn tại: " + type);
        }
    }

    private void stopCurrentMusic() {
        if (currentPlaying != null) {
            MediaPlayer oldPlayer = musicPlayers.get(currentPlaying);
            if (oldPlayer != null) {
                oldPlayer.stop();
            }
            currentPlaying = null;
        }
    }

    public void stopMusic() {
        stopCurrentMusic();
    }

    public void stopAllMusic() {
        stopCurrentMusic();
        for (MediaPlayer player : musicPlayers.values()) {
            if (player != null) {
                player.stop();
                player.dispose();
            }
        }
        musicPlayers.clear();
    }

    public boolean isReady() {
        return preloaded.get();
    }

    public void shutdown() {
        stopAllMusic();
        bgExecutor.shutdown();
    }
}