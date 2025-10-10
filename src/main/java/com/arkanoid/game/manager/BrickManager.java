package com.arkanoid.game.manager;

import com.arkanoid.game.entities.Brick;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class BrickManager {
    public static final int WiDTH = 600;
    public static final int HEIGHT = 500;
    private final List<Brick> bricks = new ArrayList<>();

    public BrickManager() {
        createBricks();
    }

    private void createBricks() {
        int rows = 5;
        int cols = 10;
        double distance = 5;
        double startX = 5;
        double startY = 5;
        double brickWidth = (WiDTH - startX) / cols;
        double brickHeight = (HEIGHT - startY) / (3 * rows);

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
}