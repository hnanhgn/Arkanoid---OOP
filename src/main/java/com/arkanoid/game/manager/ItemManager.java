
package com.arkanoid.game.manager;

import com.arkanoid.game.entities.Item;
import com.arkanoid.game.entities.ItemType;
import com.arkanoid.game.entities.Paddle;
import com.arkanoid.game.entities.Brick;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemManager {
    private final Canvas canvas;
    private final Paddle paddle;
    private final List<Item> items;
    private final Random random;
    private int lastScore = 0;

    // Các biến trạng thái item
    private boolean multiBallRequested = false;
    private boolean ghostModeRequested = false;
    private boolean explosionModeRequested = false;
    private boolean speedBoostRequested = false;
    private boolean paddleExpandRequested = false;

    // Images for items
    private Image multiBallImage;
    private Image ghostBallImage;
    private Image explosionBallImage;
    private Image speedBoostImage;
    private Image paddleExpandImage;

    // Reference to BallManager
    private BallManager ballManager;

    public ItemManager(Canvas canvas, Paddle paddle) {
        this.canvas = canvas;
        this.paddle = paddle;
        this.items = new ArrayList<>();
        this.random = new Random();

        // Load item images
        loadItemImages();
    }

    private void loadItemImages() {
        try {
            this.multiBallImage = new Image(getClass().getResourceAsStream("/images/ball1.png"));
            this.ghostBallImage = new Image(getClass().getResourceAsStream("/images/Piercing_Ball.png"));
            this.explosionBallImage = new Image(getClass().getResourceAsStream("/images/ball2.png"));
            this.speedBoostImage = new Image(getClass().getResourceAsStream("/images/speed_boost.png"));
            this.paddleExpandImage = new Image(getClass().getResourceAsStream("/images/expandPaddle.png"));

        } catch (Exception e) {
            System.out.println("Error loading item images: " + e.getMessage());
            createPlaceholderImages();
        }
    }

    private Image createColoredImage(int width, int height, Color color) {
        javafx.scene.image.WritableImage image = new javafx.scene.image.WritableImage(width, height);
        javafx.scene.image.PixelWriter writer = image.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                writer.setColor(x, y, color);
            }
        }
        return image;
    }

    private void createPlaceholderImages() {
        this.multiBallImage = createColoredImage(30, 30, Color.RED);
        this.ghostBallImage = createColoredImage(30, 30, Color.BLUE);
        this.explosionBallImage = createColoredImage(30, 30, Color.ORANGE);
        this.speedBoostImage = createColoredImage(30, 30, Color.GREEN);
    }

    public void update(int currentScore) {
        // KHÔNG spawn item từ trên xuống nữa, chỉ spawn từ gạch khi điểm chia hết cho 10

        // Update all active items
        List<Item> itemsToRemove = new ArrayList<>();

        for (Item item : items) {
            if (item.isActive()) {
                item.update();

                // Check collision with paddle
                if (item.checkPaddleCollision(paddle)) {
                    applyItemEffect(item.getType());
                    itemsToRemove.add(item);
                }

                // Check if item is out of screen
                if (item.isOutOfScreen(canvas.getHeight())) {
                    itemsToRemove.add(item);
                }
            } else {
                itemsToRemove.add(item);
            }
        }

        // Remove inactive or collided items
        items.removeAll(itemsToRemove);
    }

    // PHƯƠNG THỨC MỚI: Spawn item từ gạch bị vỡ KHI ĐIỂM CHIA HẾT CHO 10
    public void spawnItemFromBrick(Brick brick, int currentScore) {
        // CHỈ spawn item khi điểm chia hết cho 10
        if (currentScore > 0 && currentScore % 5 == 0 && currentScore != lastScore) {
            double x = brick.getX() + brick.getWidth() / 2 - 15;
            double y = brick.getY() + brick.getHeight() / 2;

            ItemType randomType = getRandomItemType();

            Image itemImage = getImageForType(randomType);
            Item newItem = new Item(x, y, randomType, itemImage);
            items.add(newItem);

            lastScore = currentScore;
            System.out.println("Score " + currentScore + " - Item spawned from brick: " + randomType + " at (" + x + ", " + y + ")");
        }
    }

    // PHƯƠNG THỨC: Lấy random item type
    private ItemType getRandomItemType() {
        ItemType[] types = ItemType.values();
        return types[random.nextInt(types.length)];
    }

    private Image getImageForType(ItemType type) {
        switch (type) {
            case MULTI_BALL: return multiBallImage;
            case GHOST_BALL: return ghostBallImage;
            case EXPLOSION_BALL: return explosionBallImage;
            case SPEED_BOOST: return speedBoostImage;
            case PADDLE_EXPAND: return paddleExpandImage;
            default: return multiBallImage;
        }
    }

    private void applyItemEffect(ItemType type) {
        System.out.println("Applying item effect: " + type);
        Soundmanager1.getInstance().play("Power_up.mp3");

        switch (type) {
            case MULTI_BALL:
                setMultiBallRequested(true);
                break;
            case GHOST_BALL:
                setGhostModeRequested(true);
                break;
            case EXPLOSION_BALL:
                setExplosionModeRequested(true);
                break;
            case SPEED_BOOST:
                setSpeedBoostRequested(true);
                break;
            case PADDLE_EXPAND:
                setPaddleExpandRequested(true);
                break;

        }
    }

    public boolean isMultiBallRequested() {
        if (multiBallRequested) {
            multiBallRequested = false;
            return true;
        }
        return false;
    }

    public boolean isGhostModeRequested() {
        if (ghostModeRequested) {
            ghostModeRequested = false;
            return true;
        }
        return false;
    }

    public boolean isExplosionModeRequested() {
        if (explosionModeRequested) {
            explosionModeRequested = false;
            return true;
        }
        return false;
    }

    public boolean isSpeedBoostRequested() {
        if (speedBoostRequested) {
            speedBoostRequested = false;
            return true;
        }
        return false;
    }

    public boolean isPaddleExpandRequested() {
        if (paddleExpandRequested) {
            paddleExpandRequested = false;
            return true;
        }
        return false;
    }

    private void setMultiBallRequested(boolean requested) {
        this.multiBallRequested = requested;
    }

    private void setGhostModeRequested(boolean requested) {
        this.ghostModeRequested = requested;
    }

    private void setExplosionModeRequested(boolean requested) {
        this.explosionModeRequested = requested;
    }

    private void setSpeedBoostRequested(boolean requested) {
        this.speedBoostRequested = requested;
    }

    private void setPaddleExpandRequested(boolean requested) {
        this.paddleExpandRequested = requested;
    }

    public void render(GraphicsContext gc) {
        for (Item item : items) {
            if (item.isActive()) {
                gc.drawImage(item.getImage(), item.getX(), item.getY(), item.getWidth(), item.getHeight());
            }
        }
    }

    public void reset() {
        items.clear();
        lastScore = 0;
        multiBallRequested = false;
        ghostModeRequested = false;
        explosionModeRequested = false;
        speedBoostRequested = false;
    }

    // Setter cho BallManager
    public void setBallManager(BallManager ballManager) {
        this.ballManager = ballManager;
    }
}
