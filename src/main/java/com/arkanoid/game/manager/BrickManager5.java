package com.arkanoid.game.manager;

import com.arkanoid.game.Config;
import com.arkanoid.game.entities.Brick;

public class BrickManager5 extends BrickManager {

    public BrickManager5() { createBricks(); }

    @Override
    public void createBricks() {
        int rows = 7;
        int cols = 10;
        double startX = 250 + (600 - cols*Config.BRICK_WIDTH - Config.BRICK_DISTANCE)/2;
        double startY = 20;

        // 1 = gạch thường, 0 = không có gạch
        int[][] face = {
                {0,1,1,0,0,0,0,1,1,0},
                {0,1,0,1,0,0,1,0,1,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,1,0,0,0,0,1,0,0},
                {0,0,0,1,1,1,1,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,1,1,1,1,1,1,1,1,0}
        };

        // tạo gạch thường
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                if(face[i][j]==1){
                    double x = startX + j*Config.BRICK_WIDTH;
                    double y = startY + i*Config.BRICK_HEIGHT;
                    bricks.add(new Brick(x,y,Config.BRICK_WIDTH-Config.BRICK_DISTANCE,
                            Config.BRICK_HEIGHT-Config.BRICK_DISTANCE, random.nextInt(8),0));
                }
            }
        }

        // đặt gạch special làm mắt
        int[][] eyes = {{1,1},{1,8}};
        for(int[] pos:eyes){
            double x = startX + pos[1]*Config.BRICK_WIDTH;
            double y = startY + pos[0]*Config.BRICK_HEIGHT;
            bricks.removeIf(b->Math.abs(b.getX()-x)<1e-3 && Math.abs(b.getY()-y)<1e-3);
            bricks.add(new Brick(x,y,Config.BRICK_WIDTH-Config.BRICK_DISTANCE,
                    Config.BRICK_HEIGHT-Config.BRICK_DISTANCE, pos[1]%8,2));
        }

        // đặt gạch lock làm 2 góc miệng
        int[][] mouthCorners = {{4,3},{4,6}};
        for(int[] pos:mouthCorners){
            double x = startX + pos[1]*Config.BRICK_WIDTH;
            double y = startY + pos[0]*Config.BRICK_HEIGHT;
            bricks.removeIf(b->Math.abs(b.getX()-x)<1e-3 && Math.abs(b.getY()-y)<1e-3);
            bricks.add(new Brick(x,y,Config.BRICK_WIDTH-Config.BRICK_DISTANCE,
                    Config.BRICK_HEIGHT-Config.BRICK_DISTANCE, pos[1]%8,3));
        }
    }
}
