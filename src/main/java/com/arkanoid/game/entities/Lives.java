package com.arkanoid.game.entities;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;

public class Lives {
    private HBox livesDisplay;
    private Text livesText;
    private int lives;
    private int maxLives;

    public Lives(int initialLives, int maxLives) {
        this.lives = initialLives;
        this.maxLives = maxLives;
        this.livesDisplay = new HBox(10);
        setupLivesDisplay();
    }

    private void setupLivesDisplay() {
        livesDisplay.setPadding(new Insets(4));

        // Tạo text hiển thị số mạng - CHỈ TẠO 1 LẦN DUY NHẤT
        livesText = new Text(String.valueOf(lives));
        livesText.setFont(Font.font("Arial", 30));
        livesText.setFill(Color.BLACK);

        livesDisplay.getChildren().add(livesText);

        // Đặt vị trí ở góc trên bên phải
        livesDisplay.setLayoutX(440);
        livesDisplay.setLayoutY(18);
    }

    // Phương thức cập nhật số mạng
    public void updateLives(int newLives) {
        this.lives = Math.min(newLives, maxLives);
        updateLivesText();
    }

    // Phương thức giảm mạng
    public void decreaseLife() {
        if (lives > 0) {
            lives--;
            updateLivesText();
        }
    }

    // Phương thức cập nhật text - CHỈ CẬP NHẬT NỘI DUNG, KHÔNG TẠO MỚI
    private void updateLivesText() {
        // Chỉ cập nhật text của đối tượng livesText hiện có
        livesText.setText(String.valueOf(lives));
    }

    public int getLives() {
        return lives;
    }

    public HBox getNode() {
        return livesDisplay;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public void reset() {
        this.lives = maxLives;
        updateLivesText();
    }
}