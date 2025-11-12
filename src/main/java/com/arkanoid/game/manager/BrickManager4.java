package com.arkanoid.game.manager;

import com.arkanoid.game.Config;
import com.arkanoid.game.entities.Brick;

public class BrickManager4 extends BrickManager {

    public BrickManager4() { createBricks(); }

    @Override
    public void createBricks() {
        int rows = 6, cols = 10;
        double startX = 250 + (600 - cols*Config.BRICK_WIDTH - Config.BRICK_DISTANCE)/2;
        double startY = 20;

        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                if(j==0 || j==cols-1 || i==j && i<3 || i+j==cols-1 && i<3){
                    double x=startX+j*Config.BRICK_WIDTH;
                    double y=startY+i*Config.BRICK_HEIGHT;
                    bricks.add(new Brick(x,y,Config.BRICK_WIDTH-Config.BRICK_DISTANCE, Config.BRICK_HEIGHT-Config.BRICK_DISTANCE, random.nextInt(8),0));
                }
            }
        }

        // Special bricks
        bricks.add(new Brick(startX+4*Config.BRICK_WIDTH, startY+2*Config.BRICK_HEIGHT, Config.BRICK_WIDTH-Config.BRICK_DISTANCE, Config.BRICK_HEIGHT-Config.BRICK_DISTANCE,2,2));

        // Lock bricks
        bricks.add(new Brick(startX+5*Config.BRICK_WIDTH, startY+0*Config.BRICK_HEIGHT, Config.BRICK_WIDTH-Config.BRICK_DISTANCE, Config.BRICK_HEIGHT-Config.BRICK_DISTANCE,3,3));
    }
}
