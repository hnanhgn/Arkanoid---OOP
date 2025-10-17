package com.arkanoid.game.manager;

import com.arkanoid.game.Config;
import com.arkanoid.game.entities.Brick;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrickManager {
    private final List<Brick> bricks = new ArrayList<>();
    private final Random random = new Random();


    public BrickManager() {
        createBricks();
    }

    private void createBricks() {
        int rows = 5;
        int cols = 10;
        double startX  = (600 - cols * Config.BRICK_WIDTH - Config.BRICK_DISTANCE) / 2; ;
        double startY = 120;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double x = startX + j * Config.BRICK_WIDTH;
                double y = startY + i * Config.BRICK_HEIGHT;
                int color = random.nextInt(8);
                int type;
                int rand = random.nextInt(100);
                type = (rand < 85) ? 0 : 1;
                if(type == 0) {
                    Brick brick = new Brick(x, y, Config.BRICK_WIDTH - Config.BRICK_DISTANCE,
                            Config.BRICK_HEIGHT - Config.BRICK_DISTANCE, color, type);
                    bricks.add(brick);
                }

            }
        }
        setSpecialBricks(rows, cols, startX, startY, Config.BRICK_WIDTH, Config.BRICK_HEIGHT, Config.BRICK_DISTANCE);

    }

    private void setSpecialBricks(int rows, int cols, double startX, double startY, double brickWidth, double brickHeight, double distance) {
        int specialRow = 4;
        int[] specialCols = {1, 3, 5, 7, 9};

        for (int col : specialCols) {
            double x = startX + col * brickWidth;
            double y = startY + specialRow * brickHeight;

            int color = random.nextInt(8);
            int type = 2;

            Brick specialBrick = new Brick(x, y, Config.BRICK_WIDTH - Config.BRICK_DISTANCE,
                    Config.BRICK_HEIGHT - Config.BRICK_DISTANCE, color, type);
            //Tìm xem có gạch nào trùng vị trí gạch loại 0 không
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
            bricks.add(specialBrick); // thêm vào danh sách bricks
        }
    }


    public List<Brick> getBricks() {
        return bricks;
    }

    public void resetBricks() {
        bricks.clear();
        createBricks();
    }
}