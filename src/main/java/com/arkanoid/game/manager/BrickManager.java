package com.arkanoid.game.manager;

import com.arkanoid.game.entities.Brick;
import java.util.ArrayList;
import java.util.List;

public class BrickManager {
    private int WiDTH = 600;
    private int HEIGHT = 500;
    private final List<Brick> bricks = new ArrayList<>();

    public BrickManager() {
        createBricks();
    }

    private void createBricks() {
        int rows = 5;
        int cols = 10;
        double distance = 5;
        double startX = 10;
        double startY = 30;
        double brickWidth = (WiDTH - startX * 2) / cols;
        double brickHeight = (HEIGHT / 4.0 - startY) / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double x = startX + j * brickWidth;
                double y = startY + i * brickHeight;
                bricks.add(new Brick(x, y, brickWidth - distance, brickHeight - distance));
            }
        }
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public void resetBricks() {
        for (Brick brick : bricks) {
            // Cần thêm phương thức reset trong Brick class
            // brick.reset();
        }
        // Hoặc tạo lại bricks
        bricks.clear();
        createBricks();
    }
}