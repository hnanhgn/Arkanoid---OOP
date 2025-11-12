package com.arkanoid.game.manager;

import com.arkanoid.game.Config;
import com.arkanoid.game.entities.Brick;

public class BrickManager6 extends BrickManager {

    public BrickManager6(){ createBricks(); }

    @Override
    public void createBricks(){
        int rows=6,cols=10;
        double startX=250 + (600-cols*Config.BRICK_WIDTH - Config.BRICK_DISTANCE)/2;
        double startY=20;

        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                if((i+j)%2==0){
                    double x=startX+j*Config.BRICK_WIDTH;
                    double y=startY+i*Config.BRICK_HEIGHT;
                    bricks.add(new Brick(x,y,Config.BRICK_WIDTH-Config.BRICK_DISTANCE,Config.BRICK_HEIGHT-Config.BRICK_DISTANCE,random.nextInt(8),0));
                }
            }
        }

        // Special bricks
        bricks.add(new Brick(startX+2*Config.BRICK_WIDTH, startY+0*Config.BRICK_HEIGHT, Config.BRICK_WIDTH-Config.BRICK_DISTANCE, Config.BRICK_HEIGHT-Config.BRICK_DISTANCE,2,2));
        bricks.add(new Brick(startX+7*Config.BRICK_WIDTH, startY+5*Config.BRICK_HEIGHT, Config.BRICK_WIDTH-Config.BRICK_DISTANCE, Config.BRICK_HEIGHT-Config.BRICK_DISTANCE,3,2));

        // Lock bricks
        for(int i=0;i<rows;i+=2){
            for(int j=1;j<cols;j+=2){
                double x=startX+j*Config.BRICK_WIDTH;
                double y=startY+i*Config.BRICK_HEIGHT;
                bricks.add(new Brick(x,y,Config.BRICK_WIDTH-Config.BRICK_DISTANCE,Config.BRICK_HEIGHT-Config.BRICK_DISTANCE,random.nextInt(8),3));
            }
        }
    }
}
