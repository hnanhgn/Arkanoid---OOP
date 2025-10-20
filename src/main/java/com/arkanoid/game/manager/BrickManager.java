package com.arkanoid.game.manager;

import com.arkanoid.game.entities.Brick;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public  class BrickManager {
    protected List<Brick> bricks;
    protected Random random = new Random();

    public BrickManager() {
        bricks = new ArrayList<>();
        createBricks();
    }

    public void createBricks(){};

    public List<Brick> getBricks() {
        return bricks;
    }

    /** Reset trạng thái gạch */
    public void resetBricks() {
        for (Brick brick : bricks) {
            if (brick.isDestroyed()) {
                brick.getNode().setVisible(true);
            }
        }
    }

    /** Kiểm tra xem toàn bộ gạch đã bị phá hủy chưa */
    public boolean allBricksDestroyed() {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                return false;
            }
        }
        return true;
    }
}
