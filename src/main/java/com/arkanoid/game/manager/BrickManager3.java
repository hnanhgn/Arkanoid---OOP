package com.arkanoid.game.manager;

import com.arkanoid.game.Config;
import com.arkanoid.game.entities.Brick;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrickManager3 extends BrickManager {
    public BrickManager3() {
        createBricks();
    }

    @Override
    public void createBricks() {
        int rows = 5;
        int cols = 10;
        double playWidth = 850 - 250; // độ rộng vùng chơi = 600
        double totalBricksWidth = cols * Config.BRICK_WIDTH;
        double startX = 250 + (playWidth - totalBricksWidth) / 2;
        double startY = 20;

        int[][] heart = {
                {1,1,0,1,1,1,1,0,1,1},
                {1,0,0,0,1,1,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,1},
                {1,1,0,0,0,0,0,0,1,1},
                {1,1,1,1,0,0,1,1,1,1}
        };

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(heart[i][j] == 1){
                    double x = startX + j * Config.BRICK_WIDTH;
                    double y = startY + i * Config.BRICK_HEIGHT;
                    int color = random.nextInt(8);
                    int type = 0;
                    Brick brick = new Brick(x, y, Config.BRICK_WIDTH - Config.BRICK_DISTANCE,
                            Config.BRICK_HEIGHT - Config.BRICK_DISTANCE, color, type);
                    bricks.add(brick);
                }
            }
        }
        setSpecialBricks(rows, cols, startX, startY, Config.BRICK_WIDTH, Config.BRICK_HEIGHT, Config.BRICK_DISTANCE);
        setLockBricks(rows, cols, startX, startY, Config.BRICK_WIDTH, Config.BRICK_HEIGHT, Config.BRICK_DISTANCE);
    }

    private void setSpecialBricks(int rows, int cols, double startX, double startY, double brickWidth, double brickHeight, double distance) {
        int specialRow = 0;
        int[] specialCols = {4,5};

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

        specialRow = 4;
        int[] specialCols2 = {0,9};

        for (int col : specialCols2) {
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
    public List<Brick> getBricks() {
        return bricks;
    }

    private void setLockBricks(int rows, int cols, double startX, double startY, double brickWidth, double brickHeight, double distance) {
        int lockRow = 5;
        int[] lockCols = {1, 3, 5, 7, 9};

        for (int col : lockCols) {
            double x = startX + col * brickWidth;
            double y = startY + lockRow * brickHeight;

            int color = random.nextInt(8);
            int type = 3;

            Brick lockBrick = new Brick(x, y, Config.BRICK_WIDTH - Config.BRICK_DISTANCE,
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
            bricks.add(lockBrick); // thêm vào danh sách bricks
        }

    }

    public void resetBricks() {
        bricks.clear();
        createBricks();
    }
}