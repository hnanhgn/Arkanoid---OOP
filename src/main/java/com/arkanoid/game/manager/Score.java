
package com.arkanoid.game.manager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.*;

public class Score {
    private int score;
    private int highScore;
    private final String FILE_PATH = "highscore.txt";

    private int loadHighScore() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            return Integer.parseInt(br.readLine());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    public Score() {
        this.score = 0;
        this.highScore = loadHighScore();
    }

    public void increaseScore(int amount) {
        score += amount;
        if (score > highScore) {
            highScore = score;
        }
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void reset() {
        score = 0;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.web("#A8D8FF"));
        gc.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 30));
        gc.fillText(String.valueOf(score), 250 / 2 - 6, 300);
    }
    public void saveHighScore() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write(String.valueOf(highScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}