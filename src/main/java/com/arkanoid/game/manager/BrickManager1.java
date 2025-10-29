package com.arkanoid.game.manager;

import com.arkanoid.game.Config;
import com.arkanoid.game.entities.Brick;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrickManager1 extends BrickManager{

    public BrickManager1() {
        createBricks();
    }

    @Override
    public void createBricks() {
        int rows = 5;
        int cols = 10;
        double startX  = (600 - cols * Config.BRICK_WIDTH - Config.BRICK_DISTANCE) / 2; ;
        double startY = 150;

        for (int i = 0; i < rows; i++) {
            int num = cols - i * 2;
            int startCol = i;
            for (int j = 0; j < num; j++) {
                double x = startX + (startCol + j) * Config.BRICK_WIDTH;
                double y = startY + i * Config.BRICK_HEIGHT;
                int color = random.nextInt(8);
                int type = 0;
                Brick brick = new Brick(x, y, Config.BRICK_WIDTH - Config.BRICK_DISTANCE,
                        Config.BRICK_HEIGHT - Config.BRICK_DISTANCE, color, type);
                bricks.add(brick);
            }
        }
        setSpecialBricks(rows, cols, startX, startY, Config.BRICK_WIDTH, Config.BRICK_HEIGHT, Config.BRICK_DISTANCE);
    }

    private void setSpecialBricks(int rows, int cols, double startX, double startY, double brickWidth, double brickHeight, double distance) {
        int specialRow = 4;
        int[] specialCols = {0, 2, 4, 5, 7, 9};

        for (int col : specialCols) {
            double x = startX + col * brickWidth;
            double y = startY + specialRow * brickHeight;

            int color = random.nextInt(8);
            int type = 2;

            Brick specialBrick = new Brick(x, y, brickWidth - distance, brickHeight - distance, color, type);
            Brick toRemove = null;
            for (Brick b : bricks) {
                if (Math.abs(b.getX() - x) < 1e-3 && Math.abs(b.getY() - y) < 1e-3) {
                    toRemove = b;
                    break;
                }
            }

            if (toRemove != null) {
                bricks.remove(toRemove);
            }
            bricks.add(specialBrick);
        }
    }

}