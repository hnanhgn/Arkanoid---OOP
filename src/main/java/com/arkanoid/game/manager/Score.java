package com.arkanoid.game.manager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Score {
    private int score;

    public Score() {
        this.score = 0;
    }

    public void increaseScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 30));
        gc.fillText(String.valueOf(score), 180, 47);
    }
}
