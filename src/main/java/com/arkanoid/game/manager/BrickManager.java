package com.arkanoid.game.manager;

import com.arkanoid.game.entities.Brick;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class BrickManager {
    protected List<Brick> bricks;
    protected Random random = new Random();

    public BrickManager() {
        bricks = new ArrayList<>();
        checkDuplicateBricks();
    }

    /** Phương thức abstract để lớp con tự định nghĩa bố cục gạch */
    public abstract void createBricks();

    public List<Brick> getBricks() {
        return bricks;
    }

    /** Reset trạng thái gạch */
    public void resetBricks() {
        for (Brick brick : bricks) {
            if (brick.isDestroyed()) {
                brick.getNode().setVisible(true);
                brick.setDestroyed(false);
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

    /** ✅ Thêm hàm kiểm tra trùng vị trí để debug dễ hơn */
    protected void checkDuplicateBricks() {
        Set<String> positions = new HashSet<>();
        for (Brick b : bricks) {
            String key = b.getX() + "," + b.getY();
            if (!positions.add(key)) {
                System.out.println("⚠️ WARNING: Duplicate brick at (" + b.getX() + ", " + b.getY() + ")");
            }
        }
    }
}